// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixins.accessor;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public interface IMinecraft
{
    @Accessor("session")
    void setSession(final Session p0);
    
    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(final int p0);
    
    @Accessor("timer")
    Timer getTimer();
}
