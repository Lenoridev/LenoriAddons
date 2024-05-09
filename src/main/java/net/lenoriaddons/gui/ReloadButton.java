package net.lenoriaddons.gui;

import net.lenoriaddons.Reference;
import net.lenoriaddons.io.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class ReloadButton {

    private final ResourceLocation BUTTON_RELOAD = new ResourceLocation(Reference.MODID, "textures/gui/button_reload.png");
    private final ResourceLocation BUTTON_RELOAD_SELECTED = new ResourceLocation(Reference.MODID, "textures/gui/button_reload_selected.png");
    public int x,y;
    private boolean hoverState;
    private final Minecraft mc = Minecraft.getMinecraft();

    public ReloadButton(int x, int y) {
        this.x =x;
        this.y =y;
        this.hoverState = false;
    }

    public void render() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (hoverState) {
            mc.getTextureManager().bindTexture(BUTTON_RELOAD_SELECTED);
        } else {
            mc.getTextureManager().bindTexture(BUTTON_RELOAD);
        }
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, 128, 32, 128, 32);
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 128 && mouseY >= y && mouseY <= y + 32) {
            SoundManager.playClickSound();
            return true;
        }
        return false;
    }

    public void hover(int mouseX, int mouseY) {
        hoverState  = mouseX >= x && mouseX <= x + 128 && mouseY >= y && mouseY <= y + 32;
    }
}
