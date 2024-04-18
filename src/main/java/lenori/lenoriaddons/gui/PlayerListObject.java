package lenori.lenoriaddons.gui;

import lenori.lenoriaddons.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class PlayerListObject extends GuiScreen{

    Minecraft mc = Minecraft.getMinecraft();

    public PlayerListObject(int x, int y, String uuid){

        drawRect(x, y, x + 170, y + 12, 0xFF383838);
        mc.fontRendererObj.drawString("WarpBeat", x+14, y+3, 0x00d415);
        PlayerHeadRender headRender = new PlayerHeadRender(x+1, y+2, 1, uuid);
    }
}
