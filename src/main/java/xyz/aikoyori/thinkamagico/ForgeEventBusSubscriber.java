package xyz.aikoyori.thinkamagico;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import xyz.aikoyori.thinkamagico.goals.RunAroundGoal;
import xyz.aikoyori.thinkamagico.init.ModMobEffects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid=MainClass.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }
    @SubscribeEvent
    public static void onEntityTick(final LivingEvent evt)
    {


    }
    @SubscribeEvent
    public static void onRenderTick(final TickEvent.RenderTickEvent event) throws IllegalArgumentException
    {

    }
    @SubscribeEvent
    public static void cameraSetup(final EntityViewRenderEvent.CameraSetup event) throws IllegalArgumentException
    {
        if(Minecraft.getInstance().player!=null)
        {
            AbstractClientPlayerEntity player = Minecraft.getInstance().player;
            if(player.getActivePotionEffect(ModMobEffects.DIZZY.get())!=null)
            {
                double partialTick = event.getRenderPartialTicks();
                float duration = (float)(player.getActivePotionEffect(ModMobEffects.DIZZY.get())).getDuration()-(float)partialTick;
                float amplifier = (float)(player.getActivePotionEffect(ModMobEffects.DIZZY.get())).getAmplifier();
                //event.setYaw((float)(event.getYaw() + ((float)((amplifier+1.0f)*0.5f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)((amplifier+1.0f)*2.0f)))*duration)));
                double rollSine = (Math.sin((1/((float)((amplifier)+1.0f*2.0f)*2.0f))*duration));
                float effectScale = Minecraft.getInstance().gameSettings.screenEffectScale;
                event.setRoll((float)(event.getRoll() + ((float)((amplifier+1.0f)*1.55f)*((float)clamp(duration,0.0f,100.0f)/10))*(rollSine)* effectScale)+(float)(((1.0-(double)effectScale)*20*((double)amplifier+1))));
                //player.renderYawOffset = (float)(player.renderYawOffset + ((float)(amplifier*2.0f+1.0f*1.0f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)(amplifier*1.0f+1.0f)))*duration));

            }
        }
    }
    @SubscribeEvent
    public static void worldRender(final RenderWorldLastEvent evt)
    {
        AbstractClientPlayerEntity player = Minecraft.getInstance().player;
        if(Minecraft.getInstance().player!=null) {
            MatrixStack stack = evt.getMatrixStack();
            if(player.getActivePotionEffect(ModMobEffects.HALLUCINATION.get())!=null)
            {
                //CreeperModel creeper = new CreeperModel();
                //Minecraft.getInstance().getRenderManager().renderEntityStatic(, 0.0D, 0.0D, 0.0D, 0.0F,evt.getPartialTicks() , stack, buffer,1);
                //creeper.render(stack,Tessellator.getInstance().getBuffer(),1,1,1.0f,1.0f,1.0f,1.0f);

            }
        }
    }
    @SubscribeEvent
    public static void onFogDense(EntityViewRenderEvent.FogDensity evt)
    {
        ActiveRenderInfo activeRenderInfo = evt.getRenderer().getActiveRenderInfo();
        LivingEntity entity;
        if (activeRenderInfo.getRenderViewEntity() instanceof LivingEntity)
        {

            entity = (LivingEntity) activeRenderInfo.getRenderViewEntity();

            if ((entity).isPotionActive(ModMobEffects.HALLUCINATION.get())) {
                int i2 = ((LivingEntity) activeRenderInfo.getRenderViewEntity()).getActivePotionEffect(ModMobEffects.HALLUCINATION.get()).getDuration();
                evt.setDensity(0.5f);
                evt.setCanceled(true);

            }
        }
    }
    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors evt)
    {
        ActiveRenderInfo activeRenderInfo = evt.getRenderer().getActiveRenderInfo();
        LivingEntity entity;
        if (activeRenderInfo.getRenderViewEntity() instanceof LivingEntity)
        {

            entity = (LivingEntity) activeRenderInfo.getRenderViewEntity();

            if ((entity).isPotionActive(ModMobEffects.HALLUCINATION.get())) {
                int i2 = ((LivingEntity) activeRenderInfo.getRenderViewEntity()).getActivePotionEffect(ModMobEffects.HALLUCINATION.get()).getDuration();
                evt.setRed(0.0f);
                evt.setGreen(0.0f);
                evt.setBlue(0.0f);

            }

        }
    }
    @SubscribeEvent
    public static void onEntitySummoned(final EntityJoinWorldEvent evt)
    {


        if(evt.getEntity() instanceof CreatureEntity && !(evt.getEntity() instanceof PlayerEntity)){
            CreatureEntity mob = (CreatureEntity) evt.getEntity();
            mob.goalSelector.addGoal(-2,new RunAroundGoal(mob,0.50));
            mob.targetSelector.getRunningGoals().forEach(goal ->{

                goal.resetTask();
            });

        /*
        if(evt.getEntity() instanceof MobEntity && !(evt.getEntity() instanceof PlayerEntity))
        {

        }*/
    }
    }
    @SubscribeEvent
    public static void onRenderEntity(final RenderLivingEvent.Pre evt) throws IllegalAccessException,NullPointerException{
        LivingEntity entity = evt.getEntity();
        if (entity.isPotionActive(ModMobEffects.IDENTITYCRISIS.get()))
        {
            evt.setCanceled(true);

            LivingRenderer renderer = evt.getRenderer();
            MatrixStack stack = evt.getMatrixStack();
            stack.push();
            EntityRendererManager rendererManager = renderer.getRenderManager();
            List<EntityType> registeredEntities = new ArrayList<EntityType>();

            Registry.ENTITY_TYPE.stream().filter(EntityType::isSummonable).iterator().forEachRemaining(registeredEntities::add);
                EntityType entityType = registeredEntities.get(evt.getEntity().world.getRandom().nextInt(registeredEntities.size()));
                Entity ent = entityType.create(evt.getEntity().world);
                while(!(ent instanceof SheepEntity))
                {
                    entityType = registeredEntities.get(evt.getEntity().world.getRandom().nextInt(registeredEntities.size()));
                    ent = entityType.create(evt.getEntity().world);
                }
                LivingEntity sheep = (LivingEntity)ent;
                LivingRenderer cloner = (LivingRenderer) new SheepRenderer(rendererManager);
                sheep.limbSwing = entity.limbSwing;
                sheep.limbSwingAmount = entity.limbSwingAmount;
                sheep.rotationYaw = entity.rotationYaw;
                sheep.renderYawOffset = entity.renderYawOffset;
                cloner.render(sheep,entity.rotationYaw,evt.getPartialRenderTick(),stack,evt.getBuffers(),evt.getLight());

                stack.pop();

        }
    }

}
