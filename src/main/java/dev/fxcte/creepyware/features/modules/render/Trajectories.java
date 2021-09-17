// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import org.lwjgl.util.glu.Cylinder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import dev.fxcte.creepyware.features.modules.Module;

public class Trajectories extends Module
{
    public Trajectories() {
        super("Trajectories", "Shows the way of projectiles.", Category.RENDER, false, false, false);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (Trajectories.mc.field_71441_e == null || Trajectories.mc.field_71439_g == null) {
            return;
        }
        this.drawTrajectories((EntityPlayer)Trajectories.mc.field_71439_g, event.getPartialTicks());
    }
    
    public void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Trajectories.mc.field_71460_t.func_175072_h();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }
    
    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    private void drawTrajectories(final EntityPlayer player, final float partialTicks) {
        final double renderPosX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * partialTicks;
        final double renderPosY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * partialTicks;
        final double renderPosZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * partialTicks;
        player.func_184586_b(EnumHand.MAIN_HAND);
        if (Trajectories.mc.field_71474_y.field_74320_O != 0 || (!(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBow) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemFishingRod) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemEnderPearl) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemEgg) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemSnowball) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemExpBottle))) {
            return;
        }
        GL11.glPushMatrix();
        final Item item = player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
        double posX = renderPosX - MathHelper.func_76134_b(player.field_70177_z / 180.0f * 3.1415927f) * 0.16f;
        double posY = renderPosY + player.func_70047_e() - 0.1000000014901161;
        double posZ = renderPosZ - MathHelper.func_76126_a(player.field_70177_z / 180.0f * 3.1415927f) * 0.16f;
        double motionX = -MathHelper.func_76126_a(player.field_70177_z / 180.0f * 3.1415927f) * MathHelper.func_76134_b(player.field_70125_A / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionY = -MathHelper.func_76126_a(player.field_70125_A / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionZ = MathHelper.func_76134_b(player.field_70177_z / 180.0f * 3.1415927f) * MathHelper.func_76134_b(player.field_70125_A / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        final int var6 = 72000 - player.func_184605_cv();
        float power = var6 / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        final float distance = MathHelper.func_76133_a(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        final float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.9f : 1.0f));
        motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75f : 1.5f));
        motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75f : 1.5f));
        motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75f : 1.5f));
        this.enableGL3D(2.0f);
        if (power > 0.6f) {
            GlStateManager.func_179131_c(0.0f, 1.0f, 0.0f, 1.0f);
        }
        else {
            GlStateManager.func_179131_c(0.8f, 0.5f, 0.0f, 1.0f);
        }
        GL11.glEnable(2848);
        final float size = (float)((item instanceof ItemBow) ? 0.3 : 0.25);
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        RayTraceResult landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            final Vec3d present = new Vec3d(posX, posY, posZ);
            final Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            final RayTraceResult possibleLandingStrip = Trajectories.mc.field_71441_e.func_147447_a(present, future, false, true, false);
            if (possibleLandingStrip != null && possibleLandingStrip.field_72313_a != RayTraceResult.Type.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }
            final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
            final List entities = this.getEntitiesWithinAABB(arrowBox.func_72317_d(motionX, motionY, motionZ).func_72321_a(1.0, 1.0, 1.0));
            for (final Object entity : entities) {
                final Entity boundingBox = (Entity)entity;
                if (boundingBox.func_70067_L()) {
                    if (boundingBox == player) {
                        continue;
                    }
                    final float var7 = 0.3f;
                    final AxisAlignedBB var8 = boundingBox.func_174813_aQ().func_72321_a((double)var7, (double)var7, (double)var7);
                    final RayTraceResult possibleEntityLanding = var8.func_72327_a(present, future);
                    if (possibleEntityLanding == null) {
                        continue;
                    }
                    hasLanded = true;
                    landingOnEntity = boundingBox;
                    landingPosition = possibleEntityLanding;
                }
            }
            if (landingOnEntity != null) {
                GlStateManager.func_179131_c(1.0f, 0.0f, 0.0f, 1.0f);
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            final float motionAdjustment = 0.99f;
            motionX *= motionAdjustment;
            motionY *= motionAdjustment;
            motionZ *= motionAdjustment;
            motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
        }
        if (landingPosition != null && landingPosition.field_72313_a == RayTraceResult.Type.BLOCK) {
            GlStateManager.func_179137_b(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
            final int side = landingPosition.field_178784_b.func_176745_a();
            if (side == 2) {
                GlStateManager.func_179114_b(90.0f, 1.0f, 0.0f, 0.0f);
            }
            else if (side == 3) {
                GlStateManager.func_179114_b(90.0f, 1.0f, 0.0f, 0.0f);
            }
            else if (side == 4) {
                GlStateManager.func_179114_b(90.0f, 0.0f, 0.0f, 1.0f);
            }
            else if (side == 5) {
                GlStateManager.func_179114_b(90.0f, 0.0f, 0.0f, 1.0f);
            }
            final Cylinder c = new Cylinder();
            GlStateManager.func_179114_b(-90.0f, 1.0f, 0.0f, 0.0f);
            c.setDrawStyle(100011);
            if (landingOnEntity != null) {
                GlStateManager.func_179131_c(0.0f, 0.0f, 0.0f, 1.0f);
                GL11.glLineWidth(2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth(0.1f);
                GlStateManager.func_179131_c(1.0f, 0.0f, 0.0f, 1.0f);
            }
            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }
        this.disableGL3D();
        GL11.glPopMatrix();
    }
    
    private List getEntitiesWithinAABB(final AxisAlignedBB bb) {
        final ArrayList list = new ArrayList();
        final int chunkMinX = MathHelper.func_76128_c((bb.field_72340_a - 2.0) / 16.0);
        final int chunkMaxX = MathHelper.func_76128_c((bb.field_72336_d + 2.0) / 16.0);
        final int chunkMinZ = MathHelper.func_76128_c((bb.field_72339_c - 2.0) / 16.0);
        final int chunkMaxZ = MathHelper.func_76128_c((bb.field_72334_f + 2.0) / 16.0);
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.field_71441_e.func_72863_F().func_186026_b(x, z) != null) {
                    Trajectories.mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)Trajectories.mc.field_71439_g, bb, (List)list, (Predicate)null);
                }
            }
        }
        return list;
    }
}
