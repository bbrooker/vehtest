package net.bbrooker.minezroleplay.data.veh.util;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


/** @author Tanawat Boonmak @version 1.0 @since 23-5-2016 @category MineZ Vehicle @docRoot @code */
public abstract class EntityLandVehicle extends Entity {

	public EntityLandVehicle(World p_i1582_1_) {
		super(p_i1582_1_);
		// TODO Auto-generated constructor stub
	}
	
	/**
	public static float vehHP = 80;
	public double Vehspeed = 0.0D;
	public int TimeInWater = 0;
	public int vehiclePosUpdate;
	public double vehPosX;
	public double vehPosY;
	public double vehPosZ;
	public double vehYaw;
	public double vehPitch;
	private float engineoff;
	private int startPlaying;
	public float getvolume;
	public int startSoundDelay = getSoundLoop();
	private Entity prevRiddenByEntity = null;	
	private boolean InitializedVehicleLoopingSounds = false;
	public abstract String getEntityTexture();
	abstract void updateMotionAndRotation();
	protected abstract String getSoundLoopRun();
	protected abstract String getSoundLoopIdle();
	protected abstract String getSoundEnter();
	protected abstract String getSoundExit();
	protected abstract int getSoundLoop();	


	public EntityLandVehicle(World par1) {
		super(par1);
		ignoreFrustumCheck = true;
		preventEntitySpawning = true;
		getDataWatcher().addObject(2, Integer.valueOf(0));
		getDataWatcher().addObject(3, Integer.valueOf(0));
		getDataWatcher().addObject(4, Integer.valueOf(0));
		getDataWatcher().addObject(5, Integer.valueOf(0));
		getDataWatcher().addObject(19, Integer.valueOf(0)); 

	}
	
	public EntityLandVehicle(World par1, double par2, double par4, double par6)
	{
	    this(par1);
	    setPosition(par2, par4 + yOffset, par6);
	    this.motionX = 0.0D;
	    this.motionY = 0.0D;
	    this.motionZ = 0.0D;
	    this.prevPosX = par2;
	    this.prevPosY = par4;
	    this.prevPosZ = par6;
	}
	
	@Override
	protected void entityInit() 
	{
		dataWatcher.addObject(22, new Float(100.0F));
	}
	
	public boolean canTriggerWalking()
	{
		return false;
	}
	
	public AxisAlignedBB getCollisionBox(Entity e)
	{
		return null;
	}
	
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return this.boundingBox;
	}
	
	public boolean canBePushed()
	{

		return true;
	}
	
	public boolean canBeCollidedWith()
	{
	    return !this.isDead;
	}
	
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}
	
	public double getMountedYOffset()
	{
		return height - 0.7D;
	}
	  
	public float getHealth()
	{
		return this.getDataWatcher().getWatchableObjectFloat(22);
	}
	
	public void setHealth(float health)
	{
		getDataWatcher().updateObject(22, Float.valueOf(health));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) 
	{
		p_70014_1_.setFloat("Health", getHealth());
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		
		setHealth(p_70037_1_.getFloat("Health"));
	}
	

	public void onEntityUpdate()
	{
		super.onEntityUpdate();
	}

	@Override
	public boolean interactFirst(EntityPlayer par1)
	{
		ItemStack i = par1.getCurrentEquippedItem();
		if((i == null) || (riddenByEntity == null))
		{
			if(!worldObj.isRemote)
			{
				par1.mountEntity(this);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void updateRiderPosition()
	{
		if(riddenByEntity != null)
		{
			riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
		}
	    if ((this.riddenByEntity instanceof EntityLivingBase)) 
	    {
	          ((EntityLivingBase)riddenByEntity).renderYawOffset = rotationYaw;
	    }
	}
	
	public void DamageEntity(float dmg)
	{
		if(!isDead)
		{
			setHealth(getHealth() - dmg);
			
			if(getHealth() <= 0.0F)
			{
				setHealth(0.0F);
				setDead();
				onDeadEvent();
			}
			setBeenAttacked();
		}
		else 
		{
			setHealth(0.0F);
		}
	}
	
	private void onDeadEvent()
	{
		worldObj.createExplosion(this, posX, posY, posZ, 0.0F, false);
	}
	
	@Override
	public void setDead()
	{
		super.setDead();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource src, float dmg)
	{
		if (hurtResistantTime == 0)
		{
			setBeenAttacked();
			DamageEntity(dmg);
			hurtResistantTime = 20;
			return true;
		}
		return false;
	}
	
	@Override
	protected void fall(float distance)
	{

		super.fall(distance / 2.0F);
	    int dmg = (int)Math.ceil(distance - 8.0F);
	    if (dmg > 0) 
	    {
	    	attackEntityFrom(DamageSource.fall, dmg / 2);
	    }
	}
	
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
	    this.vehPosX = par1;
	    this.vehPosY = par3;
	    this.vehPosZ = par5;
	    this.vehYaw = par7;
	    this.vehPitch = par8;
	    this.vehiclePosUpdate = par9;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (worldObj.isRemote)
		{
			if (this.vehiclePosUpdate != 0)
			{
				double var46 = this.posX + (vehPosX - posX);
				double var48 = this.posY + (vehPosY - posY);
		        double var5 = this.posZ + (vehPosZ - posZ);
		        double var7 = MathHelper.wrapAngleTo180_double(vehYaw - rotationYaw);
		        rotationYaw = ((float)(rotationYaw + var7));
		        rotationPitch = ((float)(rotationPitch + (vehPitch - rotationPitch)));
		        setPosition(var46, var48, var5);
		        setRotation(rotationYaw, rotationPitch);
			}
			else
			{
				setPosition(posX, posY, posZ);
				setRotation(rotationYaw, rotationPitch);
			}
			handleSoundEffects();
		}
		else 
		{
		    prevPosX = posX;
		    prevPosY = posY;
		    prevPosZ = posZ;
		    
		    handleSoundEffects();
		    handleEntityCollisions();
		    damageHandling();
		    updateMotionAndRotation();
		}
		
	    this.Vehspeed = (Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 25.0D);
	    	
	    if ((this.Vehspeed < 0.01D) && (this.Vehspeed > -0.01D)) 
	    	{
	    		this.Vehspeed = 0.0D;
	    	}
	    
	    if (this.hurtResistantTime > 0) 
	    	{
	        	this.hurtResistantTime -= 1;
	    	}
	}

	public void handleSoundEffects()
	{
		if((this.riddenByEntity != null) && (this.prevRiddenByEntity == null))
		{
			prevRiddenByEntity = riddenByEntity;
			if (getSoundEnter() != null) 
			{
				worldObj.playSoundAtEntity(this, getSoundEnter(), 1.0F, 1.0F);
			}
		}
		if ((this.riddenByEntity == null) && (this.prevRiddenByEntity != null))
		{
			this.prevRiddenByEntity = null;
			if(getSoundExit() != null)
			{
				worldObj.playSoundAtEntity(this, getSoundExit(), 1.0F, 1.0F);
			}
		    if ((this.riddenByEntity != null) && (this.startSoundDelay > -20)) 
		    {
		        this.startSoundDelay -= 1;
		    }
		    if ((this.riddenByEntity == null) && (this.startSoundDelay != getSoundLoop())) 
		    {
		        this.startSoundDelay = getSoundLoop();
		    }
		}
	}
	
	public void handleEntityCollisions()
	{
		if(!worldObj.isRemote)
		{
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.0D, 0.2D));
		    if ((list != null) && (!list.isEmpty())) 
		    {
		        for (int var27 = 0; var27 < list.size(); var27++)
		        {
		        	Entity entity = (Entity)list.get(var27);
		        	if ((entity != this.riddenByEntity) && (entity.canBePushed ()) && ((entity instanceof EntityLandVehicle))) 
		        	{
		        		entity.applyEntityCollision(this);
		        	}
		        }
		    }
		}
	}
	
	public void damageHandling()
	{
		if(this.isInWater())
		{
			TimeInWater += 1;
		}
		else
		{
			this.TimeInWater = 0;
		}
		if(TimeInWater > 20)
		{
		    DamageEntity(1.0F);
		    TimeInWater = 0;
		}
		if(worldObj.isRemote)
		{
		    if ((getHealth() <= 5.0F) && (rand.nextInt(100) < 5)) {
		       DamageEntity(1.0F);
		}
	}
		else
		{
			setHealth(getHealth());
		}
	}
	
	
	public void setRiderLocation(VehSeat seat, double yOffset, double forwardOffset, double leftOffset, boolean shouldSit)
	{
		if(seat != null)
		{

		}
	}
	
	public void setRiderLocation(VehSeat seat, double yOffset, double forwardOffset, double leftOffset, boolean shouldSit, float rotate)
	{
		if (seat != null)
		{

			seat.setLocationAndAngles(this.posX + Math.cos(Math.toRadians(this.rotationYaw)) * leftOffset - Math.sin(Math.toRadians(this.rotationYaw)) * forwardOffset, this.posY + getMountedYOffset() + yOffset, this.posZ + Math.sin(Math.toRadians(this.rotationYaw)) * leftOffset + Math.cos(Math.toRadians(this.rotationYaw)) * forwardOffset, rotate, 0.0F);
		   	seat.shouldRiderSit = shouldSit;
		}
	}
	
}
