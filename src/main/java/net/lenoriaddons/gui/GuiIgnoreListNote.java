package net.lenoriaddons.gui;

import net.lenoriaddons.LenoriAddons;
import net.lenoriaddons.Reference;
import net.lenoriaddons.io.IgnoreListJsonManager;
import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiElementTextField elementTextField;
    private String playerName,page, addedText;
    private Map<UUID, IgnoreListJsonManager.IgnoreDataObject> ignoreList;
    private static final IgnoreListJsonManager ignoreListManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());
    private final List<PlayerListObject> playerListObjects = new ArrayList<>();
    private UUID uuid;
    private BackButton backButton;
    private ReloadButton reloadButton;
    private int startIndex;
    private final int LIST_SIZE = 20;
    private PlayerHeadRender headRender;


    public GuiIgnoreListNote(String player, String addedText) {
        this.addedText = addedText;
        ignoreList = ignoreListManager.ignoreList;
        this.playerName = player;
        if (playerName == null) page = "main"; else {
            if (loadPlayer()) {
                page = "profile";
            } else page = "error";
        }
    }

    @Override
    public void initGui() {
        elementTextField = new GuiElementTextField("", 200,10, 12,0b1000001);
        elementTextField.setText("");
        if (!ignoreList.containsKey(uuid) && !page.equals("error") && uuid != null) ignoreAddPlayer();
        backButton = new BackButton(width/2-110, height/2-100);
        reloadButton = new ReloadButton(width / 2 - 64, height / 2 - 48);
        elementTextField.setFocus(true);
        elementTextField.setMaxStringLength(999);
        if (page.equals("profile")) headRender = new PlayerHeadRender(width/2-48, height/2-50, 6, uuid);
        if (ignoreList.containsKey(uuid)) elementTextField.setText(ignoreList.get(uuid).note);
        if(addedText != null) {
            char[] addedTextCharArray = addedText.toCharArray();
            for (char c : addedTextCharArray) {
                elementTextField.keyTyped(c, 30); //Using keycode 30 for "A" as default, may break stuff
            }
        }
        playerListObjects.clear();
        final int[] i = {0};
        ignoreList.forEach((uuid, ignoreDataObject) -> {playerListObjects.add(new PlayerListObject(width/2-75, height/3+13*i[0],uuid)); i[0]++;});
        for (PlayerListObject listObject : playerListObjects) {
            listObject.visible = (listObject.y >= height / 2 - 100) && (listObject.y <= height / 2 + 165);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        elementTextField.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(width / 2 - 150, height / 2 - 123, width / 2 + 150, height / 2 + 180, 0xFF242424);
        switch (page) {
            case "main":
                playerListObjects.forEach(playerListObject -> playerListObject.hover(mouseX, mouseY));
                fontRendererObj.drawString("Ignore List", width / 2 - fontRendererObj.getStringWidth("Ignore List") / 2, height / 2 - 116, 0x00d415);
                int i2 = 0;
                for (int i = startIndex; i < Math.min(playerListObjects.size(), startIndex + LIST_SIZE); i++) {
                    playerListObjects.get(i).setY(height / 3 + 13 * i2);
                    playerListObjects.get(i).drawScreen();
                    i2++;
                }
                break;
            case "profile":
                backButton.hover(mouseX, mouseY);
                fontRendererObj.drawString("IGNORE NOTE " + playerName, width / 2 - fontRendererObj.getStringWidth("IGNORE NOTE " + playerName) / 2, height / 2 - 116, 0x00d415);
                backButton.render();
                elementTextField.render(width / 2 - 90, height / 2 + 50);
                headRender.render();
                break;
            case "error":
                reloadButton.hover(mouseX, mouseY);
                fontRendererObj.drawString("Failed to load the profile of " + playerName + ". Please try again.", width / 2 - fontRendererObj.getStringWidth("Failed to load the profile of " + playerName + ". Please try again.") / 2, height / 2, 0xc71414);
                reloadButton.render();
                break;
            default:
                LenoriAddons.LOGGER.warn("Page has an illegal value! page = " + page + "!");
                break;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scrollDelta = Mouse.getEventDWheel();
        if (scrollDelta != 0) {
            if (page.equals("main")) {
                if (scrollDelta < 0) {
                    if (startIndex < playerListObjects.size() - LIST_SIZE) startIndex++;
                } else if (startIndex > 0) startIndex--;
            } else if (page.equals("profile")) elementTextField.scroll(scrollDelta);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        switch (page) {
            case "main":
                for (PlayerListObject listObject : playerListObjects) {
                    if (listObject.mouseClicked(mouseX, mouseY)) {
                        page = "profile";
                        uuid = listObject.uuid;
                        playerName = MojangAPIClient.getName(uuid);
                        headRender = new PlayerHeadRender(width / 2 - 48, height / 2 - 50, 6, uuid);
                        elementTextField.setText(ignoreList.get(uuid).note);
                        break;
                    }
                }
                break;
            case "profile":
                if (backButton.mouseClicked(mouseX, mouseY)) {
                    page = "main";
                    saveNote();
                }
                break;
            case "error":
                if (reloadButton.mouseClicked(mouseX, mouseY)) {
                    if (loadPlayer()) {
                        page = "profile";
                        headRender = new PlayerHeadRender(width / 2 - 48, height / 2 - 50, 6, uuid);
                        if (!ignoreList.containsKey(uuid)) ignoreAddPlayer();
                    }
                }
                break;
        }
    }

    private boolean loadPlayer() {
        uuid = MojangAPIClient.getUUID(playerName, -1);
        if (uuid != null) playerName = MojangAPIClient.getName(uuid);
        return !(uuid == null || playerName == null);
    }

    private void ignoreAddPlayer() {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/ignore add " + playerName);
            if (addedText == null) addedText = "";
            ignoreListManager.addData(uuid, System.currentTimeMillis(), addedText);
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText( EnumChatFormatting.GOLD + "[LenoriAddons] " + EnumChatFormatting.GREEN + "Added " + playerName + " to your ignore list."));
            ignoreList = ignoreListManager.ignoreList;
    }

    @Override
    public void onGuiClosed() {
        if (ignoreList.containsKey(uuid)) saveNote();
        super.onGuiClosed();
    }

    private void saveNote() {
        if (elementTextField.getText() != null && uuid != null) ignoreListManager.addData(uuid, ignoreList.get(uuid).timestamp, elementTextField.getText());
    }

    @Override
    public boolean doesGuiPauseGame(){return false;}
}
