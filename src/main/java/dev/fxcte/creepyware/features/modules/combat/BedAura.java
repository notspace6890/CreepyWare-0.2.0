// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import dev.fxcte.creepyware.util.BlockUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import java.util.Comparator;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class BedAura extends Module
{
    boolean moving;
    Setting<Double> range;
    Setting<Boolean> rotate;
    Setting<Boolean> dimensionCheck;
    Setting<Boolean> refill;
    
    public BedAura() {
        super("BedAura", "Fucked (Future)", Category.COMBAT, true, false, false);
        this.moving = false;
        this.range = (Setting<Double>)this.register(new Setting("Range", (T)4.5, (T)0.0, (T)10.0));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)true, 0));
        this.dimensionCheck = (Setting<Boolean>)this.register(new Setting("Speed", "DimensionCheck", 0.0, 0.0, (T)true, 0));
        this.refill = (Setting<Boolean>)this.register(new Setting("Speed", "RefillBed", 0.0, 0.0, (T)true, 0));
    }
    
    @Override
    public void onUpdate() {
        if (this.refill.getValue()) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(i) == ItemStack.field_190927_a) {
                    slot = i;
                    break;
                }
            }
            if (this.moving && slot != -1) {
                BedAura.mc.field_71442_b.func_187098_a(0, slot + 36, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.field_71439_g);
                this.moving = false;
                slot = -1;
            }
            if (slot != -1 && !(BedAura.mc.field_71462_r instanceof GuiContainer) && BedAura.mc.field_71439_g.field_71071_by.func_70445_o().func_190926_b()) {
                int t = -1;
                for (int j = 0; j < 45; ++j) {
                    if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b() == Items.field_151104_aV && j >= 9) {
                        t = j;
                        break;
                    }
                }
                if (t != -1) {
                    BedAura.mc.field_71442_b.func_187098_a(0, t, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.field_71439_g);
                    this.moving = true;
                }
            }
        }
        BedAura.mc.field_71441_e.field_147482_g.stream().filter(e -> e instanceof TileEntityBed).filter(e -> BedAura.mc.field_71439_g.func_70011_f((double)e.func_174877_v().func_177958_n(), (double)e.func_174877_v().func_177956_o(), (double)e.func_174877_v().func_177952_p()) <= this.range.getValue()).sorted(Comparator.comparing(e -> BedAura.mc.field_71439_g.func_70011_f((double)e.func_174877_v().func_177958_n(), (double)e.func_174877_v().func_177956_o(), (double)e.func_174877_v().func_177952_p()))).forEach(bed -> {
            if (!this.dimensionCheck.getValue() || BedAura.mc.field_71439_g.field_71093_bK != 0) {
                if (this.rotate.getValue()) {
                    BlockUtil.faceVectorPacketInstant(new Vec3d((Vec3i)bed.func_174877_v().func_177963_a(0.5, 0.5, 0.5)));
                }
                BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(bed.func_174877_v(), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        });
    }
}
