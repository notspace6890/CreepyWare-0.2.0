// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.PotionUtils;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.item.ItemBow;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Quiver extends Module
{
    private final Setting<Integer> tickDelay;
    
    public Quiver() {
        super("Quiver", "Rotates and shoots yourself with good potion effects", Category.COMBAT, true, false, false);
        this.tickDelay = (Setting<Integer>)this.register(new Setting("TickDelay", (T)3, (T)0, (T)8));
    }
    
    @Override
    public void onUpdate() {
        if (Quiver.mc.field_71439_g != null) {
            if (Quiver.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && Quiver.mc.field_71439_g.func_184587_cr() && Quiver.mc.field_71439_g.func_184612_cw() >= this.tickDelay.getValue()) {
                Quiver.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(Quiver.mc.field_71439_g.field_71109_bG, -90.0f, Quiver.mc.field_71439_g.field_70122_E));
                Quiver.mc.field_71442_b.func_78766_c((EntityPlayer)Quiver.mc.field_71439_g);
            }
            final List<Integer> arrowSlots;
            if ((arrowSlots = InventoryUtil.getItemInventory(Items.field_185167_i)).get(0) == -1) {
                return;
            }
            int speedSlot = -1;
            int strengthSlot = -1;
            for (final Integer slot : arrowSlots) {
                if (PotionUtils.func_185191_c(Quiver.mc.field_71439_g.field_71071_by.func_70301_a((int)slot)).getRegistryName().func_110623_a().contains("swiftness")) {
                    speedSlot = slot;
                }
                else {
                    if (!Objects.requireNonNull(PotionUtils.func_185191_c(Quiver.mc.field_71439_g.field_71071_by.func_70301_a((int)slot)).getRegistryName()).func_110623_a().contains("strength")) {
                        continue;
                    }
                    strengthSlot = slot;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
    }
    
    private int findBow() {
        return InventoryUtil.getItemHotbar((Item)Items.field_151031_f);
    }
}
