package net.lenoriaddons.gui;

import net.lenoriaddons.Reference;
import net.lenoriaddons.io.IgnoreListJsonManager;
import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiTextField textField;
    private final String addedText;
    private final String player;
    private String page;
    private Map<UUID, Long> ignoreList;
    private static final IgnoreListJsonManager ignoreListManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());
    private final List<PlayerListObject> playerListObjects = new ArrayList<>();

    public GuiIgnoreListNote(String argPlayer, String argAddedText) {
        player = argPlayer;
        addedText = argAddedText;
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
        playerListObjects.clear();
        final int[] i = {0};
        ignoreList.forEach((uuid, Long) -> {playerListObjects.add(new PlayerListObject(width/2-75, width/2+((-250)-13*i[0]),uuid)); i[0]++;});
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
                fontRendererObj.drawString("Ignore List", width / 2 - fontRendererObj.getStringWidth("Ignore List") / 2, height / 2 -116, 0x00d415);
                playerListObjects.forEach(PlayerListObject :: drawScreen);
            case "profile":
                //fontRendererObj.drawString("IGNORE NOTE "+ player, width / 2 - fontRendererObj.getStringWidth("IGNORE NOTE "+ MojangAPIClient.getName(uuid)) / 2, height / 2 -116, 0x00d415);
                //fontRendererObj.drawString("UUID: "+ uuid, width / 2 - fontRendererObj.getStringWidth("UUID: " + uuid) / 2, height / 2 -100, 0x00d415);
                //textField.drawTextBox();
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
