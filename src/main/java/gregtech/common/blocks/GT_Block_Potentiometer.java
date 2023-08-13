package gregtech.common.blocks;


import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_Item_Potentiometer;
import gregtech.common.render.GT_Renderer_Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class GT_Block_Potentiometer extends GT_Generic_Block {


    public GT_Block_Potentiometer() {
        super(GT_Item_Potentiometer.class, "potentiometer", Material.rock);
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.INSTANCE.mRenderID;
    }

    /**
     * Called upon block activation (right click on the block.)
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     */
    @Override
    public boolean onBlockActivated(
            final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY,
            final float hitZ
                                   ) {
        if (!world.isRemote) {
            final int bump = player.isSneaking() ? -1 : 1;
            int metadata = (world.getBlockMetadata(x, y, z) + bump) % 16;
            if (metadata < 0) {
                metadata += 16;
            }
            GT_Utility.sendChatToPlayer(player, String.format("Power output: %d", metadata));
            world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
        }
        return true;
    }

    public int getColorModifier(final int metadata) {
        int i = 0x00FFFF;
        i |= (8 * (metadata + 1) + 127) << 16;
        return i;
    }

    public short[] getColorModifierList(final int metadata) {
        final short[] rgb = new short[4];
        final int rgbInt = getColorModifier(metadata);
        rgb[0] = (short) ((rgbInt & 0xFF0000) >> 16);
        rgb[1] = (short) ((rgbInt & 0xFF00) >> 8);
        rgb[2] = (short) (rgbInt & 0xFF);
        rgb[3] = (short) 0xFF;
        return rgb;
    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @return
     */
    @Override
    public int isProvidingWeakPower(
            final IBlockAccess world, final int x, final int y, final int z, final int side
                                   ) {
        return world.getBlockMetadata(x, y, z);
    }

    /**
     * Called to determine whether to allow the a block to handle its own indirect power rather than using the default rules.
     *
     * @param world The world
     * @param x     The x position of this block instance
     * @param y     The y position of this block instance
     * @param z     The z position of this block instance
     * @param side  The INPUT side of the block to be powered - ie the opposite of this block's output side
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
    public boolean shouldCheckWeakPower(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return false;
    }



    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return true;
    }

    public ITexture[][] getPotentiometerTextures(final IBlockAccess world, final int x, final int y, final int z) {
        return getPotentiometerTextures(world.getBlockMetadata(x, y, z));
    }

    public ITexture[][] getPotentiometerTextures(final int aMeta) {
        final ITexture[][] textures = new ITexture[6][];
        for (int i = 0; i < 6; i++) {
            textures[i] = getPotentiometerTexture(aMeta);
        }
        return textures;
    }

    public ITexture[] getPotentiometerTexture(final int aMeta) {
        return new ITexture[]{TextureFactory.of(Textures.BlockIcons.BLOCK_POTENTIOMETER_BACKGROUND), TextureFactory.of(Textures.BlockIcons.BLOCK_POTENTIOMETER_FOREGROUND, getColorModifierList(aMeta))};
    }

    public ITexture[] getInventoryTexture(final int aMeta) {
        return getPotentiometerTexture(15);
    }

    /**
     * Get a light value for the block at the specified coordinates, normal ranges are between 0 and 15
     *
     * @param world The current world
     * @param x     X Position
     * @param y     Y position
     * @param z     Z position
     * @return The light value
     */
    @Override
    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        return world.getBlockMetadata(x, y, z);
    }

}
