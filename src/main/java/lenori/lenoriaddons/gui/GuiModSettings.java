package lenori.lenoriaddons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GuiModSettings extends GuiScreen {
    String[] checkBoxIds = new String[] {
        "chatEmojies",
        "customRunes",
        "predevTimer",
        "cataLvlGui",
        "networthGui",
        "ignoreListNotes",
        "nametagEffects",
        "dungeonTeammateLogger",
        "tomas"
    }
        

    private static final Logger LOGGER = LogManager.getLogger();
    //public static Configuration config;

    private GuiTextField textField;
    private List<CustomCheckBox> boxes = new ArrayList<>();

    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("minecraft", "gui.button.press");


    @Override
    public void initGui() {
        //buttonList.add(new GuiButton(0, width/2 - 50, height/2 - 50, 100, 20, "Install wurst"));
        //textField = new GuiTextField(2, fontRendererObj, width/2 -50, height/2 +20, 200, 100);
        //textField.setFocused(true);
        //textField.setCanLoseFocus(false);
        boxes.clear();
        for (int i = 0; i < 9; i++) {
            boxed.add(new CustomCheckbox(width/2 - 60, height/2 - (75 - i * 15), checkBoxIds[i]));
        }

        checkbox = new CustomCheckbox(width/2 - 60,height/2-75, "Activate Chat Emojis","chatEmojis");
        checkbox1 = new CustomCheckbox(width/2 - 60,height/2-60, "Activate Custom Runes","customRunes");
        checkbox2 = new CustomCheckbox(width/2 - 60,height/2-45, "Activate Predev Timer","predevTimer");
        checkbox3 = new CustomCheckbox(width/2 - 60,height/2-30, "Activate Cata LVL Gui","cataLvlGui");
        checkbox4 = new CustomCheckbox(width/2 - 60,height/2-15, "Activate Networth Gui","networthGui");
        checkbox5 = new CustomCheckbox(width/2 - 60,height/2, "Activate Ignore List Notes","ignoreListNotes");
        checkbox6 = new CustomCheckbox(width/2 - 60,height/2+15, "Activate Nametag Effects","nametagEffects");
        checkbox7 = new CustomCheckbox(width/2 - 60,height/2+30, "Activate Dungeon Teammate Logger","dungeonTeammateLogger");
        checkbox8 = new CustomCheckbox(width/2 - 60,height/2+45, "Activate Tomas","tomas");


    }

    @Override
    public void updateScreen() {
        //textField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //textField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawRect(width/2 -100, height/2-123, width/2 +170, height/2 +80, 0xFF242424);
        fontRendererObj.drawString("LENORI ADDONS", width / 2 - fontRendererObj.getStringWidth("LENORI ADDONS") / 2, height / 2 -100, 0x00d415);
        //fontRendererObj.drawString(Double.toString(slider.getValue()), width / 2 - fontRendererObj.getStringWidth(Double.toString(slider.getValue())) / 2, height / 2 -80, 0xFFFFFF);
        //textField.drawTextBox();
        boxes.forEach(box -> box.drawCheckbox(Minecraft.getMinecraft(), mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu()); break;
            case 3:
                break;
        }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boxes.forEach(box -> box.mouseClicked(mouseX, mouseY));
    }

    @Override
    public void onGuiClosed() {
        boxes.forEach(CustomCheckbox::saveCheckboxState);
        super.onGuiClosed();
    }

    public static void playClickSound() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            soundHandler.playSound(PositionedSoundRecord.create(CLICK_SOUND, 1.0f));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
}
}
