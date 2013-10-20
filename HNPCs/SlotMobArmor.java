package HNPCs;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMobArmor extends Slot
{
	private int armortype;
	public SlotMobArmor(IInventory inv, int index, int x, int y, int at)
	{
		super(inv, index, x, y);
		armortype = at;
	}
	
	public int getSlotStackLimit(){
		return 1;
	}
	public boolean isItemValid(ItemStack item)
	{
		if(item == null)
			return false;
		else if(item.getItem().isValidArmor(item, armortype))
		{
			return true;
		}
		else
			return false;
	}
}