package xyz.aikoyori.thinkamagico.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xyz.aikoyori.thinkamagico.entities.BlazePlatedMarineItemEntity;

import javax.annotation.Nullable;

public class BlazePlatedMarineGemstoneItem extends Item {

    public BlazePlatedMarineGemstoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new BlazePlatedMarineItemEntity(world,location.getPosX(),location.getPosY(),location.getPosZ());
        //return null;
    }
}
