package HNPCs;

import net.minecraft.creativetab.CreativeTabs;

public class TabHNPCs extends CreativeTabs
{
	public TabHNPCs(String label)
	{
		super(label);
	}
    public int getTabIconItemIndex()
    {
        return HostilityNPCs.blockHNPCsId;
    }
    public String getTranslatedTabLabel()
    {
    	return "HNPCs";
    }
}