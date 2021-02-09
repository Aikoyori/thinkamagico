package xyz.aikoyori.thinkamagico;

import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.aikoyori.thinkamagico.init.*;
import xyz.aikoyori.thinkamagico.recipes.DizzyBrewer;
import xyz.aikoyori.thinkamagico.recipes.LongDizzyBrewer;
import xyz.aikoyori.thinkamagico.recipes.StrongDizzyBrewer;

@Mod(MainClass.MODID)
public class MainClass
{
	public static final String MODID = "thinkamagico";
	public static final boolean BOTH_HANDS = false;
	public static final boolean DEFLECT_PROJECTILES = true;
	public static final int PARRY_WINDOW = 10;
	private static final Logger LOGGER = LogManager.getLogger();

	public MainClass() {
		ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModMobEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModMobEffects.POTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModEnchantments.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModAudio.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		//ModEntities.registerEntitySpawnEggs();

		BrewingRecipeRegistry.addRecipe(new DizzyBrewer());
		BrewingRecipeRegistry.addRecipe(new StrongDizzyBrewer());
		BrewingRecipeRegistry.addRecipe(new LongDizzyBrewer());
		//LOGGER.debug("konnichiwa, sekai sama~");
	}


}