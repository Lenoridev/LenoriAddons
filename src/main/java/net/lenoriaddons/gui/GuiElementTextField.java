/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 * THIS FILE WAS TAKEN AND MODIFIED FROM NOTENOUGHUPDATES BY NOTENOUGHUPDATES ON GITHUB, SEE <https://github.com/NotEnoughUpdates/NotEnoughUpdates>
 */

package net.lenoriaddons.gui;

import net.lenoriaddons.LenoriAddons;
import net.lenoriaddons.util.StringUtils;
import net.lenoriaddons.util.TextRenderUtils;
import net.lenoriaddons.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiElementTextField {
    public static final int DISABLE_BG = 0b1000000;
    public static final int SCALE_TEXT = 0b100000;
    public static final int NUM_ONLY = 0b10000;
    public static final int NO_SPACE = 0b01000;
    public static final int FORCE_CAPS = 0b00100;
    public static final int COLOUR = 0b00010;
    public static final int MULTILINE = 0b00001;

    private int searchBarYSize;
    private int searchBarXSize;
    private static final int searchBarPadding = 2;

    private final int MAX_LINES;
    private int startIndex;

    private int options;

    private boolean focus = false;

    private int x;
    private int y;

    private String prependText = "";
    private String masterStarUnicode = "";
    private int customTextColour = 0xffffffff;

    private final GuiTextField textField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj,
            0, 0, 0, 0
    );

    private int customBorderColour = -1;

    public GuiElementTextField(String initialText, int sizeX, int sizeY, int maxLines, int options) {
        textField.setFocused(true);
        textField.setCanLoseFocus(false);
        textField.setMaxStringLength(999);
        textField.setText(initialText);
        this.searchBarXSize = sizeX;
        this.searchBarYSize = sizeY;
        this.options = options;
        this.MAX_LINES = maxLines;
    }

    public void setMaxStringLength(int len) {
        textField.setMaxStringLength(len);
    }

    public void setCustomBorderColour(int colour) {
        this.customBorderColour = colour;
    }

    public void setCustomTextColour(int colour) {
        this.customTextColour = colour;
    }

    public String getText() {
        return textField.getText();
    }

    public String getTextDisplay() {
        String textNoColour = getText();
        while (true) {
            Matcher matcher = PATTERN_CONTROL_CODE.matcher(textNoColour);
            if (!matcher.find()) break;
            String code = matcher.group(1);
            textNoColour = matcher.replaceFirst("\u00B6" + code);
        }

        return textNoColour;
    }

    public void setPrependText(String text) {
        this.prependText = text;
    }

    public void setText(String text) {
        if (textField.getText() == null || !textField.getText().equals(text)) {
            textField.setText(text);
        }
    }

    public void setSize(int searchBarXSize, int searchBarYSize) {
        this.searchBarXSize = searchBarXSize;
        this.searchBarYSize = searchBarYSize;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return textField.getText();
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
        if (!focus) {
            textField.setCursorPosition(textField.getCursorPosition());
        }
    }

    public boolean getFocus() {
        return focus;
    }

    public int getHeight() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int paddingUnscaled = searchBarPadding / scaledresolution.getScaleFactor();

        int numLines = org.apache.commons.lang3.StringUtils.countMatches(textField.getText(), "\n") + 1;
        int extraSize = (searchBarYSize - 8) / 2 + 8;
        int bottomTextBox = searchBarYSize + extraSize * (numLines - 1);

        return bottomTextBox + paddingUnscaled * 2;
    }

    public int getWidth() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int paddingUnscaled = searchBarPadding / scaledresolution.getScaleFactor();

        return searchBarXSize + paddingUnscaled * 2;
    }

    private float getScaleFactor(String str) {
        return Math.min(1, (searchBarXSize - 2) / (float) Minecraft.getMinecraft().fontRendererObj.getStringWidth(str));
    }

    private boolean isScaling() {
        return (options & SCALE_TEXT) != 0;
    }

    private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7([^\\u00B6]|$)(?!\\u00B6)");

    public int getCursorPos(int mouseX, int mouseY) {
        int xComp = mouseX - x;
        int yComp = mouseY - y;

        int extraSize = (searchBarYSize - 8) / 2 + 8;

        String renderText = prependText + textField.getText();

        int lineNum = Math.round(((yComp - (searchBarYSize - 8) / 2)) / extraSize);

        String text = renderText;
        String textNoColour = renderText;
        if ((options & COLOUR) != 0) {
            while (true) {
                Matcher matcher = PATTERN_CONTROL_CODE.matcher(text);
                if (!matcher.find() || matcher.groupCount() < 1) break;
                String code = matcher.group(1);
                if (code.isEmpty()) {
                    text = matcher.replaceFirst("\u00A7r\u00B6");
                } else {
                    text = matcher.replaceFirst("\u00A7" + code + "\u00B6" + code);
                }
            }
        }
        while (true) {
            Matcher matcher = PATTERN_CONTROL_CODE.matcher(textNoColour);
            if (!matcher.find() || matcher.groupCount() < 1) break;
            String code = matcher.group(1);
            textNoColour = matcher.replaceFirst("\u00B6" + code);
        }

        int currentLine = 0;
        int cursorIndex = 0;
        for (; cursorIndex < textNoColour.length(); cursorIndex++) {
            if (currentLine == lineNum) break;
            if (textNoColour.charAt(cursorIndex) == '\n') {
                currentLine++;
            }
        }

        String textNC = textNoColour.substring(0, cursorIndex);
        int colorCodes = org.apache.commons.lang3.StringUtils.countMatches(textNC, "\u00B6");
        String line = text.substring(cursorIndex + (((options & COLOUR) != 0) ? colorCodes * 2 : 0)).split("\n")[0];
        int padding = Math.min(5, searchBarXSize - strLenNoColor(line)) / 2;
        String trimmed = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(line, xComp - padding);
        int linePos = strLenNoColor(trimmed);
        if (linePos != strLenNoColor(line)) {
            char after = line.charAt(linePos);
            int trimmedWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(trimmed);
            int charWidth = Minecraft.getMinecraft().fontRendererObj.getCharWidth(after);
            if (trimmedWidth + charWidth / 2 < xComp - padding) {
                linePos++;
            }
        }
        cursorIndex += linePos;

        int pre = StringUtils.cleanColour(prependText).length();
        if (cursorIndex < pre) {
            cursorIndex = 0;
        } else {
            cursorIndex -= pre;
        }

        return cursorIndex;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1) {
            textField.setText("");
        } else {
            textField.setCursorPosition(getCursorPos(mouseX, mouseY));
        }
        focus = true;
    }

    public void unfocus() {
        focus = false;
        textField.setSelectionPos(textField.getCursorPosition());
    }

    public int strLenNoColor(String str) {
        return str.replaceAll("(?i)\\u00A7.", "").length();
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (focus) {
            textField.setSelectionPos(getCursorPos(mouseX, mouseY));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (focus) {
            String text = textField.getText();
            if ((options & MULTILINE) != 0) { //Carriage return
                Pattern patternControlCode = Pattern.compile("(?i)\\u00A7([^\\u00B6\n]|$)(?!\\u00B6)");

                String textNoColour = textField.getText();
                while (true) {
                    Matcher matcher = patternControlCode.matcher(text);
                    if (!matcher.find() || matcher.groupCount() < 1) break;
                    String code = matcher.group(1);
                    if (code.isEmpty()) {
                        text = matcher.replaceFirst("\u00A7r\u00B6");
                    } else {
                        text = matcher.replaceFirst("\u00A7" + code + "\u00B6" + code);
                    }
                }
                while (true) {
                    Matcher matcher = patternControlCode.matcher(textNoColour);
                    if (!matcher.find() || matcher.groupCount() < 1) break;
                    String code = matcher.group(1);
                    textNoColour = matcher.replaceFirst("\u00B6" + code);
                }

                if (keyCode == 28) {
                    newLine();
                    return;
                } else if (keyCode == 200) { //Up
                    String textNCBeforeCursor = textNoColour.substring(0, textField.getSelectionEnd());
                    int colorCodes = org.apache.commons.lang3.StringUtils.countMatches(textNCBeforeCursor, "\u00B6");
                    String textBeforeCursor = text.substring(0, textField.getSelectionEnd() + colorCodes * 2);

                    int numLinesBeforeCursor = org.apache.commons.lang3.StringUtils.countMatches(textBeforeCursor, "\n");

                    String[] split = textBeforeCursor.split("\n");
                    int textBeforeCursorWidth;
                    String lineBefore;
                    String thisLineBeforeCursor;
                    if (split.length == numLinesBeforeCursor && split.length > 0) {
                        textBeforeCursorWidth = 0;
                        lineBefore = split[split.length - 1];
                        thisLineBeforeCursor = "";
                    } else if (split.length > 1) {
                        thisLineBeforeCursor = split[split.length - 1];
                        lineBefore = split[split.length - 2];
                        textBeforeCursorWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(thisLineBeforeCursor);
                    } else {
                        return;
                    }
                    String trimmed = Minecraft.getMinecraft().fontRendererObj
                            .trimStringToWidth(lineBefore, textBeforeCursorWidth);
                    int linePos = strLenNoColor(trimmed);
                    if (linePos != strLenNoColor(lineBefore)) {
                        char after = lineBefore.charAt(linePos);
                        int trimmedWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(trimmed);
                        int charWidth = Minecraft.getMinecraft().fontRendererObj.getCharWidth(after);
                        if (trimmedWidth + charWidth / 2 < textBeforeCursorWidth) {
                            linePos++;
                        }
                    }
                    int newPos = textField.getSelectionEnd() - strLenNoColor(thisLineBeforeCursor)
                            - strLenNoColor(lineBefore) - 1 + linePos;

                    if (GuiScreen.isShiftKeyDown()) {
                        textField.setSelectionPos(newPos);
                    } else {
                        textField.setCursorPosition(newPos);
                    }
                } else if (keyCode == 208) { //Down
                    String textNCBeforeCursor = textNoColour.substring(0, textField.getSelectionEnd());
                    int colorCodes = org.apache.commons.lang3.StringUtils.countMatches(textNCBeforeCursor, "\u00B6");
                    String textBeforeCursor = text.substring(0, textField.getSelectionEnd() + colorCodes * 2);

                    int numLinesBeforeCursor = org.apache.commons.lang3.StringUtils.countMatches(textBeforeCursor, "\n");

                    String[] split = textBeforeCursor.split("\n");
                    String thisLineBeforeCursor;
                    int textBeforeCursorWidth;
                    if (split.length == numLinesBeforeCursor) {
                        thisLineBeforeCursor = "";
                        textBeforeCursorWidth = 0;
                    } else if (split.length > 0) {
                        thisLineBeforeCursor = split[split.length - 1];
                        textBeforeCursorWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(thisLineBeforeCursor);
                    } else {
                        return;
                    }

                    String[] split2 = textNoColour.split("\n");
                    if (split2.length > numLinesBeforeCursor + 1) {
                        String lineAfter = split2[numLinesBeforeCursor + 1];
                        String trimmed = Minecraft.getMinecraft().fontRendererObj
                                .trimStringToWidth(lineAfter, textBeforeCursorWidth);
                        int linePos = strLenNoColor(trimmed);
                        if (linePos != strLenNoColor(lineAfter)) {
                            char after = lineAfter.charAt(linePos);
                            int trimmedWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(trimmed);
                            int charWidth = Minecraft.getMinecraft().fontRendererObj.getCharWidth(after);
                            if (trimmedWidth + charWidth / 2 < textBeforeCursorWidth) {
                                linePos++;
                            }
                        }
                        int newPos = textField.getSelectionEnd() - strLenNoColor(thisLineBeforeCursor)
                                + strLenNoColor(split2[numLinesBeforeCursor]) + 1 + linePos;

                        if (GuiScreen.isShiftKeyDown()) {
                            textField.setSelectionPos(newPos);
                        } else {
                            textField.setCursorPosition(newPos);
                        }
                    }
                }
            }

            String old = textField.getText();
            if ((options & FORCE_CAPS) != 0) typedChar = Character.toUpperCase(typedChar);
            if ((options & NO_SPACE) != 0 && typedChar == ' ') return;

            if (typedChar == '\u00B6') {
                typedChar = '\u00A7';
            }

            textField.setFocused(true);
            textField.textboxKeyTyped(typedChar, keyCode);

            if ((options & COLOUR) != 0) {
                if (typedChar == '&') {
                    int pos = textField.getCursorPosition() - 2;
                    if (pos >= 0 && pos < textField.getText().length()) {
                        if (textField.getText().charAt(pos) == '&') {
                            String before = textField.getText().substring(0, pos);
                            String after = "";
                            if (pos + 2 < textField.getText().length()) {
                                after = textField.getText().substring(pos + 2);
                            }
                            textField.setText(before + "\u00A7" + after);
                            textField.setCursorPosition(pos + 1);
                        }
                    }
                } else if (typedChar == '*') {
                    int pos = textField.getCursorPosition() - 2;
                    if (pos >= 0 && pos < textField.getText().length()) {
                        if (textField.getText().charAt(pos) == '*') {
                            String before = textField.getText().substring(0, pos);
                            String after = "";
                            if (pos + 2 < textField.getText().length()) {
                                after = textField.getText().substring(pos + 2);
                            }
                            textField.setText(before + "\u272A" + after);
                            textField.setCursorPosition(pos + 1);
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        if (typedChar == Integer.toString(i + 1).charAt(0)) {
                            int pos = textField.getCursorPosition() - 2;
                            if (pos >= 0 && pos < textField.getText().length()) {
                                if (textField.getText().charAt(pos) == '*') {
                                    switch (i) {
                                        case 0:
                                            masterStarUnicode = "\u278A";
                                            break;
                                        case 1:
                                            masterStarUnicode = "\u278B";
                                            break;
                                        case 2:
                                            masterStarUnicode = "\u278C";
                                            break;
                                        case 3:
                                            masterStarUnicode = "\u278D";
                                            break;
                                        case 4:
                                            masterStarUnicode = "\u278E";
                                            break;
                                        case 5:
                                            masterStarUnicode = "\u278F";
                                            break;
                                        case 6:
                                            masterStarUnicode = "\u2790";
                                            break;
                                        case 7:
                                            masterStarUnicode = "\u2791";
                                            break;
                                        case 8:
                                            masterStarUnicode = "\u2792";
                                            break;
                                        case 9:
                                            masterStarUnicode = "\u2793";
                                            break;
                                    }
                                    String before = textField.getText().substring(0, pos);
                                    String after = "";
                                    if (pos + 2 < textField.getText().length()) {
                                        after = textField.getText().substring(pos + 2);
                                    }
                                    textField.setText(before + masterStarUnicode + after);
                                    textField.setCursorPosition(pos + 1);
                                }
                            }
                        }
                    }
                }
            }

            if ((options & NUM_ONLY) != 0 && textField.getText().matches("[^0-9.]")) textField.setText(old);

            String[] texts = (prependText + textField.getText()).split("\n");
            int cursorPosition = textField.getCursorPosition();
            int nextLineBreak = text.indexOf('\n', cursorPosition);
            if (nextLineBreak == -1) nextLineBreak = text.length();
            int line = findElementFromIndex(text, texts, cursorPosition);
            if (line == -1) line++;
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(texts[line]) + 15 > searchBarXSize && !Arrays.asList(14, 200, 203, 205, 208, 28).contains(keyCode)) { //text out of bounds

                newLine();
                LenoriAddons.LOGGER.info("textfieldcursorpos: " + textField.getCursorPosition());
                LenoriAddons.LOGGER.info("text length: " + text.length());
                textField.setCursorPosition(nextLineBreak + 1);
            }

            if (startIndex < texts.length - searchBarYSize) startIndex = texts.length - searchBarYSize;
            if (Arrays.asList(200, 203, 205, 208).contains(keyCode)) { //Arrow keys
                if (textField.getCursorPosition() < text.indexOf(texts[startIndex])) startIndex--;
            }
        }
    }

    public void newLine() {
        String before = textField.getText().substring(0, textField.getCursorPosition());
        String after = textField.getText().substring(textField.getCursorPosition());
        textField.setText(before + "\n" + after);
        LenoriAddons.LOGGER.info("New line inserted. Text before: " + before + ", Text after: " + after);
        textField.setCursorPosition(before.length() + 1);
        LenoriAddons.LOGGER.info("Cursor position updated to: " + textField.getCursorPosition());
    }

    public void render(int x, int y) {
        this.x = x;
        this.y = y;
        drawTextbox(x, y, searchBarXSize, searchBarYSize, searchBarPadding, textField, focus);
    }

    private void drawTextbox(
            int x, int y, int searchBarXSize, int searchBarYSize, int searchBarPadding,
            GuiTextField textField, boolean focus
    ) {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        String renderText = prependText + textField.getText();

        GlStateManager.disableLighting();

        /*
         * Search bar
         */
        int paddingUnscaled = searchBarPadding / scaledresolution.getScaleFactor();
        if (paddingUnscaled < 1) paddingUnscaled = 1;

        int numLines = org.apache.commons.lang3.StringUtils.countMatches(renderText, "\n") + 1;
        int extraSize = (searchBarYSize - 8) / 2 + 8;
        int bottomTextBox = y + searchBarYSize + extraSize * (numLines - 1);

        int borderColour = focus ? Color.GREEN.getRGB() : Color.WHITE.getRGB();
        if (customBorderColour != -1) {
            borderColour = customBorderColour;
        }
        if ((options & DISABLE_BG) == 0) {
            //bar background
            Gui.drawRect(x - paddingUnscaled,
                    y - paddingUnscaled,
                    x + searchBarXSize + paddingUnscaled,
                    bottomTextBox + paddingUnscaled, borderColour
            );
            Gui.drawRect(x,
                    y,
                    x + searchBarXSize,
                    bottomTextBox, Color.BLACK.getRGB()
            );
        }

        //bar text
        String text = renderText;
        String textNoColor = renderText;
        if ((options & COLOUR) != 0) {
            while (true) {
                Matcher matcher = PATTERN_CONTROL_CODE.matcher(text);
                if (!matcher.find() || matcher.groupCount() < 1) break;
                String code = matcher.group(1);
                if (code.isEmpty()) {
                    text = matcher.replaceFirst("\u00A7r\u00B6");
                } else {
                    text = matcher.replaceFirst("\u00A7" + code + "\u00B6" + code);
                }
            }
        }
        while (true) {
            Matcher matcher = PATTERN_CONTROL_CODE.matcher(textNoColor);
            if (!matcher.find() || matcher.groupCount() < 1) break;
            String code = matcher.group(1);
            textNoColor = matcher.replaceFirst("\u00B6" + code);
        }

        int xStartOffset = 5;
        float scale = 1;
        char[] chars = text.toCharArray();
        String[] texts = new String[] {""};
        if (chars.length != 0) {
            if (chars[chars.length - 1] == '\n') {
                texts = (text + " ").split("\n");
            } else {
                texts = text.split("\n");
            }
        }

        int i2 = 0;
        for (int yOffI = startIndex; yOffI < Math.min(texts.length, startIndex + searchBarYSize); yOffI++) {

                    String toRender = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(Utils.chromaStringByColourCode(
                            texts[yOffI]), searchBarXSize - 10);
                    Minecraft.getMinecraft().fontRendererObj.drawString(toRender, x + 5,
                            y + (searchBarYSize - 8) / 2 + i2 * extraSize, customTextColour
                    );
                i2++;
        }

        if (startIndex > texts.length) startIndex = 0;

        if (focus && System.currentTimeMillis() % 1000 > 500) {
            //String textNCBeforeCursor = textNoColor.substring(textNoColor.indexOf(texts[startIndex]) - 1, textField.getCursorPosition() + prependText.length());
            //int colorCodes = org.apache.commons.lang3.StringUtils.countMatches(textNCBeforeCursor, "\u00B6");
            String textBeforeCursor;
            if (texts.length > searchBarYSize) {
                //if (textField.getCursorPosition() < text.indexOf(texts[startIndex])) textField.setCursorPosition(text.indexOf(texts[startIndex]));
                int startCharIndex = text.indexOf(texts[startIndex]);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = startCharIndex; i < textField.getCursorPosition(); i++) {
                    stringBuilder.append(chars[i]);
                }
                textBeforeCursor = stringBuilder.toString();
            } else {
                textBeforeCursor = text.substring(0, textField.getCursorPosition());
            }

            int numLinesBeforeCursor = org.apache.commons.lang3.StringUtils.countMatches(textBeforeCursor, "\n");
            if (numLinesBeforeCursor + 1 <= searchBarYSize && numLinesBeforeCursor >= 0) {
                int yOff = numLinesBeforeCursor * extraSize;

                String[] split = textBeforeCursor.split("\n");
                int textBeforeCursorWidth;
                if (split.length <= numLinesBeforeCursor) {
                    textBeforeCursorWidth = 0;
                } else {
                    textBeforeCursorWidth = (int) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(split[split.length -
                            1]) * scale);
                }
                Gui.drawRect(x + xStartOffset + textBeforeCursorWidth,
                        y + (searchBarYSize - 8) / 2 - 1 + yOff,
                        x + xStartOffset + textBeforeCursorWidth + 1,
                        y + (searchBarYSize - 8) / 2 + 9 + yOff, Color.WHITE.getRGB()
                );
            }
        }

        String selectedText = textField.getSelectedText();
        if (!selectedText.isEmpty()) {
            int leftIndex = Math.min(
                    textField.getCursorPosition() + prependText.length(),
                    textField.getSelectionEnd() + prependText.length()
            );
            int rightIndex;
            if (texts.length > searchBarYSize) {
                rightIndex = Math.max(
                        textField.getCursorPosition() + prependText.length(),
                        Math.min(textField.getSelectionEnd() + prependText.length(), text.indexOf(texts[searchBarYSize - 1])) //End of visible text
                );
            } else {
                rightIndex = Math.max(
                        textField.getCursorPosition() + prependText.length(),
                        textField.getSelectionEnd() + prependText.length()
                );
            }

            float texX = 0;
            int texY = 0;
            boolean sectionSignPrev = false;
            boolean ignoreNext = false;
            boolean bold = false;
            for (int i = 0; i < textNoColor.length(); i++) {
                if (ignoreNext) {
                    ignoreNext = false;
                    continue;
                }

                char c = textNoColor.charAt(i);
                if (sectionSignPrev) {
                    if (c != 'k' && c != 'K'
                            && c != 'm' && c != 'M'
                            && c != 'n' && c != 'N'
                            && c != 'o' && c != 'O') {
                        bold = c == 'l' || c == 'L';
                    }
                    sectionSignPrev = false;
                    if (i < prependText.length()) continue;
                }
                if (c == '\u00B6') {
                    sectionSignPrev = true;
                    if (i < prependText.length()) continue;
                }

                if (c == '\n') {
                    if (i >= leftIndex && i < rightIndex) {
                        Gui.drawRect(x + xStartOffset + (int) texX,
                                y + (searchBarYSize - 8) / 2 - 1 + texY,
                                x + xStartOffset + (int) texX + 3,
                                y + (searchBarYSize - 8) / 2 + 9 + texY, Color.LIGHT_GRAY.getRGB()
                        );
                    }

                    texX = 0;
                    texY += extraSize;
                    continue;
                }

                int len = Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(c));
                if (bold) len++;
                if (i >= leftIndex && i < rightIndex) {
                    Gui.drawRect(x + xStartOffset + (int) texX,
                            y + (searchBarYSize - 8) / 2 - 1 + texY,
                            x + xStartOffset + (int) (texX + len * scale),
                            y + (searchBarYSize - 8) / 2 + 9 + texY, Color.LIGHT_GRAY.getRGB()
                    );

                    TextRenderUtils.drawStringScaled(String.valueOf(c), Minecraft.getMinecraft().fontRendererObj,
                            x + xStartOffset + texX,
                            y + searchBarYSize / 2f - scale * 8 / 2f + texY, false, Color.BLACK.getRGB(), scale
                    );
                    if (bold) {
                        TextRenderUtils.drawStringScaled(String.valueOf(c), Minecraft.getMinecraft().fontRendererObj,
                                x + xStartOffset + texX + 1,
                                y + searchBarYSize / 2f - scale * 8 / 2f + texY, false, Color.BLACK.getRGB(), scale
                        );
                    }
                }

                texX += len * scale;
            }
        }
    }

    public void scroll(int scrollDelta) {
        if (scrollDelta < 0) {
            if (startIndex < (org.apache.commons.lang3.StringUtils.countMatches(prependText + getText(), "\n") + 1) - searchBarYSize) startIndex++;
        } else if (startIndex > 0) startIndex--;
    }

    private static int findElementFromIndex(String text, String[] lines, int index) {
        int currentIndex = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineLength = line.length() + 1; // +1 for the '\n' char

            if (index < currentIndex + lineLength) {
                return i;
            }

            currentIndex += lineLength;
        }

        return -1;
    }
}
