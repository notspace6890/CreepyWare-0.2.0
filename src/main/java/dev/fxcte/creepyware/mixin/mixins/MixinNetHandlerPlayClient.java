// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraftforge.fml.common.eventhandler.Event;
import dev.fxcte.creepyware.event.events.DeathEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.Util;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetHandlerPlayClient.class })
public class MixinNetHandlerPlayClient
{
    @Inject(method = { "handleEntityMetadata" }, at = { @At("RETURN") }, cancellable = true)
    private void handleEntityMetadataHook(final SPacketEntityMetadata packetIn, final CallbackInfo info) {
        final Entity entity;
        final EntityPlayer player;
        if (Util.mc.field_71441_e != null && (entity = Util.mc.field_71441_e.func_73045_a(packetIn.func_149375_d())) instanceof EntityPlayer && (player = (EntityPlayer)entity).func_110143_aJ() <= 0.0f) {
            MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
            if (CreepyWare.totemPopManager != null) {
                CreepyWare.totemPopManager.onDeath(player);
            }
        }
    }
}
