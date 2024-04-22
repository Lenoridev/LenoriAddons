package net.lenoriaddons.gui;

import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.UUID;

public class PlayerListObject extends GuiScreen{
    public int x,y;
    private final UUID uuid;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final PlayerHeadRender headRender;

    public PlayerListObject(int x, int y, UUID uuid){
        this.x=x;
        this.y=y;
        this.uuid = uuid;
        this.headRender = new PlayerHeadRender(x+2, y+2, 1, uuid);
    }

    public void drawScreen(){
        drawRect(x, y, x + 150, y + 12, 0xFF383838);
        mc.fontRendererObj.drawString(MojangAPIClient.getName(uuid), x+14, y+3, 0x00d415);
        headRender.render();
    }

    public void changeY(int y){
        this.y+=y;
    }
}
