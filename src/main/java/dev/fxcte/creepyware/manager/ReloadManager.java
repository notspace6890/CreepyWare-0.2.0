// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraftforge.common.MinecraftForge;
import dev.fxcte.creepyware.features.Feature;

public class ReloadManager extends Feature
{
    public String prefix;
    
    public void init(final String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!Feature.fullNullCheck()) {
            Command.sendMessage("§cCreepyWare has been unloaded. Type " + prefix + "reload to reload.");
        }
    }
    
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = event.getPacket()).func_149439_c().startsWith(this.prefix) && packet.func_149439_c().contains("reload")) {
            CreepyWare.load();
            event.setCanceled(true);
        }
    }
}
