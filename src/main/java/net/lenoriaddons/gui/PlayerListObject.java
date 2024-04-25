package net.lenoriaddons.gui;

import net.lenoriaddons.io.MojangAPIClient;
import net.lenoriaddons.io.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.UUID;

public class PlayerListObject extends GuiScreen{
    public int x,y;
    public boolean visible = true;
    public final UUID uuid;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final PlayerHeadRender headRender;
    private final String name;
    private boolean hoverState;

    public PlayerListObject(int x, int y, UUID uuid){
        this.x=x;
        this.y=y;
        this.uuid = uuid;
        this.headRender = new PlayerHeadRender(x+2, y+2, 1, uuid);
        this.name = MojangAPIClient.getName(uuid);
    }

    public void drawScreen(){
            if (hoverState) {
                drawRect(x, y, x + 150, y + 12, 0xFF696969);
            } else drawRect(x, y, x + 150, y + 12, 0xFF383838);
            mc.fontRendererObj.drawString(name, x + 14, y + 3, 0x00d415);
            headRender.render();
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 150 && mouseY >= y && mouseY <= y + 12) {
            SoundManager.playClickSound();
            return true; // Checkbox clicked
        }
        return false; // Checkbox not clicked
    }

    public void hover(int mouseX, int mouseY) {
        hoverState  = mouseX >= x && mouseX <= x + 150 && mouseY >= y && mouseY <= y + 12;
    }

    public void setY(int y) {
        this.y = y;
        headRender.y = y + 2;
    }
}
