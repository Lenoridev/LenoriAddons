/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 * THIS FILE WAS TAKEN AND MODIFIED FROM NOTENOUGHUPDATES BY NOTENOUGHUPDATES ON GITHUB, SEE <https://github.com/NotEnoughUpdates/NotEnoughUpdates>
 */

package net.lenoriaddons.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class TextRenderUtils {

    public static void drawStringScaled(
            String str,
            FontRenderer fr,
            float x,
            float y,
            boolean shadow,
            int colour,
            float factor
    ) {
        GlStateManager.scale(factor, factor, 1);
        fr.drawString(str, x / factor, y / factor, colour, shadow);
        GlStateManager.scale(1 / factor, 1 / factor, 1);
    }

    public static void drawStringCenteredScaledMaxWidth(
            String str,
            FontRenderer fr,
            float x,
            float y,
            boolean shadow,
            int len,
            int colour
    ) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        factor = Math.min(1, factor);
        int newLen = Math.min(strLen, len);

        float fontHeight = 8 * factor;

        drawStringScaled(str, fr, x - newLen / 2, y - fontHeight / 2, shadow, colour, factor);
    }
}
