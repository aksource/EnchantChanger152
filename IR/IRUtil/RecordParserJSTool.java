package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

public class RecordParserJSTool extends RecordParserJSItem {
	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordTool newitem = new RecordTool();
		return parseField(newitem, map, currentDir);
	}

	public Record parseField(RecordTool record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.toolType = selectIntEssential(map, "tooltype");
		record.toolMaterial = selectText(map, "material", "iron");
		record.maxUse = selectInt(map, "maxuse", -1);
		record.efficiency = selectFloat(map, "efficiency", -1);
		record.damage = selectInt(map, "damage", -1);
		record.enchantability = selectInt(map, "enchantability", -1);
		record.stackSize = selectInt(map, "stacksize", 1);

		return record;
	}
}
