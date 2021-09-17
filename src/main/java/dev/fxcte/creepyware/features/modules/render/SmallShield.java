// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class SmallShield extends Module
{
    private static SmallShield INSTANCE;
    public Setting<Boolean> normalOffset;
    public Setting<Float> offset;
    public Setting<Float> offX;
    public Setting<Float> offY;
    public Setting<Float> mainX;
    public Setting<Float> mainY;
    
    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Category.RENDER, false, false, false);
        this.normalOffset = (Setting<Boolean>)this.register(new Setting("Speed", "OffNormal", 0.0, 0.0, (T)false, 0));
        this.offset = (Setting<Float>)this.register(new Setting("Offset", (T)0.7f, (T)0.0f, (T)1.0f, v -> this.normalOffset.getValue()));
        this.offX = (Setting<Float>)this.register(new Setting("OffX", (T)0.0f, (T)(-1.0f), (T)1.0f, v -> !this.normalOffset.getValue()));
        this.offY = (Setting<Float>)this.register(new Setting("OffY", (T)0.0f, (T)(-1.0f), (T)1.0f, v -> !this.normalOffset.getValue()));
        this.mainX = (Setting<Float>)this.register(new Setting("MainX", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.mainY = (Setting<Float>)this.register(new Setting("MainY", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.setInstance();
    }
    
    public static SmallShield getINSTANCE() {
        if (SmallShield.INSTANCE == null) {
            SmallShield.INSTANCE = new SmallShield();
        }
        return SmallShield.INSTANCE;
    }
    
    private void setInstance() {
        SmallShield.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.normalOffset.getValue()) {
            SmallShield.mc.field_71460_t.field_78516_c.field_187471_h = this.offset.getValue();
        }
    }
    
    static {
        SmallShield.INSTANCE = new SmallShield();
    }
}
