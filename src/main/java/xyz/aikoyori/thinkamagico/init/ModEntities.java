package xyz.aikoyori.thinkamagico.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.entities.HallucinationProvider;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MainClass.MODID);
    public static final EntityType HALLUTYPE = EntityType.Builder.<HallucinationProvider>create(HallucinationProvider::new, EntityClassification.MISC).size(0.5f,0.5f).setShouldReceiveVelocityUpdates(true).build("hallucination_provider");
    //public static final EntityType FAKEMOBSTYPE = EntityType.Builder.<HallucinationProvider>create(HallucinationProvider::new, EntityClassification.MISC).size(0.5f,1.8f).setShouldReceiveVelocityUpdates(false).build("hallucination_fake_mobs");
    public static final RegistryObject<EntityType<HallucinationProvider>> HALLUCINNATION_PROVIDER = ENTITIES.register("hallucination_provider",()->HALLUTYPE);
    //public static final RegistryObject<EntityType<HallucinationFakeMobs>> HALLUCINATION_FAKE_MOBS = ENTITIES.register("hallucination_fake_mobs",()->FAKEMOBSTYPE);

    //public static final RegistryObject<EntityType<UnicornEntity>> UNICORN = ENTITY_TYPES.register("unicorn_entity", () -> EntityType.Builder.create(UnicornEntity::new, EntityClassification.AMBIENT).build(null));
    //public static EntityType<Entity> HALLUCINATION_PROVIDER = EntityType.Builder.create(HallucinationProvider::new, EntityClassification.MISC).build(MainClass.MODID+":hallucination_provider");

}
