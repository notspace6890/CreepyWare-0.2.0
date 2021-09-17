// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.gui.components.items.buttons;

import dev.fxcte.creepyware.features.setting.Bind;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import dev.fxcte.creepyware.features.gui.CreepyWareGui;
import dev.fxcte.creepyware.util.RenderUtil;
import dev.fxcte.creepyware.util.ColorUtil;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.features.modules.client.HUD;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import dev.fxcte.creepyware.features.setting.Setting;

public class BindButton extends Button
{
    public boolean isListening;
    private final Setting setting;
    
    public BindButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue()) {
            final int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), CreepyWare.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            final int color2 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), CreepyWare.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, this.width + 7.4f, this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? color : HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077), this.getState() ? (this.isHovering(mouseX, mouseY) ? color2 : HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        else {
            RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? CreepyWare.colorManager.getColorWithAlpha(((ClickGui)CreepyWare.moduleManager.getModuleByName("ClickGui")).alpha.getValue()) : CreepyWare.colorManager.getColorWithAlpha(((ClickGui)CreepyWare.moduleManager.getModuleByName("ClickGui")).hoverAlpha.getValue())) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        if (this.isListening) {
            CreepyWare.textManager.drawStringWithShadow("Listening...", this.x + 2.3f, this.y - 1.7f - CreepyWareGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
        else {
            CreepyWare.textManager.drawStringWithShadow(this.setting.getName() + " §7" + this.setting.getValue().toString(), this.x + 2.3f, this.y - 1.7f - CreepyWareGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            BindButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187682_dG, 1.0f));
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            super.onMouseClick();
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }
    
    @Override
    public boolean getState() {
        return !this.isListening;
    }
}
