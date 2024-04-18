package lenori.lenoriaddons.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_MODSETTINGS = 18788; // Change this ID to your GUI ID

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // Check the ID and return the corresponding GUI
        switch (ID) {
            case GUI_MODSETTINGS:
                // Assuming CustomGui is your custom GUI class, replace it with your actual class
                return new GuiModSettings();
            default:
                return null;
        }
    }
}