// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class MovementUtil implements Util
{
    public static Vec3d calculateLine(final Vec3d x1, final Vec3d x2, final double distance) {
        final double length = Math.sqrt(multiply(x2.field_72450_a - x1.field_72450_a) + multiply(x2.field_72448_b - x1.field_72448_b) + multiply(x2.field_72449_c - x1.field_72449_c));
        final double unitSlopeX = (x2.field_72450_a - x1.field_72450_a) / length;
        final double unitSlopeY = (x2.field_72448_b - x1.field_72448_b) / length;
        final double unitSlopeZ = (x2.field_72449_c - x1.field_72449_c) / length;
        final double x3 = x1.field_72450_a + unitSlopeX * distance;
        final double y = x1.field_72448_b + unitSlopeY * distance;
        final double z = x1.field_72449_c + unitSlopeZ * distance;
        return new Vec3d(x3, y, z);
    }
    
    public static Vec2f calculateLineNoY(final Vec2f x1, final Vec2f x2, final double distance) {
        final double length = Math.sqrt(multiply(x2.field_189982_i - x1.field_189982_i) + multiply(x2.field_189983_j - x1.field_189983_j));
        final double unitSlopeX = (x2.field_189982_i - x1.field_189982_i) / length;
        final double unitSlopeZ = (x2.field_189983_j - x1.field_189983_j) / length;
        final float x3 = (float)(x1.field_189982_i + unitSlopeX * distance);
        final float z = (float)(x1.field_189983_j + unitSlopeZ * distance);
        return new Vec2f(x3, z);
    }
    
    public static double multiply(final double one) {
        return one * one;
    }
    
    public static Vec3d extrapolatePlayerPositionWithGravity(final EntityPlayer player, final int ticks) {
        double totalDistance = 0.0;
        double extrapolatedMotionY = player.field_70181_x;
        for (int i = 0; i < ticks; ++i) {
            totalDistance += multiply(player.field_70159_w) + multiply(extrapolatedMotionY) + multiply(player.field_70179_y);
            extrapolatedMotionY -= 0.1;
        }
        final double horizontalDistance = multiply(player.field_70159_w) + multiply(player.field_70179_y) * ticks;
        final Vec2f horizontalVec = calculateLineNoY(new Vec2f((float)player.field_70142_S, (float)player.field_70136_U), new Vec2f((float)player.field_70165_t, (float)player.field_70161_v), horizontalDistance);
        double addedY = player.field_70181_x;
        double finalY = player.field_70163_u;
        final Vec3d tempPos = new Vec3d((double)horizontalVec.field_189982_i, player.field_70163_u, (double)horizontalVec.field_189983_j);
        for (int j = 0; j < ticks; ++j) {
            finalY += addedY;
            addedY -= 0.1;
        }
        final RayTraceResult result = MovementUtil.mc.field_71441_e.func_72933_a(player.func_174791_d(), new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c));
        if (result == null || result.field_72313_a == RayTraceResult.Type.ENTITY) {
            return new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c);
        }
        return result.field_72307_f;
    }
    
    public static Vec3d extrapolatePlayerPosition(final EntityPlayer player, final int ticks) {
        final double totalDistance = 0.0;
        final double extrapolatedMotionY = player.field_70181_x;
        for (int i = 0; i < ticks; ++i) {}
        final Vec3d lastPos = new Vec3d(player.field_70142_S, player.field_70137_T, player.field_70136_U);
        final Vec3d currentPos = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
        final double distance = multiply(player.field_70159_w) + multiply(player.field_70181_x) + multiply(player.field_70179_y);
        double extrapolatedPosY = player.field_70163_u;
        if (!player.func_189652_ae()) {
            extrapolatedPosY -= 0.1;
        }
        final Vec3d tempVec = calculateLine(lastPos, currentPos, distance * ticks);
        final Vec3d finalVec = new Vec3d(tempVec.field_72450_a, extrapolatedPosY, tempVec.field_72449_c);
        final RayTraceResult result = MovementUtil.mc.field_71441_e.func_72933_a(player.func_174791_d(), finalVec);
        return new Vec3d(tempVec.field_72450_a, player.field_70163_u, tempVec.field_72449_c);
    }
}
