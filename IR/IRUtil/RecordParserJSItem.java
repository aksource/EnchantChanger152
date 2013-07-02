package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

public class RecordParserJSItem extends RecordParserJS {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordItem newitem = new RecordItem();
		return parseField(newitem, map, currentDir);
	}

	protected Record parseField(RecordItem record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.isFull3d = selectBoolean(map, "isfull3d", false);
		record.stackSize = selectInt(map, "stacksize", 64);

		record.id = record.id - 256;
		return record;
	}

	@Override
	protected String getDefaultTextureFile() {
		return "/gui/items.png";
	}
}
