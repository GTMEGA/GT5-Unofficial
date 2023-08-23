package gregtech.common.entities.explosives;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.enums.GT_Values;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


@Getter
public abstract class GT_Entity_Explosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    protected int metadata;

    protected double realX, realY, realZ;

    public GT_Entity_Explosive(final World world) {
        super(world);
    }

    public GT_Entity_Explosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata) {
        super(world, x + 0.5, y + 0.5, z + 0.5, placedBy);
        this.setSize();
        this.realX = this.posX;
        this.realY = this.posY;
        this.realZ = this.posZ;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.fuse = GT_Values.MEFuse;
        this.metadata = metadata;
    }

    protected void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
    }

    private float getNewSize() {
        return 0.5f + ((float) (fuse - GT_Values.MEFuse)) / GT_Values.MEFuse;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        setPosition(realX, realY, realZ);
        if (fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.doExplode();
            }
        } else {
            final int n = rand.nextInt(10) + 1;
            for (int i = 0; i < n; i++) {
                final double x, y, z;
                x = rand.nextDouble() * 1.4 - 0.7;
                y = rand.nextDouble() * 1.4 - 0.7;
                z = rand.nextDouble() * 1.4 - 0.7;
                this.worldObj.spawnParticle("smoke", posX + x, posY + y, posZ + z, x / 4, y / 4, z / 4);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     *
     * @param compound
     */
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("meta", metadata);
        compound.setDouble("rX", realX);
        compound.setDouble("rY", realY);
        compound.setDouble("rZ", realZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param compound
     */
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.metadata = compound.getInteger("meta");
        this.realX = compound.getDouble("rX");
        this.realY = compound.getDouble("rY");
        this.realZ = compound.getDouble("rZ");
    }

    protected abstract void doExplode();

    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param buffer The packet data stream
     */
    @Override
    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeInt(fuse);
        buffer.writeInt(metadata);
        buffer.writeDouble(realX);
        buffer.writeDouble(realY);
        buffer.writeDouble(realZ);
    }

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param additionalData
     */
    @Override
    public void readSpawnData(final ByteBuf additionalData) {
        this.fuse = additionalData.readInt();
        this.metadata = additionalData.readInt();
        this.realX = additionalData.readDouble();
        this.realY = additionalData.readDouble();
        this.realZ = additionalData.readDouble();
    }

}
