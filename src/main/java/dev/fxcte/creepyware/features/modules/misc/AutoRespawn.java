// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class AutoRespawn extends Module
{
    public Setting<Boolean> antiDeathScreen;
    public Setting<Boolean> deathCoords;
    public Setting<Boolean> respawn;
    
    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Category.MISC, true, false, false);
        this.antiDeathScreen = (Setting<Boolean>)this.register(new Setting("Speed", "AntiDeathScreen", 0.0, 0.0, (T)true, 0));
        this.deathCoords = (Setting<Boolean>)this.register(new Setting("Speed", "DeathCoords", 0.0, 0.0, (T)false, 0));
        this.respawn = (Setting<Boolean>)this.register(new Setting("Speed", "Respawn", 0.0, 0.0, (T)true, 0));
    }
    
    @SubscribeEvent
    public void onDisplayDeathScreen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("You died at x %d y %d z %d", (int)AutoRespawn.mc.field_71439_g.field_70165_t, (int)AutoRespawn.mc.field_71439_g.field_70163_u, (int)AutoRespawn.mc.field_71439_g.field_70161_v));
            }
            if ((this.respawn.getValue() && AutoRespawn.mc.field_71439_g.func_110143_aJ() <= 0.0f) || (this.antiDeathScreen.getValue() && AutoRespawn.mc.field_71439_g.func_110143_aJ() > 0.0f)) {
                event.setCanceled(true);
                AutoRespawn.mc.field_71439_g.func_71004_bE();
            }
        }
    }
}
