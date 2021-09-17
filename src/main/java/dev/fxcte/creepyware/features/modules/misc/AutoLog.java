// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class AutoLog extends Module
{
    private static AutoLog INSTANCE;
    private final Setting<Float> health;
    private final Setting<Boolean> bed;
    private final Setting<Float> range;
    private final Setting<Boolean> logout;
    
    public AutoLog() {
        super("AutoLog", "Logs when in danger.", Category.MISC, false, false, false);
        this.health = (Setting<Float>)this.register(new Setting("Health", (T)16.0f, (T)0.1f, (T)36.0f));
        this.bed = (Setting<Boolean>)this.register(new Setting("Speed", "Beds", 0.0, 0.0, (T)true, 0));
        this.range = (Setting<Float>)this.register(new Setting("BedRange", (T)6.0f, (T)0.1f, (T)36.0f, v -> this.bed.getValue()));
        this.logout = (Setting<Boolean>)this.register(new Setting("Speed", "LogoutOff", 0.0, 0.0, (T)true, 0));
        this.setInstance();
    }
    
    public static AutoLog getInstance() {
        if (AutoLog.INSTANCE == null) {
            AutoLog.INSTANCE = new AutoLog();
        }
        return AutoLog.INSTANCE;
    }
    
    private void setInstance() {
        AutoLog.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (!Feature.nullCheck() && AutoLog.mc.field_71439_g.func_110143_aJ() <= this.health.getValue()) {
            CreepyWare.moduleManager.disableModule("AutoReconnect");
            AutoLog.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        final SPacketBlockChange packet;
        if (event.getPacket() instanceof SPacketBlockChange && this.bed.getValue() && (packet = event.getPacket()).func_180728_a().func_177230_c() == Blocks.field_150324_C && AutoLog.mc.field_71439_g.func_174831_c(packet.func_179827_b()) <= MathUtil.square(this.range.getValue())) {
            CreepyWare.moduleManager.disableModule("AutoReconnect");
            AutoLog.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
    
    static {
        AutoLog.INSTANCE = new AutoLog();
    }
}
