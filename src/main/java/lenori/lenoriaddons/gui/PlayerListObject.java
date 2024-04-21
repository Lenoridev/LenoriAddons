package lenori.lenoriaddons.gui;

import lenori.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.UUID;

public class PlayerListObject extends GuiScreen{

    Minecraft mc = Minecraft.getMinecraft();

    public PlayerListObject(int x, int y, UUID uuid){

        drawRect(x, y, x + 150, y + 12, 0xFF383838);
        mc.fontRendererObj.drawString(MojangAPIClient.getName(uuid), x+14, y+3, 0x00d415);
        new PlayerHeadRender(x+2, y+2, 1, uuid);
    }
}
