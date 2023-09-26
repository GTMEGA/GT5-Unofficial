package gregtech.common.entities.explosives;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.GT_NukeExplosion;
import lombok.NonNull;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class GT_Entity_NukeExplosive extends GT_Entity_Explosive {

    private boolean isCalculating = true;

    private int explosionDestructionTimer = 0;

    private final Map<ChunkPosition, Double> radii = new HashMap<>();

    @SuppressWarnings("unused")
    public GT_Entity_NukeExplosive(World world) {
        super(world);
    }

    public GT_Entity_NukeExplosive(World world, double x, double y, double z, EntityLivingBase placedBy, int metadata) {
        super(world, x, y, z, placedBy, metadata);
    }

    @Override
    protected @NonNull GT_Explosion createExplosion() {
        return new GT_NukeExplosion(worldObj, this, posX, posY, posZ, GT_Values.NUKEPower);
    }

    @Override
    public float getBlockResistance(Explosion explosion, World world, int x, int y, int z, Block block) {
        val oldResistance = defaultBlockResistance(explosion, world, x, y, z, block);
        if (oldResistance > 10.0) {
            return oldResistance;
        }
        return 0;
    }

    @Override
    public Block getBlockToRenderAs() {
        return GregTech_API.sBlockNuke;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.NUKE.getTextureFile();
    }

    @Override
    protected void explosionStuff() {
        val pc = getPreCalc();
        if (explosion == null || pc == null) {
            setDead();
            return;
        }
        if (isCalculating) {
            this.doExplode();
            pc.setContinueCalculating(false);
            isCalculating = false;
        }
        //
        //
        //
        val N = GT_Values.NUKEDestructionTime;
        val i = explosionDestructionTimer;
        val sliceMin = getSlice(i, N);
        val sliceMax = getSlice(i + 1, N);
        val maxRadius = GT_Values.NUKERadius + 11;
        /*System.out.print("sliceMax = " + sliceMax * maxRadius);
        System.out.println(" maxRadius = " + maxRadius);*/
        //
        pc.getTargetPositions().stream().filter(pos -> isValidPositionForSlice(pos, sliceMin, maxRadius, sliceMax)).forEach(this::handleDestruction);
        //
        if (explosionDestructionTimer >= GT_Values.NUKEDestructionTime) {
            pc.finalizeExplosion();
            super.endExplosion();
        }
        // System.out.println("explosionDestructionTimer = " + explosionDestructionTimer);
        explosionDestructionTimer += 1;
    }

    private double getSlice(int i, int n) {
        if (i == 0) {
            return 0;
        }
        return Math.cbrt((double) (i) / n);
    }

    private boolean isValidPositionForSlice(ChunkPosition pos, double sliceMin, float maxRadius, double sliceMax) {
        double dist = getRadiusForPosition(pos);
        return dist >= sliceMin * maxRadius && dist < sliceMax * maxRadius;
    }

    private void handleDestruction(ChunkPosition pos) {
        // System.out.println("handleDestruction: " + pos.chunkPosX + ", " + pos.chunkPosY + ", " + pos.chunkPosZ);
        val block = worldObj.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
        // explosion.destroyBlock(block, pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
        block.onBlockExploded(worldObj, pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ, this.explosion);
    }

    private Double getRadiusForPosition(ChunkPosition pos) {
        return radii.computeIfAbsent(pos, p -> {
            val x = pos.chunkPosX - explosion.getX();
            val y = pos.chunkPosY - explosion.getY();
            val z = pos.chunkPosZ - explosion.getZ();
            return Math.sqrt(x * x + y * y + z * z);
        });
    }

    @Override
    protected void endExplosion() {

    }

}
