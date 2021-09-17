// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import dev.fxcte.creepyware.event.events.Render3DEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.BlockBreakingEvent;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import dev.fxcte.creepyware.features.modules.Module;

public class BreakingESP extends Module
{
    private final Map<BlockPos, Integer> breakingProgressMap;
    
    public BreakingESP() {
        super("BreakingESP", "Shows block breaking progress", Category.RENDER, true, false, false);
        this.breakingProgressMap = new HashMap<BlockPos, Integer>();
    }
    
    @SubscribeEvent
    public void onBlockBreak(final BlockBreakingEvent event) {
        this.breakingProgressMap.put(event.pos, event.breakStage);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
    }
    
    public enum Mode
    {
        BAR, 
        ALPHA, 
        WIDTH;
    }
}
