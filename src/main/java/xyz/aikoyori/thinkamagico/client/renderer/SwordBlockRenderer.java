package xyz.aikoyori.thinkamagico.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xyz.aikoyori.thinkamagico.MainClass;
import xyz.aikoyori.thinkamagico.helper.BlockingItemHelper;
import xyz.aikoyori.thinkamagico.utils.BipedArmLayer;

import java.lang.reflect.Field;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SwordBlockRenderer {


    private final BlockingItemHelper blockingHelper = new BlockingItemHelper();
    private final Minecraft mc = Minecraft.getInstance();
    public float equipProgress = 0.0F;

    private static final Field FIELD_177097_H = ObfuscationReflectionHelper.findField(LivingRenderer.class, "field_177097_h");
    @SuppressWarnings("unused")
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderLiving(RenderLivingEvent.Pre<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> evt) throws IllegalAccessException {
    MatrixStack mtx =  evt.getMatrixStack();
        List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layerRenderers = (List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>>) FIELD_177097_H.get(evt.getRenderer());

        if (evt.getEntity() instanceof AbstractClientPlayerEntity) {

            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) evt.getEntity();

            PlayerModel<AbstractClientPlayerEntity> model = evt.getRenderer().getEntityModel();
            if (this.blockingHelper.isActiveItemStackBlocking(player)) {

                boolean left1 = player.getActiveHand() == Hand.OFF_HAND ;//&& player.getPrimaryHand() == HandSide.RIGHT;
                boolean left2 = false;//player.getActiveHand() == Hand.MAIN_HAND && player.getPrimaryHand() == HandSide.LEFT;
                BipedModel.ArmPose pose = !MainClass.BOTH_HANDS ? BipedModel.ArmPose.CROSSBOW_CHARGE : BipedModel.ArmPose.BLOCK;
                if (left1 || left2) {

                    if (model.leftArmPose == BipedModel.ArmPose.ITEM) {

                        model.leftArmPose = pose;
                    }
                } else {

                    if (model.rightArmPose == BipedModel.ArmPose.ITEM) {

                        model.rightArmPose = pose;
                    }
                }
            }
            if (this.blockingHelper.isActiveItemStackLancing(player)) {

               if (layerRenderers != null)
                {
                    boolean left1 = player.getActiveHand() == Hand.OFF_HAND && player.getPrimaryHand() == HandSide.RIGHT;
                    boolean left2 = player.getActiveHand() == Hand.MAIN_HAND && player.getPrimaryHand() == HandSide.LEFT;
                    BipedModel.ArmPose pose = !MainClass.BOTH_HANDS ? BipedModel.ArmPose.BLOCK : BipedModel.ArmPose.BLOCK;

                    if (left1 || left2) {

                        if (model.leftArmPose == BipedModel.ArmPose.ITEM) {

                            model.leftArmPose = pose;
                        }
                      // model.bipedLeftArm.showModel=false;
                        //model.bipedLeftArmwear.showModel=false;
                        /*
                        model.bipedLeftArm.showModel=false;
                        model.bipedLeftArmwear.showModel=false;*/
                        //player.swingProgress=0.0F;
                        //model.bipedLeftArm.rotateAngleZ=90.0F+model.bipedRightArm.rotateAngleZ;
                        // mtx.rotate(Vector3f.XP.rotationDegrees(90.0F));

                    } else {

                        if (model.rightArmPose == BipedModel.ArmPose.ITEM) {

                            model.rightArmPose = pose;
                        }
                        //model.bipedRightArm.showModel=false;
                        //model.bipedRightArmwear.showModel=false;
                        /*
                        model.bipedRightArm.showModel=false;
                        model.bipedRightArmwear.showModel=false;*/
                        //player.swingProgress=0.0F;
                        //model.bipedRightArm.rotateAngleZ=90.0F+model.bipedRightArm.rotateAngleZ;
                        //mtx.rotate(Vector3f.XP.rotationDegrees(90.0F));

                    }

                    if  (layerRenderers.get(1).getClass() == HeldItemLayer.class)
                        layerRenderers.set(1, new HeldItemRenderer(evt.getRenderer()));



                }

            }
            else
            {

                model.bipedLeftArm.showModel=true;
                model.bipedRightArm.showModel=true;
                model.bipedLeftArmwear.showModel=true;
                model.bipedRightArmwear.showModel=true;
            }

        }
    }
    private float previousYaw;

    public float getPreviousYaw() {
        return previousYaw;
    }

    public void setPreviousYaw(float previousYaw) {
        this.previousYaw = previousYaw;
    }

    public float getRotationRenderingDelta(float currentYaw,float partialTick) {
        return (currentYaw-this.previousYaw)*partialTick;
    }

    private void renderLancing(PlayerModel<AbstractClientPlayerEntity> model, PlayerEntity player, RenderLivingEvent<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> event, boolean tex,List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layerRenderers)
    {

        IVertexBuilder buffer = event.getBuffers().getBuffer(model.getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin()));
        int light = event.getLight();
        int texture = OverlayTexture.NO_OVERLAY;

        PlayerModel playerThirdPerson = model;




        ModelRenderer leftArm = model.bipedLeftArm;
        ModelRenderer leftArmwear = model.bipedLeftArmwear;


        ModelRenderer rightArm = model.bipedRightArm;
        ModelRenderer rightArmwear = model.bipedRightArmwear;


        ItemStack chestplate = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        PlayerModel armor;
        armor=ForgeHooksClient.getArmorModel(player,chestplate,EquipmentSlotType.CHEST, model);


        float armRise=120.0F;
        final float scale = 0.5F;
        rightArm.rotateAngleY = model.bipedBody.rotateAngleY;
        leftArm.rotateAngleY = model.bipedBody.rotateAngleY;
        MatrixStack stack = event.getMatrixStack();
        float partialTick = event.getPartialRenderTick();
        float delta = MathHelper.lerp(partialTick,previousYaw,player.renderYawOffset);
        //ItemStack<T> items =
        MatrixStack armorStack = (MatrixStack) stack;
        //armorStack.scale(scale, -scale, -scale);
        int sides = 1;

        if(!tex)
        {
            sides = 1;


            rightArm.rotationPointX = -MathHelper.cos((float) Math.toRadians((delta))) * 4.0F;
            rightArm.rotationPointY = player.isCrouching() ? 17 : 20;
            rightArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(delta)) * 4.0F;
            rightArm.rotateAngleX = 3.1415926F*armRise/180.0f;
            rightArm.rotateAngleY = (float) -Math.toRadians(delta-30.0F);
            rightArm.rotateAngleZ = 0.0F;

            rightArmwear.rotationPointX = -MathHelper.cos((float) Math.toRadians(delta)) * 4.0F;
            rightArmwear.rotationPointY = player.isCrouching() ? 17 : 20;
            rightArmwear.rotationPointZ = -MathHelper.sin((float) Math.toRadians(delta)) * 4.0F;
            rightArmwear.rotateAngleX = 3.1415926F*armRise/180.0f;
            rightArmwear.rotateAngleY = (float) -Math.toRadians(delta-30.0F);
            rightArmwear.rotateAngleZ = 0.0F;



            playerThirdPerson.bipedRightArm.showModel = false;
            rightArm.showModel = true;
            rightArm.render(stack, buffer, light, texture);
            rightArmwear.showModel = true;
            rightArmwear.render(stack, buffer, light, texture);
        }
        else
        {
            sides=-1;
            leftArm.rotationPointX = -MathHelper.cos((float) Math.toRadians(delta)) * -4.0F;
            leftArm.rotationPointY = player.isCrouching() ? 17 : 20;
            leftArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(delta)) * -4.0F;
            leftArm.rotateAngleX = 3.1415926F*armRise/180.0f;
            leftArm.rotateAngleY = (float) -Math.toRadians(delta+30.0F);
            leftArm.rotateAngleZ = 0.0F;


            leftArmwear.rotationPointX = -MathHelper.cos((float) Math.toRadians(delta)) * -4.0F;
            leftArmwear.rotationPointY = player.isCrouching() ? 17 : 20;
            leftArmwear.rotationPointZ = -MathHelper.sin((float) Math.toRadians(delta)) * -4.0F;
            leftArmwear.rotateAngleX = 3.1415926F*armRise/180.0f;
            leftArmwear.rotateAngleY = (float) -Math.toRadians(delta+30.0F);
            leftArmwear.rotateAngleZ = 0.0F;


            leftArm.showModel = true;
            leftArm.render(stack, buffer, light, texture);
            leftArmwear.showModel = true;
            leftArmwear.render(stack, buffer, light, texture);
        }

        this.setPreviousYaw(player.renderYawOffset);

        //playerThirdPerson.bipedLeftArm = leftArm;
        //playerThirdPerson.bipedRightArm= rightArm;
        //armor.render(stack,buffer,light,texture,1.0f,1.0f,1.0f,1.0f);
        BipedArmLayer armorlayerbiped = new BipedArmLayer(event.getRenderer(), new BipedModel(0.5F), new BipedModel(1.0F));
        armorlayerbiped.hand = sides;
        //ForgeHooksClient.getArmorModel(player,player.getItemStackFromSlot(EquipmentSlotType.CHEST),EquipmentSlotType.CHEST,armorlayerbiped.);
        //playerThirdPerson.render(stack,buffer,light,texture,1.0f,1.0f,1.0f,1.0f);

        //armorlayerbiped.render(armorStack,Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(),light,player,player.limbSwing,player.limbSwingAmount,partialTick,1.00f,player.getRotationYawHead(),player.rotationPitch);
        //if  (layerRenderers.get(0).getClass() == Armor.class)










    }
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderLiving2(RenderLivingEvent.Post<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> evt) throws IllegalAccessException {
    MatrixStack mtx =  evt.getMatrixStack();
        List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layerRenderers = (List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>>) FIELD_177097_H.get(evt.getRenderer());


        if (evt.getEntity() instanceof AbstractClientPlayerEntity) {

            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) evt.getEntity();
            if (this.blockingHelper.isActiveItemStackLancing(player)) {
                PlayerModel<AbstractClientPlayerEntity> model = evt.getRenderer().getEntityModel();

                boolean left1 = player.getActiveHand() == Hand.OFF_HAND && player.getPrimaryHand() == HandSide.RIGHT;
                boolean left2 = player.getActiveHand() == Hand.MAIN_HAND && player.getPrimaryHand() == HandSide.LEFT;

                if (left1 || left2) {
                    //model.bipedLeftArm.showModel=false;
                    //renderLancing(model,player,evt,left1 || left2,layerRenderers);

                    //model.bipedLeftArm.rotateAngleZ=90.0F+model.bipedRightArm.rotateAngleZ;
                    // mtx.rotate(Vector3f.XP.rotationDegrees(90.0F));

                } else {
                    //renderLancing(model,player,evt,left1 || left2,layerRenderers);
                    //model.bipedRightArm.rotateAngleZ=90.0F+model.bipedRightArm.rotateAngleZ;
                    //mtx.rotate(Vector3f.XP.rotationDegrees(90.0F));

                }
            }

        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onRenderHand(final RenderHandEvent evt) {
        equipProgress = evt.getEquipProgress();
        ClientPlayerEntity player = this.mc.player;
        ItemStack stack = evt.getItemStack();
        if (player != null && player.getActiveHand() == evt.getHand() && this.blockingHelper.isActiveItemStackBlocking(player)) {


            evt.setCanceled(true);
            MatrixStack matrixStack = evt.getMatrixStack();
            matrixStack.push();

            boolean rightHanded = (evt.getHand() == Hand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite()) == HandSide.RIGHT;
            this.transformSideFirstPersonBlock(matrixStack, rightHanded ? 1.0F : -1.0F, evt.getEquipProgress());
            this.mc.getFirstPersonRenderer().renderItemSide(player, stack, rightHanded ? net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND :
                    net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightHanded, matrixStack, evt.getBuffers(), evt.getLight());

            matrixStack.pop();
        }
        else if (player != null && player.getActiveHand() == evt.getHand() && this.blockingHelper.isActiveItemStackLancing(player)) {


            evt.setCanceled(true);
            MatrixStack matrixStack = evt.getMatrixStack();
            matrixStack.push();

            boolean rightHanded = (evt.getHand() == Hand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite()) == HandSide.RIGHT;
            this.transformSideFirstPersonLance(matrixStack, rightHanded ? 1.0F : -1.0F, evt.getEquipProgress());
            this.mc.getFirstPersonRenderer().renderItemSide(player, stack, rightHanded ? net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND :
                    net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightHanded, matrixStack, evt.getBuffers(), evt.getLight());

            matrixStack.pop();
        }
    }

    /**
     * values taken from Minecraft snapshot 15w33b
     */
    private void transformSideFirstPersonBlock(MatrixStack matrixStack, float side, float equippedProg) {

        matrixStack.translate(side * 0.56F, -0.52F /*+ -0.6F*/, -0.72F);
        matrixStack.translate(side * -0.14142136F, 0.08F, 0.14142136F);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-102.25F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(side * 13.365F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees((-(MathHelper.sin(equippedProg*(float)Math.PI))-1)/2*side * 13.365F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(side * 78.05F));
    }
    private void transformSideFirstPersonLance(MatrixStack matrixStack, float side, float equippedProg) {



        matrixStack.translate(side * 0.56F, -0.52F /*+ -0.6F*/, -0.72F);
        matrixStack.translate(side * -0.14142136F, 0.08F, 0.14142136F);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-102.25F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(((MathHelper.sin(equippedProg*(float)Math.PI))+1)/2*-22.25F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(side * 33.365F));
        //matrixStack.rotate(Vector3f.ZP.rotationDegrees((-(MathHelper.sin(equippedProg*(float)Math.PI))-1)/2*side * 78.05F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(side * 20.05F));
        //matrixStack.rotate(Vector3f.ZP.rotationDegrees(side * 78.05F));
    }
public static class defHeld extends HeldItemLayer
{

    public defHeld(IEntityRenderer p_i50934_1_) {
        super(p_i50934_1_);
    }
}

    public static class HeldItemRenderer extends HeldItemLayer
    {

        private final BlockingItemHelper blockingHelper = new BlockingItemHelper();
        public HeldItemRenderer(IEntityRenderer renderer)
        {
            super(renderer);
        }

        private void doNormalRender(LivingEntity p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack p_229135_5_, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
            if (!p_229135_2_.isEmpty()) {
                p_229135_5_.push();
                ((IHasArm)this.getEntityModel()).translateHand(p_229135_4_, p_229135_5_);
                p_229135_5_.rotate(Vector3f.XP.rotationDegrees(-90.0F));
                p_229135_5_.rotate(Vector3f.YP.rotationDegrees(180.0F));
                boolean lvt_8_1_ = p_229135_4_ == HandSide.LEFT;
                p_229135_5_.translate((double)((float)(lvt_8_1_ ? -1 : 1) / 16.0F), 0.125D, -0.625D);
                Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(p_229135_1_, p_229135_2_, p_229135_3_, lvt_8_1_, p_229135_5_, p_229135_6_, p_229135_7_);
                p_229135_5_.pop();
            }
        }
        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;

            boolean flag = player.getPrimaryHand() == HandSide.RIGHT;
            ItemStack itemstack = flag ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
            ItemStack itemstack1 = flag ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
            if (blockingHelper.isActiveItemStackLancing(player)) {
                matrixStackIn.push();
                if (this.getEntityModel().isChild) {
                    float f = 0.5F;
                    matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                    matrixStackIn.scale(f, f, f);
                }
                if((player.getActiveHand()==Hand.MAIN_HAND && player.getPrimaryHand() ==HandSide.RIGHT) ||( player.getActiveHand()==Hand.OFF_HAND && player.getPrimaryHand() ==HandSide.LEFT))
                {
                    this.doRender(player, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn,limbSwing,limbSwingAmount);
                    this.doNormalRender(player, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
                }
                if((player.getActiveHand()==Hand.MAIN_HAND && player.getPrimaryHand() ==HandSide.LEFT) ||( player.getActiveHand()==Hand.OFF_HAND && player.getPrimaryHand() ==HandSide.RIGHT))

                {
                    this.doRender(player, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn,limbSwing,limbSwingAmount);
                    this.doNormalRender(player, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);

                }
                matrixStackIn.pop();
            }
            else
            {
                super.render( matrixStackIn,  bufferIn,  packedLightIn, (LivingEntity) entity,  limbSwing,  limbSwingAmount,  partialTicks,  ageInTicks,  netHeadYaw,  headPitch);
            }
}

        private ItemStack getHeldStack(PlayerEntity player)
        {
            return player.getHeldItem(player.getActiveHand());
        }
        private void doRender(LivingEntity entityIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformTypeIn, HandSide side, MatrixStack matrixIn, IRenderTypeBuffer bufferIn, int combinedLightIn,float limbSwing,float limbSwingAmount) {
            if (!stackIn.isEmpty()) {

                boolean flag = side == HandSide.LEFT;
                matrixIn.push();
                if (!(this.blockingHelper.isActiveItemStackLancing((PlayerEntity) entityIn)))
                    ((IHasArm)this.getEntityModel()).translateHand(side, matrixIn);
                else
                {
                    ((IHasArm)this.getEntityModel()).translateHand(side, matrixIn);
                    ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
                    //matrixIn.rotate(Vector3f.XP.rotationDegrees(-100.0F));
                    //matrixIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
                   // matrixIn.translate((float)(flag ? -1 : 1) / 16.0F, 0.125D, -0.625);
                    if (entityIn instanceof PlayerEntity && (this.blockingHelper.isActiveItemStackLancing((PlayerEntity) entityIn)) && !entityIn.isElytraFlying())
                    {
                        PlayerEntity player = (PlayerEntity)entityIn;
                        int sides =(side == HandSide.LEFT)? -1:1;

                        float swingProg = entityIn.swingProgress;

                        if(entityIn.getActiveHand()==Hand.OFF_HAND)
                        {
                            swingProg=0.0F;
                        }
                        if(entityIn.isCrouching())
                        {
                            matrixIn.translate(sides*0.0, 0.0F, -0.2F);


                        }/*
                        matrixIn.rotate(Vector3f.XP.rotationDegrees(-(MathHelper.sin(swingProg*3.14159265F)*20.0F)));
                        matrixIn.rotate(Vector3f.YP.rotationDegrees(-(MathHelper.sin(swingProg*3.14159265F)*10.0F)));
                        matrixIn.rotate(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(swingProg*3.14159265F)*(-30.0F))));*/
                        matrixIn.rotate(Vector3f.XP.rotationDegrees(-20.0f));
                        matrixIn.rotate(Vector3f.YP.rotationDegrees(sides*5.0f));
                        matrixIn.rotate(Vector3f.ZP.rotationDegrees(sides*-5.0f));
                        //matrixIn.translate(sides*0.05, 0.4F, 0.1F);
                        matrixIn.translate(sides*0.05, 0.500F, 0.0F);
                        //matrixIn.rotate(Vector3f.XP.rotationDegrees(65F));



                    }
                }
                Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entityIn, stackIn, transformTypeIn, flag, matrixIn, bufferIn, combinedLightIn);
                matrixIn.pop();
            }
        }
    }

}