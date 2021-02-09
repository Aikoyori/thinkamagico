package xyz.aikoyori.thinkamagico.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import xyz.aikoyori.thinkamagico.init.ModEnchantments;

public class JabbingEnchantment extends Enchantment {
    public JabbingEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType[] equipSlot) {
        super(rarity, type, equipSlot);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        if (ench == ModEnchantments.SHIELDLIKE.get()) return false;
        return super.canApplyTogether(ench);
    }
}
