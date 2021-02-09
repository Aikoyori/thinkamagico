package xyz.aikoyori.thinkamagico.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.core.jmx.Server;
import xyz.aikoyori.thinkamagico.entities.HallucinationProvider;

import javax.annotation.Nullable;

public class HallucinationProviderRenderer extends EntityRenderer<HallucinationProvider> {

    private ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/creeper/creeper.png");
    //private static final RenderType RENDER_TYPE = RenderType.get(TEXTURE);

    private final float SCALE = 1.0f;

    public HallucinationProviderRenderer(EntityRendererManager renderManager) {
        super(renderManager);

        this.shadowSize = 0.5F;
        this.shadowOpaque = 0.75F;

    }


    @Nullable
    @Override
    public ResourceLocation getEntityTexture(HallucinationProvider entity) {
        return TEXTURE;
    }

    @Override
    public void render(HallucinationProvider entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

        EntityType<?> TYPE = (Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(entityIn.getModelSource())));
        EntityRenderer MODEL = this.getRenderManager().renderers.get(TYPE);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks,entityIn.prevRotationYaw,entityYaw)));
        Entity ent = TYPE.create(entityIn.world);
        try
        {
            ent = TYPE.create(Minecraft.getInstance().player.getServer().getWorld(entityIn.world.getDimensionKey()));
        }
        catch(Exception ex){}
        try
        {
            if(!(ent instanceof IAngerable))
            ent.read(entityIn.getSubTag());
        }
        catch(Exception ex){}
        //ent = TYPE.create(entityIn.getServer().getWorld(entityIn.world.getDimensionKey()));
        try
        {
            MODEL.render(ent,entityYaw,partialTicks,matrixStackIn,bufferIn,packedLightIn);
        }
        catch(Exception ex){}
        matrixStackIn.pop();
    }


}
