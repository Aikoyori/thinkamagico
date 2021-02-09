package xyz.aikoyori.thinkamagico.enchantments;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.MathHelper;

public class EnchantmentWaterbounded extends Enchantment {


    public EnchantmentWaterbounded(Rarity p_i46731_1_, EnchantmentType p_i46731_2_, EquipmentSlotType[] p_i46731_3_) {
        super(p_i46731_1_, p_i46731_2_, p_i46731_3_);
    }

    @Override
    public void onEntityDamaged(LivingEntity attacker, Entity target, int enchLevel) {

        target.setAir(MathHelper.clamp(0,target.getAir()-(enchLevel*20),target.getMaxAir()));
        target.setFire(-1);
        target.setFire(-1);
        if (EntityType.ZOMBIE.equals(target.getType())) {

        }
        float multiplier=enchLevel/3;
        if(target.isInWater())
        {
            target.addVelocity(0,-multiplier,0);
        }
        if(target.getEntityWorld().getBlockState(target.getPosition().toImmutable()).getBlock()==Blocks.LAVA)
        {
            if(target.getEntityWorld().getBlockState(target.getPosition().toImmutable()).get(FlowingFluidBlock.LEVEL)==0)
            {
                target.getEntityWorld().setBlockState(target.getPosition().toImmutable(),Blocks.OBSIDIAN.getDefaultState());

            }
            else
            {
                target.getEntityWorld().setBlockState(target.getPosition().toImmutable(),Blocks.COBBLESTONE.getDefaultState());

            }
        }



        super.onEntityDamaged(attacker, target, enchLevel);
    }



    @Override
    public int getMaxLevel() {
        return 3;
    }
}
