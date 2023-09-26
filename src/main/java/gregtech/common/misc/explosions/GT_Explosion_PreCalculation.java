package gregtech.common.misc.explosions;


import codechicken.lib.math.MathHelper;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;


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

        protected void init() {
            march(0);
        }

        private void march(final double amount) {
            distance += amount;
            val x = aX * distance;
            val y = aY * distance;
            val z = aZ * distance;
            posX = (x + parent.sourceX);
            posY = (y + parent.sourceY);
            posZ = (z + parent.sourceZ);
            chunkPosition = new ChunkPosition(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            myLength = Math.sqrt(x * x + y * y + z * z);
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

    private final @NonNull GT_Entity_Explosive explosionSource;

    private final @NonNull GT_Explosion explosion;

    private final @NonNull World world;

    private final int sourceX, sourceY, sourceZ;

    private final int maxFuse;

    private final @NonNull Set<Ray> rays = new HashSet<>();

    private final Set<ChunkPosition> seenPositions = new HashSet<>(), targetPositions = new HashSet<>();

    private int ticked = 0;

    @Setter
    private boolean continueCalculating = true;

    public void initialize() {
        active.add(this);
        val maxRaysX = explosion.getMaxX();
        val maxRaysY = explosion.getMaxY();
        val maxRaysZ = explosion.getMaxZ();
        for (int i = 0; i < maxRaysX; i++) {
            for (int j = 0; j < maxRaysY; j++) {
                for (int k = 0; k < maxRaysZ; k++) {
                    if (i == 0 || i == maxRaysX - 1 || j == 0 || j == maxRaysY - 1 || k == 0 || k == maxRaysZ - 1) {
                        var aX = i / (double) (maxRaysX - 1) * 2.0 - 1.0;
                        var aY = j / (double) (maxRaysY - 1) * 2.0 - 1.0;
                        var aZ = k / (double) (maxRaysZ - 1) * 2.0 - 1.0;
                        val magnitude = Math.sqrt(aX * aX + aY * aY + aZ * aZ);
                        aX /= magnitude;
                        aY /= magnitude;
                        aZ /= magnitude;
                        val ray = new Ray(this, aX, aY, aZ);
                        ray.power = explosion.getRayPower();
                        ray.init();
                        ray.maxLength = explosion.precalcRayMaxLength(ray/*ray.posX, ray.posY, ray.posZ, explosion.getExpRadius()*/);
                        explosion.preprocessRay(ray);
                        rays.add(ray);
                    }
                }
            }
        }
    }

    public void tick() {
        if (!continueCalculating) {
            return;
        }
        val proportionEnd = (ticked + 1) / (double) maxFuse;
        for (val ray : rays) {
            if (!ray.canContinue) {
                continue;
            }
            doRayTick(ray, proportionEnd);
        }
        ticked += 1;
    }

    private void doRayTick(final @NonNull Ray ray, final double proportionEnd) {
        ChunkPosition last = null;
        while ((ray.canContinue = explosion.rayValid(ray)) && ray.myLength < ray.maxLength * proportionEnd) {
            val currentPosition = ray.chunkPosition;
            if (currentPosition != last && hasNotEncountered(currentPosition)) {
                seenPositions.add(currentPosition);
                val x = currentPosition.chunkPosX;
                val y = currentPosition.chunkPosY;
                val z = currentPosition.chunkPosZ;
                val block = world.getBlock(x, y, z);
                val metadata = world.getBlockMetadata(x, y, z);
                if (explosion.canDamage(block, metadata, x, y, z)) {
                    if (!block.isAir(world, x, y, z)) {
                        val explosionPowerDrop = explosionSource.func_145772_a(explosion, world, x, y, z, block);
                        ray.decreasePower(getRayDrop(explosionPowerDrop));
                    }
                    if (ray.power > 0.0 && canDestroy(ray, block, x, y, z)) {
                        targetPositions.add(currentPosition);
                        explosion.handleChunkPosition(currentPosition);
                    }
                }
            }
            ray.march(explosion.getBaseRayDist());
            ray.decreasePower(explosion.getBaseRayDist() * explosion.getRayPowerDropRatio());
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

    private float getRayDrop(final float explosionPowerDrop) {
        return (explosionPowerDrop + explosion.getRayDropBump()) * explosion.getBaseRayDist();
    }

    private boolean canDestroy(final Ray ray, final Block block, final int x, final int y, final int z) {
        return explosionSource.func_145774_a(explosion, world, x, y, z, block, (float) ray.power);
    }

    public void finalizeExplosion() {
        active.remove(this);
    }

}
