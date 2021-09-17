// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.features.modules.Module;

public class StairSpeed extends Module
{
    public StairSpeed() {
        super("StairSpeed", "Great module", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (StairSpeed.mc.field_71439_g.field_70122_E && StairSpeed.mc.field_71439_g.field_70163_u - Math.floor(StairSpeed.mc.field_71439_g.field_70163_u) > 0.0 && StairSpeed.mc.field_71439_g.field_191988_bg != 0.0f) {
            StairSpeed.mc.field_71439_g.func_70664_aZ();
        }
    }
}
