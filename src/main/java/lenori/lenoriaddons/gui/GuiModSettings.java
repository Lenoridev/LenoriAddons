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

    private static final Logger LOGGER = LogManager.getLogger();
    //public static Configuration config;

    private GuiTextField textField;
    private CustomCheckbox checkbox;
    private CustomCheckbox checkbox1;
    private CustomCheckbox checkbox2;
    private CustomCheckbox checkbox3;
    private CustomCheckbox checkbox4;
    private CustomCheckbox checkbox5;
    private CustomCheckbox checkbox6;
    private CustomCheckbox checkbox7;
    private CustomCheckbox checkbox8;

    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("minecraft", "gui.button.press");


    @Override
    public void initGui() {
        //buttonList.add(new GuiButton(0, width/2 - 50, height/2 - 50, 100, 20, "Install wurst"));
        //textField = new GuiTextField(2, fontRendererObj, width/2 -50, height/2 +20, 200, 100);
        //textField.setFocused(true);
        //textField.setCanLoseFocus(false);

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
        checkbox.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox1.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox2.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox3.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox4.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox5.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox6.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox7.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);
        checkbox8.drawCheckbox(Minecraft.getMinecraft(), mouseX,  mouseY);




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
        checkbox.mouseClicked(mouseX, mouseY);
        checkbox1.mouseClicked(mouseX, mouseY);
        checkbox2.mouseClicked(mouseX, mouseY);
        checkbox3.mouseClicked(mouseX, mouseY);
        checkbox4.mouseClicked(mouseX, mouseY);
        checkbox5.mouseClicked(mouseX, mouseY);
        checkbox6.mouseClicked(mouseX, mouseY);
        checkbox7.mouseClicked(mouseX, mouseY);
        checkbox8.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        checkbox.saveCheckboxState();
        checkbox1.saveCheckboxState();
        checkbox2.saveCheckboxState();
        checkbox3.saveCheckboxState();
        checkbox4.saveCheckboxState();
        checkbox5.saveCheckboxState();
        checkbox6.saveCheckboxState();
        checkbox7.saveCheckboxState();
        checkbox8.saveCheckboxState();
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