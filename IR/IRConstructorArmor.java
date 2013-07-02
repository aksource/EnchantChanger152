package IR;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.RecordArmor;
import IR.IRUtil.RecordItem;

public class IRConstructorArmor extends IRConstructorItem {

	protected Map<String, String> armorTextureMap = new HashMap<String, String>();

	@Override
	protected Object construct(RecordItem record) throws IRInvalidValueException {

		RecordArmor ra = (RecordArmor)record;
		EnumArmorMaterial material;

		try {
			material = EnumArmorMaterial.valueOf(ra.armorMaterial.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IRInvalidValueException("未知のtoolMaterial:" + ra.armorMaterial + " が指定されました");
		}

		if (ra.armorType < 0 || 3 < ra.armorType) {
			throw new IRInvalidValueException("未知のarmorType:" + ra.armorType + " が指定されました");
		}

		String armorTexture = ra.armorTextureFilePath;
		if (armorTexture == null) {
			String prefix = material.toString().toLowerCase();
			armorTexture = "/armor/" + prefix + "_" + (ra.armorType == 2 ? 2 : 1) + ".png";
		}

		int enchantability = ra.enchantability != -1 ? ra.enchantability : material.getEnchantability();

		return construct(ra, material, enchantability, armorTexture);
	}

	protected Object construct(RecordArmor record, EnumArmorMaterial material, int enchantability, String armorTexture) {
		IRForgeItemArmor item = new IRForgeItemArmor(record.id, record.textureFile, record.armorType, material, enchantability, armorTexture);
		return setParams(item, record);
	}
	private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
	protected Object setParams(ItemArmor item, RecordArmor record) {

		if (record.durability != -1)
			item.setMaxDamage(this.maxDamageArray[record.armorType] * record.durability);

		if (record.damageReductionAmount != -1) {
			Field f = ItemArmor.class.getFields()[1];
			f.setAccessible(true);

			try {
				f.setInt(item, record.damageReductionAmount);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return super.setParams(item, record);
	}
}
