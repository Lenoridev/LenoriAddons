package net.lenoriaddons.gui;

import net.lenoriaddons.Reference;
import net.lenoriaddons.io.IgnoreListJsonManager;
import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiIgnoreListNote extends GuiScreen {

    private GuiElementTextField elementTextField;
    private final String addedText;
    private String playerName,page;
    private Map<UUID, IgnoreListJsonManager.IgnoreDataObject> ignoreList;
    private static final IgnoreListJsonManager ignoreListManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());
    private final List<PlayerListObject> playerListObjects = new ArrayList<>();
    private UUID uuid;
    private BackButton backButton;
    private int startIndex;
    private final int LIST_SIZE = 20;
    private PlayerHeadRender headRender;


    public GuiIgnoreListNote(String player, String addedText) {
        this.addedText = addedText;
        ignoreList = ignoreListManager.ignoreList;
        this.playerName = player;
        if (playerName == null) page = "main"; else {
            page = "profile";
            uuid = MojangAPIClient.getUUID(playerName, -1);
            playerName = MojangAPIClient.getName(uuid);
        }
    }

    @Override
    public void initGui() {
        elementTextField = new GuiElementTextField("", 200,10, 12,0b1000001);
        backButton = new BackButton(width/2-110, height/2-100);
        elementTextField.setFocus(true);
        elementTextField.setMaxStringLength(999);
        if (page.equals("profile")) headRender = new PlayerHeadRender(width/2-48, height/2-50, 6, uuid);
        if(addedText != null) elementTextField.setText(addedText);
        if (ignoreList.containsKey(uuid)) elementTextField.setText(ignoreList.get(uuid).note);
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
        backButton.hover(mouseX, mouseY);
        playerListObjects.forEach(playerListObject -> playerListObject.hover(mouseX, mouseY));
        drawRect(width / 2 - 150, height / 2 - 123, width / 2 + 150, height / 2 + 180, 0xFF242424);
        if (page.equals("main")) {
            fontRendererObj.drawString("Ignore List", width / 2 - fontRendererObj.getStringWidth("Ignore List") / 2, height / 2 -116, 0x00d415);
            int i2 = 0;
            for (int i = startIndex; i < Math.min(playerListObjects.size(), startIndex + LIST_SIZE); i++){
                playerListObjects.get(i).setY(height / 3 + 13 * i2);
                playerListObjects.get(i).drawScreen();
                i2++;
            }
        } else if (page.equals("profile")){
            fontRendererObj.drawString("IGNORE NOTE "+ playerName, width / 2 - fontRendererObj.getStringWidth("IGNORE NOTE "+ playerName) / 2, height / 2 -116, 0x00d415);
            //fontRendererObj.drawString("UUID: "+ uuid, width / 2 - fontRendererObj.getStringWidth("UUID: " + uuid) / 2, height / 2 -100, 0x00d415);
            backButton.render();
            elementTextField.render(width/2 -90, height/2 +50);
            headRender.render();
        } else page="main";
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
            } else elementTextField.scroll(scrollDelta);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (PlayerListObject listObject : playerListObjects) {
            if (page.equals("main") && listObject.mouseClicked(mouseX, mouseY)) {
                page = "profile";
                uuid = listObject.uuid;
                playerName = MojangAPIClient.getName(uuid);
                headRender = new PlayerHeadRender(width/2-48, height/2-50, 6, uuid);
                elementTextField.setText(ignoreList.get(uuid).note);
                break;
            }
        }
        if (backButton.mouseClicked(mouseX, mouseY)) {
            page = "main";
            saveNote();
        }
    }

    @Override
    public void onGuiClosed() {
        saveNote();
        super.onGuiClosed();
    }

    private void saveNote() {
        if (elementTextField.getText() != null && uuid != null) ignoreListManager.addData(uuid, ignoreList.get(uuid).timestamp, elementTextField.getText());
    }

    @Override
    public boolean doesGuiPauseGame(){return false;}
}
