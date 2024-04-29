/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 * THIS FILE WAS TAKEN AND MODIFIED FROM NOTENOUGHUPDATES BY NOTENOUGHUPDATES ON GITHUB, SEE <https://github.com/NotEnoughUpdates/NotEnoughUpdates>
 */

package net.lenoriaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static long startTime = 0;
    private static final EnumChatFormatting[] rainbow = new EnumChatFormatting[]{
            EnumChatFormatting.RED,
            EnumChatFormatting.GOLD,
            EnumChatFormatting.YELLOW,
            EnumChatFormatting.GREEN,
            EnumChatFormatting.AQUA,
            EnumChatFormatting.LIGHT_PURPLE,
            EnumChatFormatting.DARK_PURPLE
    };
    private static final Pattern CHROMA_REPLACE_PATTERN = Pattern.compile("\u00a7z(.+?)(?=\u00a7|$)");

    public static String chromaStringByColourCode(String str) {
        if (str.contains("\u00a7z")) {
            Matcher matcher = CHROMA_REPLACE_PATTERN.matcher(str);

            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                matcher.appendReplacement(
                        sb,
                        Utils.chromaString(matcher.group(1))
                                .replace("\\", "\\\\")
                                .replace("$", "\\$")
                );
            }
            matcher.appendTail(sb);

            str = sb.toString();
        }
        return str;
    }

    public static String chromaString(String str) {
        str = cleanColour(str);

        long currentTimeMillis = System.currentTimeMillis();
        if (startTime == 0) startTime = currentTimeMillis;

        int chromaSpeed = 100;
        //if (chromaSpeed < 10) chromaSpeed = 10;
        //if (chromaSpeed > 5000) chromaSpeed = 5000;

        StringBuilder rainbowText = new StringBuilder();
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int index = ((int) (len / 12f - (currentTimeMillis - startTime) / chromaSpeed)) % rainbow.length;
            len += Minecraft.getMinecraft().fontRendererObj.getCharWidth(c);

            if (index < 0) index += rainbow.length;
            rainbowText.append(rainbow[index]);
            rainbowText.append(c);
        }
        return rainbowText.toString();
    }

    public static String cleanColour(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }

}
