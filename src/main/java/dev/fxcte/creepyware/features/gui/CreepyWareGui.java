// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.gui;

import java.io.IOException;
import org.lwjgl.input.Mouse;
import dev.fxcte.creepyware.features.gui.components.items.Item;
import java.util.Iterator;
import dev.fxcte.creepyware.features.gui.components.items.buttons.Button;
import dev.fxcte.creepyware.features.gui.components.items.buttons.ModuleButton;
import dev.fxcte.creepyware.features.modules.Module;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.gui.components.Component;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class CreepyWareGui extends GuiScreen
{
    private static CreepyWareGui CreepyWareGui;
    private static CreepyWareGui INSTANCE;
    private final ArrayList<Component> components;
    
    public CreepyWareGui() {
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }
    
    public static CreepyWareGui getInstance() {
        if (dev.fxcte.creepyware.features.gui.CreepyWareGui.INSTANCE == null) {
            dev.fxcte.creepyware.features.gui.CreepyWareGui.INSTANCE = new CreepyWareGui();
        }
        return dev.fxcte.creepyware.features.gui.CreepyWareGui.INSTANCE;
    }
    
    public static CreepyWareGui getClickGui() {
        final CreepyWareGui creepyWareGui = dev.fxcte.creepyware.features.gui.CreepyWareGui.CreepyWareGui;
        return getInstance();
    }
    
    private void setInstance() {
        dev.fxcte.creepyware.features.gui.CreepyWareGui.INSTANCE = this;
    }
    
    private void load() {
        int x = -74;
        for (final Module.Category category : CreepyWare.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 4, true) {
                @Override
                public void setupItems() {
                    CreepyWare.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort((item1, item2) -> item1.getName().compareTo(item2.getName())));
    }
    
    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
                break;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.checkMouseWheel();
        this.func_146276_q_();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }
    
    public void func_73864_a(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void func_146286_b(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }
    
    public int getTextOffset() {
        return -6;
    }
    
    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }
    
    public void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    
    static {
        dev.fxcte.creepyware.features.gui.CreepyWareGui.INSTANCE = new CreepyWareGui();
    }
}
