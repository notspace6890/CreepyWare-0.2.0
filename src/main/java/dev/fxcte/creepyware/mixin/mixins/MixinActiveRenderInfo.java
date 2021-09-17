// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.fxcte.creepyware.util.RenderUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ActiveRenderInfo.class })
public class MixinActiveRenderInfo
{
    @Inject(method = { "updateRenderInfo(Lnet/minecraft/entity/Entity;Z)V" }, at = { @At("HEAD") }, remap = false)
    private static void updateRenderInfo(final Entity entity, final boolean wtf, final CallbackInfo ci) {
        RenderUtil.updateModelViewProjectionMatrix();
    }
}
