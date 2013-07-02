package IR;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class IRFuelHandler implements IFuelHandler {
	Map<Integer, Integer> fuelMap = new HashMap<Integer, Integer>();

	public void addFuel(int itemid, int burntime) {
		fuelMap.put(itemid, burntime);
	}

	public int size() {
		return fuelMap.size();
	}

	@Override
	public int getBurnTime(ItemStack stack) {
		Integer burntime = fuelMap.get(stack.itemID);
		if (burntime != null) return burntime;
		return 0;
	}

}
