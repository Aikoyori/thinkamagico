package xyz.aikoyori.thinkamagico.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class ShieldlikeEnchantment extends Enchantment {
    public ShieldlikeEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] equipSlot) {
        super(rarity, type, equipSlot);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
