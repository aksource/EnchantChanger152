package IR;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import IR.IRUtil.Record;
import cpw.mods.fml.common.ICraftingHandler;

/**
 * アイテムの初期エンチャントを実現するためのクラス
 * @author gnsk
 *
 */
public class IRItemEnchanter implements ICraftingHandler {

	private static class EnchantmetData {
		public Enchantment enchantment;
		public int level;
	}

	private Map<Integer, Record.Enchantment[]> enchantmentMap = new HashMap<Integer, Record.Enchantment[]>();

	public void addEnchantment(int itemId, Record.Enchantment[] enchs) {
		enchantmentMap.put(itemId, enchs);
	}

	public int size() {
		return enchantmentMap.size();
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack stack, IInventory craftMatrix) {
		Record.Enchantment[] enchs = enchantmentMap.get(stack.itemID);
		if (enchs != null) {
			for (Record.Enchantment ench : enchs) {
				stack.addEnchantment(Enchantment.enchantmentsList[ench.id], ench.level);
			}
		}
	}


	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {

	}

}
