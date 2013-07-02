package IR;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.RecordFood;
import IR.IRUtil.RecordItem;

public class IRConstructorFood extends IRConstructorItem {

	@Override
	protected Object construct(RecordItem record) throws IRInvalidValueException {
		RecordFood rf = (RecordFood)record;

		EnumAction action;

		try {
			action = EnumAction.valueOf(rf.useAction.toLowerCase());
		} catch (IllegalArgumentException e) {
			throw new IRInvalidValueException("未知のtoolMaterial:" + rf.useAction + " が指定されました");
		}

		return construct(rf, action);
	}

	protected Object construct(RecordFood record, EnumAction action) {
		ItemFood food = new IRForgeItemFood(record.id, record.textureFile, record.heal, record.saturationModifire, record.isWolfsFavoriteMeat, action, record.useDuration);
		return setParams(food, record);
	}

	protected Object setParams(ItemFood item, RecordFood record) {

		if (record.alwaysEdible) item.setAlwaysEdible();
		if (record.effect != null) {
			RecordFood.Effect e = record.effect;
			item.setPotionEffect(e.id, e.duration, e.amplifier, e.probability);
		}

		return super.setParams(item, record);
	}
}