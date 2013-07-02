package IR;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class IRForgeItemFood  extends ItemFood{

	protected String textureFile;
	protected EnumAction action;
	protected int useDuration;

	public IRForgeItemFood(int id, String textureFile, int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat, EnumAction action, int duration) {
		super(id, healAmount, saturationModifier, isWolfsFavoriteMeat);
//		this.textureFile = textureFile;
		this.action = action;
		this.useDuration = duration;
	}

//	@Override
//	public String getTextureFile() {
//		return textureFile;
//	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return action;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return useDuration;
	}
}