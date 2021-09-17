// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import dev.fxcte.creepyware.event.events.ValueChangeEvent;
import net.minecraftforge.common.MinecraftForge;
import dev.fxcte.creepyware.CreepyWare;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketChangeSetting implements Packet<INetHandlerPlayServer>
{
    public String setting;
    
    public CPacketChangeSetting(final String module, final String setting, final String value) {
        this.setting = setting + "-" + module + "-" + value;
    }
    
    public CPacketChangeSetting(final Module module, final Setting setting, final String value) {
        this.setting = setting.getName() + "-" + module.getName() + "-" + value;
    }
    
    public void func_148837_a(final PacketBuffer buf) throws IOException {
        this.setting = buf.func_150789_c(256);
    }
    
    public void func_148840_b(final PacketBuffer buf) throws IOException {
        buf.func_180714_a(this.setting);
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        final Module module = CreepyWare.moduleManager.getModuleByName(this.setting.split("-")[1]);
        final Setting setting1 = module.getSettingByName(this.setting.split("-")[0]);
        MinecraftForge.EVENT_BUS.post((Event)new ValueChangeEvent(setting1, this.setting.split("-")[2]));
    }
}
