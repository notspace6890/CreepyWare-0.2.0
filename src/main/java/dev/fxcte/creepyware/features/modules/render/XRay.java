// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.command.Command;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.event.events.ClientEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class XRay extends Module
{
    private static XRay INSTANCE;
    public Setting<String> newBlock;
    public Setting<Boolean> showBlocks;
    
    public XRay() {
        super("XRay", "Lets you look through walls.", Category.RENDER, false, false, true);
        this.newBlock = (Setting<String>)this.register(new Setting("Speed", "NewBlock", 0.0, 0.0, (T)"Add Block...", 0));
        this.showBlocks = (Setting<Boolean>)this.register(new Setting("Speed", "ShowBlocks", 0.0, 0.0, (T)false, 0));
        this.setInstance();
    }
    
    public static XRay getInstance() {
        if (XRay.INSTANCE == null) {
            XRay.INSTANCE = new XRay();
        }
        return XRay.INSTANCE;
    }
    
    private void setInstance() {
        XRay.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        XRay.mc.field_71438_f.func_72712_a();
    }
    
    @Override
    public void onDisable() {
        XRay.mc.field_71438_f.func_72712_a();
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (CreepyWare.configManager.loadingConfig || CreepyWare.configManager.savingConfig) {
            return;
        }
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.newBlock) && !this.shouldRender(this.newBlock.getPlannedValue())) {
                this.register(new Setting(this.newBlock.getPlannedValue(), (T)true, v -> this.showBlocks.getValue()));
                Command.sendMessage("<Xray> Added new Block: " + this.newBlock.getPlannedValue());
                if (this.isOn()) {
                    XRay.mc.field_71438_f.func_72712_a();
                }
                event.setCanceled(true);
            }
            else {
                final Setting setting = event.getSetting();
                if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newBlock) || setting.equals(this.showBlocks)) {
                    return;
                }
                if (setting.getValue() instanceof Boolean && !setting.getPlannedValue()) {
                    this.unregister(setting);
                    if (this.isOn()) {
                        XRay.mc.field_71438_f.func_72712_a();
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
    
    public boolean shouldRender(final Block block) {
        return this.shouldRender(block.func_149732_F());
    }
    
    public boolean shouldRender(final String name) {
        for (final Setting setting : this.getSettings()) {
            if (!name.equalsIgnoreCase(setting.getName())) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    static {
        XRay.INSTANCE = new XRay();
    }
}
