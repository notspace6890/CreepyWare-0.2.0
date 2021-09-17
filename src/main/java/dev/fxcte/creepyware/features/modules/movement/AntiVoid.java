// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class AntiVoid extends Module
{
    public Setting<Double> yLevel;
    public Setting<Double> yForce;
    
    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Category.MOVEMENT, false, false, false);
        this.yLevel = (Setting<Double>)this.register(new Setting("YLevel", (T)1.0, (T)0.1, (T)5.0));
        this.yForce = (Setting<Double>)this.register(new Setting("YMotion", (T)0.1, (T)0.0, (T)1.0));
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (!AntiVoid.mc.field_71439_g.field_70145_X && AntiVoid.mc.field_71439_g.field_70163_u <= this.yLevel.getValue()) {
            final RayTraceResult trace = AntiVoid.mc.field_71441_e.func_147447_a(AntiVoid.mc.field_71439_g.func_174791_d(), new Vec3d(AntiVoid.mc.field_71439_g.field_70165_t, 0.0, AntiVoid.mc.field_71439_g.field_70161_v), false, false, false);
            if (trace != null && trace.field_72313_a == RayTraceResult.Type.BLOCK) {
                return;
            }
            AntiVoid.mc.field_71439_g.field_70181_x = this.yForce.getValue();
            if (AntiVoid.mc.field_71439_g.func_184187_bx() != null) {
                AntiVoid.mc.field_71439_g.func_184187_bx().field_70181_x = this.yForce.getValue();
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.yLevel.getValue().toString() + ", " + this.yForce.getValue().toString();
    }
}
