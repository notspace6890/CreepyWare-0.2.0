// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import dev.fxcte.creepyware.DiscordPresence;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP;
    public Setting<Boolean> users;
    public Setting<String> largeImageText;
    public Setting<String> smallImageText;
    
    public RPC() {
        super("RPC", "Discord rich presence", Category.MISC, false, false, false);
        this.showIP = (Setting<Boolean>)this.register(new Setting("Speed", "IP", 0.0, 0.0, (T)false, 0));
        this.users = (Setting<Boolean>)this.register(new Setting("Speed", "Users", 0.0, 0.0, (T)false, 0));
        this.largeImageText = (Setting<String>)this.register(new Setting("LargeImageText", (T)"CreepyWare", "Sets the large image text of the DiscordRPC."));
        this.smallImageText = (Setting<String>)this.register(new Setting("SmallImageText", (T)"UwU", "Sets the small image text of the DiscordRPC."));
        RPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
