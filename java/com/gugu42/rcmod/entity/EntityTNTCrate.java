package com.gugu42.rcmod.entity;

import com.gugu42.rcmod.TNTCrateExplosion;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTNTCrate extends Entity implements IEntityAdditionalSpawnData
{
    public int fuse;
    private EntityLivingBase tntPlacedBy;

    public EntityTNTCrate(World par1World)
    {
        super(par1World);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.setRenderYawOffset(this.height / 2.0F);
    }

    public EntityTNTCrate(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setPosition(par2, par4, par6);
        //this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
//        this.motionY = 0.20000000298023224D;
        //this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.fuse = 80;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    protected void entityInit() {}

    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0)
        {
            this.setDead();
            
            if (!this.world.isRemote)
            {
                this.explode();
            }

        }
        else
        {
        	//TODO - Fix particles
            //this.world.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        
        //TODO - Fix sounds
        if(this.fuse == 20){
            //this.world.playSoundAtEntity(this, "rcmod:tntcrate.countdown", 1.0f, 1.0f);
        }
        if(this.fuse == 40){
            //this.world.playSoundAtEntity(this, "rcmod:tntcrate.countdown", 1.0f, 1.0f);
        }
        if(this.fuse == 60){
            //this.world.playSoundAtEntity(this, "rcmod:tntcrate.countdown", 1.0f, 1.0f);
        }
        
    }

    private void explode()
    {
        float f = 4.0F;
        TNTCrateExplosion explosion = new TNTCrateExplosion(this.world, (Entity)null, this.posX, this.posY, this.posZ, f);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }

    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Fuse", (byte)this.fuse);
    }

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.fuse = par1NBTTagCompound.getByte("Fuse");
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
         buffer.writeInt(this.fuse);
        
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        
        this.fuse = additionalData.readInt();
    }
}