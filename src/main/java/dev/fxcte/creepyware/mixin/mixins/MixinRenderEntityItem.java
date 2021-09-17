// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.texture.TextureMap;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.fxcte.creepyware.features.modules.render.ItemPhysics;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import java.util.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.item.EntityItem;

@Mixin({ RenderEntityItem.class })
public abstract class MixinRenderEntityItem extends MixinRenderer<EntityItem>
{
    private final Minecraft mc;
    @Shadow
    @Final
    private RenderItem field_177080_a;
    @Shadow
    @Final
    private Random field_177079_e;
    private long tick;
    
    public MixinRenderEntityItem() {
        this.mc = Minecraft.func_71410_x();
    }
    
    @Shadow
    public abstract int func_177078_a(final ItemStack p0);
    
    @Shadow
    public abstract boolean shouldSpreadItems();
    
    @Shadow
    public abstract boolean shouldBob();
    
    @Shadow
    protected abstract ResourceLocation func_110775_a(final EntityItem p0);
    
    private double formPositive(final float rotationPitch) {
        return (rotationPitch > 0.0f) ? rotationPitch : ((double)(-rotationPitch));
    }
    
    @Overwrite
    private int func_177077_a(final EntityItem itemIn, final double x, final double y, final double z, final float p_177077_8_, final IBakedModel p_177077_9_) {
        if (ItemPhysics.INSTANCE.isEnabled()) {
            final ItemStack itemstack = itemIn.func_92059_d();
            itemstack.func_77973_b();
            final boolean flag = p_177077_9_.func_177555_b();
            final int i = this.func_177078_a(itemstack);
            final float f2 = 0.0f;
            GlStateManager.func_179109_b((float)x, (float)y + 0.0f + 0.1f, (float)z);
            float f3 = 0.0f;
            if (flag || (this.mc.func_175598_ae().field_78733_k != null && this.mc.func_175598_ae().field_78733_k.field_74347_j)) {
                GlStateManager.func_179114_b(f3, 0.0f, 1.0f, 0.0f);
            }
            if (!flag) {
                f3 = -0.0f * (i - 1) * 0.5f;
                final float f4 = -0.0f * (i - 1) * 0.5f;
                final float f5 = -0.046875f * (i - 1) * 0.5f;
                GlStateManager.func_179109_b(f3, f4, f5);
            }
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            return i;
        }
        final ItemStack itemstack = itemIn.func_92059_d();
        itemstack.func_77973_b();
        final boolean flag = p_177077_9_.func_177556_c();
        final int i = this.func_177078_a(itemstack);
        final float f6 = this.shouldBob() ? (MathHelper.func_76126_a((itemIn.func_174872_o() + p_177077_8_) / 10.0f + itemIn.field_70290_d) * 0.1f + 0.1f) : 0.0f;
        final float f7 = p_177077_9_.func_177552_f().func_181688_b(ItemCameraTransforms.TransformType.GROUND).field_178363_d.y;
        GlStateManager.func_179109_b((float)x, (float)y + f6 + 0.25f * f7, (float)z);
        if (flag || this.field_76990_c.field_78733_k != null) {
            final float f8 = ((itemIn.func_174872_o() + p_177077_8_) / 20.0f + itemIn.field_70290_d) * 57.295776f;
            GlStateManager.func_179114_b(f8, 0.0f, 1.0f, 0.0f);
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }
    
    @Overwrite
    public void func_76986_a(final EntityItem entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (ItemPhysics.INSTANCE.isEnabled()) {
            double rotation = (System.nanoTime() - this.tick) / 3000000.0;
            if (!this.mc.field_71415_G) {
                rotation = 0.0;
            }
            final ItemStack itemstack = entity.func_92059_d();
            itemstack.func_77973_b();
            this.field_177079_e.setSeed(187L);
            this.mc.func_175598_ae().field_78724_e.func_110577_a(TextureMap.field_110575_b);
            this.mc.func_175598_ae().field_78724_e.func_110581_b(TextureMap.field_110575_b).func_174936_b(false, false);
            GlStateManager.func_179091_B();
            GlStateManager.func_179092_a(516, 0.1f);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179094_E();
            final IBakedModel ibakedmodel = this.field_177080_a.func_175037_a().func_178089_a(itemstack);
            final int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);
            final BlockPos blockpos = new BlockPos((Entity)entity);
            if (entity.field_70125_A > 360.0f) {
                entity.field_70125_A = 0.0f;
            }
            if (!Double.isNaN(entity.field_70165_t) && !Double.isNaN(entity.field_70163_u) && !Double.isNaN(entity.field_70161_v) && entity.field_70170_p != null) {
                if (entity.field_70122_E) {
                    if (entity.field_70125_A != 0.0f && entity.field_70125_A != 90.0f && entity.field_70125_A != 180.0f && entity.field_70125_A != 270.0f) {
                        final double d0 = this.formPositive(entity.field_70125_A);
                        final double d2 = this.formPositive(entity.field_70125_A - 90.0f);
                        final double d3 = this.formPositive(entity.field_70125_A - 180.0f);
                        final double d4 = this.formPositive(entity.field_70125_A - 270.0f);
                        if (d0 <= d2 && d0 <= d3 && d0 <= d4) {
                            if (entity.field_70125_A < 0.0f) {
                                entity.field_70125_A += (float)rotation;
                            }
                            else {
                                entity.field_70125_A -= (float)rotation;
                            }
                        }
                        if (d2 < d0 && d2 <= d3 && d2 <= d4) {
                            if (entity.field_70125_A - 90.0f < 0.0f) {
                                entity.field_70125_A += (float)rotation;
                            }
                            else {
                                entity.field_70125_A -= (float)rotation;
                            }
                        }
                        if (d3 < d2 && d3 < d0 && d3 <= d4) {
                            if (entity.field_70125_A - 180.0f < 0.0f) {
                                entity.field_70125_A += (float)rotation;
                            }
                            else {
                                entity.field_70125_A -= (float)rotation;
                            }
                        }
                        if (d4 < d2 && d4 < d3 && d4 < d0) {
                            if (entity.field_70125_A - 270.0f < 0.0f) {
                                entity.field_70125_A += (float)rotation;
                            }
                            else {
                                entity.field_70125_A -= (float)rotation;
                            }
                        }
                    }
                }
                else {
                    final BlockPos blockpos2 = new BlockPos((Entity)entity);
                    blockpos2.func_177982_a(0, 1, 0);
                    final Material material = entity.field_70170_p.func_180495_p(blockpos2).func_185904_a();
                    final Material material2 = entity.field_70170_p.func_180495_p(blockpos).func_185904_a();
                    final boolean flag2 = entity.func_70055_a(Material.field_151586_h);
                    final boolean flag3 = entity.func_70090_H();
                    if (flag2 | material == Material.field_151586_h | material2 == Material.field_151586_h | flag3) {
                        entity.field_70125_A += (float)(rotation / 4.0);
                    }
                    else {
                        entity.field_70125_A += (float)(rotation * 2.0);
                    }
                }
            }
            GL11.glRotatef(entity.field_70177_z, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(entity.field_70125_A + 90.0f, 1.0f, 0.0f, 0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.func_177555_b()) {
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179152_a((float)ItemPhysics.INSTANCE.Scaling.getValue(), (float)ItemPhysics.INSTANCE.Scaling.getValue(), (float)ItemPhysics.INSTANCE.Scaling.getValue());
                    this.field_177080_a.func_180454_a(itemstack, ibakedmodel);
                    GlStateManager.func_179121_F();
                }
                else {
                    GlStateManager.func_179094_E();
                    if (j > 0 && this.shouldSpreadItems()) {
                        GlStateManager.func_179109_b(0.0f, 0.0f, 0.046875f * j);
                    }
                    this.field_177080_a.func_180454_a(itemstack, ibakedmodel);
                    if (!this.shouldSpreadItems()) {
                        GlStateManager.func_179109_b(0.0f, 0.0f, 0.046875f);
                    }
                    GlStateManager.func_179121_F();
                }
            }
            GlStateManager.func_179121_F();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            this.mc.func_175598_ae().field_78724_e.func_110577_a(TextureMap.field_110575_b);
            this.mc.func_175598_ae().field_78724_e.func_110581_b(TextureMap.field_110575_b).func_174935_a();
            return;
        }
        final ItemStack itemstack2 = entity.func_92059_d();
        final int k = itemstack2.func_190926_b() ? 187 : (Item.func_150891_b(itemstack2.func_77973_b()) + itemstack2.func_77960_j());
        this.field_177079_e.setSeed(k);
        boolean flag4 = false;
        if (this.func_180548_c(entity)) {
            this.field_76990_c.field_78724_e.func_110581_b(this.func_110775_a(entity)).func_174936_b(false, false);
            flag4 = true;
        }
        GlStateManager.func_179091_B();
        GlStateManager.func_179092_a(516, 0.1f);
        GlStateManager.func_179147_l();
        RenderHelper.func_74519_b();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179094_E();
        final IBakedModel ibakedmodel = this.field_177080_a.func_184393_a(itemstack2, entity.field_70170_p, (EntityLivingBase)null);
        final int l = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);
        final boolean flag5 = ibakedmodel.func_177556_c();
        if (!flag5) {
            final float f3 = -0.0f * (l - 1) * 0.5f;
            final float f4 = -0.0f * (l - 1) * 0.5f;
            final float f5 = -0.09375f * (l - 1) * 0.5f;
            GlStateManager.func_179109_b(f3, f4, f5);
        }
        if (this.field_188301_f) {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(entity));
        }
        for (int m = 0; m < l; ++m) {
            GlStateManager.func_179094_E();
            if (flag5) {
                if (m > 0) {
                    final float f6 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f7 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f8 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.func_179109_b(this.shouldSpreadItems() ? f6 : 0.0f, this.shouldSpreadItems() ? f7 : 0.0f, f8);
                }
                final IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.field_177080_a.func_180454_a(itemstack2, transformedModel);
                GlStateManager.func_179121_F();
            }
            else {
                if (m > 0) {
                    final float f9 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    final float f10 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    GlStateManager.func_179109_b(f9, f10, 0.0f);
                }
                final IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.field_177080_a.func_180454_a(itemstack2, transformedModel);
                GlStateManager.func_179121_F();
                GlStateManager.func_179109_b(0.0f, 0.0f, 0.09375f);
            }
        }
        if (this.field_188301_f) {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        this.func_180548_c(entity);
        if (flag4) {
            this.field_76990_c.field_78724_e.func_110581_b(this.func_110775_a(entity)).func_174935_a();
        }
    }
}
