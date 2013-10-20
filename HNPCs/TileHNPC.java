package HNPCs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileHNPC extends TileEntity
{
	public abstract void onBlockAdded(World world, int x, int y, int z);
	public abstract void onPostBlockPlaced(World world, int x, int y, int z, int meta);
	public abstract boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z, int id);
	public abstract void breakBlock(World world, int x, int y, int z, int id, int meta);
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ);
}