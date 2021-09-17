// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class ItemPhysics extends Module
{
    public static ItemPhysics INSTANCE;
    public final Setting<Float> Scaling;
    
    public ItemPhysics() {
        super("ItemPhysics", "Apply physics to items.", Category.RENDER, false, false, false);
        this.Scaling = (Setting<Float>)this.register(new Setting("Scaling", (T)0.5f, (T)0.0f, (T)10.0f));
        this.setInstance();
    }
    
    public static ItemPhysics getInstance() {
        if (ItemPhysics.INSTANCE == null) {
            ItemPhysics.INSTANCE = new ItemPhysics();
        }
        return ItemPhysics.INSTANCE;
    }
    
    private void setInstance() {
        ItemPhysics.INSTANCE = this;
    }
    
    static {
        ItemPhysics.INSTANCE = new ItemPhysics();
    }
}
