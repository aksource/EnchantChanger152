package MergeEnchantment;

import java.util.Arrays;

import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="MergeEnchantment", name="MergeEnchantment", version="1.2b",dependencies="required-after:FML")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class MergeEnchantment
{
	@Mod.Instance("MergeEnchantment")
	public static MergeEnchantment instance;
	
	public static CraftingManager cm;
	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		setUpModInfo(event.getModMetadata());
	}
	private void setUpModInfo(ModMetadata meta)
	{
		meta.authorList = Arrays.asList(new String[]{"アルゴ"});
		meta.autogenerated = true;
		meta.description = "Add recipes that can merge tools enchantments";
		meta.logoFile = "";
		meta.url = "http://forum.minecraftuser.jp/viewtopic.php?f=13&t=6672";
		meta.credits = "Updated By A.K.";
	}
	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		cm = CraftingManager.getInstance();
		cm.getRecipeList().add(new AddEnchantmentRecipes());
		cm.getRecipeList().add(new MergeEnchantmentRecipes());
	}
}