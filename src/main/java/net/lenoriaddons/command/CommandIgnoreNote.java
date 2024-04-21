package net.lenoriaddons.command;

import net.lenoriaddons.gui.GuiIgnoreListNote;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandIgnoreNote extends CommandBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private String specifiedPlayer;
    private String addedText;

    @Override
    public String getCommandName() {
        return "ignorenote";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ignorenote [player] <note>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.register(this);
            if(args.length == 0) {
                ((EntityPlayer) sender).addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED +"Usage: /ignorenote <player> [additional text]"));
            }else if(args.length==1) {
                specifiedPlayer = args[0];
            } else {
                addedText = args[1];
            }
            LOGGER.warn("Command executed by non-player sender.");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiIgnoreListNote(specifiedPlayer,addedText));
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
