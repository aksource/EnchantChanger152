package ak.BigItems;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="BigItems", name="BigItems", version="1.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class BigItems
{
	@Instance("BigItems")
	public static BigItems instance;
	@SidedProxy(clientSide = "ak.BigItems.Client.ClientProxy", serverSide = "ak.BigItems.CommonProxy")
	public static CommonProxy proxy;


	public static int[] ItemIDs;
	public static double Scale;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		ItemIDs = config.get(Configuration.CATEGORY_GENERAL, "Big Item Ids", new int[]{267,272,268,283,276}, "Put Item Ids which you want to make it big.").getIntList();
		Scale = config.get(Configuration.CATEGORY_GENERAL, "Item Scale", 2.0d, "Item Scale").getDouble(2.0d);
		config.save();
	}
	@Init
	public void load(FMLInitializationEvent event)
	{
		proxy.registerClientInformation();
	}
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
//		for(int i = 0;i<ItemIDs.length;i++)
//			System.out.println(ItemIDs[i]);
	}
}