package IR;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.minecraft.src.ModLoader;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraftforge.client.MinecraftForgeClient;
import IR.IRUtil.InstantRecipesParserJS;
import IR.IRUtil.Record;
import IR.IRUtil.RecordArmor;
import IR.IRUtil.RecordBlock;
import IR.IRUtil.RecordFood;
import IR.IRUtil.RecordItem;
import IR.IRUtil.RecordParserJS;
import IR.IRUtil.RecordParserJSArmor;
import IR.IRUtil.RecordParserJSBlock;
import IR.IRUtil.RecordParserJSFood;
import IR.IRUtil.RecordParserJSItem;
import IR.IRUtil.RecordParserJSRecipeAlias;
import IR.IRUtil.RecordParserJSSword;
import IR.IRUtil.RecordParserJSTool;
import IR.IRUtil.RecordRecipeAlias;
import IR.IRUtil.RecordSword;
import IR.IRUtil.RecordTool;
import IR.IRUtil.TextureBinder;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="InstantRecipes", name="InstantRecipes", version="0.2 for mc152")
@NetworkMod(clientSideRequired=true, serverSideRequired=false/*, channels={"EC|Levi","EC|CSC","EC|CS","EC|Sw"}, packetHandler=Packet_EnchantChanger.class*/)
public class InstantRecipes
{
	@Instance("InstantRecipes")
	public static InstantRecipes instance;
	//@SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
	//public static CommonProxy proxy;
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{

	}
	@Init
	public void load(FMLInitializationEvent event)
	{
		if (StatList.mineBlockStatArray.length != 4096) {
			StatBase[] old = StatList.mineBlockStatArray;
			StatBase[] neo = new StatBase[4096];
			System.arraycopy(old, 0, neo, 0, old.length);
			StatList.mineBlockStatArray = neo;
		}

		File homeDir = new File(ModLoader.getMinecraftInstance().getMinecraftDir(), "mods/InstantRecipes");

		if (!homeDir.exists()) {
			homeDir.mkdir();
			return;
		}

		try {
			Record[] records = InstantRecipesParserJS.parse(homeDir, ".json");

			String[] textures = TextureBinder.bindTextures(homeDir, records, new TextureBinder.IImageLoader() {
				public BufferedImage load(String path) throws IOException {
					try {
						return ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, path);
					} catch (Exception e) {
						throw new IOException("画像 " + path + "が見つかりませんでした。");
					}
				}
			});

			for (String texture : textures) {
				MinecraftForgeClient.preloadTexture(texture);
			}

			IRItemFactory.constructRecords(records);
			IRItemFactory.addRecipes(records);
		} catch (InvalidSyntaxException e) {
			ModLoader.getLogger().severe("InstantRecipe Error Message :: " + e.toString());
			throw new RuntimeException("JOSNファイル読込中に構文エラーが検出されました。\n http://www.jsoneditoronline.org/ などを利用し、JSONファイルの構文エラーを取り除いてから再度実行してください。\n" + e.getMessage());
		} catch (Exception e) {
			ModLoader.getLogger().severe("InstantRecipe Error Message :: " + e.toString());
			throw new RuntimeException(e);
		}
	}
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{

	}
	public static void linkRecord(String type, RecordParserJS parser, IRConstructorBase constructor) {
		InstantRecipesParserJS.registerParser(type, parser);
		IRItemFactory.registerConstructor(type, constructor);
	}
	static {
		linkRecord(RecordItem.TYPE, new RecordParserJSItem(), new IRConstructorItem());
		linkRecord(RecordFood.TYPE, new RecordParserJSFood(), new IRConstructorFood());
		linkRecord(RecordBlock.TYPE, new RecordParserJSBlock(), new IRConstructorBlock());
		linkRecord(RecordArmor.TYPE, new RecordParserJSArmor(), new IRConstructorArmor());
		linkRecord(RecordTool.TYPE, new RecordParserJSTool(), new IRConstructorTool());
		linkRecord(RecordSword.TYPE, new RecordParserJSSword(), new IRConstructorSword());
		linkRecord(RecordRecipeAlias.TYPE, new RecordParserJSRecipeAlias(), new IRConstructorDummy());
	}
}