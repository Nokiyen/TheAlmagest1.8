package noki.almagest.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;


public class EntityMira extends EntityCreature {
	
	public EntityMira(World world) {
		super(world);
		// うろうろ移動するAIの追加
		this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
		// 見回すAIの追加
		this.tasks.addTask(2, new EntityAILookIdle(this));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
	}
	
/*	@Override
	public boolean isAIEnabled() { return true; }*/
	
	@Override
	public String getLivingSound() {
		
		return "mob.cat.meow";
		
	}
	
	@Override
	public String getHurtSound() {
		
		return "mob.cat.hitt";
		
	}
	
	@Override
	public String getDeathSound() {
		
		return "mob.cat.hitt";
		
	}
 
	/*
	* このMobが動いているときの音のファイルパスを返す.
	* 引数のblockはMobの下にあるBlock.
	*/
/*	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
		
	}*/
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		
		return EnumCreatureAttribute.UNDEFINED;
		
	}
	
	@Override
	protected void applyEntityAttributes() {
		
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
		
	}
	
	@Override
	public Item getDropItem() {
		
		return null;
		
	}
	
}
