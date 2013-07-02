package IR;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.RecordItem;
import IR.IRUtil.RecordSword;

public class IRConstructorSword extends IRConstructorItem {

	@Override
	protected Object construct(RecordItem record) throws IRInvalidValueException {

		RecordSword rs = (RecordSword)record;
		EnumToolMaterial material;

		try {
			material = EnumToolMaterial.valueOf(rs.toolMaterial.toUpperCase().replace("DIAMOND", "EMERALD"));
		} catch (IllegalArgumentException e) {
			throw new IRInvalidValueException("未知のmaterial:" + rs.toolMaterial + " が指定されました");
		}

		int damage = rs.damage != -1 ? rs.damage : material.getDamageVsEntity() + 4;
		int enchantability = rs.enchantability != -1 ? rs.enchantability : material.getEnchantability();

		return construct(rs, material, damage, enchantability);
	}

	protected Object construct(RecordSword record, EnumToolMaterial material, int damage, int enchantability) throws IRInvalidValueException {
		ItemSword sword = new IRForgeItemSword(record.id, record.textureFile, material, damage, enchantability);
		return setParams(sword, record);
	}

	protected Object setParams(ItemSword item, RecordSword record) {

		if (record.maxUse != -1) item.setMaxDamage(record.maxUse);

		return super.setParams(item, record);
	}
}
