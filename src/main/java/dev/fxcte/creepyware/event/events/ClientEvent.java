// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.Feature;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.fxcte.creepyware.event.EventStage;

@Cancelable
public class ClientEvent extends EventStage
{
    private Feature feature;
    private Setting setting;
    
    public ClientEvent(final int stage, final Feature feature) {
        super(stage);
        this.feature = feature;
    }
    
    public ClientEvent(final Setting setting) {
        super(2);
        this.setting = setting;
    }
    
    public Feature getFeature() {
        return this.feature;
    }
    
    public Setting getSetting() {
        return this.setting;
    }
}
