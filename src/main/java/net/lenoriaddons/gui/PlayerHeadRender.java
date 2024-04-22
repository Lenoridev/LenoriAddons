package net.lenoriaddons.gui;

import net.lenoriaddons.io.MojangAPIClient;
import net.lenoriaddons.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class PlayerHeadRender {
    private ResourceLocation skinLocation;
    private final Minecraft mc = Minecraft.getMinecraft();
    public int x,y,size;
    private final UUID uuid;

    public PlayerHeadRender(int x, int y, int size, UUID uuid) {
        this.x =x;
        this.y =y;
        this.size= size;
        this.uuid =uuid;

        File imageFile = new File(Minecraft.getMinecraft().mcDataDir, "cache/"+ Reference.MODID + "/skins/"+ uuid + ".png");
        if (imageFile.exists()) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                skinLocation = mc.getTextureManager().getDynamicTextureLocation("custom_image", new net.minecraft.client.renderer.texture.DynamicTexture(image));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Image skin = MojangAPIClient.getSkin(uuid);
            if (skin != null) {
                try {
                    if (!imageFile.getParentFile().exists()) {
                        if (!imageFile.getParentFile().mkdirs()) {
                            System.err.println("Failed to create directories for the file: " + imageFile.getParent());
                            return;
                        }
                    }

                    if (imageFile.exists()) {
                        System.err.println("File already exists: " + imageFile.getAbsolutePath());
                        return;
                    }

                    boolean success = ImageIO.write((RenderedImage) skin, "PNG", imageFile);
                    if (success) {
                        System.out.println("Image successfully saved to: " + imageFile.getAbsolutePath());
                        try {
                            BufferedImage image = ImageIO.read(imageFile);
                            skinLocation = mc.getTextureManager().getDynamicTextureLocation("custom_image", new net.minecraft.client.renderer.texture.DynamicTexture(image));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("Failed to write image to file: " + imageFile.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("No skin image to save!");
            }
        }
    }

    public void render(){
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (skinLocation != null) {
            if(size>0) {
                mc.getTextureManager().bindTexture(skinLocation);
                drawModalRectWithCustomSizedTexture(x, y, size*8, size*8, size*8, size*8, size*64, size*64);
            } else {
                System.err.println("PlayerHeadRender: Failed to render Player Head "+ uuid);
            }
        } else {
            System.err.println("PlayerHeadRender: Filed to load skin for "+ uuid);
        }
    }
}

