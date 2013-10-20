package HNPCs;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityHNPCs extends EntityMob
{
	private int maxHealth = 20;
	public EntityHNPCs(World world)
	{
		super(world);
		this.moveSpeed = 0.3F;
//		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
//		this.tasks.addTask(1, new EntityAIBreakDoor(this));
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityDummyGoal.class, this.moveSpeed, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, this.moveSpeed, 64.0F));
		this.tasks.addTask(3, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
		this.tasks.addTask(4, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 64.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 64.0F, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityDummyGoal.class, 64.0F, 0, false));
	}
	public boolean isAIEnabled()
	{
		return true;
	}
	@Override
	public int getMaxHealth() {
		return this.maxHealth;
	}
	public void setMaxHealth(int mh){
		this.maxHealth = mh;
	}
	public void setTexture(String tex){
		this.texture = tex;
	}
	public void initCreature()
	{
		super.initCreature();
	}
}