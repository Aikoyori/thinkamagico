package xyz.aikoyori.thinkamagico.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.sounds.WhiteNoise;

public class ModAudio{

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MainClass.MODID);
    public static RegistryObject<SoundEvent> STATIC = SOUNDS.register("static",()->new SoundEvent(new ResourceLocation(MainClass.MODID,"static")));
}
