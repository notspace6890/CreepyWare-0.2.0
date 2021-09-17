// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.fxcte.creepyware.event.events.BlockBreakingEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.fxcte.creepyware.features.modules.movement.Speed;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderGlobal.class })
public abstract class MixinRenderGlobal
{
    @Redirect(method = { "setupTerrain" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
    public void initializeHook(final ChunkRenderContainer chunkRenderContainer, final double viewEntityXIn, final double viewEntityYIn, final double viewEntityZIn) {
        double y = viewEntityYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        chunkRenderContainer.func_178004_a(viewEntityXIn, y, viewEntityZIn);
    }
    
    @Redirect(method = { "renderEntities" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
    public void setRenderPositionHook(final RenderManager renderManager, final double renderPosXIn, final double renderPosYIn, final double renderPosZIn) {
        double y = renderPosYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        renderManager.func_178628_a(renderPosXIn, TileEntityRendererDispatcher.field_147555_c = y, renderPosZIn);
    }
    
    @Redirect(method = { "drawSelectionBox" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
    public AxisAlignedBB offsetHook(final AxisAlignedBB axisAlignedBB, final double x, final double y, final double z) {
        double yIn = y;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            yIn = Speed.getInstance().startY;
        }
        return axisAlignedBB.func_72317_d(x, y, z);
    }
    
    @Inject(method = { "sendBlockBreakProgress" }, at = { @At("HEAD") })
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress, final CallbackInfo ci) {
        final BlockBreakingEvent event = new BlockBreakingEvent(pos, breakerId, progress);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}
