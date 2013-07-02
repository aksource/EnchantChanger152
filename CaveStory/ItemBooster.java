package CaveStory;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemBooster extends ItemArmor {

	private int id;
	public ItemBooster(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
		id = par1;
	}
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
	{
		String st;
		if(id == CaveStory.CaveStoryID)
			st = (layer == 1)? CaveStory.Armor08_1:CaveStory.Armor08_2;
		else
			st = (layer == 1)? CaveStory.Armor20_1:CaveStory.Armor20_2;
    	return st;
	}
}
