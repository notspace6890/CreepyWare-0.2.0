// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.features.Feature;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.MoveEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Sprint extends Module
{
    public Setting<Mode> mode;
    private static Sprint INSTANCE;
    
    public Sprint() {
        super("Sprint", "Modifies sprinting", Category.MOVEMENT, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.LEGIT, 0));
        this.setInstance();
    }
    
    private void setInstance() {
        Sprint.INSTANCE = this;
    }
    
    public static Sprint getInstance() {
        if (Sprint.INSTANCE == null) {
            Sprint.INSTANCE = new Sprint();
        }
        return Sprint.INSTANCE;
    }
    
    @SubscribeEvent
    public void onSprint(final MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (Sprint.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || Sprint.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f)) {
            event.setCanceled(true);
        }
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if ((!Sprint.mc.field_71474_y.field_74351_w.func_151470_d() && !Sprint.mc.field_71474_y.field_74368_y.func_151470_d() && !Sprint.mc.field_71474_y.field_74370_x.func_151470_d() && !Sprint.mc.field_71474_y.field_74366_z.func_151470_d()) || Sprint.mc.field_71439_g.func_70093_af() || Sprint.mc.field_71439_g.field_70123_F) {
                    break;
                }
                if (Sprint.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f) {
                    break;
                }
                Sprint.mc.field_71439_g.func_70031_b(true);
                break;
            }
            case LEGIT: {
                if (!Sprint.mc.field_71474_y.field_74351_w.func_151470_d() || Sprint.mc.field_71439_g.func_70093_af() || Sprint.mc.field_71439_g.func_184587_cr() || Sprint.mc.field_71439_g.field_70123_F || Sprint.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f) {
                    break;
                }
                if (Sprint.mc.field_71462_r != null) {
                    break;
                }
                Sprint.mc.field_71439_g.func_70031_b(true);
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.nullCheck()) {
            Sprint.mc.field_71439_g.func_70031_b(false);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Sprint.INSTANCE = new Sprint();
    }
    
    public enum Mode
    {
        LEGIT, 
        RAGE;
    }
}
