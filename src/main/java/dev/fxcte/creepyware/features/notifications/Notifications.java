// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.notifications;

import dev.fxcte.creepyware.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.HUD;
import dev.fxcte.creepyware.util.Timer;

public class Notifications
{
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer;
    
    public Notifications(final String text, final long disableTime) {
        this.timer = new Timer();
        this.text = text;
        this.disableTime = disableTime;
        this.width = (float)CreepyWare.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }
    
    public void onDraw(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        if (this.timer.passedMs(this.disableTime)) {
            CreepyWare.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect(scaledResolution.func_78326_a() - 4 - this.width, (float)y, (float)(scaledResolution.func_78326_a() - 2), (float)(y + CreepyWare.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3), 1962934272);
        CreepyWare.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, scaledResolution.func_78326_a() - this.width - 3.0f, (float)(y + 2), -1, true);
    }
}
