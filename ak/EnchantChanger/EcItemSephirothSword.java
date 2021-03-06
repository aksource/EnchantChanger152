package ak.EnchantChanger;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;


public class EcItemSephirothSword extends EcItemSword
{
	public static double BoxSize = 5D;
	public static boolean SephirothSprintAttack = false;
	public static Entity SephirothSprintAttackEntity=null;

	public EcItemSephirothSword(int par1)
	{
		super(par1, EnumToolMaterial.EMERALD);
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		if(par3EntityPlayer.isSneaking())
		{
			if(!par3EntityPlayer.capabilities.isCreativeMode)
				par3EntityPlayer.heal(-par3EntityPlayer.getHealth() + 1);
			par3EntityPlayer.addPotionEffect(new PotionEffect(1,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(3,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(5,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(8,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(11,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(12,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(13,1200,3));
			par3EntityPlayer.addPotionEffect(new PotionEffect(16,1200,3));
		}
		return par1ItemStack;
	}

	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
	{
		if(player.isSprinting()&& !this.SephirothSprintAttack)
		{
			this.SephirothSprintAttack=true;
			this.SephirothSprintAttackEntity=entity;
		}
		return false;
	}

	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(this.SephirothSprintAttack && par3Entity instanceof EntityPlayer)
		{
			this.SephirothSprintAttack(par2World, (EntityPlayer)par3Entity);
			this.SephirothSprintAttack = false;
		}
	}

	public void SephirothSprintAttack(World world,EntityPlayer player)
	{
		Entity entity = SephirothSprintAttackEntity;

		if(entity instanceof EntityLiving)
		{
			List EntityList = world.getEntitiesWithinAABB(EntityLiving.class, entity.boundingBox.expand(BoxSize, BoxSize, BoxSize));
			for (int i=0; i < EntityList.size();i++)
			{
				Entity entity1=(Entity) EntityList.get(i);
				if(entity1 != entity && entity1 != player)
				{
					player.attackTargetEntityWithCurrentItem(entity1);
				}
			}
		}
		EcItemSephirothSword.SephirothSprintAttack = false;
	}
}
