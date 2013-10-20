package HNPCs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import HNPCs.Client.GuiSpawnBlock;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == HostilityNPCs.guiSBID)
		{
			TileSpawnBlock tile = (TileSpawnBlock) world.getBlockTileEntity(x, y, z);
			return new ContainerSpawnBlock(player.inventory,tile);
		}
		else
			return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == HostilityNPCs.guiSBID)
		{
			TileSpawnBlock tile = (TileSpawnBlock) world.getBlockTileEntity(x, y, z);
			return new GuiSpawnBlock(player.inventory,tile);
		}
		else
			return null;
	}
	public void registerClientInfomations(){}
}