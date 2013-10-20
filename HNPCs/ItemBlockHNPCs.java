package HNPCs;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHNPCs extends ItemBlock
{
	public ItemBlockHNPCs(int par1) {
		super(par1);
		this.setHasSubtypes(true);
	}
	@Override
	public String getUnlocalizedName(ItemStack item)
	{
		int meta = item.getItemDamage();
		return String.format("tile.HNPCs.%d", meta);
	}
	@SideOnly(Side.CLIENT)

	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean par4) {
		int meta = item.getItemDamage();
		if(meta == 1 && item.hasTagCompound())
		{
			NBTTagCompound nbt = item.getTagCompound();
			int X = nbt.getCompoundTag("goalPos").getInteger("posX");
			int Y = nbt.getCompoundTag("goalPos").getInteger("posX");
			int Z = nbt.getCompoundTag("goalPos").getInteger("posX");
			String string = String.format("Goal Position X:%d, Y:%d, Z:%d", X,Y,Z);
			list.add(string);
		}
	}
	public int getMetadata(int meta)
	{
		return meta;
	}
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileGoalBlock)
		{
			NBTTagCompound nbt;
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound("tag"));
			}
			if(!stack.getTagCompound().hasKey("goalPos"))
			{
				stack.getTagCompound().setCompoundTag("goalPos", new NBTTagCompound());
			}
			stack.getTagCompound().getCompoundTag("goalPos").setInteger("posX", x);
			stack.getTagCompound().getCompoundTag("goalPos").setInteger("posY", y);
			stack.getTagCompound().getCompoundTag("goalPos").setInteger("posZ", z);
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int i1 = world.getBlockId(x, y, z);
		if (i1 == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1)
		{
			side = 1;
		}
		else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
				&& (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(world, x, y, z)))
		{
			if (side == 0)
			{
				--y;
			}

			if (side == 1)
			{
				++y;
			}

			if (side == 2)
			{
				--z;
			}

			if (side == 3)
			{
				++z;
			}

			if (side == 4)
			{
				--x;
			}

			if (side == 5)
			{
				++x;
			}
		}

		if (item.stackSize == 0)
		{
			return false;
		}
		else if (!player.canPlayerEdit(x, y, z, side, item))
		{
			return false;
		}
		else if (world.canPlaceEntityOnSide(this.getBlockID(), x, y, z, false, side, player, item))
		{
			Block block = HostilityNPCs.blockHNPCs;;
			int j1 = this.getMetadata(item.getItemDamage());
			int k1 = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, j1);

			if (placeBlockAt(item, player, world, x, y, z, side, hitX, hitY, hitZ, k1))
			{
				if(item.hasTagCompound() && world.getBlockTileEntity(x, y, z) instanceof TileSpawnBlock)
				{
					TileSpawnBlock tile = (TileSpawnBlock) world.getBlockTileEntity(x, y, z);
					NBTTagCompound nbt = item.getTagCompound();
					int goalX = nbt.getCompoundTag("goalPos").getInteger("posX");
					int goalY = nbt.getCompoundTag("goalPos").getInteger("posX");
					int goalZ = nbt.getCompoundTag("goalPos").getInteger("posX");
					tile.setGoalPos(goalX,goalY,goalZ);
				}
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--item.stackSize;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}