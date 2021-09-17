// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;
import dev.fxcte.creepyware.features.gui.CreepyWareGui;
import dev.fxcte.creepyware.util.Util;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.command.Command;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.event.events.ClientEvent;
import net.minecraft.client.settings.GameSettings;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class ClickGui extends Module
{
    private static ClickGui INSTANCE;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> outline;
    public Setting<Boolean> gear;
    public Setting<Boolean> blurEffect;
    public Setting<Boolean> rainbowRolling;
    public Setting<String> prefix;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> hoverAlpha;
    public Setting<Integer> alpha;
    public Setting<Boolean> customFov;
    public Setting<Float> fov;
    public Setting<Boolean> openCloseChange;
    public Setting<String> open;
    public Setting<String> close;
    public Setting<String> moduleButton;
    public Setting<Boolean> devSettings;
    public Setting<Integer> topRed;
    public Setting<Integer> topGreen;
    public Setting<Integer> topBlue;
    public Setting<Integer> topAlpha;
    
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT, true, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Speed", "Sync", 0.0, 0.0, (T)false, 0));
        this.outline = (Setting<Boolean>)this.register(new Setting("Speed", "Outline", 0.0, 0.0, (T)false, 0));
        this.gear = (Setting<Boolean>)this.register(new Setting("gear", (T)true, "draws gear like future"));
        this.blurEffect = (Setting<Boolean>)this.register(new Setting("Speed", "Blur", 0.0, 0.0, (T)true, 0));
        this.rainbowRolling = (Setting<Boolean>)this.register(new Setting("RollingRainbow", (T)false, v -> this.colorSync.getValue() && Colors.INSTANCE.rainbow.getValue()));
        this.prefix = (Setting<String>)this.register(new Setting<String>("Speed", "Prefix", 0.0, 0.0, ".", 0).setRenderName(true));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)170, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.hoverAlpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)180, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("HoverAlpha", (T)240, (T)0, (T)255));
        this.customFov = (Setting<Boolean>)this.register(new Setting("Speed", "CustomFov", 0.0, 0.0, (T)false, 0));
        this.fov = (Setting<Float>)this.register(new Setting("Fov", (T)150.0f, (T)(-180.0f), (T)180.0f, v -> this.customFov.getValue()));
        this.openCloseChange = (Setting<Boolean>)this.register(new Setting("Speed", "Open/Close", 0.0, 0.0, (T)false, 0));
        this.open = (Setting<String>)this.register(new Setting<Object>("Open:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
        this.close = (Setting<String>)this.register(new Setting<Object>("Close:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
        this.moduleButton = (Setting<String>)this.register(new Setting<Object>("Buttons:", "", v -> !this.openCloseChange.getValue()).setRenderName(true));
        this.devSettings = (Setting<Boolean>)this.register(new Setting("Speed", "TopSetting", 0.0, 0.0, (T)true, 0));
        this.topRed = (Setting<Integer>)this.register(new Setting("TopRed", (T)80, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topGreen = (Setting<Integer>)this.register(new Setting("TopGreen", (T)0, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topBlue = (Setting<Integer>)this.register(new Setting("TopBlue", (T)185, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.topAlpha = (Setting<Integer>)this.register(new Setting("TopAlpha", (T)255, (T)0, (T)255, v -> this.devSettings.getValue()));
        this.setInstance();
    }
    
    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }
        return ClickGui.INSTANCE;
    }
    
    private void setInstance() {
        ClickGui.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) {
            ClickGui.mc.field_71474_y.func_74304_a(GameSettings.Options.FOV, (float)this.fov.getValue());
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                CreepyWare.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to §a" + CreepyWare.commandManager.getPrefix());
            }
            CreepyWare.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }
    
    @Override
    public void onEnable() {
        Util.mc.func_147108_a((GuiScreen)new CreepyWareGui());
        if (this.blurEffect.getValue()) {
            ClickGui.mc.field_71460_t.func_175069_a(new ResourceLocation("-"));
        }
    }
    
    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            CreepyWare.colorManager.setColor(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), this.hoverAlpha.getValue());
        }
        else {
            CreepyWare.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
        CreepyWare.commandManager.setPrefix(this.prefix.getValue());
    }
    
    @Override
    public void onTick() {
        if (!(ClickGui.mc.field_71462_r instanceof CreepyWareGui)) {
            this.disable();
        }
    }
    
    @Override
    public void onDisable() {
        if (ClickGui.mc.field_71462_r instanceof CreepyWareGui) {
            Util.mc.func_147108_a((GuiScreen)null);
        }
    }
    
    static {
        ClickGui.INSTANCE = new ClickGui();
    }
}
