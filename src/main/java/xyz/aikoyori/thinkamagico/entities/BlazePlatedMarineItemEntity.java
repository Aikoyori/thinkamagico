package xyz.aikoyori.thinkamagico.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xyz.aikoyori.thinkamagico.init.ModItems;

public class BlazePlatedMarineItemEntity extends ItemEntity {

    public BlazePlatedMarineItemEntity(EntityType<? extends ItemEntity> itemEntity, World world) {
        super(itemEntity, world);
    }

    public BlazePlatedMarineItemEntity(World world, double posX, double posY, double posZ) {
        super(world,posX,posY,posZ);
    }

    @Override
    public boolean isInWater() {

        return super.isInWater();
    }

    @Override
    public void tick() {
        if(this.isInWater())
        {
            (ModItems.SANDBLUE).get().createEntity(this.world,this,new ItemStack(ModItems.SANDBLUE.get().asItem(),this.getItem().getCount()));
            this.remove();
        }
        super.tick();
    }
}
