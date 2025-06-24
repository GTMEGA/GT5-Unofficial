package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GT_Generic_Block;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Block_Rubber_Log extends GT_Generic_Block {
    public enum Textures {
        Side,
        Side_Dry,
        Side_Wet,
        Top,
        ;
    }

    @RequiredArgsConstructor
    public enum Cardinal {
        SOUTH(ForgeDirection.SOUTH),
        WEST(ForgeDirection.WEST),
        NORTH(ForgeDirection.NORTH),
        EAST(ForgeDirection.EAST),
        ;

        public final ForgeDirection direction;

        public int mask() {
            return this.ordinal();
        }

        private static final int MASK = 0b11;

        public static Cardinal of(int meta) {
            return values()[meta & MASK];
        }
    }

    public enum NodeState {
        NONE,
        DRY,
        WET,
        ;

        public int mask() {
            return this.ordinal() << 2;
        }

        private static final int MASK = 0b1100;

        public static NodeState of(int meta) {
            return values()[(meta & MASK) >> 2];
        }
    }

    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;

    public GT_Block_Rubber_Log() {
        super(ItemBlock.class, "BlockRubberLog", Material.wood);

        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setStepSound(soundTypeWood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        val meta = world.getBlockMetadata(x, y, z);

        return this.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        val facing = ForgeDirection.getOrientation(side);
        if (facing == ForgeDirection.UP || facing == ForgeDirection.DOWN) {
            return this.icons[Textures.Top.ordinal()];
        }

        val cardinal = Cardinal.of(meta);
        val nodeState = NodeState.of(meta);

        if (nodeState != NodeState.NONE && cardinal.direction == facing) {
            return this.icons[nodeState.ordinal()];
        }

        return this.icons[Textures.Side.ordinal()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.icons = new IIcon[Textures.values().length];

        val base = GT_Values.RES_PATH_BLOCK + "misc/blockLogRubber";

        this.icons[Textures.Top.ordinal()] = reg.registerIcon(base + "End");
        this.icons[Textures.Side.ordinal()] = reg.registerIcon(base + "Side");
        this.icons[Textures.Side_Dry.ordinal()] = reg.registerIcon(base + "Side.dry");
        this.icons[Textures.Side_Wet.ordinal()] = reg.registerIcon(base + "Side.wet");
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if (!world.isRemote) {
            int count = this.quantityDropped(world.rand);

            for (int j1 = 0; j1 < count; j1++) {
                if (!(world.rand.nextFloat() > chance)) {
                    Item item = this.getItemDropped(meta, world.rand, fortune);
                    if (item != null) {
                        this.dropBlockAsItem(world, x, y, z, new ItemStack(item, 1, 0));
                    }

                    if (meta != 0 && world.rand.nextInt(6) == 0) {
                        this.dropBlockAsItem(world, x, y, z, ItemList.Resin.get(1));
                    }
                }
            }
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int b) {
        byte range = 4;
        int l = range + 1;
        if (world.checkChunksExist(x - l, y - l, z - l, x + l, y + l, z + l)) {
            for (int xOffset = -range; xOffset <= range; xOffset++) {
                for (int yOffset = -range; yOffset <= range; yOffset++) {
                    for (int zOffset = -range; zOffset <= range; zOffset++) {
                        Block neighbor = world.getBlock(x + xOffset, y + yOffset, z + zOffset);
                        if ( neighbor == ItemList.Rubber_Leaves.getBlock()) {
                            int meta = world.getBlockMetadata(x + xOffset, y + yOffset, z + zOffset);
                            if ((meta & 8) == 0) {
                                world.setBlockMetadataWithNotify(x + xOffset, y + yOffset, z + zOffset, meta | 8, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);

            val facing = Cardinal.of(meta);
            val nodeState = NodeState.of(meta);

            if (nodeState == NodeState.DRY) {
                if (random.nextInt(200) == 0) {
                    val newMeta = facing.mask() | NodeState.WET.mask();
                    val flag = GT_Values.setBlockNotify | GT_Values.setBlockUpdate | GT_Values.setBlockNoUpdateFromClient;

                    world.setBlockMetadataWithNotify(x, y, z, newMeta, flag);
                } else {
                    world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
                }
            }
        }
    }

    public int tickRate(World world) {
        return 100;
    }

    public int getMobilityFlag() {
        return 2;
    }

    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public boolean isWood(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 4;
    }

    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 20;
    }
}
