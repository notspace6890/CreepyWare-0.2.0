// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class VanillaSpeed extends Module
{
    public Setting<Double> speed;
    
    public VanillaSpeed() {
        super("VanillaSpeed", "ec.me", Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)1.0, (T)1.0, (T)10.0));
    }
    
    @Override
    public void onUpdate() {
        if (VanillaSpeed.mc.field_71439_g == null || VanillaSpeed.mc.field_71441_e == null) {
            return;
        }
        final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        VanillaSpeed.mc.field_71439_g.field_70159_w = calc[0];
        VanillaSpeed.mc.field_71439_g.field_70179_y = calc[1];
    }
}
