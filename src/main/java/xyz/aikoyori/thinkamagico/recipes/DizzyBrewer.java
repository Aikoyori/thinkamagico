package xyz.aikoyori.thinkamagico.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import xyz.aikoyori.thinkamagico.init.ModMobEffects;

import javax.annotation.Nonnull;

public class DizzyBrewer implements IBrewingRecipe {

    public DizzyBrewer(){

    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        if (ingredient.getItem() == Items.ROTTEN_FLESH)
            return true;
        else
            return false;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        if (PotionUtils.getPotionFromItem(stack) == Potions.WATER)
            return true;
        else
            return false;
    }

    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        /*return new ItemStack(ModMobEffects.DIZZY_POT.get());*/
        if (isInput(input) && isIngredient(ingredient))
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), ModMobEffects.DIZZY_POT.get());
        return ItemStack.EMPTY;
    }
}
