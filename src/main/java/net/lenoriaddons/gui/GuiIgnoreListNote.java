package net.lenoriaddons.gui;

import net.lenoriaddons.Reference;
import net.lenoriaddons.io.IgnoreListJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiTextField textField;
    private final String addedText;
    private final String player;
    private String page;
    private Map<UUID, Long> ignoreList;
    private static final IgnoreListJsonManager ignoreListManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());
    private final List<PlayerListObject> playerListObjects = new ArrayList<>();
    private int scrolledHeight = 0;

    public GuiIgnoreListNote(String player, String addedText) {
        this.player = player;
        this.addedText = addedText;
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
        ignoreList.forEach((uuid, Long) -> {playerListObjects.add(new PlayerListObject(width/2-75, height/3+13*i[0],uuid)); i[0]++;});
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
    public void handleMouseInput() throws IOException {
        int scrollDelta = Mouse.getEventDWheel();
        scrolledHeight += scrollDelta/5;
        if (scrollDelta != 0) {
            for (PlayerListObject listObject : playerListObjects) {
                listObject.setY(scrolledHeight);
                listObject.visible = listObject.y >= height / 2 - 40;
            }
        }
        System.out.println(scrollDelta);
        super.handleMouseInput();
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
    public boolean doesGuiPauseGame(){return false;}
}
