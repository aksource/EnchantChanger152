package IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class IRMergeableShapedRecipes extends ShapedRecipes{
	protected static Random rand = new Random();

	protected int recipeWidth;
	protected int recipeHeight;
    protected ItemStack[] recipeItems;
	protected List<ItemStack> outputs = new ArrayList<ItemStack>();

	public IRMergeableShapedRecipes(int width, int height, ItemStack[] items, ItemStack product) {
		super(width, height, items, product);

		recipeWidth = width;
		recipeHeight = height;
		recipeItems = items;
		outputs.add(product);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
		ItemStack output = outputs.get(rand.nextInt(outputs.size()));
		return new ItemStack(output.itemID, output.stackSize, output.getItemDamage());
	}

	public boolean canMerge(IRMergeableShapedRecipes that) {

		if (this.recipeWidth != that.recipeWidth || this.recipeHeight != that.recipeHeight) return false;

		return checkMergeable(this.recipeWidth, this.recipeHeight, this.recipeItems, that.recipeItems, false)
			|| checkMergeable(this.recipeWidth, this.recipeHeight, this.recipeItems, that.recipeItems, true);
	}

	private boolean checkMergeable(int width, int height, ItemStack[] thisItems, ItemStack[] thatItems, boolean asMirrorRecipe) {

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				ItemStack a = thisItems[i * width + j];
				ItemStack b = thatItems[i * width + (asMirrorRecipe ?  width - j - 1 : j)];
				if (a == null && b == null) continue;
				if (a == null || b == null) return false;
				if (a.itemID != b.itemID) return false;
				if (a.getItemDamage() != -1 && a.getItemDamage() != b.getItemDamage()) return false;
			}
		}

		return true;
	}

	public void mergeRecipe(IRMergeableShapedRecipes mergeTarget) {
		this.outputs.addAll(mergeTarget.outputs);
		mergeTarget.outputs = null;
		mergeTarget.recipeItems = null;
	}

}
