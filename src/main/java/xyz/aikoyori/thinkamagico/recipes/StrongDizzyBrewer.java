package xyz.aikoyori.thinkamagico.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import xyz.aikoyori.thinkamagico.init.ModMobEffects;

import javax.annotation.Nonnull;

public class StrongDizzyBrewer implements IBrewingRecipe {

    public StrongDizzyBrewer(){

    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        if (ingredient.getItem() == Items.GLOWSTONE_DUST)
            return true;
        else
            return false;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        if (PotionUtils.getPotionFromItem(stack) == ModMobEffects.DIZZY_POT.get())
            return true;
        else
            return false;
    }

    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        /*return new ItemStack(ModMobEffects.DIZZY_POT.get());*/
        if (isInput(input) && isIngredient(ingredient))
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), ModMobEffects.DIZZY_POT_HEAVY.get());
        return ItemStack.EMPTY;
    }
}
