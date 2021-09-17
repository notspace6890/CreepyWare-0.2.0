// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class NoEntityTrace extends Module
{
    private static NoEntityTrace INSTANCE;
    public Setting<Boolean> pickaxe;
    public Setting<Boolean> crystal;
    public Setting<Boolean> gapple;
    
    public NoEntityTrace() {
        super("NoEntityTrace", "NoEntityTrace.", Category.MISC, false, false, false);
        this.pickaxe = (Setting<Boolean>)this.register(new Setting("Speed", "Pickaxe", 0.0, 0.0, (T)true, 0));
        this.crystal = (Setting<Boolean>)this.register(new Setting("Speed", "Crystal", 0.0, 0.0, (T)true, 0));
        this.gapple = (Setting<Boolean>)this.register(new Setting("Speed", "Gapple", 0.0, 0.0, (T)true, 0));
        this.setInstance();
    }
    
    public static NoEntityTrace getINSTANCE() {
        if (NoEntityTrace.INSTANCE == null) {
            NoEntityTrace.INSTANCE = new NoEntityTrace();
        }
        return NoEntityTrace.INSTANCE;
    }
    
    private void setInstance() {
        NoEntityTrace.INSTANCE = this;
    }
    
    static {
        NoEntityTrace.INSTANCE = new NoEntityTrace();
    }
}
