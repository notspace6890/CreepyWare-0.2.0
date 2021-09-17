// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.event.events;

import net.minecraft.network.Packet;
import dev.fxcte.creepyware.event.EventStage;

public class EventNetworkPacketEvent extends EventStage
{
    public Packet m_Packet;
    
    public EventNetworkPacketEvent(final Packet p_Packet) {
        this.m_Packet = p_Packet;
    }
    
    public Packet GetPacket() {
        return this.m_Packet;
    }
    
    public Packet getPacket() {
        return this.m_Packet;
    }
}
