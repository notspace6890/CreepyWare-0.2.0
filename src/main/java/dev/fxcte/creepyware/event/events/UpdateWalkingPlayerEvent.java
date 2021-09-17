// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.fxcte.creepyware.event.EventStage;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage
{
    public UpdateWalkingPlayerEvent(final int stage) {
        super(stage);
    }
}
