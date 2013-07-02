package IR.IRUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonNodeType;
import argo.jdom.JsonStringNode;

/**
 * JSON形式で記述されたファイルを解析してRecordを生成するクラス
 * @author gnsk
 *
 */
public abstract class RecordParserJS {

	/**
	 * JsonNodeMapを解析しRecordTypeに対応したレコードクラスを生成する
	 * ここではレコードの生成のみを行い、値の解析とセットはparseFieldで行う
	 * @param map Jsonノード
	 * @param currentDir 対象のJsonが記述されたファイルが存在するディレクトリ。テクスチャ用画像の相対パスを解決するために利用される
	 * @return
	 * @throws IRInvalidFormatException
	 */
	public abstract Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException;

	/**
	 * テクスチャファイルが指定されなかった場合の既定値
	 * blockなら/terrain.png、itemなら/gui/items.pngといった感じのやつ
	 * @return
	 */
	protected abstract String getDefaultTextureFile();

	/**
	 * parseで生成したレコードに解析した値をセットする
	 * RecordParserJSではレコードオブジェクトの共通項目を設定している
	 * @param record
	 * @param map
	 * @param currentDir
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected Record parseField(Record record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		record.id = selectIntEssential(map, "id");
		record.name = selectTextEssential(map, "name");
		record.burnTime = selectInt(map, "burntime", 0);

		if (selectNodeEssential(map, "texture").getType() == JsonNodeType.NUMBER) {
			record.textrueIndex = selectIntEssential(map, "texture");
			record.textureFile = getDefaultTextureFile();
			record.canBindTexture = false;
		} else {
			String path = selectTextEssential(map, "texture");
			record.textrueIndex = 0;
			record.textureFile = resolvePath(currentDir, path);
			record.canBindTexture = true;
		}

		try {
			record.recipe = parseRecipe(selectNode(map, "recipe"));
		} catch (IRInvalidFormatException ex) {
			ex.markLocation("recipe");
			throw ex;
		}

		try {
			record.localNames = parseLocalName(selectNode(map, "localname"));
		} catch (IRInvalidFormatException ex) {
			ex.markLocation("localname");
			throw ex;
		}

		try {
			record.enchantments = parseEnchantments(selectNode(map, "enchantment"));
		} catch (IRInvalidFormatException ex) {
			ex.markLocation("enchantment");
			throw ex;
		}

		return record;
	}

	protected Record.Recipe parseRecipe(JsonNode recipeField) throws IRInvalidFormatException {
		if (recipeField == null) return null;

		if (recipeField.getType() != JsonNodeType.OBJECT)
			throw new IRInvalidFormatException("recipeを正しく設定してください");

		Record.Recipe recipe = new Record.Recipe();
//		Map<JsonNode, JsonNode> map = recipeField.getFields();
		Map<JsonStringNode, JsonNode> map = recipeField.getFields();

		recipe.productCount = selectInt(map, "count", 1);
		recipe.meta = selectInt(map, "meta", 0);

		try {
			recipe.form = parseRecipeForm(selectNode(map, "form"));
		} catch (IRInvalidFormatException e) {
			e.markLocation("form");
			throw e;
		}

		try {
			if (recipe.form != null) {
				recipe.partsMap = parseShapedRecipeParts(selectNode(map, "parts"));
			} else {
				recipe.parts = parseShapelessRecipeParts(selectNode(map, "parts"));
			}
		} catch (IRInvalidFormatException e) {
			e.markLocation("parts");
			throw e;
		}

		try {
			recipe.smelting = parseSmeltingRecipe(selectNode(map, "smelting"));
		} catch (IRInvalidFormatException ex) {
			ex.markLocation("smelting");
			throw ex;
		}

		if (recipe.form == null && recipe.parts == null && recipe.smelting == null) {
			throw new IRInvalidFormatException("recipeの情報が足りません");
		}

		return recipe;
	}


	protected String[] parseRecipeForm(JsonNode formField) throws IRInvalidFormatException {
		if (formField == null) return null;

		if (formField.getType() != JsonNodeType.ARRAY)
			throw new IRInvalidFormatException("formは配列で指定してください");

		List<String> list = new ArrayList<String>(3);

		int width = 0;

		for (JsonNode jn : (List<JsonNode>)formField.getElements()) {
			String text = jn.getText();

			if (3 < text.length())
				throw new IRInvalidFormatException("３列以上のレシピは追加できません");

			if (width != 0 && width != text.length())
				throw new IRInvalidFormatException("各行の列数は一致させていください");

			width = text.length();

			list.add(text);
		}

		if (3 < list.size())
			throw new IRInvalidFormatException("３行以上のレシピは追加できません");

		return list.toArray(new String[list.size()]);
	}

	protected Map<Character, Record.Stuff> parseShapedRecipeParts(JsonNode partsField) throws IRInvalidFormatException {
		if (partsField == null) return null;

		if (partsField.getType() != JsonNodeType.OBJECT)
			throw new IRInvalidFormatException("shapedレシピのpartsはオブジェクトで指定してください");

		Map<Character, Record.Stuff> result = new HashMap<Character, Record.Stuff>();

		for (Entry<JsonStringNode, JsonNode> e : ((Map<JsonStringNode, JsonNode>)partsField.getFields()).entrySet()) {
			JsonNode keynode = e.getKey();
			JsonNode valuenode = e.getValue();

			if (keynode.getType() != JsonNodeType.STRING)
				throw new IRInvalidFormatException("書式が正しくありません");

			String kstr = keynode.getText();

			if (kstr.length() != 1)
				throw new IRInvalidFormatException("keyは一文字で指定してください");

			result.put(kstr.charAt(0), parseStuff(valuenode));
		}

		return result;
	}

	protected Record.Stuff[] parseShapelessRecipeParts(JsonNode partsField) throws IRInvalidFormatException {
		if (partsField == null) return null;

		if (partsField.getType() != JsonNodeType.ARRAY)
			throw new IRInvalidFormatException("shapelessレシピのpartsは配列で指定してください");

		List<Record.Stuff> list = new ArrayList<Record.Stuff>();

		for (JsonNode node : (List<JsonNode>)partsField.getElements()) {
			list.add(parseStuff(node));
		}

		return list.toArray(new Record.Stuff[list.size()]);
	}

	protected Record.Stuff parseSmeltingRecipe(JsonNode smeltingField) throws IRInvalidFormatException {
		if (smeltingField == null) return null;
		return parseStuff(smeltingField);
	}

	protected Record.Stuff parseStuff(JsonNode node) throws IRInvalidFormatException {
		if (node.getType() == JsonNodeType.NUMBER) {
			return new Record.Stuff(parseAsInt(node));
		} else if (node.getType() == JsonNodeType.OBJECT) {
			Map<JsonStringNode, JsonNode> partsmap = node.getFields();
			int id = selectIntEssential(partsmap, "id");
			int meta = selectIntEssential(partsmap, "meta");
			return new Record.Stuff(id, meta);
		} else if (node.getType() == JsonNodeType.ARRAY) {
			List<JsonNode> list = node.getElements();
			try {
				return new Record.Stuff(Integer.parseInt(list.get(0).getText()), Integer.parseInt(list.get(1).getText()));
			} catch (NumberFormatException ex) {
				throw new IRInvalidFormatException("レシピの材料を配列で指定する場合はIDとMetaの整数２つで指定してください");
			}
		} else {
			throw new IRInvalidFormatException("レシピ材料の書式にエラーがあります");
		}
	}

	protected Record.NameEntry[] parseLocalName(JsonNode field) throws IRInvalidFormatException {
		if (field == null) return new Record.NameEntry[0];

		if (field.getType() != JsonNodeType.OBJECT)
			throw new IRInvalidFormatException("localNameはオブジェクトの形式で指定してください");

		List<Record.NameEntry> names = new ArrayList<Record.NameEntry>();

		for (Entry<JsonStringNode, JsonNode> e : ((Map<JsonStringNode, JsonNode>)field.getFields()).entrySet()) {
			Record.NameEntry ne = new Record.NameEntry(e.getKey().getText(), e.getValue().getText());
			names.add(ne);
		}

		return names.toArray(new Record.NameEntry[names.size()]);
	}

	protected Record.Enchantment[] parseEnchantments(JsonNode field) throws IRInvalidFormatException {
		if (field == null) return new Record.Enchantment[0];

		if (field.getType() == JsonNodeType.OBJECT) {
			return new Record.Enchantment[] { parseEnchantment(field) };
		} else if (field.getType() == JsonNodeType.ARRAY) {
			List<Record.Enchantment> list = new ArrayList<Record.Enchantment>();
			for (JsonNode node : (List<JsonNode>)field.getElements()) {
				list.add(parseEnchantment(node));
			}
			return list.toArray(new Record.Enchantment[list.size()]);
		} else {
			throw new IRInvalidFormatException("enchantmentはオブジェクトの形式で指定してください");
		}
	}

	protected Record.Enchantment parseEnchantment(JsonNode field) throws IRInvalidFormatException {
		if (field.getType() != JsonNodeType.OBJECT)
			throw new IRInvalidFormatException("enchantmentの要素はオブジェクトの形式で指定してください");

		Map<JsonStringNode, JsonNode> map = field.getFields();
		int id = selectIntEssential(map, "id");
		int level = selectIntEssential(map, "level");

		return new Record.Enchantment(id, level);
	}

	/**
	 * 相対パスをリソースパスに変換する
	 * @param currentDir
	 * @param path
	 * @return
	 */
	protected String resolvePath(File currentDir, String path) {
		path = path.replace("\\", "/");
		if (path.startsWith("/")) {
			return path;
		}

		while (path.startsWith("../")) {
			path = path.substring(3);
			File p = currentDir.getParentFile();
			if (p == null) break;
			currentDir = p;
		}

		return new File(currentDir, path).getPath().replace("\\", "/");
	}

	/**
	 * mapで指定したオブジェクトからノードを選択する。存在しない場合はnullを返す
	 * @param map
	 * @param name
	 * @return
	 */
	protected static JsonNode selectNode(Map<JsonStringNode, JsonNode> map, String name) {
		return map.get(JsonNodeFactories.string(name));
	}

	/**
	 * mapで指定したオブジェクトから必須ノードを選択する。存在しない場合は例外が発生する
	 * @param map
	 * @param name
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static JsonNode selectNodeEssential(Map<JsonStringNode, JsonNode> map, String name) throws IRInvalidFormatException {
		JsonNode field = selectNode(map, name);
		if (field == null) throw new IRInvalidFormatException(name + "を指定してください", name);
		return field;
	}

	/**
	 * mapで指定したオブジェクトから文字列を選択する。文字列を読み取れなかった場合はdefaultValueを返す
	 * @param map
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static String selectText(Map<JsonStringNode, JsonNode> map, String name, String defaultValue) throws IRInvalidFormatException {
		JsonNode field = selectNode(map, name);
		if (field == null) return defaultValue;
		if (field.getType() != JsonNodeType.STRING)
			throw new IRInvalidFormatException(name + "は文字列で指定してください", name);
		return field.getText();
	}

	/**
	 * mapで指定したオブジェクトから文字列を選択する。文字列を読み取れなかった場合は例外が発生する
	 * @param map
	 * @param name
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static String selectTextEssential(Map<JsonStringNode, JsonNode> map, String name) throws IRInvalidFormatException {
		JsonNode field = selectNode(map, name);
		if (field == null) throw new IRInvalidFormatException(name + "を指定してください", name);
		if (field.getType() != JsonNodeType.STRING)
			throw new IRInvalidFormatException(name + "は文字列で指定してください", name);
		return field.getText();
	}

	/**
	 * mapで指定したオブジェクトから整数を選択する。整数を読み取れなかった場合はdefaultValueを返す
	 * @param map
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static int selectInt(Map<JsonStringNode, JsonNode> map, String name, int defaultValue) throws IRInvalidFormatException {
		JsonNode field = selectNode(map, name);
		if (field == null) return defaultValue;

		try {
			return Integer.parseInt(field.getText());
		} catch (Exception ex) {
			throw new IRInvalidFormatException(name + "は整数で指定してください", ex, name);
		}
	}

	/**
	 * mapで指定したオブジェクトから整数を選択する。整数を読み取れなかった場合は例外が発生する
	 * @param map
	 * @param name
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static int selectIntEssential(Map<JsonStringNode, JsonNode> map, String name) throws IRInvalidFormatException {
		JsonNode field = selectNodeEssential(map, name);

		try {
			return Integer.parseInt(field.getText());
		} catch (Exception ex) {
			throw new IRInvalidFormatException(name + "は整数で指定してください", ex, name);
		}
	}

	/**
	 * mapで指定したオブジェクトから実数を選択する。実数を読み取れなかった場合はdefaultValueを返す
	 * @param map
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static float selectFloat(Map<JsonStringNode, JsonNode> map, String name, float defaultValue) throws IRInvalidFormatException {
		JsonNode field = selectNode(map, name);
		if (field == null) return defaultValue;

		try {
			return Float.parseFloat(field.getText());
		} catch (Exception ex) {
			throw new IRInvalidFormatException(name + "は数値で指定してください", ex, name);
		}
	}

	/**
	 * mapで指定したオブジェクトから実数を選択する。実数を読み取れなかった場合は例外が発生する
	 * @param map
	 * @param name
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static float selectFloatEssential(Map<JsonStringNode, JsonNode> map, String name) throws IRInvalidFormatException {
		JsonNode field = selectNodeEssential(map, name);

		try {
			return Float.parseFloat(field.getText());
		} catch (Exception ex) {
			throw new IRInvalidFormatException(name + "は数値で指定してください", ex, name);
		}
	}

	/**
	 * mapで指定したオブジェクトからブール値を選択する。ブール値を読み取れなかった場合はdefaultValueを返す
	 * @param map
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	protected static boolean selectBoolean(Map<JsonStringNode, JsonNode> map, String name, boolean defaultValue) {
		JsonNode field = selectNode(map, name);
		if (field == null) return defaultValue;
		if (field.getType() == JsonNodeType.TRUE) return true;
		if (field.getType() == JsonNodeType.FALSE) return false;
		return defaultValue;
	}

	/**
	 * mapで指定したオブジェクトからブール値を選択する。ブール値を読み取れなかった場合は例外が発生する
	 * @param map
	 * @param name
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static boolean selectBooleanEssential(Map<JsonStringNode, JsonNode> map, String name) throws IRInvalidFormatException {
		JsonNode field = selectNodeEssential(map, name);
		if (field.getType() == JsonNodeType.TRUE) return true;
		if (field.getType() == JsonNodeType.FALSE) return false;
		throw new IRInvalidFormatException(name + "はブール値(true, false)で指定してください", name);
	}

	/**
	 * 指定されたノードを整数として解析する。失敗した場合は例外が発生する
	 * @param node
	 * @return
	 * @throws IRInvalidFormatException
	 */
	protected static int parseAsInt(JsonNode node) throws IRInvalidFormatException {
		try {
			return Integer.parseInt(node.getText());
		} catch (NumberFormatException ex) {
			throw new IRInvalidFormatException("数値を解析できませんでした");
		}
	}
}
