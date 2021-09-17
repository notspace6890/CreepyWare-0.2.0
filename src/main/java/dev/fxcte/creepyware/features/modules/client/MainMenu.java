// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class MainMenu extends Module
{
    public static MainMenu INSTANCE;
    public Setting<Boolean> mainScreen;
    
    public MainMenu() {
        super("MainMenu", "Controls custom screens used by the client", Category.CLIENT, true, false, false);
        this.mainScreen = (Setting<Boolean>)this.register(new Setting("MainScreen", (T)false));
        MainMenu.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
    }
}
