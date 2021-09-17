// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Anchor extends Module
{
    public static boolean Anchoring;
    private final Setting<Integer> pitch;
    private final Setting<Boolean> pull;
    int holeblocks;
    
    public Anchor() {
        super("Anchor", "For disabled people that can't move into holes on their own.", Category.MOVEMENT, false, false, false);
        this.pitch = (Setting<Integer>)this.register(new Setting("Pitch", (T)60, (T)0, (T)90));
        this.pull = (Setting<Boolean>)this.register(new Setting("Speed", "Pull", 0.0, 0.0, (T)true, 0));
    }
    
    public boolean isBlockHole(final BlockPos blockPos) {
        this.holeblocks = 0;
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 3, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 2, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        return this.holeblocks >= 9;
    }
    
    public Vec3d GetCenter(final double d, final double d2, final double d3) {
        final double d4 = Math.floor(d) + 0.5;
        final double d5 = Math.floor(d2);
        final double d6 = Math.floor(d3) + 0.5;
        return new Vec3d(d4, d5, d6);
    }
    
    @Subscribe
    @Override
    public void onUpdate() {
        if (Anchor.mc.field_71441_e == null) {
            return;
        }
        if (Anchor.mc.field_71439_g.field_70125_A >= this.pitch.getValue()) {
            if (this.isBlockHole(this.getPlayerPos().func_177979_c(1)) || this.isBlockHole(this.getPlayerPos().func_177979_c(2)) || this.isBlockHole(this.getPlayerPos().func_177979_c(3)) || this.isBlockHole(this.getPlayerPos().func_177979_c(4))) {
                Anchor.Anchoring = true;
                if (!this.pull.getValue()) {
                    Anchor.mc.field_71439_g.field_70159_w = 0.0;
                    Anchor.mc.field_71439_g.field_70179_y = 0.0;
                }
                else {
                    final Vec3d center = this.GetCenter(Anchor.mc.field_71439_g.field_70165_t, Anchor.mc.field_71439_g.field_70163_u, Anchor.mc.field_71439_g.field_70161_v);
                    final double d = Math.abs(center.field_72450_a - Anchor.mc.field_71439_g.field_70165_t);
                    final double d2 = Math.abs(center.field_72449_c - Anchor.mc.field_71439_g.field_70161_v);
                    if (d > 0.1 || d2 > 0.1) {
                        final double d3 = center.field_72450_a - Anchor.mc.field_71439_g.field_70165_t;
                        final double d4 = center.field_72449_c - Anchor.mc.field_71439_g.field_70161_v;
                        Anchor.mc.field_71439_g.field_70159_w = d3 / 2.0;
                        Anchor.mc.field_71439_g.field_70179_y = d4 / 2.0;
                    }
                }
            }
            else {
                Anchor.Anchoring = false;
            }
        }
    }
    
    @Override
    public void onDisable() {
        Anchor.Anchoring = false;
        this.holeblocks = 0;
    }
    
    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Anchor.mc.field_71439_g.field_70165_t), Math.floor(Anchor.mc.field_71439_g.field_70163_u), Math.floor(Anchor.mc.field_71439_g.field_70161_v));
    }
}
