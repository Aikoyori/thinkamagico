package xyz.aikoyori.thinkamagico.potioneffects;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;


public class DizzyEffect extends Effect {
    private int swayTimer = 0;
    public DizzyEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }
    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {

        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);

    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        //System.out.println("EFFECT RUN "+amplifier);
        //swayTimer+=amplifier
        if(!(entityLivingBaseIn instanceof PlayerEntity))
        {
            float duration = (float)(entityLivingBaseIn.getActivePotionEffect(this.getEffect()).getDuration());
            entityLivingBaseIn.rotationYaw = (float)(entityLivingBaseIn.rotationYaw + ((amplifier+1.0f)*0.5f));
            entityLivingBaseIn.renderYawOffset = (float)(entityLivingBaseIn.renderYawOffset + ((float)((amplifier+1.0f)*0.50f*1.0f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)((amplifier+1.0f)*0.10f)))*duration));

            //entityLivingBaseIn.rotationYaw = (float)(entityLivingBaseIn.rotationYaw + ((float)((amplifier+1.0f)*0.5f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)((amplifier+1.0f)*0.10f)))*duration));
            //entityLivingBaseIn.renderYawOffset = (float)(entityLivingBaseIn.renderYawOffset + ((float)((amplifier+1.0f)*0.50f*1.0f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)((amplifier+1.0f)*0.10f)))*duration));


        }


        //Minecraft.getInstance().setRenderViewEntity();
        //GL11.glRotated((float)(entityLivingBaseIn.renderYawOffset + ((float)(amplifier*1.0f+1.0f*1.0f)*((float)clamp(duration,0.0f,100.0f)/10))*Math.sin((1/((float)(amplifier*1.0f+1.0f)))*duration)),0.0f,1.0f,0.0f);
        if(swayTimer>720)
        {
            swayTimer=0;
        }
        /*super.performEffect(entityLivingBaseIn, amplifier);*/
    }
    public void swayCamera(){

    }


    @Override
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {

    }

}
