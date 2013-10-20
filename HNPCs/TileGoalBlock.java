package HNPCs;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileGoalBlock extends TileHNPC
{
	private EntityLiving dummyGoal;
	public void updateEntity() {
		if(dummyGoal != null && dummyGoal.isDead)
		{
			this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}
	}
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	public void setDummyGoal(EntityLiving entity)
	{
		dummyGoal = entity;
	}
	public EntityLiving getDummyGoal()
	{
		return dummyGoal;
	}
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {}
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int meta){
		EntityLiving goal = new EntityDummyGoal(world,x,y,z);
		goal.setLocationAndAngles(x + 0.5f, y, z + 0.5f, 0, 0);
		goal.initCreature();
		this.setDummyGoal(goal);
		if(!world.isRemote)
			world.spawnEntityInWorld(goal);
	}
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z, int id){
		return true;
	}
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		if(this.getDummyGoal() == null)
		{
			List list = world.getEntitiesWithinAABB(EntityDummyGoal.class, AxisAlignedBB.getBoundingBox(x, y , z, x + 1, y + 1, z + 1));
			this.setDummyGoal((EntityLiving) list.get(0));
		}
		if(this.getDummyGoal() != null)
		{
			this.getDummyGoal().setDead();
		}
	}
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
//		ItemStack item = player.getCurrentEquippedItem();
//		if(item != null && item.getItem() instanceof ItemBlockHNPCs && item.getItemDamage() ==1)
//		{
//			NBTTagCompound nbt;
//			if(item.hasTagCompound())
//			{
//				nbt = item.getTagCompound();
//			}
//			else
//			{
//				nbt = new NBTTagCompound();
//				item.setTagCompound(nbt);
//			}
//			if(!nbt.hasKey("goalPos"))
//			{
//				nbt.setTag("goalPos", new NBTTagList("goalPos"));
//				NBTTagList posAll = nbt.getTagList("goalPos");
//				NBTTagCompound pos = new NBTTagCompound();
//				pos.setInteger("posX", 0);
//				pos.setInteger("posY", -1);
//				pos.setInteger("posZ", 0);
//				posAll.appendTag(pos);
//			}
//			((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posX", x);
//			((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posY", y);
//			((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posZ", z);
//		}
		return false;
	}
}