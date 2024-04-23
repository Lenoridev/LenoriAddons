package net.lenoriaddons.gui;

import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.UUID;

public class PlayerListObject extends GuiScreen{
    public int x,y;
    public boolean visible = true;
    private final UUID uuid;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final PlayerHeadRender headRender;
    private final String name;

    public PlayerListObject(int x, int y, UUID uuid){
        this.x=x;
        this.y=y;
        this.uuid = uuid;
        this.headRender = new PlayerHeadRender(x+2, y+2, 1, uuid);
        this.name = MojangAPIClient.getName(uuid);
    }

    public void drawScreen(){
        if (visible) {
            drawRect(x, y, x + 150, y + 12, 0xFF383838);
            mc.fontRendererObj.drawString(name, x + 14, y + 3, 0x00d415);
            headRender.render();
        }
    }

    public void setY(int y) {
        this.y+=y;
        headRender.y+=y;
    }
}
