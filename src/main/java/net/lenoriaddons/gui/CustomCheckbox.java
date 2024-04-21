package net.lenoriaddons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static net.minecraft.client.gui.Gui.drawRect;

public class CustomCheckbox {
    public static final Configuration CONFIG = new Configuration(new File("config/LenoriAddons.cfg"));

    private final int x;
    private final int y;
    public boolean isChecked;
    private final String label;
    private static final String CATEGORY = "general";
    private final String configId;
    private final Minecraft mc = Minecraft.getMinecraft();

    public CustomCheckbox(int x, int y, String configID) {
        this.x = x;
        this.y = y;
        this.configId = configID;
        this.label = I18n.format(configID);
        this.isChecked = CONFIG.getBoolean(configId, CATEGORY, false, configID);
    }

    public void drawCheckbox() {
        // Draw checkbox
        mc.fontRendererObj.drawString(label, x + 20, y + 2, 0xFFFFFF, true);
        drawRect(x, y, x + 10, y + 10, 0xFF000000); // Outer box
        if (isChecked) {//bei mir sieht das ein wenig anders aus...
            drawRect(x + 2, y + 2, x + 8, y + 8, 0xFFFFFFFF); // Inner box (checked)
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 10 && mouseY >= y && mouseY <= y + 10) {
            isChecked = !isChecked;
            GuiModSettings.playClickSound();
            return true; // Checkbox clicked
        }
        return false; // Checkbox not clicked
    }

    public void saveCheckboxState(){
        CONFIG.get(CATEGORY, configId, false).set(isChecked);
    }

    // Getter for checkbox state
    public boolean isChecked() {
        return isChecked;
    }
}
