// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.fxcte.creepyware.event.EventStage;

@Cancelable
public class StepEvent extends EventStage
{
    private final Entity entity;
    private float height;
    
    public StepEvent(final int stage, final Entity entity) {
        super(stage);
        this.entity = entity;
        this.height = entity.field_70138_W;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
}
