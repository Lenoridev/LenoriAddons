package lenori.lenoriaddons.gui;

import lenori.lenoriaddons.MojangAPIClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.event.world.NoteBlockEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiTextField textField;
    private String addedText;
    private String player;
    private String uuid;
    private String page;
    private File ignoreList;



    public GuiIgnoreListNote(String argPlayer, String argAddedText) {
        player = argPlayer;
        addedText = argAddedText;
        uuid = MojangAPIClient.getUUID(player, -1);
    }

    @Override
    public void initGui() {
        textField = new GuiTextField(1, fontRendererObj, width/2 -90, height/2 -30, 200, 100);
        textField.setFocused(true);
        textField.setCanLoseFocus(false);
        textField.setMaxStringLength(999);
        if(addedText != null){
            textField.setText(addedText);
        }
        page= "main";
    }

    @Override
    public void updateScreen() {
        textField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(page.equals("main")) {
            drawRect(width / 2 - 170, height / 2 - 123, width / 2 + 170, height / 2 + 180, 0xFF242424);

        } else if(page.equals("profile")) {
            //profile gui code
        }

        fontRendererObj.drawString("IGNORE NOTE "+ player, width / 2 - fontRendererObj.getStringWidth("IGNORE NOTE "+ player) / 2, height / 2 -116, 0x00d415);
        //textField.drawTextBox();
        fontRendererObj.drawString("UUID: "+ uuid, width / 2 - fontRendererObj.getStringWidth(uuid) / 2, height / 2 -100, 0x00d415);
        //PlayerHeadRender playerHead = new PlayerHeadRender(width/2 , height/2, 4, uuid);
        PlayerListObject listObject = new PlayerListObject(width/2-30, height/2, uuid);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // Basically trash
    private void drawImageWithAlpha(BufferedImage image, int x, int y, int width, int height) {

    }

}
