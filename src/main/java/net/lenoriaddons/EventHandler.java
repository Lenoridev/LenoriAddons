package net.lenoriaddons;

import net.lenoriaddons.gui.ImageRenderInfo;
import net.lenoriaddons.io.IgnoreListJsonManager;
import net.lenoriaddons.io.MojangAPIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    int page;
    boolean listenForIgnoreList;
    int startIndex;
    int endIndex;
    int listNumber;
    public static IgnoreListJsonManager jsonManager = new IgnoreListJsonManager(new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/ignore_list.json").getPath());


    private static final Logger LOGGER = LogManager.getLogger();
    List<ImageRenderInfo> imagesToRender = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event) {
        if(!event.entity.worldObj.provider.isDaytime() && !event.entityPlayer.worldObj.isRemote) {
            event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA +"Gute Nacht, " + event.entityPlayer.getDisplayNameString()));
        }
    }
    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        if(event.gui instanceof GuiInventory) {
            //event.gui = new GuiModSettings();
        }

        }
    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String originalMessage = event.message.getUnformattedText();
        event.message = event.message.createCopy();
        event.message = new ChatComponentText(originalMessage.toString().replace(":skull:", "MEMES"));
        String message = event.message.getUnformattedText();
        if(message.contains("------ Ignored Users (Page")) {
            startIndex = message.indexOf("ge ");
            endIndex = message.indexOf(" of");
            page = Integer.parseInt(message.substring(startIndex + 3, endIndex));
            System.out.println("page:" + page);
            listenForIgnoreList = true;
        }
        if (listenForIgnoreList) {
            if(Character.isDigit(message.charAt(0)) && message.contains(".")) {
                listNumber = Integer.parseInt(message.substring(0, message.indexOf(".")));
                System.out.println("Listening for ignored players");
                String ignoredPlayerName = message.substring(message.indexOf(".") + 2);
                System.out.println("List Number: " + listNumber + " Name: " + ignoredPlayerName);
                jsonManager.addData(MojangAPIClient.getUUID(ignoredPlayerName, -1), System.currentTimeMillis());
                if(listNumber % 10 == 0) {
                    listenForIgnoreList = false;
                }
            }
        }
    }


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre e){
        int width = e.resolution.getScaledWidth() / 2;
        int height = e.resolution.getScaledHeight() / 2;

        for (ImageRenderInfo renderInfo : imagesToRender) {
            if(e.type == RenderGameOverlayEvent.ElementType.CHAT) {
            e.setCanceled(true);
            Minecraft.getMinecraft().renderEngine.bindTexture(renderInfo.image);
            LOGGER.info(renderInfo.x + " " + renderInfo.y);
            Gui.drawModalRectWithCustomSizedTexture(renderInfo.x, height - renderInfo.y, 0.0F, 0.0F, 11, 12, 11, 12);
            e.setCanceled(false);
        }  else {

        }

        ResourceLocation texture = new ResourceLocation("laddons", "textures/gui/skull.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        Gui.drawModalRectWithCustomSizedTexture(width/2+300, height/2+300, 0.0F, 0.0F, 11, 12, 11, 12);
    }}

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {


}
}
