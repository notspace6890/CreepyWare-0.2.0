// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.event.EventStage;

public class ValueChangeEvent extends EventStage
{
    public Setting setting;
    public Object value;
    
    public ValueChangeEvent(final Setting setting, final Object value) {
        this.setting = setting;
        this.value = value;
    }
}
