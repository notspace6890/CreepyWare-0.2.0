// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import dev.fxcte.creepyware.features.modules.client.HUD;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.render.NoRender;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.fxcte.creepyware.features.gui.custom.GuiCustomNewChat;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiIngame.class })
public class MixinGuiIngame extends Gui
{
    @Shadow
    @Final
    public GuiNewChat field_73840_e;
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void init(final Minecraft mcIn, final CallbackInfo ci) {
        this.field_73840_e = new GuiCustomNewChat(mcIn);
    }
    
    @Inject(method = { "renderPortal" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPortalHook(final float n, final ScaledResolution scaledResolution, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().portal.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPumpkinOverlayHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().pumpkin.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPotionEffectsHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (CreepyWare.moduleManager != null && !HUD.getInstance().potionIcons.getValue()) {
            info.cancel();
        }
    }
}
