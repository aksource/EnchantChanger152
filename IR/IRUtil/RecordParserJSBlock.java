package IR.IRUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import IR.IRUtil.RecordBlock.DropItem;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.JsonStringNode;

public class RecordParserJSBlock extends RecordParserJS {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordBlock newrecord = new RecordBlock();
		return parseField(newrecord, map, currentDir);
	}

	protected Record parseField(RecordBlock record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.hardness = selectFloat(map, "hardness", 1f);
		record.blastRegistantce = selectFloat(map, "registance", 5f);
		record.lightValue = selectFloat(map, "lightvalue", 0f);
		record.material = selectText(map, "material", "wood");
		record.stepSound = selectText(map, "stepsound", "stone");

		record.blockHeight = selectFloat(map, "height", 1f);
		record.renderType = selectInt(map, "rendertype", 0);
		record.alphaBlending = selectBoolean(map, "alphablending", false);

		record.isOpaqueCube = selectBoolean(map, "opaqueblock", record.blockHeight == 1 && record.renderType == 0 && !record.alphaBlending);
		record.collidable = selectBoolean(map, "collidable", true);


		try {
			record.dropItems = parseDropItems(selectNode(map, "dropitems"));
		} catch (IRInvalidFormatException e) {
			e.markLocation("dropitems");
			throw e;
		}

		return record;
	}

	protected RecordBlock.DropItem[] parseDropItems(JsonNode dropItemField) throws IRInvalidFormatException {
		if (dropItemField == null) return null;

		if (dropItemField.getType() == JsonNodeType.ARRAY) {
			List<DropItem> result = new ArrayList<RecordBlock.DropItem>();

			for (JsonNode jn : (List<JsonNode>)dropItemField.getElements()) {
				result.add(parseDropItem(jn));
			}

			return result.toArray(new DropItem[result.size()]);

		} else {
			return new RecordBlock.DropItem[] {parseDropItem(dropItemField)};
		}
	}

	protected RecordBlock.DropItem parseDropItem(JsonNode node) throws IRInvalidFormatException {
		RecordBlock.DropItem di = new RecordBlock.DropItem();

		if (node.getType() == JsonNodeType.NUMBER) {
			di.id = parseAsInt(node);
		} else if (node.getType() == JsonNodeType.OBJECT) {
			Map<JsonStringNode, JsonNode> map = node.getFields();
			di.id = selectIntEssential(map, "id");
			di.meta = selectInt(map, "meta", -1);
			di.min = selectInt(map, "min", 1);
			di.max = selectInt(map, "max", 0);
			if (di.max != 0 && di.max < di.min) di.max = di.min;
			di.chance = selectFloat(map, "chance", 1);
		}
		return di;
	}

	@Override
	protected String getDefaultTextureFile() {
		return "/terrain.png";
	}
}
