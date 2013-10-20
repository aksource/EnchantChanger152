package HNPCs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHNPCs extends BlockContainer
{
	public BlockHNPCs(int id)
	{
		super(id, Material.iron);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister reg)
	{
		
	}
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta)
	{
		if(meta == 0)
			return Block.bedrock.getIcon(side, meta);
		else
			return Block.mobSpawner.getIcon(side, meta);
	}
	public boolean isOpaqueCube()
	{
		return false;
	}
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
		if(tile != null)
			tile.onPostBlockPlaced(world, x, y, z, meta);
		super.onPostBlockPlaced(world, x, y, z, meta);
    }
//	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
//	{
//		dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
//
//		super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
//	}
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
		if(tile != null)
			tile.removeBlockByPlayer(world, player, x, y, z, this.blockID);
		return super.removeBlockByPlayer(world, player, x, y, z);
    }
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
		if(tile != null)
			tile.breakBlock(world, x, y, z, id, meta);
		super.breakBlock(world, x, y, z, id, meta);
	}
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
		if(tile != null)
			return tile.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
		else
			return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
    public void onNeighborBlockChange(World world, int x, int y, int z, int NeiborID)
    {
    	boolean rsSignale = world.isBlockIndirectlyGettingPowered(x, y, z);
		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileSpawnBlock)
			((TileSpawnBlock)tile).setActivate(rsSignale);
    }
    public int idDropped(int meta, Random rand, int fortune)
    {
        return this.blockID;
    }
    public int damageDropped(int meta)
    {
    	return meta;
    }
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
//		TileHNPC tile = (TileHNPC) world.getBlockTileEntity(x, y, z);
//		if(tile != null && tile instanceof TileSpawnBlock)
//			return ((TileSpawnBlock)tile).getBlockDropped(world, x, y, z, this.damageDropped(metadata), this.blockID);
//		else
			return super.getBlockDropped(world, x, y, z, metadata, fortune);
    }
	@Override
	public TileEntity createNewTileEntity(World world) {
		return createTileEntity(world,0);
	}
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if(metadata == 0)
			return new TileGoalBlock();
		else if(metadata == 1)
			return new TileSpawnBlock();
		else
			return null;
	}
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
}