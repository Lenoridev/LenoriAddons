package net.lenoriaddons;

import net.lenoriaddons.command.CommandIgnoreNote;
import net.lenoriaddons.command.CommandLenoriAddons;
import net.lenoriaddons.gui.GuiHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID,name = Reference.NAME, version= Reference.VERSION)
public class LenoriAddons {

    @Mod.Instance(Reference.MODID)
    public static LenoriAddons instance;

    public static final Logger LOGGER = LogManager.getLogger("LenoriAddons");


    public void preInit(FMLPreInitializationEvent event) {

        instance = this;
        NetworkRegistry.INSTANCE.registerGuiHandler(LenoriAddons.instance, new GuiHandler());
    }
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        System.out.println("LenoriAddons successfully loaded.");

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        ClientCommandHandler.instance.registerCommand(new CommandLenoriAddons());
        ClientCommandHandler.instance.registerCommand(new CommandIgnoreNote());
    }

    public static void postInit(FMLPostInitializationEvent event) {

    }

}
