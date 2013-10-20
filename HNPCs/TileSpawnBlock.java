package HNPCs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;


public class TileSpawnBlock extends TileHNPC implements IInventory
{
	public int goalPosX = 0;
	public int goalPosY = -1;
	public int goalPosZ = 0;
	private boolean canSpawn = false;
	private int spawnDelay = 20;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnRange = 4;
	private int spawnCount = 4;
	private int maxSpawnNum = 200;
	private boolean spawnInfinitely = false;
	private boolean activate = false;
	private ItemStack[] items = new ItemStack[5];
	private int count = 0;
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		goalPosX = nbt.getInteger("goalX");
		goalPosY = nbt.getInteger("goalY");
		goalPosZ = nbt.getInteger("goalZ");
		canSpawn = nbt.getBoolean("canSpawn");
		spawnDelay = nbt.getInteger("spawnDelay");
		minSpawnDelay = nbt.getInteger("minSpawnDelay");
		maxSpawnDelay = nbt.getInteger("maxSpawnDelay");
		spawnRange = nbt.getInteger("spawnRange");
		spawnCount = nbt.getInteger("spawnCount");
		maxSpawnNum = nbt.getInteger("maxSpawnNum");
		spawnInfinitely = nbt.getBoolean("spawnInfinitely");
		activate = nbt.getBoolean("activate");
		NBTTagList nbttaglist = nbt.getTagList("Items");
		this.items = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.items.length)
			{
				this.items[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("goalX", goalPosX);
		nbt.setInteger("goalY", goalPosY);
		nbt.setInteger("goalZ", goalPosZ);
		nbt.setBoolean("canSpawn", canSpawn);
		nbt.setInteger("spawnDelay", spawnDelay);
		nbt.setInteger("minSpawnDelay", minSpawnDelay);
		nbt.setInteger("maxSpawnDelay", maxSpawnDelay);
		nbt.setInteger("spawnRange", spawnRange);
		nbt.setInteger("spawnCount", spawnCount);
		nbt.setInteger("maxSpawnNum", maxSpawnNum);
		nbt.setBoolean("spawnInfinitely", spawnInfinitely);
		nbt.setBoolean("activate", activate);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
	}
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {}
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int meta){}
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z, int id){
		PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat("Break SpawnBlock!!"));
		if(!player.capabilities.isCreativeMode)
		{
//			Block block = Block.blocksList[id];
//			block.harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
		}
		return true;
	}
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
//		PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat("Break SpawnBlock!!"));
//		Block block = Block.blocksList[id];
//		block.dropBlockAsItem(world, x, y, z, 1, 0);
	}
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
		if(isActivate())
		{
			String string = String.format("The Goal Block of This Spawn Block is X:%d, Y: %d, Z: %d", this.goalPosX,this.goalPosY,this.goalPosZ);
			PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat(string));
		}
		else
		{
			player.openGui(HostilityNPCs.instance, HostilityNPCs.guiSBID, world, x, y, z);
		}
		return false;
	}
	public void updateEntity() {
		checkGoal();
		if(canSpawn && this.isActivate())
		{
			spawnMob();
		}
	}
	private void spawnMob(){
		double d0;

		if (this.worldObj.isRemote)
		{
			double d1 = (double)((float)this.xCoord + this.worldObj.rand.nextFloat());
			double d2 = (double)((float)this.yCoord + this.worldObj.rand.nextFloat());
			d0 = (double)((float)this.zCoord + this.worldObj.rand.nextFloat());
			this.worldObj.spawnParticle("smoke", d1, d2, d0, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("flame", d1, d2, d0, 0.0D, 0.0D, 0.0D);

			if (this.spawnDelay > 0)
			{
				--this.spawnDelay;
			}
			for (int i = 0; i < this.spawnCount; ++i)
			{
				this.count++;
				if(this.count >= this.maxSpawnNum && !this.spawnInfinitely)
					break;
			}
		}
		else
		{
			if (this.spawnDelay == -1)
			{
				this.randomDelay();
			}

			if (this.spawnDelay > 0)
			{
				--this.spawnDelay;
				return;
			}

			for (int i = 0; i < this.spawnCount; ++i)
			{
				EntityHNPCs entity = new EntityHNPCs(this.worldObj);
				this.count++;
				for(int j = 0;j<5;j++)
				{
					if(this.getStackInSlot(j) != null)
					{
						if(j != 0)
							entity.setCurrentItemOrArmor(j, this.getStackInSlot(5 - j));
						else
							entity.setCurrentItemOrArmor(j, this.getStackInSlot(j));
					}

				}
				int j = this.worldObj.getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand((double)(this.spawnRange * 2), 4.0D, (double)(this.spawnRange * 2))).size();

				d0 = (double)this.xCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * (double)this.spawnRange;
				double d3 = (double)(this.yCoord + this.worldObj.rand.nextInt(3) - 1);
				double d4 = (double)this.zCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * (double)this.spawnRange;
				entity.setLocationAndAngles(d0, d3, d4, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);

				entity.initCreature();
				this.worldObj.spawnEntityInWorld(entity);
				this.worldObj.playAuxSFX(2004, this.xCoord, this.yCoord, this.zCoord, 0);

				if (entity != null)
				{
					entity.spawnExplosionParticle();
				}
				if(this.count >= this.maxSpawnNum && !this.spawnInfinitely)
					break;
			}
			this.randomDelay();
		}
		canSpawn = this.count < this.maxSpawnNum || this.spawnInfinitely;
	}
	private void randomDelay(){
		if (this.maxSpawnDelay <= this.minSpawnDelay)
		{
			this.spawnDelay = this.minSpawnDelay;
		}
		else
		{
			int i = this.maxSpawnDelay - this.minSpawnDelay;
			this.spawnDelay = this.minSpawnDelay + this.worldObj.rand.nextInt(i);
		}
	}
	public void onChunkUnload(){
		canSpawn = false;
	}
	private void checkGoal()
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(goalPosX, goalPosY, goalPosZ);
		if(tile != null && tile instanceof TileGoalBlock && checkDistance(tile))
			canSpawn = true;
		else
			canSpawn = false;
	}
	private boolean checkDistance(TileEntity tile){
		double distance = MathHelper.sqrt_double(Math.pow(tile.xCoord - this.xCoord, 2) + Math.pow(tile.yCoord - this.yCoord, 2) + Math.pow(tile.zCoord - this.zCoord, 2));
		return distance < 512D;
	}
	public int getSpawnDelay() {
		return spawnDelay;
	}
	public void setSpawnDelay(int spawnDelay) {
		this.spawnDelay = spawnDelay;
	}
	public int getSpawnRange() {
		return spawnRange;
	}
	public void setSpawnRange(int spawnRange) {
		this.spawnRange = spawnRange;
	}
	public int getMaxSpawnNum() {
		return maxSpawnNum;
	}
	public void setMaxSpawnNum(int maxSpawnNum) {
		this.maxSpawnNum = maxSpawnNum;
	}
	public boolean isSpawnInfinitely() {
		return spawnInfinitely;
	}
	public void setSpawnInfinitely(boolean spawnInfinitely) {
		this.spawnInfinitely = spawnInfinitely;
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	public void setGoalPos(int x,int y, int z)
	{
		this.goalPosX = x;
		this.goalPosY = y;
		this.goalPosZ = z;
	}
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int ids)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ItemStack item = new ItemStack(ids, 1, metadata);
		NBTTagCompound nbt = new NBTTagCompound();
		item.setTagCompound(nbt);
		if(!nbt.hasKey("goalPos"))
		{
			nbt.setTag("goalPos", new NBTTagList("goalPos"));
			NBTTagList posAll = nbt.getTagList("goalPos");
			NBTTagCompound pos = new NBTTagCompound();
			pos.setInteger("posX", 0);
			pos.setInteger("posY", -1);
			pos.setInteger("posZ", 0);
			posAll.appendTag(pos);
		}
		((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posX", this.goalPosX);
		((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posY", this.goalPosY);
		((NBTTagCompound)nbt.getTagList("goalPos").tagAt(0)).setInteger("posZ", this.goalPosZ);
		ret.add(item);
		return ret;
	}
	public void readToPacket(ByteArrayDataInput data) {
		this.activate = data.readBoolean();
		this.canSpawn = data.readBoolean();
		this.maxSpawnNum = data.readInt();
		this.spawnInfinitely = data.readBoolean();
	}
	public void writeToPacket(DataOutputStream dos) {
		try {
			dos.writeBoolean(activate);
			dos.writeBoolean(canSpawn);
			dos.writeInt(maxSpawnNum);;
			dos.writeBoolean(spawnInfinitely);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void readToPacketS(ByteArrayDataInput data) {
		this.activate = data.readBoolean();
		this.canSpawn = data.readBoolean();
	}
	public void writeToPacketS(DataOutputStream dos) {
		try {
			dos.writeBoolean(activate);
			dos.writeBoolean(canSpawn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void readToPacketC(ByteArrayDataInput data) {
		this.maxSpawnNum = data.readInt();
		this.spawnInfinitely = data.readBoolean();
//		this.goalPosX = data.readInt();
//		this.goalPosY = data.readInt();
//		this.goalPosZ = data.readInt();
	}
	public void writeToPacketC(DataOutputStream dos) {
		try {
			dos.writeInt(maxSpawnNum);;
			dos.writeBoolean(spawnInfinitely);
//			dos.writeInt(goalPosX);
//			dos.writeInt(goalPosY);
//			dos.writeInt(goalPosZ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacketTileSpawn(this);
	}
	@Override
	public int getSizeInventory() {
		return items.length;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
	}
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(items[i] == null)
			return null;
		else
		{
			if(items[i].stackSize < j)
				j = items[i].stackSize;
			items[i].stackSize -= j;
			if(items[i].stackSize ==0)
				items[i] = null;
			return items[i];
		}
		
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return items[i];
	}
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
	}
	@Override
	public String getInvName() {
		return "spawnsetting";
	}
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	@Override
	public void onInventoryChanged(){

	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	@Override
	public void openChest() {
		
	}
	@Override
	public void closeChest() {
		
	}
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}