package ak.EnchantChanger;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabEC extends CreativeTabs
{
	public CreativeTabEC(String var1)
	{
		super(var1);
	}

	@SideOnly(Side.CLIENT)

	public int getTabIconItemIndex()
	{
		return EnchantChanger.ZackSwordItemID;
	}
	public String getTranslatedTabLabel()
	{
		return "E.Changer";
	}
}