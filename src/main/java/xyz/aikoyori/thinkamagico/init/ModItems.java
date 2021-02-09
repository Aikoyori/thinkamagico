package xyz.aikoyori.thinkamagico.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.items.MagicCaneItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MainClass.MODID);
    public static RegistryObject<Item> SANDBLUE = ITEMS.register("sandblue", () -> new Item(new Item.Properties().group(ModCreativeTab.MOD_ITEM_GROUP)));
    public static RegistryObject<Item> BLAZIFIED_SANDBLUE = ITEMS.register("blazified_sandblue", () -> new Item(new Item.Properties().group(ModCreativeTab.MOD_ITEM_GROUP)));
    public static RegistryObject<Item> SANDBLUE_SAND = ITEMS.register("sandblue_sand",  () -> new BlockItem(ModBlocks.SANDBLUE_SAND.get() ,new Item.Properties().group(ModCreativeTab.MOD_ITEM_GROUP)));
    public static RegistryObject<Item> SANDBLUE_CRYSTAL_BLOCK = ITEMS.register("sandblue_crystal_block",  () -> new BlockItem(ModBlocks.SANDBLUE_CRYSTAL.get() ,new Item.Properties().group(ModCreativeTab.MOD_ITEM_GROUP)));
    public static RegistryObject<Item> MAGIC_CANE = ITEMS.register("magic_cane", () -> new MagicCaneItem(new Item.Properties().group(ModCreativeTab.MOD_ITEM_GROUP)));


}
