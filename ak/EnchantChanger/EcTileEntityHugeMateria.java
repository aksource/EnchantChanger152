package ak.EnchantChanger;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class EcTileEntityHugeMateria extends TileEntity implements IInventory {

	private ItemStack[] Hugeitemstacks = new ItemStack[5];
	private int[][] EnchArray;
	private ItemStack[] MaterialArray;
	private ArrayList<Integer> magicArray;
	private ItemStack result;
	public int MaterializingTime = 0;
	public float angle = 0;
	public EcTileEntityHugeMateria()
	{
		EnchArray = new int[EcItemMasterMateria.MasterMateriaNum - 1][10];
		MaterialArray = new ItemStack[Enchantment.enchantmentsList.length + 3];
		magicArray = new ArrayList<Integer>(EcItemMateria.MagicMateriaNum);
		result = null;
		initArrays();
	}
	private void initArrays()
	{
		int i,j;
		for(i = 0;i<EnchArray.length;i++){
			for(j = 0;j<EnchArray[i].length;j++){
				EnchArray[i][j] = -1;
			}
		}
		for(i=0;i<256 + 3;i++){
			MaterialArray[i]=null;
		}
		EnchArray[0][0] = EnchantChanger.EnchantmentMeteoId;
		EnchArray[0][1] = EnchantChanger.EndhantmentHolyId;
		EnchArray[0][2] = EnchantChanger.EnchantmentTelepoId;
		EnchArray[0][3] = EnchantChanger.EnchantmentFloatId;
		EnchArray[0][4] = EnchantChanger.EnchantmentThunderId;
		EnchArray[0][5] = 256;
		EnchArray[0][6] = 257;
		EnchArray[0][7] = 258;
		EnchArray[1][0] = Enchantment.protection.effectId;
		EnchArray[1][1] = Enchantment.fireProtection.effectId;
		EnchArray[1][2] = Enchantment.featherFalling.effectId;
		EnchArray[1][3] = Enchantment.blastProtection.effectId;
		EnchArray[1][4] = Enchantment.projectileProtection.effectId;
		EnchArray[1][5] = Enchantment.thorns.effectId;
		EnchArray[2][0] = Enchantment.respiration.effectId;
		EnchArray[2][1] = Enchantment.aquaAffinity.effectId;
		EnchArray[3][0] = Enchantment.sharpness.effectId;
		EnchArray[3][1] = Enchantment.smite.effectId;
		EnchArray[3][2] = Enchantment.baneOfArthropods.effectId;
		EnchArray[3][3] = Enchantment.knockback.effectId;
		EnchArray[3][4] = Enchantment.fireAspect.effectId;
		EnchArray[3][5] = Enchantment.looting.effectId;
		EnchArray[4][0] = Enchantment.efficiency.effectId;
		EnchArray[4][1] = Enchantment.silkTouch.effectId;
		EnchArray[4][2] = Enchantment.unbreaking.effectId;
		EnchArray[4][3] = Enchantment.fortune.effectId;
		EnchArray[5][0] = Enchantment.power.effectId;
		EnchArray[5][1] = Enchantment.punch.effectId;
		EnchArray[5][2] = Enchantment.flame.effectId;
		EnchArray[5][3] = Enchantment.infinity.effectId;
		MaterialArray[0] = new ItemStack(Item.ingotIron);
		MaterialArray[1] = new ItemStack(Item.blazePowder);
		MaterialArray[2] = new ItemStack(Item.feather);
		MaterialArray[3] = new ItemStack(Item.gunpowder);
		MaterialArray[4] = new ItemStack(Item.arrow);
		MaterialArray[5] = new ItemStack(Item.reed);
		MaterialArray[6] = new ItemStack(Item.pickaxeGold);
		MaterialArray[7] = new ItemStack(Block.cactus);
		MaterialArray[16] = new ItemStack(Item.fireballCharge);
		MaterialArray[17] = new ItemStack(Item.flintAndSteel);
		MaterialArray[18] = new ItemStack(Item.spiderEye);
		MaterialArray[19] = new ItemStack(Item.slimeBall);
		MaterialArray[20] = new ItemStack(Item.blazeRod);
		MaterialArray[21] = new ItemStack(Item.appleGold);
		MaterialArray[32] = new ItemStack(Item.pickaxeGold);
		MaterialArray[33] = new ItemStack(Item.silk);
		MaterialArray[34] = new ItemStack(Item.ingotIron);
		MaterialArray[35] = new ItemStack(Item.appleGold);
		MaterialArray[48] = new ItemStack(Item.fireballCharge);
		MaterialArray[49] = new ItemStack(Item.slimeBall);
		MaterialArray[50] = new ItemStack(Item.blazeRod);
		MaterialArray[51] = new ItemStack(Item.bow);
		MaterialArray[EnchantChanger.EnchantmentMeteoId] = new ItemStack(Block.dragonEgg);
		MaterialArray[EnchantChanger.EndhantmentHolyId] = new ItemStack(Item.appleGold, 1, 1);
		MaterialArray[EnchantChanger.EnchantmentTelepoId] = new ItemStack(Item.enderPearl);
		MaterialArray[EnchantChanger.EnchantmentFloatId] = new ItemStack(Item.eyeOfEnder);
		MaterialArray[EnchantChanger.EnchantmentThunderId] = new ItemStack(Block.blockGold);
		MaterialArray[256] = new ItemStack(Item.bucketMilk);
		MaterialArray[257] = new ItemStack(Item.bootsGold);
		MaterialArray[258] = new ItemStack(Item.netherStalkSeeds);
		magicArray.add(EnchantChanger.EnchantmentMeteoId);
		magicArray.add(EnchantChanger.EndhantmentHolyId);
		magicArray.add(EnchantChanger.EnchantmentTelepoId);
		magicArray.add(EnchantChanger.EnchantmentFloatId);
		magicArray.add(EnchantChanger.EnchantmentThunderId);
		magicArray.add(256);
		magicArray.add(257);
		magicArray.add(258);
	}
	@Override
	public int getSizeInventory()
	{
		return Hugeitemstacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return Hugeitemstacks[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		Hugeitemstacks[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}
	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	@SideOnly(Side.CLIENT)
	public int getMaterializingProgressScaled(int par1)
	{
		return this.MaterializingTime * par1/200;
	}
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.Hugeitemstacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.Hugeitemstacks.length)
			{
				this.Hugeitemstacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		this.MaterializingTime = par1NBTTagCompound.getShort("MaterializingTime");
	}
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("MaterializingTime", (short)this.MaterializingTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.Hugeitemstacks.length; ++var3)
		{
			if (this.Hugeitemstacks[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.Hugeitemstacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}
	public void updateEntity()
	{
		if(this.angle >360F)
		{
			this.angle = 0;
		}
		else
		{
			this.angle +=1.0F;
		}
		boolean var2 = false;

		if (!this.worldObj.isRemote)
		{
			if (this.canMake())
			{
				++this.MaterializingTime;

				if (this.MaterializingTime == 200)
				{
					this.MaterializingTime = 0;
					this.makeMateria();
					var2 = true;
				}
			}
			else
			{
				this.MaterializingTime = 0;
			}
		}

		if (var2)
		{
			this.onInventoryChanged();
		}
	}
	@Override
	public String getInvName() {
		return "container.hugeMateria";
	}
	public boolean isMaterializing()
	{
		return this.MaterializingTime >0;
	}
	public boolean canMake()
	{
		ItemStack hMateria = this.getStackInSlot(0);
		ItemStack base = this.getStackInSlot(1);
		ItemStack diamond = this.getStackInSlot(2);
		ItemStack material = this.getStackInSlot(3);
		ItemStack resultItem = this.getStackInSlot(4);
		int lvPlus = 0;
		if(diamond != null && diamond.itemID == Item.diamond.itemID)
			lvPlus = 9;
		if(base == null || !(base .getItem() instanceof EcItemMateria) || resultItem != null || material == null || (diamond!= null && diamond.itemID != Item.diamond.itemID))
			return false;
		else
		{
			if(hMateria != null && hMateria.getItem() instanceof EcItemMasterMateria)
			{
				int dmg = hMateria.getItemDamage();
				if(dmg >=0 && dmg < 6)
					return makeResult(material, dmg, lvPlus);
				else
					return false;
			}
			return materiaLvUp(material,lvPlus);
		}
	}
	private boolean makeResult(ItemStack material, int dmg, int lvPlus)
	{
		for(int i= 0;i<EnchArray[dmg].length;i++)
		{
			if(EnchArray[dmg][i] != -1 && MaterialArray[EnchArray[dmg][i]].isItemEqual(material))
			{
				if(magicArray.contains(EnchArray[dmg][i]))
					result = new ItemStack(EnchantChanger.ItemMat,1,magicArray.indexOf(EnchArray[dmg][i]) + 1);
				else
					result = new ItemStack(EnchantChanger.ItemMat,1,0);
				if(EnchArray[dmg][i] >= 0 && EnchArray[dmg][i] < 256){
					int lv;
					if(Enchantment.enchantmentsList[EnchArray[dmg][i]].getMaxLevel() == 1)
						lv = 1;
					else
						lv = 1 + lvPlus;
					EnchantChanger.addEnchantmentToItem(result, Enchantment.enchantmentsList[EnchArray[dmg][i]], lv);
				}
				return true;
			}
		}
		return false;
	}
	private boolean materiaLvUp(ItemStack materia, int lvPlus)
	{
		if(materia.getItem() instanceof EcItemMateria && materia.getItemDamage() > 0 && materia.isItemEnchanted())
		{
			result = materia.copy();
			NBTTagCompound nbt = result.getTagCompound();
			nbt.removeTag("ench");
			EnchantChanger.addEnchantmentToItem(materia, Enchantment.enchantmentsList[EnchantChanger.getMateriaEnchKind(materia)], EnchantChanger.getMateriaEnchLv(materia) + 1 + lvPlus);
			return true;
		}
		else
			return false;
	}
	public boolean makeMateria()
	{
		for(int i=1;i < 4;i++)
		{
			if(this.getStackInSlot(i) != null)
				this.decrStackSize(i, 1);
		}
		this.setInventorySlotContents(4, result);
		return true;
	}
//	public boolean canMakeMateria()
//	{
//		if(this.Hugeitemstacks[1] == null || (this.Hugeitemstacks[1] != null && !(this.Hugeitemstacks[1].getItem() instanceof EcItemMateria)))
//			return false;
//		else if(this.Hugeitemstacks[4] != null)
//		{
//			return false;
//		}
//		else if(this.Hugeitemstacks[3] != null && this.Hugeitemstacks[3].getItem() instanceof EcItemMateria && this.Hugeitemstacks[3].isItemEnchanted())
//		{
//			return true;
//		}
//		else if(this.Hugeitemstacks[0] != null && this.Hugeitemstacks[0].getItem() instanceof EcItemMasterMateria)
//		{
//			if(this.Hugeitemstacks[0].getItemDamage() == 0)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Block.dragonEgg.blockID
//						|| (this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID && this.Hugeitemstacks[3].getItemDamage() == 1)
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.enderPearl.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.eyeOfEnder.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Block.blockGold.blockID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.bucketMilk.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.bootsGold.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.netherStalkSeeds.itemID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else if(this.Hugeitemstacks[0].getItemDamage() == 1)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Item.ingotIron.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.blazePowder.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.feather.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.gunpowder.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.arrow.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Block.cactus.blockID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else if(this.Hugeitemstacks[0].getItemDamage() == 2)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Item.reed.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.pickaxeGold.itemID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else if(this.Hugeitemstacks[0].getItemDamage() == 3)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Item.fireballCharge.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.flintAndSteel.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.spiderEye.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.slimeBall.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.blazeRod.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else if(this.Hugeitemstacks[0].getItemDamage() == 4)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Item.pickaxeGold.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.silk.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.ingotIron.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else if(this.Hugeitemstacks[0].getItemDamage() == 5)
//			{
//				if(this.Hugeitemstacks[3] != null && (this.Hugeitemstacks[3].getItem().itemID == Item.fireballCharge.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.slimeBall.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.blazeRod.itemID
//						|| this.Hugeitemstacks[3].getItem().itemID == Item.bow.itemID))
//				{
//					return true;
//				}
//				else
//					return false;
//			}
//			else
//			{
//				return false;
//			}
//		}
//		else
//		{
//			return false;
//		}
//	}
//	public void makeMateria()
//	{
//		int var1 = 0;
//		ItemStack materia = null;
//		if(this.canMakeMateria())
//		{
//			if(this.Hugeitemstacks[2] != null && this.Hugeitemstacks[2].getItem().itemID == Item.diamond.itemID)
//			{
//				var1 = 9;
//				this.Hugeitemstacks[2].stackSize--;
//				if(this.Hugeitemstacks[2].stackSize <=0)
//					this.Hugeitemstacks[2] = null;
//			}
//			if(this.Hugeitemstacks[0] != null)
//			{
//				if(this.Hugeitemstacks[0].getItemDamage() == 0)
//				{
//					if(this.Hugeitemstacks[3] != null)
//					{
//						if(this.Hugeitemstacks[3].getItem().itemID == Block.dragonEgg.blockID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 1);
//							EnchantChanger.addEnchantmentToItem(materia, EnchantChanger.Meteo, 1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID && this.Hugeitemstacks[3].getItemDamage() == 1)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 2);
//							EnchantChanger.addEnchantmentToItem(materia, EnchantChanger.Holy, 1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.enderPearl.itemID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 3);
//							EnchantChanger.addEnchantmentToItem(materia, EnchantChanger.Telepo, 1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.eyeOfEnder.itemID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 4);
//							EnchantChanger.addEnchantmentToItem(materia, EnchantChanger.Float, 1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Block.blockGold.blockID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 5);
//							EnchantChanger.addEnchantmentToItem(materia, EnchantChanger.Thunder, 1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.bucketMilk.itemID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 6);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.bootsGold.itemID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 7);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.netherStalkSeeds.itemID)
//						{
//							materia = new ItemStack(EnchantChanger.MateriaID, 1, 8);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//				else if(this.Hugeitemstacks[0].getItemDamage() == 1)
//				{
//					if(this.Hugeitemstacks[3] != null)
//					{
//						materia = new ItemStack(EnchantChanger.MateriaID, 1, 0);
//						if(this.Hugeitemstacks[3].getItem().itemID == Item.ingotIron.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.protection, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.blazePowder.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.fireProtection, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.feather.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.featherFalling, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.gunpowder.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.blastProtection, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.arrow.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.projectileProtection, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Block.cactus.blockID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.thorns, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//				else if(this.Hugeitemstacks[0].getItemDamage() == 2)
//				{
//					materia = new ItemStack(EnchantChanger.MateriaID, 1, 0);
//					if(this.Hugeitemstacks[3] != null)
//					{
//						if(this.Hugeitemstacks[3].getItem().itemID == Item.reed.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.respiration, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.pickaxeGold.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.aquaAffinity, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//				else if(this.Hugeitemstacks[0].getItemDamage() == 3)
//				{
//					materia = new ItemStack(EnchantChanger.MateriaID, 1, 0);
//					if(this.Hugeitemstacks[3] != null)
//					{
//						if(this.Hugeitemstacks[3].getItem().itemID == Item.fireballCharge.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.sharpness, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.flintAndSteel.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.smite, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.spiderEye.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.baneOfArthropods, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.slimeBall.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.knockback, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.blazeRod.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.fireAspect, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.looting, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//				else if(this.Hugeitemstacks[0].getItemDamage() == 4)
//				{
//					materia = new ItemStack(EnchantChanger.MateriaID, 1, 0);
//					if(this.Hugeitemstacks[3] != null)
//					{
//						if(this.Hugeitemstacks[3].getItem().itemID == Item.pickaxeGold.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.efficiency, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.silk.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.silkTouch, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.ingotIron.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.unbreaking, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.appleGold.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.fortune, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == EnchantChanger.MateriaID)
//						{
//							materia = this.Hugeitemstacks[3].copy();
//							NBTTagCompound nbt = materia.getTagCompound();
//							nbt.removeTag("ench");
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.enchantmentsList[EnchantChanger.getMateriaEnchKind(this.Hugeitemstacks[3])], EnchantChanger.getMateriaEnchLv(this.Hugeitemstacks[3]) + 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//				else if(this.Hugeitemstacks[0].getItemDamage() == 5)
//				{
//					materia = new ItemStack(EnchantChanger.MateriaID, 1, 0);
//					if(this.Hugeitemstacks[3] != null)
//					{
//						if(this.Hugeitemstacks[3].getItem().itemID == Item.fireballCharge.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.power, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.slimeBall.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.punch, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.blazeRod.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.flame, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//						else if(this.Hugeitemstacks[3].getItem().itemID == Item.bow.itemID)
//						{
//							EnchantChanger.addEnchantmentToItem(materia, Enchantment.infinity, 1 + var1);
//							this.Hugeitemstacks[4] = materia;
//						}
//					}
//				}
//			}
//			if(this.Hugeitemstacks[3].getItem().itemID == EnchantChanger.MateriaID && this.Hugeitemstacks[3].isItemEnchanted())
//			{
//				materia = this.Hugeitemstacks[3].copy();
//				NBTTagCompound nbt = materia.getTagCompound();
//				nbt.removeTag("ench");
//				EnchantChanger.addEnchantmentToItem(materia, Enchantment.enchantmentsList[EnchantChanger.getMateriaEnchKind(this.Hugeitemstacks[3])], EnchantChanger.getMateriaEnchLv(this.Hugeitemstacks[3]) + 1 + var1);
//				this.Hugeitemstacks[4] = materia;
//			}
//			if(this.Hugeitemstacks[1] != null)
//			{
//				this.Hugeitemstacks[1].stackSize--;
//				if(this.Hugeitemstacks[1].stackSize <=0)
//					this.Hugeitemstacks[1] = null;
//			}
//			if(this.Hugeitemstacks[3] != null)
//			{
//				this.Hugeitemstacks[3].stackSize--;
//				if(this.Hugeitemstacks[3].stackSize <=0)
//					this.Hugeitemstacks[3] = null;
//			}
//		}
//	}
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

}