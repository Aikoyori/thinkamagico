package xyz.aikoyori.thinkamagico.init;

import net.minecraft.client.particle.Particle;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.particles.MagicParticle;
import xyz.aikoyori.thinkamagico.potioneffects.DizzyEffect;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MainClass.MODID);

    public static RegistryObject<BasicParticleType> MAGIC = PARTICLES.register("magic", () -> new BasicParticleType(true));
    public static RegistryObject<BasicParticleType> MAGIC_SIGNAL = PARTICLES.register("magic_signal", () -> new BasicParticleType(true));
}
