package IR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import IR.IRUtil.IRInvalidFormatException;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.Record;

/**
 * Recordに対応する、実際のItemやらBlockやらの具象化を行うクラス
 * @author gnsk
 *
 */
public class IRItemFactory {

	private static final Map<String, IRConstructorBase> constructorMap = new HashMap<String, IRConstructorBase>();

	public static void registerConstructor(String targetRecordType, IRConstructorBase constructor) {
		constructorMap.put(targetRecordType, constructor);
	}

	public static void constructRecords(Record[] records) throws IOException, IRInvalidFormatException, IRInvalidValueException {
		IRFuelHandler fuelHandler = new IRFuelHandler();

		for (Record record : records) {
			String type = record.getRecordType();
			IRConstructorBase constructor = constructorMap.get(type);
			if (constructor == null)
				throw new IRInvalidValueException("Typeに対応するConstructorが定義されていません", record);

			if (record.burnTime != 0)
				fuelHandler.addFuel(record.getItemListIndex(), record.burnTime);

			if (constructor instanceof IRConstructorDummy) continue;

			int itemId = record.getItemListIndex();
			if (Item.itemsList[itemId] != null) {
				throw new IRInvalidValueException(new StringBuilder().append("ID:").append(itemId)
					.append(" は Item:").append(Item.itemsList[itemId].getUnlocalizedName()).append(" で既に使用されています").toString(), record);
			}

			try {
				constructor.construct(record);
			} catch (IRInvalidValueException e) {
				e.setCauseRecord(record);
				throw e;
			}
		}

		if (fuelHandler.size() != 0) GameRegistry.registerFuelHandler(fuelHandler);

	}

	public static void addRecipes(Record[] records) throws IOException {
		IRItemEnchanter enchanter = new IRItemEnchanter();

		List<IRMergeableShapedRecipes> shapedRecipes = new ArrayList<IRMergeableShapedRecipes>();
		List<IRMergeableShapelessRecipes> shapelessRecipes = new ArrayList<IRMergeableShapelessRecipes>();

		for (Record record : records) {
			if (record.recipe == null) continue;

			ItemStack product = new ItemStack(record.getItemListIndex(), record.recipe.productCount, record.recipe.meta);

			if (record.enchantments != null && record.enchantments.length != 0) {
				enchanter.addEnchantment(product.itemID, record.enchantments);
			}

			if (record.recipe.form != null) {
				addShapedRecipe(shapedRecipes, record, product);
			} else if (record.recipe.parts != null) {
				addShapelessRecipe(shapelessRecipes, record, product);
			}

			if (record.recipe.smelting != null) {
				if (record.recipe.smelting.hasMetadata()) {
					FurnaceRecipes.smelting().addSmelting(record.recipe.smelting.id, record.recipe.smelting.meta, product, 0);
				} else {
					FurnaceRecipes.smelting().addSmelting(record.recipe.smelting.id, product, 0);
				}
			}
		}

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRMergeableShapedRecipes r : shapedRecipes) recipes.add(r);
		for (IRMergeableShapelessRecipes r : shapelessRecipes) recipes.add(r);

		if (enchanter.size() != 0) {
			GameRegistry.registerCraftingHandler(enchanter);
		}
	}

	private static Map<Character, ItemStack> partsCacheMap = new HashMap<Character, ItemStack>();

	private static void addShapedRecipe(List<IRMergeableShapedRecipes> shapedRecipes, Record record, ItemStack product) {
		partsCacheMap.clear();

		for (Entry<Character, Record.Stuff> entry : record.recipe.partsMap.entrySet()) {
			Record.Stuff stuff = entry.getValue();
			if (stuff.hasMetadata()) {
				partsCacheMap.put(entry.getKey(), new ItemStack(stuff.id, 1, stuff.meta));
			} else {
				partsCacheMap.put(entry.getKey(), new ItemStack(stuff.id, 1, -1));
			}
		}

		int width = record.recipe.form[0].length();
		int height = record.recipe.form.length;

		ItemStack[] recipes = new ItemStack[width * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				recipes[i * width + j] = partsCacheMap.get(record.recipe.form[i].charAt(j));
			}
		}

		IRMergeableShapedRecipes newrecipe = new IRMergeableShapedRecipes(width, height, recipes, product);

		for (IRMergeableShapedRecipes r : shapedRecipes) {
			if (r.canMerge(newrecipe)) {
				r.mergeRecipe(newrecipe);
				return;
			}
		}

		shapedRecipes.add(newrecipe);
	}

	private static void addShapelessRecipe(List<IRMergeableShapelessRecipes> shapelessRecipes, Record record, ItemStack product) {
		List<ItemStack> recipes = new ArrayList<ItemStack>();

		for (Record.Stuff stuff : record.recipe.parts) {
			if (stuff.hasMetadata()) {
				recipes.add(new ItemStack(stuff.id, 1, stuff.meta));
			} else {
				recipes.add(new ItemStack(stuff.id, 1, -1));
			}
		}

		IRMergeableShapelessRecipes newrecipe = new IRMergeableShapelessRecipes(product, recipes);

		for (IRMergeableShapelessRecipes r : shapelessRecipes) {
			if (r.canMerge(newrecipe)) {
				r.mergeRecipe(newrecipe);
				return;
			}
		}

		shapelessRecipes.add(newrecipe);
	}
}