// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import javax.annotation.Nullable;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static final Minecraft mc;
    
    @Nullable
    public static EntityPlayerSP getPlayer() {
        return Wrapper.mc.field_71439_g;
    }
    
    @Nullable
    public static WorldClient getWorld() {
        return Wrapper.mc.field_71441_e;
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.mc.field_71466_p;
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
