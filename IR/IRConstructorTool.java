package IR;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemTool;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.RecordItem;
import IR.IRUtil.RecordTool;

public class IRConstructorTool extends IRConstructorItem {

	@Override
	protected Object construct(RecordItem record) throws IRInvalidValueException {

		RecordTool rt = (RecordTool)record;
		EnumToolMaterial material;

		try {
			material = EnumToolMaterial.valueOf(rt.toolMaterial.toUpperCase().replace("DIAMOND", "EMERALD"));
		} catch (IllegalArgumentException e) {
			throw new IRInvalidValueException("未知のmaterial:" + rt.toolMaterial + " が指定されました");
		}

		int enchantability = rt.enchantability != -1 ? rt.enchantability : material.getEnchantability();

		return construct(rt, material, enchantability);
	}

	protected Object construct(RecordTool record, EnumToolMaterial material, int enchantability) throws IRInvalidValueException {
		ItemTool tool;
		switch (record.toolType) {
		case 0:
			tool = new IRForgeItemAxe(record.id, record.textureFile, material, enchantability);
			break;
		case 1:
			tool = new IRForgeItemPickaxe(record.id, record.textureFile, material, enchantability);
			break;
		case 2:
			tool = new IRForgeItemSpade(record.id, record.textureFile, material, enchantability);
			break;
		default:
			throw new IRInvalidValueException("未知のtoolType:" + record.toolType + " が指定されました");
		}

		return setParams(tool, record);
	}

	protected Object setParams(ItemTool item, RecordTool record) {

		if (record.maxUse != -1) item.setMaxDamage(record.maxUse);
		if (record.efficiency != -1) item.efficiencyOnProperMaterial = record.efficiency;
		if (record.damage != -1) item.damageVsEntity = record.damage;

		return super.setParams(item, record);
	}
}
