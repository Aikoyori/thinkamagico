package xyz.aikoyori.thinkamagico.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.init.SwordBlockingRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;


public class InitiateBlockHandler {
    private final BlockingItemHelper blockingHelper = new BlockingItemHelper();
    public Map<UUID, ItemStack> lastHeldItem = new HashMap<>();
    public Map<UUID, Boolean> canBlock = new HashMap<>();
    private boolean lancing = false;
    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRightClickItem(final PlayerInteractEvent.RightClickItem evt) {
        PlayerEntity player = evt.getPlayer();
        if (this.blockingHelper.canItemStackBlock(evt.getItemStack())) {

            boolean flag = true;
            ItemStack offhandStack = player.getHeldItemOffhand();
            if (evt.getHand() == Hand.MAIN_HAND && !offhandStack.isEmpty()) {


                Predicate<ItemStack> actionNone = item -> item.getItem().getUseAction(item) == UseAction.NONE;
                Predicate<ItemStack> foodNotHungry = foodItem -> foodItem.getItem().getFood() != null && !player.canEat(foodItem.getItem().getFood().canEatWhenFull());
                Predicate<ItemStack> bowNoAmmo = bowItem -> {
                    UseAction action = bowItem.getItem().getUseAction(bowItem);
                    return (action == UseAction.BOW || action == UseAction.CROSSBOW) && player.findAmmo(bowItem).isEmpty();
                };
                Predicate<ItemStack> spearNoUse = tridentItem -> {
                    UseAction action = tridentItem.getItem().getUseAction(tridentItem);
                    return action == UseAction.SPEAR && (tridentItem.getDamage() >= tridentItem.getMaxDamage() - 1 || EnchantmentHelper.getRiptideModifier(tridentItem) > 0 && !player.isWet());
                };

                flag = flag && (actionNone.test(offhandStack) || foodNotHungry.test(offhandStack) || bowNoAmmo.test(offhandStack) || spearNoUse.test(offhandStack));
            }

            Hand oppositeHand = evt.getHand() == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            if (flag && (!MainClass.BOTH_HANDS || player.getHeldItem(oppositeHand).isEmpty())) {
                player.setActiveHand(evt.getHand());
                // cause reequip animation, but don't swing hand
                evt.setCancellationResult(ActionResultType.CONSUME);
                evt.setCanceled(true);
            }
        }

        if (this.blockingHelper.canItemStackLance(evt.getItemStack())) {
            boolean flag = true;
            ItemStack offhandStack = player.getHeldItemOffhand();
            if (evt.getHand() == Hand.MAIN_HAND && !offhandStack.isEmpty()) {


                Predicate<ItemStack> actionNone = item -> item.getItem().getUseAction(item) == UseAction.NONE;
                Predicate<ItemStack> foodNotHungry = foodItem -> foodItem.getItem().getFood() != null && !player.canEat(foodItem.getItem().getFood().canEatWhenFull());
                Predicate<ItemStack> bowNoAmmo = bowItem -> {
                    UseAction action = bowItem.getItem().getUseAction(bowItem);
                    return (action == UseAction.BOW || action == UseAction.CROSSBOW) && player.findAmmo(bowItem).isEmpty();
                };
                Predicate<ItemStack> spearNoUse = tridentItem -> {
                    UseAction action = tridentItem.getItem().getUseAction(tridentItem);
                    return action == UseAction.SPEAR && (tridentItem.getDamage() >= tridentItem.getMaxDamage() - 1 || EnchantmentHelper.getRiptideModifier(tridentItem) > 0 && !player.isWet());
                };

                flag = flag && (actionNone.test(offhandStack) || foodNotHungry.test(offhandStack) || bowNoAmmo.test(offhandStack) || spearNoUse.test(offhandStack));
            }

            Hand oppositeHand = evt.getHand() == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            if (flag && (!MainClass.BOTH_HANDS || player.getHeldItem(oppositeHand).isEmpty())) {
                player.setActiveHand(evt.getHand());
                RayTraceResult result = Minecraft.getInstance().objectMouseOver;
                // cause reequip animation, but don't swing hand
                evt.setCancellationResult(ActionResultType.CONSUME);
                evt.setCanceled(true);
            }
        }
    }



    public void playerAttackEntity(PlayerEntity player, Entity targetEntity)
    {

        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(player, targetEntity)) return;
        if (targetEntity.canBeAttackedWithItem()) {
            if (!targetEntity.hitByEntity(player)) {
                float f = (float)player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((LivingEntity)targetEntity).getCreatureAttribute());
                } else {
                    f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), CreatureAttribute.UNDEFINED);
                }

                float f2 = player.getCooledAttackStrength(0.5F);
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                //player.attack
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackModifier(player);
                    if (player.isSprinting() && flag) {
                        player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && player.fallDistance > 0.0F && !player.isOnGround() && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Effects.BLINDNESS) && !player.isPassenger() && targetEntity instanceof LivingEntity;
                    flag2 = flag2 && !player.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        f *= hitResult.getDamageModifier();
                    }

                    f = f + f1;
                    boolean flag3 = false;
                    double d0 = (double)(player.distanceWalkedModified - player.prevDistanceWalkedModified);
                    if (flag && !flag2 && !flag1 && player.isOnGround() && d0 < (double)player.getAIMoveSpeed()) {
                        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
                        if (itemstack.getItem() instanceof SwordItem) {
                            flag3 = true;
                        }
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(player);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity)targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isBurning()) {
                            flag4 = true;
                            targetEntity.setFire(1);
                        }
                    }

                    Vector3d vec3d = targetEntity.getMotion();
                    boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity)targetEntity).applyKnockback( (float)i * 0.5F, (double) MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F))));
                            } else {
                                targetEntity.addVelocity((double)(-MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                            }

                            player.setMotion(player.getMotion().mul(0.6D, 1.0D, 0.6D));
                            player.setSprinting(false);
                        }

                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;
/*
                            for(LivingEntity livingentity : player.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != player && livingentity != targetEntity && !player.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && player.getDistanceSq(livingentity) < 9.0D) {
                                    livingentity.knockBack(player, 0.4F, (double)MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F))));
                                    livingentity.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
                                }
                            }
*/
                            targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
                            //player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                            //player.spawnSweepParticles();
                        }

                        if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged) {
                            ((ServerPlayerEntity)targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.setMotion(vec3d);
                        }

                        if (flag2) {
                            player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
                            player.onCriticalHit(targetEntity);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                            } else {
                                player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F) {
                            player.onEnchantmentCritical(targetEntity);
                        }

                        player.setLastAttackedEntity(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, player);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
                        ItemStack itemstack1 = player.getHeldItemMainhand();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof EnderDragonPartEntity) {
                            entity = ((EnderDragonPartEntity)targetEntity).dragon;
                        }

                        if (!player.world.isRemote && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hitEntity((LivingEntity)entity, player);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, Hand.MAIN_HAND);
                                player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)targetEntity).getHealth();
                            player.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setFire(j * 4);
                            }

                            if (player.world instanceof ServerWorld && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerWorld)player.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        player.addExhaustion(0.1F);
                    } else {
                        player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.extinguish();
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(final PlayerInteractEvent.EntityInteract evt)
    {
        if (this.blockingHelper.canItemStackLance(evt.getItemStack())) {

            Multimap<Attribute, AttributeModifier> multimap = evt.getItemStack().getAttributeModifiers(EquipmentSlotType.MAINHAND);
            this.playerAttackEntity(evt.getPlayer(),evt.getTarget());
                evt.getTarget().hurtResistantTime=15;

                if(evt.getTarget() instanceof PlayerEntity){
                    PlayerEntity playerTarget = (PlayerEntity) evt.getTarget();
                    playerTarget.disableShield(true);
                }

        }
    }




    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent evt)
    {
        if(evt.getPlayer() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) evt.getPlayer();
            //System.err.println("LOGIN");
            canBlock.put(player.getUniqueID(),false);

        }
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onItemUseStart(final LivingEntityUseItemEvent.Start evt) {

        if (evt.getEntityLiving() instanceof PlayerEntity && this.blockingHelper.canItemStackBlock(evt.getItem())) {

            evt.setDuration(BlockingItemHelper.SWORD_USE_DURATION);
        }
        if (evt.getEntityLiving() instanceof PlayerEntity && this.blockingHelper.canItemStackLance(evt.getItem())) {

            evt.setDuration(BlockingItemHelper.SWORD_LANCE_DURATION);
        }

    }
    public void onItemUseStop(final LivingEntityUseItemEvent.Stop evt)
    {

    }
    @SuppressWarnings({"unused", "ConstantConditions"})
    @SubscribeEvent
    public void onLivingAttack(final LivingAttackEvent evt) {

        if (evt.getEntityLiving().getEntityWorld().isRemote() || !(evt.getEntityLiving() instanceof PlayerEntity)) {

            return;
        }

        DamageSource source = evt.getSource();
        PlayerEntity player = (PlayerEntity) evt.getEntityLiving();
        if (this.blockingHelper.isActiveItemStackBlocking(player)) {

            if (this.blockingHelper.getBlockUseDuration(player) < MainClass.PARRY_WINDOW) {

                float amount = evt.getAmount();
                if (amount > 0.0F && this.blockingHelper.canBlockDamageSource(player, source)) {

                    this.blockingHelper.damageSword(player, amount);
                    if (!source.isProjectile()) {

                        if (source.getImmediateSource() instanceof LivingEntity) {

                            LivingEntity entity = (LivingEntity) source.getImmediateSource();
                            entity.applyKnockback( 0.5F, player.getPosX() - entity.getPosX(), player.getPosZ() - entity.getPosZ());
                        }
                    }

                    // play shield block sound on client
                    player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SwordBlockingRegistry.ITEM_SWORD_BLOCK, player.getSoundCategory(), 1.0F, 0.8F + player.world.rand.nextFloat() * 0.4F);
                    evt.setCanceled(true);
                }
            }

            if (MainClass.DEFLECT_PROJECTILES && source.getImmediateSource() instanceof AbstractArrowEntity
                    && ((AbstractArrowEntity) source.getImmediateSource()).getPierceLevel() == 0) {

                evt.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent evt) {

        if (evt.getEntityLiving() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) evt.getEntityLiving();
            float damage = evt.getAmount();
            if (damage > 0.0F && this.blockingHelper.isActiveItemStackBlocking(player)) {

                this.blockingHelper.damageSword(player, damage);
                if (!evt.getSource().isUnblockable()) {

                    evt.setAmount(Math.min(evt.getAmount(),  evt.getAmount()/2.0F));
                }
            }
        }
    }

}