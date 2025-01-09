package gregtech.common.misc.explosions;


import codechicken.lib.math.MathHelper;
import gregtech.common.entities.GT_Entity_Explosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;

import net.minecraftforge.common.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gregtech.common.misc.explosions.GT_Explosion.serializePos;
import static gregtech.common.misc.explosions.GT_Explosion.deserializePos;
import static java.lang.Math.max;


@Getter
@RequiredArgsConstructor
public class GT_Explosion_PreCalculation {

    @RequiredArgsConstructor
    @ToString
    @EqualsAndHashCode(
            of = {
                    "aX",
                    "aY",
                    "aZ"
            }
    )
    public static class Ray {

        public final @NonNull GT_Explosion_PreCalculation parent;

        public final double aX, aY, aZ;

        public final boolean[] flagFields = new boolean[3];

        public final int[] intFields = new int[3];

        public final double[] doubleFields = new double[3];

        public double maxLength;

        public double distance = 0.0;

        public double power = 0.0;

        public double myLength = 0.0;

        public double posX, posY, posZ;

        public ChunkPosition chunkPosition = null;

        public boolean canContinue = true;

        private int parX, parY, parZ;

        @SneakyThrows
        private void toByteArray(DataOutputStream dOut) {
            dOut.writeDouble(aX);
            dOut.writeDouble(aY);
            dOut.writeDouble(aZ);
            dOut.writeBoolean(flagFields[0]);
            dOut.writeBoolean(flagFields[1]);
            dOut.writeBoolean(flagFields[2]);
            dOut.writeInt(intFields[0]);
            dOut.writeInt(intFields[1]);
            dOut.writeInt(intFields[2]);
            dOut.writeDouble(doubleFields[0]);
            dOut.writeDouble(doubleFields[1]);
            dOut.writeDouble(doubleFields[2]);
            dOut.writeDouble(maxLength);
            dOut.writeDouble(distance);
            dOut.writeDouble(power);
            dOut.writeDouble(myLength);
            dOut.writeDouble(posX);
            dOut.writeDouble(posY);
            dOut.writeDouble(posZ);
            dOut.writeInt(chunkPosition.chunkPosX);
            dOut.writeInt(chunkPosition.chunkPosY);
            dOut.writeInt(chunkPosition.chunkPosZ);
            dOut.writeBoolean(canContinue);
        }

        @SneakyThrows
        public static NBTTagCompound serializeNBT(Collection<Ray> rays) {
            val b = new ByteArrayOutputStream();
            val dOut = new DataOutputStream(b);
            dOut.writeInt(rays.size());
            for (val ray: rays) {
                ray.toByteArray(dOut);
            }
            dOut.close();
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByteArray("data_v1", b.toByteArray());
            return nbt;
        }

        @SneakyThrows
        private static void fromByteArray_v1(GT_Explosion_PreCalculation parent, Collection<Ray> rayList, byte[] bytes) {
            val dIn = new DataInputStream(new ByteArrayInputStream(bytes));
            val count = dIn.readInt();
            for (int i = 0; i < count; i++) {
                val ax = dIn.readDouble();
                val ay = dIn.readDouble();
                val az = dIn.readDouble();
                val ray = new Ray(parent, ax, ay, az);
                ray.parX = parent.explosion.getX();
                ray.parY = parent.explosion.getY();
                ray.parZ = parent.explosion.getZ();
                ray.flagFields[0] = dIn.readBoolean();
                ray.flagFields[1] = dIn.readBoolean();
                ray.flagFields[2] = dIn.readBoolean();
                ray.intFields[0] = dIn.readInt();
                ray.intFields[1] = dIn.readInt();
                ray.intFields[2] = dIn.readInt();
                ray.doubleFields[0] = dIn.readDouble();
                ray.doubleFields[1] = dIn.readDouble();
                ray.doubleFields[2] = dIn.readDouble();
                ray.maxLength = dIn.readDouble();
                ray.distance = dIn.readDouble();
                ray.power = dIn.readDouble();
                ray.myLength = dIn.readDouble();
                ray.posX = dIn.readDouble();
                ray.posY = dIn.readDouble();
                ray.posZ = dIn.readDouble();
                val cX = dIn.readInt();
                val cY = dIn.readInt();
                val cZ = dIn.readInt();
                ray.chunkPosition = new ChunkPosition(cX, cY, cZ);
                ray.canContinue = dIn.readBoolean();
                rayList.add(ray);
            }
        }

        public static void deserializeNBT(GT_Explosion_PreCalculation parent, Collection<Ray> rayList, NBTTagCompound nbt) {
            if (nbt.hasKey("data_v1")) {
                fromByteArray_v1(parent, rayList, nbt.getByteArray("data_v1"));
            }
        }

        protected void init() {
            parX = parent.explosion.getX();
            parY = parent.explosion.getY();
            parZ = parent.explosion.getZ();
            march(0);
        }

        private void march(final double amount) {
            distance += amount;
            val x = aX * distance;
            val y = aY * distance;
            val z = aZ * distance;
            posX          = (x + parX);
            posY          = (y + parY);
            posZ          = (z + parZ);
            chunkPosition = new ChunkPosition(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            myLength      = Math.sqrt(x * x + y * y + z * z);
            this.parent.explosion.processRay(this);
        }

        protected void decreasePower(final float drop) {
            power -= drop;
            if (power <= 0.0) {
                canContinue = false;
            }
        }

    }

    private final @NonNull GT_Explosion<?> explosion;

    private final @NonNull Set<Ray> rays = new HashSet<>();

    private final Set<ChunkPosition> seenPositions = new HashSet<>(), targetPositions = new HashSet<>();

    private int fuse = 0;

    private int ticked = 0;

    public NBTTagCompound serializeNBT() {
        val nbt = new NBTTagCompound();
        val seen = new NBTTagList();
        val target = new NBTTagList();
        nbt.setTag("rays", Ray.serializeNBT(this.rays));
        nbt.setTag("seen", seen);
        nbt.setTag("target", target);
        System.out.println(this.rays.size());
        System.out.println(seenPositions.size());
        System.out.println(targetPositions.size());
        for (val pos: seenPositions) {
            seen.appendTag(new NBTTagIntArray(serializePos(pos)));
        }
        for (val pos: targetPositions) {
            target.appendTag(new NBTTagIntArray(serializePos(pos)));
        }
        nbt.setInteger("fuse", fuse);
        nbt.setInteger("ticked", ticked);
        return nbt;
    }

    public static GT_Explosion_PreCalculation deserializeNBT(GT_Explosion<?> parent, NBTTagCompound nbt) {
        val result = new GT_Explosion_PreCalculation(parent);
        val rays = nbt.getCompoundTag("rays");
        val seen  = nbt.getTagList("seen", Constants.NBT.TAG_COMPOUND);
        val target = nbt.getTagList("target", Constants.NBT.TAG_COMPOUND);
        int seenCount = seen.tagCount();
        int targetCount = target.tagCount();
        Ray.deserializeNBT(result, result.rays, rays);
        for (int i = 0; i < seenCount; i++) {
            result.seenPositions.add(deserializePos(seen.func_150306_c(i)));
        }
        for (int i = 0; i < targetCount; i++) {
            result.targetPositions.add(deserializePos(target.func_150306_c(i)));
        }
        result.fuse = nbt.getInteger("fuse");
        result.ticked = nbt.getInteger("ticked");
        return result;
    }

    private static final float PHI = (float) (Math.PI * (Math.sqrt(5) - 1));
    private static void fibSphere(int samples, int index, Vec3 pos) {
        float y = 1 - (index / (float)(samples - 1)) * 2;
        float radius = (float) Math.sqrt(1 - y * y);
        float theta = PHI * index;

        double x = MathHelper.cos(theta) * radius;
        double z = MathHelper.sin(theta) * radius;

        pos.xCoord = x;
        pos.yCoord = y;
        pos.zCoord = z;
    }

    public GT_Explosion_PreCalculation initialize() {
        if (!isServerSide()) {
            return this;
        }
        this.fuse = getExplosionSource().fuse;
        int x = explosion.getMaxX();
        int y = explosion.getMaxY();
        int z = explosion.getMaxZ();
        float min = Math.min(x, Math.min(y, z));
        float xBias = min / x;
        float yBias = min / y;
        float zBias = min / z;
        int count = 32 * Math.max(x, Math.max(y, z));
        val vec = Vec3.createVectorHelper(0, 0, 0);
        for (int i = 0; i < count; i++) {
            fibSphere(count, i, vec);
            vec.xCoord *= xBias;
            vec.yCoord *= yBias;
            vec.zCoord *= zBias;
            vec.normalize();
            val ray = new Ray(this, vec.xCoord, vec.yCoord, vec.zCoord);
            ray.power = explosion.getRayPower();
            ray.init();
            ray.maxLength = explosion.preCalculateRayMaximumLength(ray);
            explosion.preprocessRay(ray);
            rays.add(ray);
        }
         return this;
    }

    public boolean isServerSide() {
        return !explosion.getPubWorld().isRemote;
    }

    public GT_Entity_Explosive getExplosionSource() {
        return explosion.getGtExplosive();
    }

    public void tick() {
        if (!isServerSide()) {
            return;
        }
        val proportionEnd = (ticked + 1) / (double) max(fuse, 1);
        for (val ray : rays) {
            if (!ray.canContinue) {
                continue;
            }
            doRayTick(ray, proportionEnd);
        }
        ticked += 1;
    }

    private void doRayTick(final @NonNull Ray ray, final double proportionEnd) {
        if (!isServerSide()) {
            return;
        }
        ChunkPosition last              = null;
        val           world             = explosion.getPubWorld();
        val           source            = getExplosionSource();
        val           tier              = source.getTier().asInterface();
        val           rayDropBump       = explosion.getRayDropBump();
        val           rayBaseDist       = explosion.getBaseRayDist();
        val           rayPowerDropRatio = explosion.getRayPowerDropRatio();
        while ((ray.canContinue = explosion.isRayValid(ray)) && ray.myLength < ray.maxLength * proportionEnd) {
            val currentPosition = ray.chunkPosition;
            if (currentPosition != last && hasNotEncountered(currentPosition)) {
                seenPositions.add(currentPosition);
                val x        = currentPosition.chunkPosX;
                val y        = currentPosition.chunkPosY;
                val z        = currentPosition.chunkPosZ;
                val block    = world.getBlock(x, y, z);
                val metadata = world.getBlockMetadata(x, y, z);
                if (explosion.canDamage(block, metadata, x, y, z)) {
                    if (!block.isAir(world, x, y, z)) {
                        val explosionPowerDrop = tier.getBlockResistance(explosion, world, x, y, z, block);
                        ray.decreasePower((explosionPowerDrop + rayDropBump) * rayBaseDist);
                    }
                    if (ray.power > 0.0 && canDestroy(ray, block, x, y, z)) {
                        targetPositions.add(currentPosition);
                        explosion.handleChunkPosition(currentPosition);
                    }
                }
            }
            ray.march(rayBaseDist);
            ray.decreasePower(rayBaseDist * rayPowerDropRatio);
            last = currentPosition;
        }
    }

    private boolean hasNotEncountered(final ChunkPosition currentPosition) {
        val first = !seenPositions.contains(currentPosition) && explosion.handlePositionPre(currentPosition);
        if (!first) {
            return false;
        }
        return true;
    }

    private boolean canDestroy(final Ray ray, final Block block, final int x, final int y, final int z) {
        return getExplosionSource().canBlockBeExploded(explosion, explosion.getPubWorld(), x, y, z, block, (float) ray.power);
    }

}
