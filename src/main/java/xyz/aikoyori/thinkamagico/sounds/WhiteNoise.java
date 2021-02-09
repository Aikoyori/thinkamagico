package xyz.aikoyori.thinkamagico.sounds;

import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;

public class WhiteNoise extends Sound {
    public WhiteNoise(String nameIn, float volumeIn, float pitchIn, int weightIn, Type typeIn, boolean streamingIn, boolean preloadIn, int attenuationDistanceIn) {
        super(nameIn, volumeIn, pitchIn, weightIn, typeIn, streamingIn, preloadIn, attenuationDistanceIn);
    }


    @Override
    public ResourceLocation getSoundAsOggLocation() {
        return super.getSoundAsOggLocation();
    }

}
