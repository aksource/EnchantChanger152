package IR.IRUtil;

import java.io.File;
import java.util.Map;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.JsonStringNode;

public class RecordParserJSFood  extends RecordParserJSItem {

	@Override
	public Record parse(Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		RecordFood newrecord = new RecordFood();
		return parseField(newrecord, map, currentDir);
	}

	protected Record parseField(RecordFood record, Map<JsonStringNode, JsonNode> map, File currentDir) throws IRInvalidFormatException {
		super.parseField(record, map, currentDir);

		record.heal = selectInt(map, "heal", 3);
		record.isWolfsFavoriteMeat = selectBoolean(map, "isdogfood", false);
		record.saturationModifire = selectFloat(map, "saturation", 0.6f);
		record.useDuration = selectInt(map, "duration", 32);
		record.useAction = selectText(map, "action", "eat");

		try {
			parseEffect(record, selectNode(map, "effect"));
		} catch (IRInvalidFormatException e) {
			e.markLocation("effect");
			throw e;
		}

		record.alwaysEdible = selectBoolean(map, "alwaysedible", record.effect != null);

		return record;
	}

	protected void parseEffect(RecordFood record, JsonNode field) throws IRInvalidFormatException {
		if (field == null) return;

		if (field.getType() != JsonNodeType.OBJECT)
			throw new IRInvalidFormatException("effectはオブジェクトの形式で指定してください");

		Map<JsonStringNode, JsonNode> map = field.getFields();

		RecordFood.Effect effect = new RecordFood.Effect();
		effect.id = selectIntEssential(map, "id");
		effect.duration = selectInt(map, "duration", 5);
		effect.amplifier = selectInt(map, "amplifier", 1);
		effect.probability = selectFloat(map, "probability", 1);

		record.effect = effect;
	}
}