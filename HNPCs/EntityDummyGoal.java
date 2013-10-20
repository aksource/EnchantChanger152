package HNPCs;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityDummyGoal extends EntityLiving
{
	private int setX;
	private int setY;
	private int setZ;
	private TileEntity tile;
	public EntityDummyGoal(World world)
	{
		super(world);
	}
	public EntityDummyGoal(World world, int x, int y, int z)
	{
		this(world);
//		this.noClip = true;
//		this.posX = x;
//		this.posY = y;
//		this.posZ = z;
		setX = x;
		setY = y;
		setZ = z;
		tile = world.getBlockTileEntity(x, y, z);
		this.height = 2.0f;
		this.width = 1.0f;
	}
	@Override
	public int getMaxHealth() {
		return 20;
	}
	protected void entityInit() {
		super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
	}
	public void onUpdate()
	{
		super.onUpdate();
		this.setLocationAndAngles(setX + 0.5f, setY, setZ + 0.5f, 0, 0);
//		tile = this.worldObj.getBlockTileEntity(setX, setY, setZ);
//		if(tile == null)
//		{
//			setDead();
//		}
	}
	public void setDead()
	{
		System.out.println("Tile not found!");
		super.setDead();
	}
//	public boolean attackEntityFrom(DamageSource dmgsource, int dmg)
//	{
//		if(dmgsource.getEntity() != null && dmgsource.getEntity() instanceof EntityHNPCs)
//			return super.attackEntityFrom(dmgsource, dmg);
//		else
//			return false;
//	}
	public void onDeath(DamageSource dmgsrc)
	{
		PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat("Broke GoalBlock!! You lose!!"));
	}
//	public boolean canBeCollidedWith()
//	{
//		return false;
//	}
//	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {}
//	public void addVelocity(double par1, double par3, double par5){}
    protected boolean canDespawn()
    {
        return false;
    }
	protected void fall(float par1) {}
	public void writeEntityToNBT(NBTTagCompound nbt){
		super.writeEntityToNBT(nbt);
		nbt.setInteger("fixX", setX);
		nbt.setInteger("fixY", setY);
		nbt.setInteger("fixZ", setZ);
	}
	public void readEntityFromNBT(NBTTagCompound nbt){
		super.readEntityFromNBT(nbt);
		setX = nbt.getInteger("fixX");
		setY = nbt.getInteger("fixY");
		setZ = nbt.getInteger("fixZ");
	}
}