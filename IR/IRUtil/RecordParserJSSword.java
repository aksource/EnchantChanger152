package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

public class RecordParserJSSword extends RecordParserJSItem {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordSword newitem = new RecordSword();
		return parseField(newitem, map, currentDir);
	}

	public Record parseField(RecordSword record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.toolMaterial = selectText(map, "material", "iron");
		record.maxUse = selectInt(map, "maxuse", -1);
		record.damage = selectInt(map, "damage", -1);
		record.enchantability = selectInt(map, "enchantability", -1);
		record.stackSize = selectInt(map, "stacksize", 1);

		return record;
	}
}
