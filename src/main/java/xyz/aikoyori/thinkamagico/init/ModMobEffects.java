package xyz.aikoyori.thinkamagico.init;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.potioneffects.DizzyEffect;
import xyz.aikoyori.thinkamagico.potioneffects.IdentityCrisisEffect;


public class ModMobEffects {
    public static final DeferredRegister<Effect> EFFECTS =  DeferredRegister.create(ForgeRegistries.POTIONS, MainClass.MODID);
    public static RegistryObject<Effect> DIZZY = EFFECTS.register("dizzy", () -> new DizzyEffect(EffectType.HARMFUL,9427822));
    public static RegistryObject<Effect> HALLUCINATION = EFFECTS.register("hallucination", () -> new DizzyEffect(EffectType.HARMFUL,0x667344));
    public static RegistryObject<Effect> IDENTITYCRISIS = EFFECTS.register("identity_crisis", () -> new IdentityCrisisEffect(EffectType.HARMFUL,0xff2510));

    public static final DeferredRegister<Potion> POTS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, MainClass.MODID);
    public static RegistryObject<Potion> DIZZY_POT = POTS.register("dizzy",() -> new Potion("dizzy",new EffectInstance( DIZZY.get(),1500)));
    public static RegistryObject<Potion> DIZZY_POT_LONG = POTS.register("long_dizzy",() -> new Potion("long_dizzy",new EffectInstance( DIZZY.get(),3000)));
    public static RegistryObject<Potion> DIZZY_POT_HEAVY = POTS.register("strong_dizzy",() -> new Potion("strong_dizzy",new EffectInstance( DIZZY.get(),1200,1)));
}
