// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.fxcte.creepyware.event.events.PushEvent;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.List;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.fxcte.creepyware.event.events.StepEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import net.minecraft.entity.MoverType;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Random;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public float field_70177_z;
    @Shadow
    public float field_70125_A;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public float field_70141_P;
    @Shadow
    public World field_70170_p;
    @Shadow
    @Final
    private double[] field_191505_aI;
    @Shadow
    private long field_191506_aJ;
    @Shadow
    protected boolean field_70134_J;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    private int field_190534_ay;
    @Shadow
    private int field_70150_b;
    @Shadow
    private float field_191959_ay;
    @Shadow
    protected Random field_70146_Z;
    
    @Shadow
    public abstract boolean func_70051_ag();
    
    @Shadow
    public abstract boolean func_184218_aH();
    
    @Shadow
    public abstract boolean func_70093_af();
    
    @Shadow
    public abstract void func_174826_a(final AxisAlignedBB p0);
    
    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();
    
    @Shadow
    public abstract void func_174829_m();
    
    @Shadow
    protected abstract void func_184231_a(final double p0, final boolean p1, final IBlockState p2, final BlockPos p3);
    
    @Shadow
    protected abstract boolean func_70041_e_();
    
    @Shadow
    public abstract boolean func_70090_H();
    
    @Shadow
    public abstract boolean func_184207_aI();
    
    @Shadow
    public abstract Entity func_184179_bs();
    
    @Shadow
    public abstract void func_184185_a(final SoundEvent p0, final float p1, final float p2);
    
    @Shadow
    protected abstract void func_145775_I();
    
    @Shadow
    public abstract boolean func_70026_G();
    
    @Shadow
    protected abstract void func_180429_a(final BlockPos p0, final Block p1);
    
    @Shadow
    protected abstract SoundEvent func_184184_Z();
    
    @Shadow
    protected abstract float func_191954_d(final float p0);
    
    @Shadow
    protected abstract boolean func_191957_ae();
    
    @Shadow
    public abstract void func_85029_a(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void func_70081_e(final int p0);
    
    @Shadow
    public abstract void func_70015_d(final int p0);
    
    @Shadow
    protected abstract int func_190531_bD();
    
    @Shadow
    public abstract boolean func_70027_ad();
    
    @Shadow
    public abstract int func_82145_z();
    
    @Overwrite
    public void func_70091_d(final MoverType type, double x, double y, double z) {
        final Entity _this = (Entity)this;
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, y, z));
            this.func_174829_m();
        }
        else {
            if (type == MoverType.PISTON) {
                final long i = this.field_70170_p.func_82737_E();
                if (i != this.field_191506_aJ) {
                    Arrays.fill(this.field_191505_aI, 0.0);
                    this.field_191506_aJ = i;
                }
                if (x != 0.0) {
                    final int j = EnumFacing.Axis.X.ordinal();
                    final double d0 = MathHelper.func_151237_a(x + this.field_191505_aI[j], -0.51, 0.51);
                    x = d0 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d0;
                    if (Math.abs(x) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else if (y != 0.0) {
                    final int l4 = EnumFacing.Axis.Y.ordinal();
                    final double d2 = MathHelper.func_151237_a(y + this.field_191505_aI[l4], -0.51, 0.51);
                    y = d2 - this.field_191505_aI[l4];
                    this.field_191505_aI[l4] = d2;
                    if (Math.abs(y) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else {
                    if (z == 0.0) {
                        return;
                    }
                    final int i2 = EnumFacing.Axis.Z.ordinal();
                    final double d3 = MathHelper.func_151237_a(z + this.field_191505_aI[i2], -0.51, 0.51);
                    z = d3 - this.field_191505_aI[i2];
                    this.field_191505_aI[i2] = d3;
                    if (Math.abs(z) <= 9.999999747378752E-6) {
                        return;
                    }
                }
            }
            this.field_70170_p.field_72984_F.func_76320_a("move");
            final double d4 = this.field_70165_t;
            final double d5 = this.field_70163_u;
            final double d6 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d7 = x;
            final double d8 = y;
            double d9 = z;
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.field_70122_E && this.func_70093_af() && _this instanceof EntityPlayer) {
                final double d10 = 0.05;
                while (x != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), 0.0)).isEmpty()) {
                    x = (d7 = ((x < 0.05 && x >= -0.05) ? 0.0 : ((x > 0.0) ? (x -= 0.05) : (x += 0.05))));
                }
                while (z != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(0.0, (double)(-this.field_70138_W), z)).isEmpty()) {
                    z = (d9 = ((z < 0.05 && z >= -0.05) ? 0.0 : ((z > 0.0) ? (z -= 0.05) : (z += 0.05))));
                }
                while (x != 0.0 && z != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), z)).isEmpty()) {
                    x = (d7 = ((x < 0.05 && x >= -0.05) ? 0.0 : ((x > 0.0) ? (x -= 0.05) : (x += 0.05))));
                    z = (d9 = ((z < 0.05 && z >= -0.05) ? 0.0 : ((z > 0.0) ? (z -= 0.05) : (z += 0.05))));
                }
            }
            final List list1 = this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72321_a(x, y, z));
            final AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            if (y != 0.0) {
                for (int k = list1.size(), m = 0; m < k; ++m) {
                    y = list1.get(m).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            }
            if (x != 0.0) {
                for (int l5 = list1.size(), j2 = 0; j2 < l5; ++j2) {
                    x = list1.get(j2).func_72316_a(this.func_174813_aQ(), x);
                }
                if (x != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
                }
            }
            if (z != 0.0) {
                for (int i3 = list1.size(), k2 = 0; k2 < i3; ++k2) {
                    z = list1.get(k2).func_72322_c(this.func_174813_aQ(), z);
                }
                if (z != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
                }
            }
            final boolean bl;
            final boolean flag = bl = (this.field_70122_E || (d8 != y && d8 < 0.0));
            if (this.field_70138_W > 0.0f && flag && (d7 != x || d9 != z)) {
                final StepEvent preEvent = new StepEvent(0, _this);
                MinecraftForge.EVENT_BUS.post((Event)preEvent);
                final double d11 = x;
                final double d12 = y;
                final double d13 = z;
                final AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                y = preEvent.getHeight();
                final List list2 = this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72321_a(d7, y, d9));
                AxisAlignedBB axisalignedbb3 = this.func_174813_aQ();
                final AxisAlignedBB axisalignedbb4 = axisalignedbb3.func_72321_a(d7, 0.0, d9);
                double d14 = y;
                for (int k3 = list2.size(), j3 = 0; j3 < k3; ++j3) {
                    d14 = list2.get(j3).func_72323_b(axisalignedbb4, d14);
                }
                axisalignedbb3 = axisalignedbb3.func_72317_d(0.0, d14, 0.0);
                double d15 = d7;
                for (int i4 = list2.size(), l6 = 0; l6 < i4; ++l6) {
                    d15 = list2.get(l6).func_72316_a(axisalignedbb3, d15);
                }
                axisalignedbb3 = axisalignedbb3.func_72317_d(d15, 0.0, 0.0);
                double d16 = d9;
                for (int k4 = list2.size(), j4 = 0; j4 < k4; ++j4) {
                    d16 = list2.get(j4).func_72322_c(axisalignedbb3, d16);
                }
                axisalignedbb3 = axisalignedbb3.func_72317_d(0.0, 0.0, d16);
                AxisAlignedBB axisalignedbb5 = this.func_174813_aQ();
                double d17 = y;
                for (int i5 = list2.size(), l7 = 0; l7 < i5; ++l7) {
                    d17 = list2.get(l7).func_72323_b(axisalignedbb5, d17);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(0.0, d17, 0.0);
                double d18 = d7;
                for (int k5 = list2.size(), j5 = 0; j5 < k5; ++j5) {
                    d18 = list2.get(j5).func_72316_a(axisalignedbb5, d18);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(d18, 0.0, 0.0);
                double d19 = d9;
                for (int i6 = list2.size(), l8 = 0; l8 < i6; ++l8) {
                    d19 = list2.get(l8).func_72322_c(axisalignedbb5, d19);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(0.0, 0.0, d19);
                final double d20 = d15 * d15 + d16 * d16;
                final double d21 = d18 * d18 + d19 * d19;
                if (d20 > d21) {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.func_174826_a(axisalignedbb3);
                }
                else {
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.func_174826_a(axisalignedbb5);
                }
                for (int k6 = list2.size(), j6 = 0; j6 < k6; ++j6) {
                    y = list2.get(j6).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
                if (d11 * d11 + d13 * d13 >= x * x + z * z) {
                    x = d11;
                    y = d12;
                    z = d13;
                    this.func_174826_a(axisalignedbb2);
                }
                else {
                    final StepEvent postEvent = new StepEvent(1, _this);
                    MinecraftForge.EVENT_BUS.post((Event)postEvent);
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = (d7 != x || d9 != z);
            this.field_70124_G = (d8 != y);
            this.field_70122_E = (this.field_70124_G && d8 < 0.0);
            this.field_70132_H = (this.field_70123_F || this.field_70124_G);
            final int j7 = MathHelper.func_76128_c(this.field_70165_t);
            final int i7 = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224);
            final int k7 = MathHelper.func_76128_c(this.field_70161_v);
            BlockPos blockpos = new BlockPos(j7, i7, k7);
            IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
            final BlockPos blockpos2;
            final IBlockState iblockstate2;
            final Block block1;
            if (iblockstate.func_185904_a() == Material.field_151579_a && ((block1 = (iblockstate2 = this.field_70170_p.func_180495_p(blockpos2 = blockpos.func_177977_b())).func_177230_c()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
                iblockstate = iblockstate2;
                blockpos = blockpos2;
            }
            this.func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
            if (d7 != x) {
                this.field_70159_w = 0.0;
            }
            if (d9 != z) {
                this.field_70179_y = 0.0;
            }
            final Block block2 = iblockstate.func_177230_c();
            if (d8 != y) {
                block2.func_176216_a(this.field_70170_p, _this);
            }
            if (this.func_70041_e_() && (!this.field_70122_E || !this.func_70093_af() || !(_this instanceof EntityPlayer)) && !this.func_184218_aH()) {
                final double d22 = this.field_70165_t - d4;
                double d23 = this.field_70163_u - d5;
                final double d24 = this.field_70161_v - d6;
                if (block2 != Blocks.field_150468_ap) {
                    d23 = 0.0;
                }
                if (block2 != null && this.field_70122_E) {
                    block2.func_176199_a(this.field_70170_p, blockpos, _this);
                }
                this.field_70140_Q += (float)(MathHelper.func_76133_a(d22 * d22 + d24 * d24) * 0.6);
                this.field_82151_R += (float)(MathHelper.func_76133_a(d22 * d22 + d23 * d23 + d24 * d24) * 0.6);
                if (this.field_82151_R > this.field_70150_b && iblockstate.func_185904_a() != Material.field_151579_a) {
                    this.field_70150_b = (int)this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        final Entity entity = (this.func_184207_aI() && this.func_184179_bs() != null) ? this.func_184179_bs() : _this;
                        final float f = (entity == _this) ? 0.35f : 0.4f;
                        float f2 = MathHelper.func_76133_a(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224 + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224) * f;
                        if (f2 > 1.0f) {
                            f2 = 1.0f;
                        }
                        this.func_184185_a(this.func_184184_Z(), f2, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    }
                    else {
                        this.func_180429_a(blockpos, block2);
                    }
                }
                else if (this.field_82151_R > this.field_191959_ay && this.func_191957_ae() && iblockstate.func_185904_a() == Material.field_151579_a) {
                    this.field_191959_ay = this.func_191954_d(this.field_82151_R);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag2 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_186664_h(0.001))) {
                this.func_70081_e(1);
                if (!flag2) {
                    ++this.field_190534_ay;
                    if (this.field_190534_ay == 0) {
                        this.func_70015_d(8);
                    }
                }
            }
            else if (this.field_190534_ay <= 0) {
                this.field_190534_ay = -this.func_190531_bD();
            }
            if (flag2 && this.func_70027_ad()) {
                this.func_184185_a(SoundEvents.field_187541_bC, 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.field_190534_ay = -this.func_190531_bD();
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }
    
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(final Entity entity, final double x, final double y, final double z) {
        final PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            entity.field_70159_w += event.x;
            entity.field_70181_x += event.y;
            entity.field_70179_y += event.z;
            entity.field_70160_al = event.airbone;
        }
    }
}
