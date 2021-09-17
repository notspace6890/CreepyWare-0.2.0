// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.fxcte.creepyware.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.toasts.GuiToast;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiToast.class })
public class MixinGuiToast
{
    @Inject(method = { "drawToast" }, at = { @At("HEAD") }, cancellable = true)
    public void drawToastHook(final ScaledResolution resolution, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().advancements.getValue()) {
            info.cancel();
        }
    }
}
