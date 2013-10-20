package HNPCs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSpawnBlock extends Container
{
	private TileSpawnBlock tilespawn;
	public ContainerSpawnBlock(InventoryPlayer inv, TileSpawnBlock tile)
	{
		tilespawn = tile;
		int i;
		this.addSlotToContainer(new Slot(tile, 0, 80,8));
		for (i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new SlotMobArmor(tile, i + 1, 8, 8 + i * 18, i));
		}
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
		}
	}
    public void onCraftMatrixChanged(IInventory inv)
    {
    	inv.onInventoryChanged();
    }
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum){
		Slot slot = (Slot) this.inventorySlots.get(slotNum);
		ItemStack item = null;
		if(slot != null && slot.getHasStack()){
			ItemStack item1 = slot.getStack();
			item = item1.copy();
			if(slotNum >= 0 && slotNum < 5)
			{
				slot.putStack(null);
				slot.onSlotChanged();
				return null;
			}
			else
			{
				for(int i = 0; i < 4;i++)
				{
					if(item.getItem().isValidArmor(item, i))
					{
						item.stackSize = 1;
//						((Slot) this.inventorySlots.get(i + 1)).putStack(item);
						tilespawn.setInventorySlotContents(i + 1, item);
						slot.onSlotChanged();
						return item1;
					}
				}
//				if(!((Slot) this.inventorySlots.get(0)).getHasStack())
//				{
					item.stackSize = 1;
//					((Slot) this.inventorySlots.get(0)).putStack(item);
					tilespawn.setInventorySlotContents(0, item);
					slot.onSlotChanged();
					return item1;
//				}
//				else if(slotNum >=5 && slotNum < 27 + 5)
//				{
//					if(!this.mergeItemStack(item1, 5, 27 + 5, false))
//						return null;
//				}
//				else if(slotNum >=27 + 5 && slotNum < 5 + 27 + 9 && !this.mergeItemStack(item1, 27 + 5, 9 + 27 + 5, false))
//					return null;
			}

		}
		return item;
	}
	@Override
	public ItemStack slotClick(int slot, int mouse, int key, EntityPlayer player){
		Slot slot1;
		if(slot > 4 && slot < this.inventorySlots.size()/* || slot < 0*/)
			return super.slotClick(slot, mouse, key, player);
		else if(player.inventory.getItemStack() != null)
		{
			ItemStack handItem = player.inventory.getItemStack();
			ItemStack copyItem = handItem.copy();
			if(slot != 0 && handItem.getItem().isValidArmor(handItem, slot - 1))
			{
				slot1 = (Slot) this.inventorySlots.get(slot);
				copyItem.stackSize = 1;
				slot1.putStack(copyItem);
				slot1.onSlotChanged();
			}
			else if(slot == 0)
			{
				slot1 = (Slot) this.inventorySlots.get(slot);
				copyItem.stackSize = 1;
				slot1.putStack(copyItem);
				slot1.onSlotChanged();
			}
			return handItem;
		}
		else
		{
			slot1 = (Slot) this.inventorySlots.get(slot);
			slot1.putStack(null);
			slot1.onSlotChanged();
			return null;
		}
	}
	@Override
	protected void retrySlotClick(int slot, int mouse, boolean shift, EntityPlayer player){}
}