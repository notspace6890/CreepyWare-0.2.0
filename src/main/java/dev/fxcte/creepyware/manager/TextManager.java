// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import net.minecraft.util.math.MathHelper;
import dev.fxcte.creepyware.util.MathUtil;
import java.awt.Color;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.FontMod;
import java.awt.Font;
import dev.fxcte.creepyware.features.gui.font.CustomFont;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.Feature;

public class TextManager extends Feature
{
    private final Timer idleTimer;
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont;
    private boolean idling;
    
    public TextManager() {
        this.idleTimer = new Timer();
        this.customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
        this.updateResolution();
    }
    
    public void init(final boolean startup) {
        final FontMod cFont = CreepyWare.moduleManager.getModuleByClass(FontMod.class);
        try {
            this.setFontRenderer(new Font(cFont.fontName.getValue(), cFont.fontStyle.getValue(), cFont.fontSize.getValue()), cFont.antiAlias.getValue(), cFont.fractionalMetrics.getValue());
        }
        catch (Exception ex) {}
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x, y, color, true);
    }
    
    public float drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        if (!CreepyWare.moduleManager.isModuleEnabled(FontMod.class)) {
            return (float)TextManager.mc.field_71466_p.func_175065_a(text, x, y, color, shadow);
        }
        if (shadow) {
            return this.customFont.drawStringWithShadow(text, x, y, color);
        }
        return this.customFont.drawString(text, x, y, color);
    }
    
    public void drawRainbowString(final String text, final float x, final float y, final int startColor, final float factor, final boolean shadow) {
        Color currentColor = new Color(startColor);
        final float hueIncrement = 1.0f / factor;
        final String[] rainbowStrings = text.split("§.");
        float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
        final float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
        final float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;
        for (int i = 0; i < text.length(); ++i) {
            final char currentChar = text.charAt(i);
            final char nextChar = text.charAt(MathUtil.clamp(i + 1, 0, text.length() - 1));
            if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
                shouldRainbow = false;
            }
            else if ((String.valueOf(currentChar) + nextChar).equals("§+")) {
                shouldRainbow = true;
            }
            if (shouldContinue) {
                shouldContinue = false;
            }
            else {
                if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
                    final String escapeString = text.substring(i);
                    this.drawString(escapeString, x + currentWidth, y, Color.WHITE.getRGB(), shadow);
                    break;
                }
                this.drawString(String.valueOf(currentChar).equals("§") ? "" : String.valueOf(currentChar), x + currentWidth, y, shouldRainbow ? currentColor.getRGB() : Color.WHITE.getRGB(), shadow);
                if (String.valueOf(currentChar).equals("§")) {
                    shouldContinue = true;
                }
                currentWidth += this.getStringWidth(String.valueOf(currentChar));
                if (!String.valueOf(currentChar).equals(" ")) {
                    currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
                    currentHue += hueIncrement;
                }
            }
        }
    }
    
    public int getStringWidth(final String text) {
        if (CreepyWare.moduleManager.isModuleEnabled(FontMod.class)) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.field_71466_p.func_78256_a(text);
    }
    
    public int getFontHeight() {
        if (CreepyWare.moduleManager.isModuleEnabled(FontMod.class)) {
            final String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return TextManager.mc.field_71466_p.field_78288_b;
    }
    
    public void setFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }
    
    public Font getCurrentFont() {
        return this.customFont.getFont();
    }
    
    public void updateResolution() {
        this.scaledWidth = TextManager.mc.field_71443_c;
        this.scaledHeight = TextManager.mc.field_71440_d;
        this.scaleFactor = 1;
        final boolean flag = TextManager.mc.func_152349_b();
        int i = TextManager.mc.field_71474_y.field_74335_Z;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        final double scaledWidthD = this.scaledWidth / (double)this.scaleFactor;
        final double scaledHeightD = this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.func_76143_f(scaledWidthD);
        this.scaledHeight = MathHelper.func_76143_f(scaledHeightD);
    }
    
    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}
