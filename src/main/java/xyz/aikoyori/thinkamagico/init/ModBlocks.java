package xyz.aikoyori.thinkamagico.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.blocks.SandblueSand;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MainClass.MODID);
    public static RegistryObject<Block> SANDBLUE_SAND = BLOCKS.register("sandblue_sand", () -> new SandblueSand(Block.Properties.create(Material.SAND).hardnessAndResistance(0.5F,0.7F).sound(SoundType.SAND)));
    public static RegistryObject<Block> SANDBLUE_CRYSTAL = BLOCKS.register("sandblue_crystal_block", () -> new Block(Block.Properties.create(Material.GLASS).hardnessAndResistance(4.0F,6.0F).sound(SoundType.GLASS)));

    public ModBlocks()
    {


    }

}