package IR;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class IRForgeItemSword extends ItemSword {

	public int enchantability;
	public String textureFile;
	public int damage;

	public IRForgeItemSword(int par1, String textureFile, EnumToolMaterial par2EnumToolMaterial, int damage, int enchantability) {
		super(par1, par2EnumToolMaterial);
		this.damage = damage;
		this.enchantability = enchantability;
//		this.textureFile = textureFile;
	}

	@Override
	public int getItemEnchantability() {
		return enchantability;
	}

//	@Override
//	public String getTextureFile() {
//		return textureFile;
//	}

	@Override
	public int getDamageVsEntity(Entity par1Entity) {
		return damage;
	}
}
