package xyz.aikoyori.thinkamagico;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xyz.aikoyori.thinkamagico.client.renderer.MagicProjectileRenderer;
import xyz.aikoyori.thinkamagico.client.renderer.SwordBlockRenderer;
import xyz.aikoyori.thinkamagico.client.renderer.HallucinationProviderRenderer;
import xyz.aikoyori.thinkamagico.helper.InitiateBlockHandler;
import xyz.aikoyori.thinkamagico.init.ModBlocks;
import xyz.aikoyori.thinkamagico.init.ModEntities;
import xyz.aikoyori.thinkamagico.worldgen.GenWorldOre;

@Mod.EventBusSubscriber(modid=MainClass.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModEventBusSubscriber {

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(

        );
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(

        );
    }
    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
        return setup(entry, new ResourceLocation(MainClass.MODID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
        entry.setRegistryName(registryName);
        return entry;
    }



    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent evt) {
        // sword blocking
        MinecraftForge.EVENT_BUS.register(new InitiateBlockHandler());
        GenWorldOre.registerOres();
    }
/*
    @SubscribeEvent
    public static void onEntitySetup(final RegistryEvent.Register<EntityType<?>> entityTypeRegister)
    {
        entityTypeRegister.getRegistry().registerAll(
                ModEntities.HALLUCINATION_PROVIDER
        );
    }
*/
    @SubscribeEvent
    public static void onFMLClientSetup(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new SwordBlockRenderer());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.HALLUCINNATION_PROVIDER.get(), HallucinationProviderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MAGIC_PROJECTILE.get(), MagicProjectileRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(ModEntities.HALLUCINATION_FAKE_MOBS.get(), FakeMobsRenderer::new);
        RenderTypeLookup.setRenderLayer(ModBlocks.SANDBLUE_SAND.get(), RenderType.getCutout());

        //RenderingRegistry.registerEntityRenderingHandler(HallucinationProvider.class,HallucinationProvider :: new);
    }


}

