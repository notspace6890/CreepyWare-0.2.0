// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.util.EntityUtil;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import dev.fxcte.creepyware.features.modules.Module;

public class BowAim extends Module
{
    public BowAim() {
        super("BowAim", "Automatically aim ur bow at ppl because lazy.", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (BowAim.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow && BowAim.mc.field_71439_g.func_184587_cr() && BowAim.mc.field_71439_g.func_184612_cw() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100.0f;
            for (final EntityPlayer p : BowAim.mc.field_71441_e.field_73010_i) {
                if (!(p instanceof EntityPlayerSP)) {
                    if (CreepyWare.friendManager.isFriend(p.func_70005_c_())) {
                        continue;
                    }
                    final float dis = p.func_70032_d((Entity)BowAim.mc.field_71439_g);
                    if (dis >= tickDis) {
                        continue;
                    }
                    tickDis = dis;
                    player = p;
                }
            }
            if (player != null) {
                final Vec3d pos = EntityUtil.getInterpolatedPos((Entity)player, BowAim.mc.func_184121_ak());
                final float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos((Entity)BowAim.mc.field_71439_g, BowAim.mc.func_184121_ak()), pos);
                BowAim.mc.field_71439_g.field_70177_z = angels[0];
                BowAim.mc.field_71439_g.field_70125_A = angels[1];
            }
        }
    }
}
