/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 * THIS FILE WAS TAKEN AND MODIFIED FROM NOTENOUGHUPDATES BY NOTENOUGHUPDATES ON GITHUB, SEE <https://github.com/NotEnoughUpdates/NotEnoughUpdates>
 */

package net.lenoriaddons.util;

public class StringUtils {
    public static String cleanColour(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }
}
