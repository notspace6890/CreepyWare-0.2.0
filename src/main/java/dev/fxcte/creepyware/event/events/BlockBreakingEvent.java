// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import net.minecraft.util.math.BlockPos;
import dev.fxcte.creepyware.event.EventStage;

public class BlockBreakingEvent extends EventStage
{
    public BlockPos pos;
    public int breakingID;
    public int breakStage;
    
    public BlockBreakingEvent(final BlockPos pos, final int breakingID, final int breakStage) {
        this.pos = pos;
        this.breakingID = breakingID;
        this.breakStage = breakStage;
    }
}
