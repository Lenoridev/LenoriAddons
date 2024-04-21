package lenori.lenoriaddons.gui;

import lenori.lenoriaddons.Reference;
import lenori.lenoriaddons.io.IgnoreListJsonManager;
import lenori.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiTextField textField;
    private final String addedText;
    private final String player;
    private final UUID uuid;
    private String page;
    private Map<UUID, Long> ignoreList;
    private static final IgnoreListJsonManager ignoreListManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());


    public GuiIgnoreListNote(String argPlayer, String argAddedText) {
        player = argPlayer;
        addedText = argAddedText;
        uuid = MojangAPIClient.getUUID(player, -1);
        ignoreList = ignoreListManager.ignoreList;
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
        switch (page) {
            case "main":
                drawRect(width / 2 - 150, height / 2 - 123, width / 2 + 150, height / 2 + 180, 0xFF242424);
                PlayerListObject listObject = new PlayerListObject(width/2-75, height/2, uuid);
            case "profile":
                fontRendererObj.drawString("IGNORE NOTE "+ player, width / 2 - fontRendererObj.getStringWidth("IGNORE NOTE "+ MojangAPIClient.getName(uuid)) / 2, height / 2 -116, 0x00d415);
                fontRendererObj.drawString("UUID: "+ uuid, width / 2 - fontRendererObj.getStringWidth("UUID: " + uuid) / 2, height / 2 -100, 0x00d415);
                textField.drawTextBox();
        }
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
