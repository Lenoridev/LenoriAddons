package lenori.lenoriaddons.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static net.minecraft.client.gui.Gui.drawRect;

public class CustomCheckbox {
    public static final Configuration CONFIG = new Configuration(new File("config/LenoriAddons.cfg"));

    private int x, y; // Position of the checkbox
    public boolean isChecked;
    private String label;
    private static final String CATEGORY_GENERAL = "general";
    private String KEY_CHECKBOX_STATE;

    public CustomCheckbox(int x, int y, String configID) {
        this.x = x;
        this.y = y;
        this.label = ChatComponent.translatable(configId); //irgendwie so was
        this.KEY_CHECKBOX_STATE = configID;
        this.isChecked = CONFIG.getBoolean(KEY_CHECKBOX_STATE, CATEGORY_GENERAL, false, configID);
    }

    public void drawCheckbox(Minecraft mc, int mouseX, int mouseY) {
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
        CONFIG.get(CATEGORY_GENERAL, KEY_CHECKBOX_STATE, false).set(isChecked);
    }

    // Getter for checkbox state
    public boolean isChecked() {
        return isChecked;
    }
}
