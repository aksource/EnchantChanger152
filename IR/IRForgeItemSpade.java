package IR;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSpade;

public class IRForgeItemSpade extends ItemSpade{

	public int enchantability;
	public String textureFile;

	protected IRForgeItemSpade(int par1, String textureFile, EnumToolMaterial par2EnumToolMaterial, int enchantability) {
		super(par1, par2EnumToolMaterial);
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

}
