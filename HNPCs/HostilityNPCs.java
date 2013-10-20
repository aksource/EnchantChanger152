package HNPCs;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="HostilityNPCs", name="HostilityNPCs", version="1.0")
@NetworkMod(clientSideRequired=true, 
serverSideRequired=false,
channels={"HNPC|Spawn"},
packetHandler=PacketHandler.class)
public class HostilityNPCs
{
	@Instance("HostilityNPCs")
	public static HostilityNPCs instance;
	@SidedProxy(clientSide = "HNPCs.Client.ClientProxy", serverSide = "HNPCs.CommonProxy")
	public static CommonProxy proxy;
	
	public static int blockHNPCsId;
	public static Block blockHNPCs;
	public static int[] addHelmet;
	public static int[] addChestplate;
	public static int[] addLeggings;
	public static int[] addBoots;
	
	public static int guiSBID = 0;
	
	public static CreativeTabs tab = new TabHNPCs("HNPCs");
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		blockHNPCsId = config.get(Configuration.CATEGORY_BLOCK, "BlockHNPCsID", 3500).getInt();
//		addHelmet = config.get(Configuration.CATEGORY_ITEM, "addHelmet", new int[]{},"Additional Helmet ID. set new Line if you want to add more than one item.").getIntList();
//		addChestplate = config.get(Configuration.CATEGORY_ITEM, "addChestplate", new int[]{},"Additional Chestplate ID. set new Line if you want to add more than one item.").getIntList();
//		addLeggings = config.get(Configuration.CATEGORY_ITEM, "addLeggings", new int[]{},"Additional Leggings ID. set new Line if you want to add more than one item.").getIntList();
//		addBoots = config.get(Configuration.CATEGORY_ITEM, "addHelmet", new int[]{},"Additional addBoots ID. set new Line if you want to add more than one item.").getIntList();
		config.save();
	}
	@Init
	public void load(FMLInitializationEvent event)
	{
		proxy.registerClientInfomations();
//		MinecraftForge.EVENT_BUS.register(new InteractBlockHook());
		blockHNPCs = new BlockHNPCs(blockHNPCsId).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockhnpcs").setCreativeTab(tab);
		GameRegistry.registerBlock(blockHNPCs, ItemBlockHNPCs.class, "HNPCsBlock");
		GameRegistry.registerTileEntity(TileGoalBlock.class, "HNPCs:Goal");
		GameRegistry.registerTileEntity(TileSpawnBlock.class, "HNPCs:Spawn");
		EntityRegistry.registerModEntity(EntityHNPCs.class, "entityHNPCs", 0, this, 1048, 1, true);
		EntityRegistry.registerModEntity(EntityDummyGoal.class, "entityHNPCs", 1, this, 64, 1, true);
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		LanguageRegistry.addName(blockHNPCs, "HNPCsBlock");
		LanguageRegistry.instance().addStringLocalization("tile.HNPCs.0.name", "GoalBlock");
		LanguageRegistry.instance().addStringLocalization("tile.HNPCs.1.name", "SpawnBlock");
	}
	
//	public class InteractBlockHook
//	{
//		@ForgeSubscribe
//		public void InteractBlock(PlayerInteractEvent event)
//		{
//			TileEntity tile = event.entityPlayer.worldObj.getBlockTileEntity(event.x, event.y, event.z);
//			ItemStack item = event.entityPlayer.getCurrentEquippedItem();
//			if(item != null
//					&& item.getItem() instanceof ItemBlockHNPCs
//					&& !event.entityPlayer.worldObj.isRemote
//					&& event.action == event.action.RIGHT_CLICK_BLOCK
//					&& tile != null
//					&& tile instanceof TileGoalBlock)
//			{
//				NBTTagCompound nbt;
//				if(!item.hasTagCompound())
//				{
//					item.setTagCompound(new NBTTagCompound("tag"));
//				}
//				if(!item.getTagCompound().hasKey("goalPos"))
//				{
//					item.getTagCompound().setCompoundTag("goalPos", new NBTTagCompound());
//				}
//				item.getTagCompound().getCompoundTag("goalPos").setInteger("posX", event.x);
//				item.getTagCompound().getCompoundTag("goalPos").setInteger("posY", event.y);
//				item.getTagCompound().getCompoundTag("goalPos").setInteger("posZ", event.z);
//				ChatPos(item);
//				event.setCanceled(true);
//			}
//		}
//		public void ChatPos(ItemStack item)
//		{
//			if(item.hasTagCompound())
//			{
//				NBTTagCompound nbt = item.getTagCompound();
//				int X = nbt.getCompoundTag("goalPos").getInteger("posX");
//				int Y = nbt.getCompoundTag("goalPos").getInteger("posX");
//				int Z = nbt.getCompoundTag("goalPos").getInteger("posX");
//				String string = String.format("Goal Position X:%d, Y:%d, Z:%d", X,Y,Z);
//				PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat(string));
//			}
//		}
//	}
}