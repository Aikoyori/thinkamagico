package xyz.aikoyori.thinkamagico.helper;

import com.google.common.collect.Sets;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import xyz.aikoyori.thinkamagico.init.ModEnchantments;

import java.util.Set;

public class BlockingItemHelper {

    public static Set<Item> exclude = Sets.newHashSet();
    public static Set<Item> include = Sets.newHashSet();

    public static final int SWORD_USE_DURATION = 72000;
    public static final int SWORD_LANCE_DURATION = 72000;
    private Item activeItem = Items.AIR;
    private boolean activeBlock;
    private boolean activeLance;

    public void damageSword(PlayerEntity player, float damage) {

        if (damage >= 3.0F) {

            ItemStack stack = player.getActiveItemStack();
            Hand hand = player.getActiveHand();
            int i = 1 + MathHelper.floor(damage);

            stack.damageItem(i, player, entity -> {
                entity.sendBreakAnimation(hand);
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, stack, hand);
            });

            if (stack.isEmpty()) {

                player.setItemStackToSlot(hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                player.resetActiveHand();
                player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    public int getBlockUseDuration(PlayerEntity player) {

        return SWORD_USE_DURATION - player.getItemInUseCount();
    }

    public boolean isActiveItemStackBlocking(PlayerEntity player) {

        return player.isHandActive() && this.canItemStackBlock(player.getActiveItemStack());
    }

    public boolean isActiveItemStackLancing(PlayerEntity player) {

        return player.isHandActive() && this.canItemStackLance(player.getActiveItemStack());
    }


    public boolean canItemStackBlock(ItemStack stack) {
        this.activeBlock = false;
        Item item = stack.getItem();
        if (item != this.activeItem) {

            if (item instanceof Object) {

                this.activeBlock = (EnchantmentHelper.getEnchantments(stack).get(ModEnchantments.SHIELDLIKE.get()) != null);
            }
        }

        return this.activeBlock;
    }
    public boolean canItemStackLance(ItemStack stack) {
        this.activeLance = false;
        Item item = stack.getItem();
        if (item != this.activeItem) {

            if (item instanceof Object) {

                this.activeLance = (EnchantmentHelper.getEnchantments(stack).get(ModEnchantments.JABBING.get()) != null);
            }
        }

            return this.activeLance;
    }

    /**
     * modeled after net.minecraft.entity.LivingEntity#canBlockDamageSource
     */
    public boolean canBlockDamageSource(PlayerEntity player, DamageSource damageSourceIn) {

        Entity entity = damageSourceIn.getImmediateSource();
        if (entity instanceof AbstractArrowEntity) {

            AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity)entity;
            if (abstractarrowentity.getPierceLevel() > 0) {

                return false;
            }
        }

        if (!damageSourceIn.isUnblockable()) {

            Vector3d vec3d2 = damageSourceIn.getDamageLocation();
            if (vec3d2 != null) {

                Vector3d vec3d = player.getLook(1.0F);
                Vector3d vec3d1 = vec3d2.subtractReverse(player.getPositionVec()).normalize();
                vec3d1 = new Vector3d(vec3d1.x, 0.0, vec3d1.z);
                return vec3d1.dotProduct(vec3d) * Math.PI < Math.PI / 3.6; // 100 degrees protection arc
            }
        }

        return false;
    }

}