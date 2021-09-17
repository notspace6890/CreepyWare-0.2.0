// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import dev.fxcte.creepyware.features.modules.client.Media;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.fxcte.creepyware.features.modules.client.HUD;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.FontMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FontRenderer.class })
public abstract class MixinFontRenderer
{
    @Shadow
    protected abstract int func_180455_b(final String p0, final float p1, final float p2, final int p3, final boolean p4);
    
    @Shadow
    protected abstract void func_78255_a(final String p0, final boolean p1);
    
    @Inject(method = { "drawString(Ljava/lang/String;FFIZ)I" }, at = { @At("HEAD") }, cancellable = true)
    public void renderStringHook(final String text, final float x, final float y, final int color, final boolean dropShadow, final CallbackInfoReturnable<Integer> info) {
        if (FontMod.getInstance().isOn() && FontMod.getInstance().full.getValue() && CreepyWare.textManager != null) {
            final float result = CreepyWare.textManager.drawString(text, x, y, color, dropShadow);
            info.setReturnValue((int)result);
        }
    }
    
    @Redirect(method = { "drawString(Ljava/lang/String;FFIZ)I" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
    public int renderStringHook(final FontRenderer fontrenderer, final String text, final float x, final float y, final int color, final boolean dropShadow) {
        if (CreepyWare.moduleManager != null && HUD.getInstance().shadow.getValue() && dropShadow) {
            return this.func_180455_b(text, x - 0.5f, y - 0.5f, color, true);
        }
        return this.func_180455_b(text, x, y, color, dropShadow);
    }
    
    @Redirect(method = { "renderString(Ljava/lang/String;FFIZ)I" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public void renderStringAtPosHook(final FontRenderer renderer, final String text, final boolean shadow) {
        if (Media.getInstance().isOn() && Media.getInstance().changeOwn.getValue()) {
            this.func_78255_a(text.replace(Media.getPlayerName(), Media.getInstance().ownName.getValue()), shadow);
        }
        else {
            this.func_78255_a(text, shadow);
        }
    }
}
