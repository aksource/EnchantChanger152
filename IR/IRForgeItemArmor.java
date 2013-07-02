package IR;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class IRForgeItemArmor extends ItemArmor{

	public int enchantability;
	public String armorTexture;
	public String textureFile;

	public IRForgeItemArmor(int id, String textureFile, int armorType, EnumArmorMaterial material, int enchantability, String armorTexture) {
		super(id, material, 0, armorType);
		this.enchantability = enchantability;
		this.armorTexture = armorTexture;
//		this.textureFile = textureFile;
	}

	@Override
	public int getItemEnchantability() {
		return enchantability;
	}

//	@Override
//	public String getArmorTextureFile(ItemStack itemstack) {
//		return armorTexture;
//	}
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
    	return armorTexture;
    }
//	@Override
//	public String getTextureFile() {
//		return textureFile;
//	}
}
