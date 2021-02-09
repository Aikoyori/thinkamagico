package xyz.aikoyori.thinkamagico.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import xyz.aikoyori.thinkamagico.entities.MagicProjectile;

public class MagicProjectileRenderer extends EntityRenderer<MagicProjectile> {

    private ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/creeper/creeper.png");
    public MagicProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);

        this.shadowSize = 0.0F;
        this.shadowOpaque = 0.0F;

    }

    @Override
    public void render(MagicProjectile entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(MagicProjectile entity) {
        return TEXTURE;
    }
}
