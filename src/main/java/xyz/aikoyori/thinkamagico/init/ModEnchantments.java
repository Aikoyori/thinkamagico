package xyz.aikoyori.thinkamagico.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.enchantments.EnchantmentWaterbounded;
import xyz.aikoyori.thinkamagico.enchantments.JabbingEnchantment;
import xyz.aikoyori.thinkamagico.enchantments.ShieldlikeEnchantment;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =  DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MainClass.MODID);
    public static RegistryObject<Enchantment> WATERBOUNDED = ENCHANTMENTS.register("waterbounded", () -> new EnchantmentWaterbounded(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}));
    public static RegistryObject<Enchantment> SHIELDLIKE = ENCHANTMENTS.register("shieldlike", () -> new ShieldlikeEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}) {});
    public static RegistryObject<Enchantment> JABBING = ENCHANTMENTS.register("jabbing", () -> new JabbingEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}) {});

}
