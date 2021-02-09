package xyz.aikoyori.thinkamagico.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;

public class DizzyPotionItem extends PotionItem {

    public DizzyPotionItem(Properties builder) {
        super(builder);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
