// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class ModuleTools extends Module
{
    private static ModuleTools INSTANCE;
    public Setting<Notifier> notifier;
    public Setting<PopNotifier> popNotifier;
    
    public ModuleTools() {
        super("ModuleTools", "Change settings", Category.CLIENT, true, false, false);
        this.notifier = (Setting<Notifier>)this.register(new Setting("Speed", "ModuleNotifier", 0.0, 0.0, (T)Notifier.FUTURE, 0));
        this.popNotifier = (Setting<PopNotifier>)this.register(new Setting("Speed", "PopNotifier", 0.0, 0.0, (T)PopNotifier.FUTURE, 0));
        ModuleTools.INSTANCE = this;
    }
    
    public static ModuleTools getInstance() {
        if (ModuleTools.INSTANCE == null) {
            ModuleTools.INSTANCE = new ModuleTools();
        }
        return ModuleTools.INSTANCE;
    }
    
    public enum Notifier
    {
        PHOBOS, 
        FUTURE;
    }
    
    public enum PopNotifier
    {
        PHOBOS, 
        FUTURE, 
        NONE;
    }
}
