// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import dev.fxcte.creepyware.util.Util;
import dev.fxcte.creepyware.features.Feature;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import dev.fxcte.creepyware.features.modules.Module;

public class EchestBP extends Module
{
    private GuiScreen echestScreen;
    
    public EchestBP() {
        super("EchestBP", "Allows to open your echest later.", Category.PLAYER, false, false, false);
        this.echestScreen = null;
    }
    
    @Override
    public void onUpdate() {
        final Container container;
        final InventoryBasic basic;
        if (EchestBP.mc.field_71462_r instanceof GuiContainer && (container = ((GuiContainer)EchestBP.mc.field_71462_r).field_147002_h) instanceof ContainerChest && ((ContainerChest)container).func_85151_d() instanceof InventoryBasic && (basic = (InventoryBasic)((ContainerChest)container).func_85151_d()).func_70005_c_().equalsIgnoreCase("Ender Chest")) {
            this.echestScreen = EchestBP.mc.field_71462_r;
            EchestBP.mc.field_71462_r = null;
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck() && this.echestScreen != null) {
            Util.mc.func_147108_a(this.echestScreen);
        }
        this.echestScreen = null;
    }
}
