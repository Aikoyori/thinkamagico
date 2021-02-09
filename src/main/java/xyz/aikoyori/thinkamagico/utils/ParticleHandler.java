package xyz.aikoyori.thinkamagico.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.init.ModParticles;
import xyz.aikoyori.thinkamagico.particles.MagicParticle;
import xyz.aikoyori.thinkamagico.particles.MagicParticleStill;

@Mod.EventBusSubscriber(modid = MainClass.MODID,bus= Mod.EventBusSubscriber.Bus.MOD)
public class ParticleHandler  {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent evt)
    {
        Minecraft.getInstance().particles.registerFactory(ModParticles.MAGIC.get(), MagicParticle.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticles.MAGIC_SIGNAL.get(), MagicParticleStill.Factory::new);
    }
}
