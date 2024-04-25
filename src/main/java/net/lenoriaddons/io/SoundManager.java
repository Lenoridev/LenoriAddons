package net.lenoriaddons.io;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class SoundManager {

    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("minecraft", "gui.button.press");
    private static final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

    public static void playClickSound() {
        if (player != null) {
            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            soundHandler.playSound(PositionedSoundRecord.create(CLICK_SOUND, 1.0f));
        }
    }
}
