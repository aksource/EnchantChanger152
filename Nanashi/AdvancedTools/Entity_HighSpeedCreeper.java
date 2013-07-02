package Nanashi.AdvancedTools;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Entity_HighSpeedCreeper extends EntityCreeper
{
	public Entity_HighSpeedCreeper(World var1)
	{
		super(var1);
		this.texture = AdvancedTools.mobTexture + "hscreeper.png";
		this.moveSpeed = 0.365F;
		this.experienceValue = 15;
//		this.tasks = new EntityAITasks();
//		this.tasks.addTask(1, new EntityAISwimming(this));
//		this.tasks.addTask(2, new EntityAICreeperSwell(this));
//		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, this.moveSpeed, false));
//		this.tasks.addTask(4, new EntityAIWander(this, 0.2F));
//		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
//		this.tasks.addTask(5, new EntityAILookIdle(this));
//		this.targetTasks = new EntityAITasks();
//		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, false));
//		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
	}

	public int getMaxHealth()
	{
		return 30;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.isPotionActive(Potion.moveSpeed) && this.health <= 10)
		{
			this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 20, 1));
		}
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.creeper";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.creeperdeath";
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource var1, int var2)
	{
		return var1.damageType == "arrow" ? false : super.attackEntityFrom(var1, var2);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int var2)
	{
		super.dropFewItems(var1, var2);

		if (this.rand.nextFloat() <= 0.25F + 0.1F * (float)var2)
		{
			this.dropItem(AdvancedTools.RedEnhancer.itemID, 1);
		}

		if (this.rand.nextFloat() <= 0.1F + 0.1F * (float)var2)
		{
			this.dropItem(AdvancedTools.BlueEnhancer.itemID, 1);
		}
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt var1)
	{
		super.onStruckByLightning(var1);
	}
}
