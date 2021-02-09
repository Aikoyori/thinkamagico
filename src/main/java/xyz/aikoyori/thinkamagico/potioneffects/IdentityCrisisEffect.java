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


public class IdentityCrisisEffect extends Effect {
    public IdentityCrisisEffect(EffectType typeIn, int liquidColorIn) {
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

    }

    @Override
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {

    }

}
