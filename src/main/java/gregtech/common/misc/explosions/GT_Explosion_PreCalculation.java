package gregtech.common.misc.explosions;


import codechicken.lib.math.MathHelper;
import gregtech.common.entities.GT_Entity_Explosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;

import java.util.HashSet;
import java.util.Set;

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


    protected static final Set<GT_Explosion_PreCalculation> active = new HashSet<>();

    private final @NonNull GT_Explosion<?> explosion;

    private final @NonNull Set<Ray> rays = new HashSet<>();

    private final Set<ChunkPosition> seenPositions = new HashSet<>(), targetPositions = new HashSet<>();

    private int fuse = 0;

    private int ticked = 0;

    public GT_Explosion_PreCalculation initialize() {
        if (!isServerSide() || active.contains(this)) {
            return this;
        }
        this.fuse = getExplosionSource().fuse;
        active.add(this);
        //
        val maxRaysX = explosion.getMaxX();
        val maxRaysY = explosion.getMaxY();
        val maxRaysZ = explosion.getMaxZ();
        for (int i = 0; i < maxRaysX; i++) {
            for (int j = 0; j < maxRaysY; j++) {
                for (int k = 0; k < maxRaysZ; k++) {
                    if (i == 0 || i == maxRaysX - 1 || j == 0 || j == maxRaysY - 1 || k == 0 || k == maxRaysZ - 1) {
                        var aX        = i / (double) (maxRaysX - 1) * 2.0 - 1.0;
                        var aY        = j / (double) (maxRaysY - 1) * 2.0 - 1.0;
                        var aZ        = k / (double) (maxRaysZ - 1) * 2.0 - 1.0;
                        val magnitude = Math.sqrt(aX * aX + aY * aY + aZ * aZ);
                        aX /= magnitude;
                        aY /= magnitude;
                        aZ /= magnitude;
                        val ray = new Ray(this, aX, aY, aZ);
                        ray.power = explosion.getRayPower();
                        ray.init();
                        ray.maxLength = explosion.preCalculateRayMaximumLength(ray);
                        explosion.preprocessRay(ray);
                        rays.add(ray);
                    }
                }
            }
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
        for (val active : active) {
            if (active.targetPositions.contains(currentPosition)) {
                return false;
            }
        }
        return true;
    }

    private boolean canDestroy(final Ray ray, final Block block, final int x, final int y, final int z) {
        return getExplosionSource().canBlockBeExploded(explosion, explosion.getPubWorld(), x, y, z, block, (float) ray.power);
    }

    public void finalizeExplosion() {
        if (!isServerSide()) {
            return;
        }
        active.remove(this);
    }

}
