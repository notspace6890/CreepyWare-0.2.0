// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.Map;
import dev.fxcte.creepyware.util.Util;

public class InventoryManager implements Util
{
    public Map<String, List<ItemStack>> inventories;
    private int recoverySlot;
    
    public InventoryManager() {
        this.inventories = new HashMap<String, List<ItemStack>>();
        this.recoverySlot = -1;
    }
    
    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange((this.recoverySlot == 8) ? 7 : (this.recoverySlot + 1)));
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot));
            InventoryManager.mc.field_71439_g.field_71071_by.field_70461_c = this.recoverySlot;
            InventoryManager.mc.field_71442_b.func_78750_j();
            this.recoverySlot = -1;
        }
    }
    
    public void recoverSilent(final int slot) {
        this.recoverySlot = slot;
    }
}
