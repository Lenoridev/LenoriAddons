package net.lenoriaddons.gui;

import net.minecraft.util.ResourceLocation;

public class ImageRenderInfo {

    //Unused, will probably be deleted soon

    public ResourceLocation image;
    public int x;
    public int y;

    public ImageRenderInfo(String image, int x, int y) {
        this.image = new ResourceLocation("laddons",  image);
        this.x = x;
        this.y = y;
    }
}
