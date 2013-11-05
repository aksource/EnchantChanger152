package ak.MultiToolHolders.Client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.MultiToolHolders.ToolHolderData;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ClientTickHandler implements ITickHandler
{
	private Minecraft mc = Minecraft.getMinecraft();
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.PLAYER)))
		{
            this.playerTick(((World)tickData[1]), (EntityClientPlayerMP)tickData[0]);
		}
	}
	private void playerTick(World world, EntityClientPlayerMP player)
	{
		ItemStack item = player.getCurrentEquippedItem();
		if(item != null && item.getItem() instanceof ItemMultiToolHolder)
		{
			ToolHolderData tooldata = ItemMultiToolHolder.getToolData(item, world);
			int slot = ((ItemMultiToolHolder)item.getItem()).SlotNum;
			if(tooldata != null && tooldata.getStackInSlot(slot) != null)
			{
				ItemMultiToolHolder.renderToolsInfo(tooldata.getStackInSlot(slot));
			}
			else
			{
				System.out.println("check");
			}
		}
	}
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return this.getClass().toString();
	}
	
}