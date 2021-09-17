// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TitleUtils
{
    int ticks;
    int bruh;
    int breakTimer;
    String bruh1;
    boolean qwerty;
    
    public TitleUtils() {
        this.ticks = 0;
        this.bruh = 0;
        this.breakTimer = 0;
        this.bruh1 = "Cr33pyWare | b0.1.8";
        this.qwerty = false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        ++this.ticks;
        if (this.ticks % 17 == 0) {
            Display.setTitle(this.bruh1.substring(0, this.bruh1.length() - this.bruh));
            if ((this.bruh == this.bruh1.length() && this.breakTimer != 0) || (this.bruh == 0 && this.breakTimer != 0)) {
                ++this.breakTimer;
                return;
            }
            this.breakTimer = 0;
            if (this.bruh == this.bruh1.length()) {
                this.qwerty = true;
            }
            this.bruh = (this.qwerty ? (--this.bruh) : (++this.bruh));
            if (this.bruh == 0) {
                this.qwerty = false;
            }
        }
    }
}
