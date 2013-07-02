package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

public class RecordParserJSRecipeAlias extends RecordParserJS {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordRecipeAlias newrecipe = new RecordRecipeAlias();
		return parseField(newrecipe, map, currentDir);
	}

	protected Record parseField(RecordRecipeAlias record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		record.canBindTexture = false;
		record.id = selectIntEssential(map, "id");
		record.burnTime = selectInt(map, "burntime", 0);

		try {
			record.recipe = parseRecipe(selectNodeEssential(map, "recipe"));
		} catch (IRInvalidFormatException ex) {
			ex.markLocation("recipe");
			throw ex;
		}

		if (record.recipe != null)
			record.recipe.meta = selectInt(map, "meta", 0);

		record.name = "recipe for itemID:" + record.id;

		return record;
	}

	@Override
	protected String getDefaultTextureFile() {
		return null;
	}

}
