// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import dev.fxcte.creepyware.features.modules.Module;

public class ReverseStep extends Module
{
    public ReverseStep() {
        super("ReverseStep", "Screams chinese words and teleports you", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            final EntityPlayerSP field_71439_g = ReverseStep.mc.field_71439_g;
            --field_71439_g.field_70181_x;
        }
    }
}
