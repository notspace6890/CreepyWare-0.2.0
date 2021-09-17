// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import dev.fxcte.creepyware.event.EventStage;

public class PerspectiveEvent extends EventStage
{
    private float aspect;
    
    public PerspectiveEvent(final float aspect) {
        this.aspect = aspect;
    }
    
    public float getAspect() {
        return this.aspect;
    }
    
    public void setAspect(final float aspect) {
        this.aspect = aspect;
    }
}
