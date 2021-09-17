// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import java.util.Random;
import dev.fxcte.creepyware.features.modules.Module;

public class FxcteSexDupe extends Module
{
    private final Random random;
    
    public FxcteSexDupe() {
        super("FxcteSexDupe", "Hacker shit", Category.MISC, true, false, false);
        this.random = new Random();
    }
    
    @Override
    public void onEnable() {
        final EntityPlayerSP player = FxcteSexDupe.mc.field_71439_g;
        final WorldClient world = FxcteSexDupe.mc.field_71441_e;
        if (player == null || FxcteSexDupe.mc.field_71441_e == null) {
            return;
        }
        final ItemStack itemStack = player.func_184614_ca();
        if (itemStack.func_190926_b()) {
            this.setDisabledMessage("You need an object in your hand to dupe");
            this.disable();
            return;
        }
        final int count = this.random.nextInt(31) + 1;
        for (int i = 0; i <= count; ++i) {
            final EntityItem entityItem = player.func_146097_a(itemStack.func_77946_l(), false, true);
            if (entityItem != null) {
                world.func_73027_a(entityItem.field_145783_c, (Entity)entityItem);
            }
        }
        final int total = count * itemStack.func_190916_E();
        player.func_71165_d("I'm cum inside Fxcte and i got " + total + " " + itemStack.func_82833_r() + " thanks to creepyware");
        this.disable();
    }
    
    private void setDisabledMessage(final String s) {
    }
}
