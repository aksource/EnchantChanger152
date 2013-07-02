package IR.IRUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;

/**
 * ディレクトリ内のJsonファイルを検索＆解析し、Recordを生成するクラス
 * @author gnsk
 *
 */
public class InstantRecipesParserJS {

	private static final Map<String, RecordParserJS> recordParserMap = new HashMap<String, RecordParserJS>();
	private static Set<File> addedResourceDirSet = new HashSet<File>();

	/**
	 * typeに対応するrecordを解析するためのparserを登録する
	 * 独自に拡張したRecordを追加したい場合はこの関数を使ってください
	 * @param type "item", "food", "armor"などアイテムの種類を表す文字列
	 * @param parser typeで指定されたRecordクラスを生成するparser
	 */
	public static void registerParser(String type, RecordParserJS parser) {
		recordParserMap.put(type, parser);
	}

	/**
	 * dirで指定されたディレクトリ内のsuffixで終わるすべてのファイルに対し構文チェックと解析を行い、解析結果をリストで返す
	 * @param dir 検索対象ディレクトリ
	 * @param suffix ".txt", ".json"など
	 * @return 解析結果
	 * @throws IOException
	 * @throws InvalidSyntaxException json構文に違反があった場合に発生する
	 * @throws IRInvalidFormatException InstantRecipesファイルフォーマットに違反があった場合に発生する（必須のフィールドがなかった場合など）
	 */
	public static Record[] parse(File dir, String suffix) throws IOException, InvalidSyntaxException, IRInvalidFormatException {
		ArrayList<Record> list = new ArrayList<Record>();
		searchDir(dir, suffix, list, new File("/"));
		return list.toArray(new Record[list.size()]);
	}

	private static void searchDir(File dir, String suffix, List<Record> list, File currentDir) throws IOException, InvalidSyntaxException, IRInvalidFormatException {
		for (File f : dir.listFiles()) {
			try {
				if (f.isDirectory()) {
					searchDir(f, suffix, list, new File(currentDir, f.getName()));
				} else if (f.getName().endsWith(".zip")) {
					searchArcive(f, list, suffix);
				} else if (f.getName().endsWith(suffix)){
					searchFile(f, list, currentDir);
				}
			} catch (IRInvalidFormatException ex) {
				ex.markLocation(f.getName());
				throw ex;
			}
		}
	}

	private static void searchArcive(File f, List<Record> list, String suffix) throws ZipException, IOException, InvalidSyntaxException, IRInvalidFormatException {

		try {
			addResourceDirecotory(f);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ZipFile zip = new ZipFile(f);
		try {
			Enumeration<? extends ZipEntry> ez = zip.entries();
			while(ez.hasMoreElements()) {
				ZipEntry ze = ez.nextElement();
				if (ze.isDirectory()) continue;
				String name = ze.getName();
				if (!name.endsWith(suffix)) continue;
				File dir = new File("/", name).getParentFile();
				InputStream is = zip.getInputStream(ze);
				try {
					parseInputStream(is, list, dir);
				} catch (IRInvalidFormatException e) {
					e.markLocation(ze.getName());
					throw e;
				} finally {
					is.close();
				}
			}
		} finally {
			zip.close();
		}
	}

	private static void searchFile(File f, List<Record> list, File currentDir) throws IOException, InvalidSyntaxException, IRInvalidFormatException {

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(f);
			parseInputStream(fis, list, currentDir);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	private static void parseInputStream(InputStream is, List<Record> list, File currentDir)
			throws UnsupportedEncodingException, IOException, InvalidSyntaxException, IRInvalidFormatException {

		JdomParser parser = new JdomParser();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		// remove bom if exist
		br.mark(1);
		if (br.read() != 0xFEFF) br.reset();

		JsonRootNode json = parser.parse(br);

		int i = 0;
		for (JsonNode jnode : (List<JsonNode>)json.getElements()) {

			try {
				Record r = parseRecord(jnode, currentDir);
				if (r != null) list.add(r);
			} catch (IRInvalidFormatException ex) {
				ex.markLocation(String.format("list[%d]", i));
				throw ex;
			}

			i++;
		}
	}

	private static Record parseRecord(JsonNode json, File currentDir) throws IRInvalidFormatException {
//		Map<JsonNode, JsonNode> map = (Map<JsonNode, JsonNode>)json.getFields();
		Map<JsonStringNode, JsonNode> map = (Map<JsonStringNode, JsonNode>)json.getFields();
		JsonNode field = map.get(JsonNodeFactories.string("type"));
		String type = field != null ? field.getText() : "item";

		RecordParserJS parser = recordParserMap.get(type);
		if (parser == null) {
			throw new IRInvalidFormatException("未知のtypeが指定されました 。 " + type);
		}

		return parser.parse(map, currentDir);
	}

	public static void addResourceDirecotory(File dir) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (addedResourceDirSet.contains(dir)) return;

		ClassLoader loader = InstantRecipesParserJS.class.getClassLoader();
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
		method.setAccessible(true);

		if (loader instanceof URLClassLoader) {
			URLClassLoader urlloader = (URLClassLoader)loader;
			method.invoke(urlloader, new Object[] {dir.toURI().toURL()});
		}

		addedResourceDirSet.add(dir);

	}
}
