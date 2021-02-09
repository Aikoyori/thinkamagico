package xyz.aikoyori.thinkamagico.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.entities.HallucinationProvider;
import xyz.aikoyori.thinkamagico.entities.MagicProjectile;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MainClass.MODID);
    public static final EntityType HALLUTYPE = EntityType.Builder.<HallucinationProvider>create(HallucinationProvider::new, EntityClassification.MISC).size(0.5f,0.5f).setShouldReceiveVelocityUpdates(true).build("hallucination_provider");
    public static final RegistryObject<EntityType<HallucinationProvider>> HALLUCINNATION_PROVIDER = ENTITIES.register("hallucination_provider",()->HALLUTYPE);
    public static final EntityType MAGICPROJTYPE = EntityType.Builder.<MagicProjectile>create(MagicProjectile::new, EntityClassification.MISC).size(0.01f,0.01f).setShouldReceiveVelocityUpdates(true).build("magic_projectile");
    public static final RegistryObject<EntityType<MagicProjectile>> MAGIC_PROJECTILE = ENTITIES.register("magic_projectile",()->MAGICPROJTYPE);


    //public static final RegistryObject<EntityType<HallucinationFakeMobs>> HALLUCINATION_FAKE_MOBS = ENTITIES.register("hallucination_fake_mobs",()->FAKEMOBSTYPE);

    //public static final RegistryObject<EntityType<UnicornEntity>> UNICORN = ENTITY_TYPES.register("unicorn_entity", () -> EntityType.Builder.create(UnicornEntity::new, EntityClassification.AMBIENT).build(null));
    //public static EntityType<Entity> HALLUCINATION_PROVIDER = EntityType.Builder.create(HallucinationProvider::new, EntityClassification.MISC).build(MainClass.MODID+":hallucination_provider");

}
