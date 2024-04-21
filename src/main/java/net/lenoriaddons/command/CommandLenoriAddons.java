package net.lenoriaddons.command;

import net.lenoriaddons.gui.GuiModSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CommandLenoriAddons extends CommandBase {

    private static final Logger LOGGER = LogManager.getLogger();

        @Override
        public String getCommandName() {
            return "lenoriaddons";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/lenoriaddons";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            if (sender instanceof EntityPlayer) {
                MinecraftForge.EVENT_BUS.register(this);
            } else {
                LOGGER.warn("Command executed by non-player sender.");
            }
        }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiModSettings());
        MinecraftForge.EVENT_BUS.unregister(this);
    }


    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }



}
