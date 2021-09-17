// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.awt.Color;
import dev.fxcte.creepyware.util.RenderUtil;
import dev.fxcte.creepyware.features.modules.client.Colors;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import dev.fxcte.creepyware.features.modules.render.HandChams;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderRightArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderRightArmBegin(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.func_71410_x().field_71439_g && HandChams.INSTANCE.isEnabled()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0f, 240.0f);
            if (HandChams.INSTANCE.rainbow.getValue()) {
                final Color rainbowColor = HandChams.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(HandChams.INSTANCE.speed.getValue() * 100, 0, HandChams.INSTANCE.saturation.getValue() / 100.0f, HandChams.INSTANCE.brightness.getValue() / 100.0f));
                GL11.glColor4f(rainbowColor.getRed() / 255.0f, rainbowColor.getGreen() / 255.0f, rainbowColor.getBlue() / 255.0f, HandChams.INSTANCE.alpha.getValue() / 255.0f);
            }
            else {
                final Color color = HandChams.INSTANCE.colorSync.getValue() ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getBlue(), Colors.INSTANCE.getCurrentColor().getGreen(), HandChams.INSTANCE.alpha.getValue()) : new Color(HandChams.INSTANCE.red.getValue(), HandChams.INSTANCE.green.getValue(), HandChams.INSTANCE.blue.getValue(), HandChams.INSTANCE.alpha.getValue());
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            }
        }
    }
    
    @Inject(method = { "renderRightArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderRightArmReturn(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.func_71410_x().field_71439_g && HandChams.INSTANCE.isEnabled()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderLeftArmBegin(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.func_71410_x().field_71439_g && HandChams.INSTANCE.isEnabled()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0f, 240.0f);
            if (HandChams.INSTANCE.rainbow.getValue()) {
                final Color rainbowColor = HandChams.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(HandChams.INSTANCE.speed.getValue() * 100, 0, HandChams.INSTANCE.saturation.getValue() / 100.0f, HandChams.INSTANCE.brightness.getValue() / 100.0f));
                GL11.glColor4f(rainbowColor.getRed() / 255.0f, rainbowColor.getGreen() / 255.0f, rainbowColor.getBlue() / 255.0f, HandChams.INSTANCE.alpha.getValue() / 255.0f);
            }
            else {
                final Color color = HandChams.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(HandChams.INSTANCE.speed.getValue() * 100, 0, HandChams.INSTANCE.saturation.getValue() / 100.0f, HandChams.INSTANCE.brightness.getValue() / 100.0f));
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, HandChams.INSTANCE.alpha.getValue() / 255.0f);
            }
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderLeftArmReturn(final AbstractClientPlayer clientPlayer, final CallbackInfo ci) {
        if (clientPlayer == Minecraft.func_71410_x().field_71439_g && HandChams.INSTANCE.isEnabled()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
}
