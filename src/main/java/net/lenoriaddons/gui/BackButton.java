package net.lenoriaddons.gui;

import net.lenoriaddons.Reference;
import net.lenoriaddons.io.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class BackButton {

    private final ResourceLocation BUTTON_BACK = new ResourceLocation(Reference.MODID, "textures/gui/button_back.png");
    private final ResourceLocation BUTTON_BACK_SELECTED = new ResourceLocation(Reference.MODID, "textures/gui/button_back_selected.png");
    public int x,y;
    private boolean hoverState;
    private final Minecraft mc = Minecraft.getMinecraft();

    public BackButton(int x, int y) {
        this.x =x;
        this.y =y;
        this.hoverState = false;
    }

    public void render() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (hoverState) {
            mc.getTextureManager().bindTexture(BUTTON_BACK_SELECTED);
        } else {
            mc.getTextureManager().bindTexture(BUTTON_BACK);
        }
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            SoundManager.playClickSound();
            return true;
        }
        return false;
    }

    public void hover(int mouseX, int mouseY) {
        hoverState  = mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16;
    }
}
