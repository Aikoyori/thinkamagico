package xyz.aikoyori.thinkamagico.blocks;

import net.minecraft.block.*;

public class SandblueSand extends FallingBlock {
    public SandblueSand(Properties prop) {
        super(prop);

    }


    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

}
