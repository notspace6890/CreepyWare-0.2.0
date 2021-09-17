// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import dev.fxcte.creepyware.event.EventStage;

public class KeyEvent extends EventStage
{
    public boolean info;
    public boolean pressed;
    
    public KeyEvent(final int stage, final boolean info, final boolean pressed) {
        super(stage);
        this.info = info;
        this.pressed = pressed;
    }
}
