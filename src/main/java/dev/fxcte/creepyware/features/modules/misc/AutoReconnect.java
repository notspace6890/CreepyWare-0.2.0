// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.client.multiplayer.GuiConnecting;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.util.Util;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiOpenEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.client.multiplayer.ServerData;
import dev.fxcte.creepyware.features.modules.Module;

public class AutoReconnect extends Module
{
    private static ServerData serverData;
    private static AutoReconnect INSTANCE;
    private final Setting<Integer> delay;
    
    public AutoReconnect() {
        super("AutoReconnect", "Reconnects you if you disconnect.", Category.MISC, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Speed", "Delay", 0.0, 0.0, (T)5, 0));
        this.setInstance();
    }
    
    public static AutoReconnect getInstance() {
        if (AutoReconnect.INSTANCE == null) {
            AutoReconnect.INSTANCE = new AutoReconnect();
        }
        return AutoReconnect.INSTANCE;
    }
    
    private void setInstance() {
        AutoReconnect.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void sendPacket(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            this.updateLastConnectedServer();
            if (AutoLog.getInstance().isOff()) {
                final GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
                event.setGui((GuiScreen)new GuiDisconnectedHook(disconnected));
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        this.updateLastConnectedServer();
    }
    
    public void updateLastConnectedServer() {
        final ServerData data = Util.mc.func_147104_D();
        if (data != null) {
            AutoReconnect.serverData = data;
        }
    }
    
    static {
        AutoReconnect.INSTANCE = new AutoReconnect();
    }
    
    private class GuiDisconnectedHook extends GuiDisconnected
    {
        private final Timer timer;
        
        public GuiDisconnectedHook(final GuiDisconnected disconnected) {
            super(disconnected.field_146307_h, disconnected.field_146306_a, disconnected.field_146304_f);
            (this.timer = new Timer()).reset();
        }
        
        public void func_73876_c() {
            if (this.timer.passedS(AutoReconnect.this.delay.getValue())) {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiConnecting(this.field_146307_h, this.field_146297_k, (AutoReconnect.serverData == null) ? this.field_146297_k.field_71422_O : AutoReconnect.serverData));
            }
        }
        
        public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
            super.func_73863_a(mouseX, mouseY, partialTicks);
            final String s = "Reconnecting in " + MathUtil.round((AutoReconnect.this.delay.getValue() * 1000 - this.timer.getPassedTimeMs()) / 1000.0, 1);
            AutoReconnect.this.renderer.drawString(s, (float)(this.field_146294_l / 2 - AutoReconnect.this.renderer.getStringWidth(s) / 2), (float)(this.field_146295_m - 16), 16777215, true);
        }
    }
}
