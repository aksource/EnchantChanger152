package IR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class IRMergeableShapelessRecipes extends ShapelessRecipes {
	protected static Random rand = new Random();

	protected List<ItemStack> sortedRecipeItems;
	protected List<ItemStack> outputs = new ArrayList<ItemStack>();


	public IRMergeableShapelessRecipes(ItemStack product, List<ItemStack> items) {
		super(product, items);

		// メタ指定とメタ無視が混ざっていた場合でも処理できるようにitems自体を並び替えておく
		Collections.sort(items, new Comparator(){
			@Override
			public int compare(Object arg0, Object arg1) {
				ItemStack a = (ItemStack)arg0;
				ItemStack b = (ItemStack)arg1;

				if (a.itemID != b.itemID) return Integer.valueOf(a.itemID).compareTo(b.itemID);
				return Integer.valueOf(b.getItemDamage()).compareTo(a.getItemDamage());
			}
		});

		sortedRecipeItems = items;
		outputs.add(product);
	}

	public boolean canMerge(IRMergeableShapelessRecipes that) {

		if (this.sortedRecipeItems.size() != that.sortedRecipeItems.size()) return false;

		for (int i = 0; i < this.sortedRecipeItems.size(); i++) {
			ItemStack a = this.sortedRecipeItems.get(i);
			ItemStack b = that.sortedRecipeItems.get(i);

			if (a.itemID != b.itemID) return false;
			if (a.getItemDamage() != b.getItemDamage()) return false;
		}

		return true;
	}

	public void mergeRecipe(IRMergeableShapelessRecipes mergeTarget) {
		this.outputs.addAll(mergeTarget.outputs);
		mergeTarget.outputs = null;
		mergeTarget.sortedRecipeItems = null;
	}

}
