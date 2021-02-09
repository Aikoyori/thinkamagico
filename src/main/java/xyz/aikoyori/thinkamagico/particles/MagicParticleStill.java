package xyz.aikoyori.thinkamagico.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MagicParticleStill extends SimpleAnimatedParticle {


    protected MagicParticleStill(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, IAnimatedSprite sprites) {
        super(world, x, y, z, sprites,0.0f);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        Random rand = new Random();
        this.setSize(1.5f,1.5f);
        this.maxAge = (int)(10.0D);
        this.selectSpriteWithAge(sprites);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        //this.age++;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MagicParticleStill(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
