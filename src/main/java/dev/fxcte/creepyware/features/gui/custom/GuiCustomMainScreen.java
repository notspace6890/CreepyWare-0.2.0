// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.gui.custom;

import dev.fxcte.creepyware.util.RenderUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public class GuiCustomMainScreen extends GuiScreen
{
    private final String backgroundURL = "https://i.imgur.com/GCJRhiA.png";
    private final ResourceLocation resourceLocation;
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;
    
    public GuiCustomMainScreen() {
        this.resourceLocation = new ResourceLocation("textures/background.png");
    }
    
    public static void drawCompleteImage(final float posX, final float posY, final float width, final float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(width, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static boolean isHovered(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }
    
    public void func_73866_w_() {
        this.x = this.field_146294_l / 2;
        this.y = this.field_146295_m / 4 + 48;
        this.field_146292_n.add(new TextButton(0, this.x, this.y + 20, "Singleplayer"));
        this.field_146292_n.add(new TextButton(1, this.x, this.y + 44, "Multiplayer"));
        this.field_146292_n.add(new TextButton(2, this.x, this.y + 66, "Settings"));
        this.field_146292_n.add(new TextButton(2, this.x, this.y + 88, "Exit"));
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }
    
    public void func_73876_c() {
        super.func_73876_c();
    }
    
    public void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) {
        if (isHovered(this.x - CreepyWare.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, CreepyWare.textManager.getStringWidth("Singleplayer"), CreepyWare.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        }
        else if (isHovered(this.x - CreepyWare.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, CreepyWare.textManager.getStringWidth("Multiplayer"), CreepyWare.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        else if (isHovered(this.x - CreepyWare.textManager.getStringWidth("Settings") / 2, this.y + 66, CreepyWare.textManager.getStringWidth("Settings"), CreepyWare.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
        }
        else if (isHovered(this.x - CreepyWare.textManager.getStringWidth("Exit") / 2, this.y + 88, CreepyWare.textManager.getStringWidth("Exit"), CreepyWare.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_71400_g();
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.xOffset = -1.0f * ((mouseX - this.field_146294_l / 2.0f) / (this.field_146294_l / 32.0f));
        this.yOffset = -1.0f * ((mouseY - this.field_146295_m / 2.0f) / (this.field_146295_m / 18.0f));
        this.x = this.field_146294_l / 2;
        this.y = this.field_146295_m / 4 + 48;
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        this.field_146297_k.func_110434_K().func_110577_a(this.resourceLocation);
        drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, (float)(this.field_146294_l + 32), (float)(this.field_146295_m + 18));
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    public BufferedImage parseBackground(final BufferedImage background) {
        int width;
        int srcWidth;
        int srcHeight;
        int height;
        for (width = 1920, srcWidth = background.getWidth(), srcHeight = background.getHeight(), height = 1080; width < srcWidth || height < srcHeight; width *= 2, height *= 2) {}
        final BufferedImage imgNew = new BufferedImage(width, height, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    private static class TextButton extends GuiButton
    {
        public TextButton(final int buttonId, final int x, final int y, final String buttonText) {
            super(buttonId, x, y, CreepyWare.textManager.getStringWidth(buttonText), CreepyWare.textManager.getFontHeight(), buttonText);
        }
        
        public void func_191745_a(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
            if (this.field_146125_m) {
                this.field_146124_l = true;
                this.field_146123_n = (mouseX >= this.field_146128_h - CreepyWare.textManager.getStringWidth(this.field_146126_j) / 2.0f && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
                CreepyWare.textManager.drawStringWithShadow(this.field_146126_j, this.field_146128_h - CreepyWare.textManager.getStringWidth(this.field_146126_j) / 2.0f, (float)this.field_146129_i, Color.WHITE.getRGB());
                if (this.field_146123_n) {
                    RenderUtil.drawLine(this.field_146128_h - 1 - CreepyWare.textManager.getStringWidth(this.field_146126_j) / 2.0f, (float)(this.field_146129_i + 2 + CreepyWare.textManager.getFontHeight()), this.field_146128_h + CreepyWare.textManager.getStringWidth(this.field_146126_j) / 2.0f + 1.0f, (float)(this.field_146129_i + 2 + CreepyWare.textManager.getFontHeight()), 1.0f, Color.WHITE.getRGB());
                }
            }
        }
        
        public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
            return this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h - CreepyWare.textManager.getStringWidth(this.field_146126_j) / 2.0f && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
        }
    }
}
