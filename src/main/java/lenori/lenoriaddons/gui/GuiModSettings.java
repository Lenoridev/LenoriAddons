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
import java.util.ArrayList;
import java.util.List;

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
    };
        

    private static final Logger LOGGER = LogManager.getLogger();
    //public static Configuration config;

    private GuiTextField textField;
    private final List<CustomCheckbox> boxes = new ArrayList<>();

    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("minecraft", "gui.button.press");


    @Override
    public void initGui() {
        boxes.clear();
        for (int i = 0; i < 9; i++) {
            boxes.add(new CustomCheckbox(width/2 - 75, height/2 - (75 - i * 15), checkBoxIds[i]));
        }
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
        drawRect(width/2 -150, height/2-123, width/2 +150, height/2 +80, 0xFF242424);
        fontRendererObj.drawString("LENORI ADDONS", width / 2 - fontRendererObj.getStringWidth("LENORI ADDONS") / 2, height / 2 -100, 0x00d415);
        //fontRendererObj.drawString(Double.toString(slider.getValue()), width / 2 - fontRendererObj.getStringWidth(Double.toString(slider.getValue())) / 2, height / 2 -80, 0xFFFFFF);
        boxes.forEach(CustomCheckbox::drawCheckbox);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button){
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
