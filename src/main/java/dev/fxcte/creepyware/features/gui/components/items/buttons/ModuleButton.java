// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.gui.components.items.buttons;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import dev.fxcte.creepyware.util.Util;
import dev.fxcte.creepyware.features.gui.CreepyWareGui;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import java.util.Iterator;
import dev.fxcte.creepyware.features.setting.Bind;
import dev.fxcte.creepyware.features.setting.Setting;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import dev.fxcte.creepyware.features.gui.components.items.Item;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import dev.fxcte.creepyware.features.modules.Module;

public class ModuleButton extends Button
{
    private final Module module;
    private final ResourceLocation logo;
    private List<Item> items;
    private boolean subOpen;
    
    public ModuleButton(final Module module) {
        super(module.getName());
        this.logo = new ResourceLocation("textures/gear.png");
        this.items = new ArrayList<Item>();
        this.module = module;
        this.initSettings();
    }
    
    public static void drawCompleteImage(final float posX, final float posY, final int width, final int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float)width, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float)width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public void initSettings() {
        final ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (final Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if (setting.getValue() instanceof String || setting.getValue() instanceof Character) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.isNumberSetting()) {
                    if (setting.hasRestriction()) {
                        newItems.add(new Slider(setting));
                        continue;
                    }
                    newItems.add(new UnlimitedSlider(setting));
                }
                if (!setting.isEnumSetting()) {
                    continue;
                }
                newItems.add(new EnumButton(setting));
            }
        }
        this.items = newItems;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            final ClickGui gui = CreepyWare.moduleManager.getModuleByClass(ClickGui.class);
            CreepyWare.textManager.drawStringWithShadow(((boolean)gui.openCloseChange.getValue()) ? (this.subOpen ? gui.close.getValue() : gui.open.getValue()) : gui.moduleButton.getValue(), this.x - 1.5f + this.width - 7.4f, this.y - 2.0f - CreepyWareGui.getClickGui().getTextOffset(), -1);
        }
        if (ClickGui.getInstance().gear.getValue()) {
            ModuleButton.mc.func_110434_K().func_110577_a(this.logo);
            drawCompleteImage(this.x - 1.5f + this.width - 7.4f, this.y - 2.2f - CreepyWareGui.getClickGui().getTextOffset(), 9, 9);
        }
        if (this.subOpen) {
            float height = 1.0f;
            for (final Item item : this.items) {
                if (!item.isHidden()) {
                    item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                    item.setHeight(15);
                    item.setWidth(this.width - 9);
                    item.drawScreen(mouseX, mouseY, partialTicks);
                }
                item.update();
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                Util.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187682_dG, 1.0f));
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (item.isHidden()) {
                        continue;
                    }
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    @Override
    public void toggle() {
        this.module.toggle();
    }
    
    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}
