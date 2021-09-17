// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import dev.fxcte.creepyware.util.Util;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Media extends Module
{
    private static Media instance;
    public final Setting<Boolean> changeOwn;
    public final Setting<String> ownName;
    
    public Media() {
        super("Media", "Helps with creating Media", Category.CLIENT, false, false, false);
        this.changeOwn = (Setting<Boolean>)this.register(new Setting("Speed", "MyName", 0.0, 0.0, (T)true, 0));
        this.ownName = (Setting<String>)this.register(new Setting("Name", (T)"Name here...", v -> this.changeOwn.getValue()));
        Media.instance = this;
    }
    
    public static Media getInstance() {
        if (Media.instance == null) {
            Media.instance = new Media();
        }
        return Media.instance;
    }
    
    public static String getPlayerName() {
        if (Feature.fullNullCheck() || !ServerModule.getInstance().isConnected()) {
            return Util.mc.func_110432_I().func_111285_a();
        }
        final String name = ServerModule.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return Util.mc.func_110432_I().func_111285_a();
        }
        return name;
    }
}
