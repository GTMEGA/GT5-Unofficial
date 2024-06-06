package gregtech.common.misc.explosions.detonator_util;

import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion_Info;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import gregtech.common.misc.explosions.definitions.GT_DaisyCutterTier;
import gregtech.common.misc.explosions.definitions.GT_FlatBombTier;
import gregtech.common.misc.explosions.definitions.GT_MiningExplosiveTier;
import gregtech.common.misc.explosions.definitions.GT_TunnelExplosiveTier;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RemoteDetonationTargetList {

    @Getter
    @RequiredArgsConstructor
    public enum ExplosiveType {
        MINING(
                GT_MiningExplosiveTier.class,
                new ExplosiveType.ExplosiveColorPalette(new Color(187, 102, 41, 0xFF), new Color(133, 69, 22, 0xFF), new Color(98, 98, 98, 0xFF), new Color(138, 137, 137, 0xFF))
        ),
        TUNNEL(
                GT_TunnelExplosiveTier.class,
                new ExplosiveType.ExplosiveColorPalette(new Color(133, 84, 10, 0xFF), new Color(80, 40, 4, 0xFF), new Color(124, 124, 124, 0xFF), new Color(89, 89, 89, 0xFF))
        ),
        DAISY_CUTTER(
                GT_DaisyCutterTier.class,
                new ExplosiveType.ExplosiveColorPalette(new Color(22, 98, 22, 0xFF), new Color(15, 68, 15, 0xFF), new Color(84, 175, 84, 0xFF), new Color(107, 222, 107, 0xFF))
        ),
        FLAT_BOMB(
                GT_FlatBombTier.class,
                new ExplosiveType.ExplosiveColorPalette(new Color(98, 98, 98, 0xFF), new Color(137, 137, 137, 0xFF), new Color(98, 98, 98, 0xFF), new Color(137, 137, 137, 0xFF))
        );


        @RequiredArgsConstructor
        @Getter
        public static class ExplosiveColorPalette {

            private final Color backgroundColor;

            private final Color backgroundColorSelected;

            private final Color textColor;

            private final Color textColorSelected;


        }

        public static ExplosiveType getType(final GT_Block_Explosive<?> explosive) {
            for (val type : values()) {
                val tier    = type.getTier();
                val expTier = explosive.getTier().getClass();
                if (expTier.equals(tier)) {
                    return type;
                }
            }
            return null;
        }

        private final Class<? extends IGT_ExplosiveTier<?>> tier;

        private final ExplosiveType.ExplosiveColorPalette palette;

    }


    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString(onlyExplicitlyIncluded = true)
    public static class Target {

        @EqualsAndHashCode.Exclude
        private int index = -1;

        @ToString.Include
        @EqualsAndHashCode.Exclude
        private ExplosiveType explosiveType;

        @ToString.Include
        @EqualsAndHashCode.Exclude
        @Setter
        private IGT_ExplosiveTier<?> tier;

        @ToString.Include
        private int x = -1, y = -1, z = -1;

        @EqualsAndHashCode.Exclude
        private boolean triggered = false;

        @EqualsAndHashCode.Exclude
        private boolean valid = false;

        public Target(final int x, final int y, final int z) {
            this(-1, null, null, x, y, z, false, false);
        }

        public Target(final int index, final ExplosiveType explosive, final IGT_ExplosiveTier<?> tier, final int x, final int y, final int z) {
            this(index, explosive, tier, x, y, z, false, true);
        }

        @NonNull
        public Target addFromCompound(final @NonNull NBTTagCompound compound) {
            setValid(compound.hasKey("index") && compound.hasKey("x") && compound.hasKey("y") && compound.hasKey("z"));
            setIndex(compound.getInteger("index"));
            setExplosiveType(ExplosiveType.values()[compound.getInteger("type")]);
            setTier(GT_Explosion_Info.getTierType(compound.getInteger("expType"), compound.getInteger("tier")));
            setX(compound.getInteger("x"));
            setY(compound.getInteger("y"));
            setZ(compound.getInteger("z"));
            setTriggered(compound.getBoolean("t"));
            return this;
        }

        public double getDistance(final EntityPlayer player) {
            val dX = player.posX - x;
            val dY = player.posY - y;
            val dZ = player.posZ - z;
            return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        }

        @NonNull
        protected NBTTagCompound writeToNBT(final @NonNull NBTTagCompound nbtTagCompound) {
            nbtTagCompound.setInteger("index", index);
            nbtTagCompound.setInteger("type", explosiveType.ordinal());
            nbtTagCompound.setInteger("expType", tier.getTierTrackIndex());
            nbtTagCompound.setInteger("tier", tier.getTier());
            nbtTagCompound.setInteger("x", x);
            nbtTagCompound.setInteger("y", y);
            nbtTagCompound.setInteger("z", z);
            nbtTagCompound.setBoolean("t", triggered);
            return nbtTagCompound;
        }

        protected void trigger(final @NonNull World world, final @NonNull EntityPlayer player) {
            val block    = world.getBlock(x, y, z);
            val metadata = world.getBlockMetadata(x, y, z);
            if (block instanceof GT_Block_Explosive && !world.isRemote) {
                val explosive = (GT_Block_Explosive<?>) block;
                if (explosive.isPrimed(metadata)) {
                    explosive.remoteTrigger(world, x, y, z, player);
                }
            }
            triggered = true;
        }

        protected boolean canDetonate(final @NonNull World world, final int pX, final int pY, final int pZ) {
            boolean loaded = isLoaded(world), inRange = getDistanceFromTarget(pX, pY, pZ) <= GT_Values.MEMaxRemoteRange;
            return loaded && inRange && !triggered;
        }

        protected boolean isLoaded(final @NonNull World world) {
            return world.getChunkProvider().chunkExists(x >> 4, z >> 4);
        }

        public int getDistanceFromTarget(final int pX, final int pY, final int pZ) {
            return magnitude(pX - this.x, pY - this.y, pZ - this.z);
        }

        private int magnitude(final int x, final int y, final int z) {
            return MathHelper.floor_double(Math.sqrt(x * x + y * y + z * z));
        }

    }

    @NonNull
    public static RemoteDetonationTargetList readFromNBT(final @NonNull NBTTagCompound compound, final @NonNull EntityPlayer player) {
        val delay = compound.hasKey("delay") ? compound.getInteger("delay") : GT_Values.MERemoteDelay;
        return compound.hasKey("targets") && compound.hasKey("dim") ? new RemoteDetonationTargetList(compound).addFromList(compound.getTagList("targets", Constants.NBT.TAG_COMPOUND)) : new RemoteDetonationTargetList(player, delay);
    }

    @NonNull
    public RemoteDetonationTargetList addFromList(final @NonNull NBTTagList listComp) {
        for (var i = 0; i < listComp.tagCount(); i++) {
            addTarget(new Target().addFromCompound(listComp.getCompoundTagAt(i)));
        }
        return this;
    }

    private void addTarget(final @NonNull Target target) {
        if (target.isValid() && !targets.containsValue(target) && !targetsReversed.containsKey(target)) {
            getNextIndex(target);
            targets.put(target.getIndex(), target);
            targetsReversed.put(target, target.getIndex());
            maxTarget = Math.max(target.getIndex(), maxTarget);
        }
    }

    private void getNextIndex(final @NonNull Target target) {
        target.setIndex(getNextIndex(target.getIndex()));
    }

    private int getNextIndex(final int initial) {
        var temp = initial;
        while (targets.containsKey(temp)) {
            temp += 1;
        }
        return temp;
    }

    private final Map<Integer, Target> targets = new HashMap<>();

    private final Map<Target, Integer> targetsReversed = new HashMap<>();

    private int dimension;

    @Builder.Default
    private int maxTarget = -1, timer = -1;

    @Builder.Default
    private int delay = GT_Values.MERemoteDelay;

    @Builder.Default
    private boolean triggered = false, done = true;

    public RemoteDetonationTargetList(final @NonNull EntityPlayer player, final int delay) {
        this(player.dimension, -1, -1, GT_Values.MERemoteDelay, false, false);
        this.delay = delay;
    }

    public RemoteDetonationTargetList(final @NonNull NBTTagCompound compound) {
        setDimension(compound.getInteger("dim"));
        setTriggered(compound.getBoolean("active"));
        setDelay(compound.getInteger("delay"));
        setTimer(compound.getInteger("timer"));
        setDone(compound.getBoolean("done"));
    }

    public void clear() {
        maxTarget = -1;
        targets.clear();
        targetsReversed.clear();
    }

    public boolean containsTarget(final int x, final int y, final int z) {
        return targets.containsValue(new Target(x, y, z)) && targetsReversed.containsKey(new Target(x, y, z));
    }

    @NonNull
    public NBTTagCompound writeToNBT() {
        return writeToNBT(new NBTTagCompound());
    }

    @NonNull
    public NBTTagCompound writeToNBT(final @NonNull NBTTagCompound compound) {
        if (isDone()) {
            val result = new NBTTagCompound();
            result.setInteger("delay", delay);
            return new NBTTagCompound();
        }
        val list = new NBTTagList();
        for (val target : targets.values()) {
            list.appendTag(target.writeToNBT(new NBTTagCompound()));
        }
        compound.setInteger("num", numTargets());
        compound.setBoolean("active", triggered);
        compound.setInteger("timer", timer);
        compound.setInteger("delay", delay);
        compound.setInteger("dim", dimension);
        compound.setBoolean("done", done);
        compound.setTag("targets", list);
        return compound;
    }

    public int numTargets() {
        return targets.size();
    }

    public void tick(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
        if (!triggered) {
            return;
        }
        if (this.timer++ % delay == 0) {
            targets.values().stream().filter(target -> target.canDetonate(world, x, y, z)).min(Comparator.comparingInt(target -> target.index)).ifPresent(firstValid -> firstValid.trigger(world, player));
        }
        if (!hasUntriggered(world, x, y, z)) {
            setDone(true);
        }
    }

    public boolean hasUntriggered(final @NonNull World world, final int x, final int y, final int z) {
        return targets.values().stream().anyMatch(target -> target.canDetonate(world, x, y, z));
    }

    public void addTarget(final ExplosiveType type, final IGT_ExplosiveTier<?> tier, final int x, final int y, final int z) {
        val nextIndex = getNextIndex(maxTarget);
        addTarget(new Target(nextIndex, type, tier, x, y, z));
    }

    public void removeTarget(final int x, final int y, final int z) {
        removeTarget(new Target(x, y, z));
    }

    private void removeTarget(final @NonNull Target target) {
        if (targetsReversed.containsKey(target)) {
            val key = targetsReversed.get(target);
            targetsReversed.remove(target);
            targets.remove(key);
        }
    }

    public void trigger(final @NonNull World world, final @NonNull EntityPlayer player) {
        if (world.isRemote || isEmpty() || !validDimension(player) || triggered || done) {
            return;
        }
        setTriggered(true);
        setTimer(0);
    }

    public boolean isEmpty() {
        return targets.isEmpty();
    }

    public boolean validDimension(final @NonNull EntityPlayer player) {
        return validDimension(player.dimension);
    }

    public boolean validDimension(final int dimension) {
        if (isEmpty()) {
            this.dimension = dimension;
        }
        return this.dimension == dimension;
    }

}
