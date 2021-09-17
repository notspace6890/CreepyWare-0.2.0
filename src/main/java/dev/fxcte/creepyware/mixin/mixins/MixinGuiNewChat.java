// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import net.minecraft.client.Minecraft;
import java.awt.Color;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.Colors;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.fxcte.creepyware.features.modules.misc.ChatModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiNewChat.class })
public class MixinGuiNewChat extends Gui
{
    @Shadow
    @Final
    public List<ChatLine> field_146253_i;
    private ChatLine chatLine;
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.func_73734_a(left, top, right, bottom, (ChatModifier.getInstance().isOn() && ChatModifier.getInstance().clean.getValue()) ? 0 : color);
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        if (text.contains("§+")) {
            final float colorSpeed = (float)(101 - Colors.INSTANCE.rainbowSpeed.getValue());
            CreepyWare.textManager.drawRainbowString(text, x, y, Color.HSBtoRGB(Colors.INSTANCE.hue, 1.0f, 1.0f), 100.0f, true);
        }
        else {
            Minecraft.func_71410_x().field_71466_p.func_175063_a(text, x, y, color);
        }
        return 0;
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (ChatModifier.getInstance().isOn() && ChatModifier.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
    public int chatLinesSize(final List<ChatLine> list) {
        return (ChatModifier.getInstance().isOn() && ChatModifier.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
}
