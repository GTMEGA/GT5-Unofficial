package gregtech.common.misc.explosions;


import codechicken.lib.math.MathHelper;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;


@Getter
@RequiredArgsConstructor
public class GT_Explosion_PreCalculation {

    @RequiredArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode(of = {"aX", "aY", "aZ"})
    public static class Ray {

        private final @NonNull GT_Explosion_PreCalculation parent;

        private final double aX, aY, aZ;

        private final double maxLength;

        private double distance = 0.0;

        @Setter
        private double power = 0.0;

        @Accessors(fluent = true)
        private boolean canContinue = true;

        protected void decreasePower(final float drop) {
            power -= drop;
            if (power <= 0.0) {
                disable();
            }
        }

        private void march(final double amount) {
            distance += amount;
        }

        @NonNull
        private ChunkPosition getChunkPosition() {
            return new ChunkPosition(MathHelper.floor_double(getX()), MathHelper.floor_double(getY()), MathHelper.floor_double(getZ()));
        }

        double getX() {
            return aX * distance + parent.sourceX;
        }

        double getY() {
            return aY * distance + parent.sourceY;
        }

        double getZ() {
            return aZ * distance + parent.sourceZ;
        }

        private void disable() {
            canContinue = false;
        }

        double getLength() {
            val x = aX * distance;
            val y = aY * distance;
            val z = aZ * distance;
            return Math.sqrt(x * x + y * y + z * z);
        }

    }


    private final @NonNull GT_Entity_Explosive explosionSource;

    private final @NonNull GT_Explosion explosion;

    private final @NonNull World world;

    private final int sourceX, sourceY, sourceZ;

    private final int maxFuse;

    private final @NonNull Set<Ray> rays = new HashSet<>();

    private int ticked = 0;

    private final Set<ChunkPosition> seenPositions = new HashSet<>(), targetPositions = new HashSet<>();

    public void createRays() {
        val maxRaysX = explosion.getMaxX();
        val maxRaysY = explosion.getMaxY();
        val maxRaysZ = explosion.getMaxZ();
        for (int i = 0; i < maxRaysX; i++) {
            for (int j = 0; j < maxRaysY; j++) {
                for (int k = 0; k < maxRaysZ; k++) {
                    if (i == 0 || i == maxRaysX - 1 || j == 0 || j == maxRaysY - 1 || k == 0 || k == maxRaysZ - 1) {
                        var aX = i / (double)(maxRaysX - 1) * 2.0  - 1.0;
                        var aY = j / (double)(maxRaysY - 1) * 2.0  - 1.0;
                        var aZ = k / (double)(maxRaysZ - 1) * 2.0  - 1.0;
                        val magnitude = Math.sqrt(aX * aX + aY * aY + aZ * aZ);
                        aX /= magnitude;
                        aY /= magnitude;
                        aZ /= magnitude;
                        val ray = new Ray(this, aX, aY, aZ, explosion.getExpRadius());
                        ray.setPower(explosion.getRayPower());
                        rays.add(ray);
                    }
                }
            }
        }
    }

    public void tick() {
        val proportionEnd = (ticked + 1) / (double) maxFuse;
        for (val ray: rays) {
            if (!ray.canContinue()) {
                continue;
            }
            doRayTick(ray, proportionEnd);
        }
        ticked += 1;
    }

    private void doRayTick(final @NonNull Ray ray, final double proportionEnd) {
        while (ray.canContinue() && explosion.rayValid(ray) && ray.getLength() < explosion.getRangeForRay(ray) * proportionEnd) {
            val currentPosition = ray.getChunkPosition();
            if (hasNotEncountered(currentPosition)) {
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
                    if (ray.getPower() > 0.0 && canDestroy(ray, block, x, y, z)) {
                        targetPositions.add(currentPosition);
                        explosion.handleChunkPosition(currentPosition);
                    }
                }
                ray.march(explosion.getBaseRayDist());
            }
            ray.march(explosion.getBaseRayDist());
            ray.decreasePower(explosion.getBaseRayDist() * explosion.getRayPowerDropRatio());
        }
        if (!explosion.rayValid(ray)) {
            ray.disable();
        }
    }

    private boolean hasNotEncountered(final ChunkPosition currentPosition) {
        return !seenPositions.contains(currentPosition) && explosion.handlePositionPre(currentPosition);
    }

    private float getRayDrop(final float explosionPowerDrop) {
        return (explosionPowerDrop + explosion.getRayDropBump()) * explosion.getBaseRayDist();
    }

    private boolean canDestroy(final Ray ray, final Block block, final int x, final int y, final int z) {
        return explosionSource.func_145774_a(explosion, world, x, y, z, block, (float) ray.getPower());
    }

}
