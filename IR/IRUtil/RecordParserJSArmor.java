package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

public class RecordParserJSArmor extends RecordParserJSItem {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordArmor armor = new RecordArmor();
		return parseField(armor, map, currentDir);
	}

	public Record parseField(RecordArmor record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.armorType = selectIntEssential(map, "armortype");
		record.armorMaterial = selectText(map, "material", "iron");
		record.damageReductionAmount = selectInt(map, "armorpoint", -1);
		record.durability = selectInt(map, "durability", -1);
		record.enchantability = selectInt(map, "enchantability", -1);
		String path = selectText(map, "armortexture", null);
		if (path != null) record.armorTextureFilePath = resolvePath(currentDir, path);
		record.stackSize = selectInt(map, "stacksize", 1);
		return record;
	}
}
