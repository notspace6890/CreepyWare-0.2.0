// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import java.util.Arrays;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import java.util.function.Predicate;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import java.util.ArrayList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.List;

public class BlockUtil implements Util
{
    public static final List<Block> blackList;
    public static final List<Block> shulkerList;
    public static List<Block> unSolidBlocks;
    
    public static List<BlockPos> getBlockSphere(final float breakRange, final Class clazz) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), breakRange, (int)breakRange, false, true, 0).stream().filter(pos -> clazz.isInstance(BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public static void faceVectorPacketInstant(final Vec3d var0) {
        final float[] var = RotationUtil.getLegitRotations(var0);
        Wrapper.getPlayer().field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(var[0], var[1], Wrapper.getPlayer().field_70122_E));
    }
    
    public static EnumFacing getFacing(final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.values()) {
            final RayTraceResult rayTraceResult = BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5 + facing.func_176730_m().func_177958_n() * 1.0 / 2.0, pos.func_177956_o() + 0.5 + facing.func_176730_m().func_177956_o() * 1.0 / 2.0, pos.func_177952_p() + 0.5 + facing.func_176730_m().func_177952_p() * 1.0 / 2.0), false, true, false);
            if (rayTraceResult == null || (rayTraceResult.field_72313_a == RayTraceResult.Type.BLOCK && rayTraceResult.func_178782_a().equals((Object)pos))) {
                return facing;
            }
        }
        if (pos.func_177956_o() > BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }
    
    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.field_71441_e == null || pos == null) {
            return facings;
        }
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.func_177972_a(side);
            final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour);
            if (blockState != null && blockState.func_177230_c().func_176209_a(blockState, false)) {
                if (!blockState.func_185904_a().func_76222_j()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    
    public static EnumFacing getFirstFacing(final BlockPos pos) {
        final Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            final EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }
    
    public static EnumFacing getRayTraceFacing(final BlockPos pos) {
        final RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5, pos.func_177958_n() - 0.5, pos.func_177958_n() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return EnumFacing.UP;
        }
        return result.field_178784_b;
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace, final boolean entityCheck) {
        final Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        }
        if (!rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return 1;
                }
            }
        }
        for (final EnumFacing side : getPossibleSides(pos)) {
            if (!canBeClicked(pos.func_177972_a(side))) {
                continue;
            }
            return 3;
        }
        return 2;
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.field_72450_a - pos.func_177958_n());
            final float f2 = (float)(vec.field_72448_b - pos.func_177956_o());
            final float f3 = (float)(vec.field_72449_c - pos.func_177952_p());
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
    }
    
    public static void rightClickBed(final BlockPos pos, final float range, final boolean rotate, final EnumHand hand, final AtomicDouble yaw, final AtomicDouble pitch, final AtomicBoolean rotating, final boolean packet) {
        final Vec3d posVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5);
        final RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), posVec);
        final EnumFacing face = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
        final Vec3d eyesPos = RotationUtil.getEyesPos();
        if (rotate) {
            final float[] rotations = RotationUtil.getLegitRotations(posVec);
            yaw.set((double)rotations[0]);
            pitch.set((double)rotations[1]);
            rotating.set(true);
        }
        rightClickBlock(pos, posVec, hand, face, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(hand);
        BlockUtil.mc.field_71467_ac = 4;
    }
    
    public static void rightClickBlockLegit(final BlockPos pos, final float range, final boolean rotate, final EnumHand hand, final AtomicDouble Yaw2, final AtomicDouble Pitch, final AtomicBoolean rotating, final boolean packet) {
        final Vec3d eyesPos = RotationUtil.getEyesPos();
        final Vec3d posVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5);
        final double distanceSqPosVec = eyesPos.func_72436_e(posVec);
        for (final EnumFacing side : EnumFacing.values()) {
            final Vec3d hitVec = posVec.func_178787_e(new Vec3d(side.func_176730_m()).func_186678_a(0.5));
            final double distanceSqHitVec = eyesPos.func_72436_e(hitVec);
            if (distanceSqHitVec <= MathUtil.square(range) && distanceSqHitVec < distanceSqPosVec && BlockUtil.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) == null) {
                if (rotate) {
                    final float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    Yaw2.set((double)rotations[0]);
                    Pitch.set((double)rotations[1]);
                    rotating.set(true);
                }
                rightClickBlock(pos, hitVec, hand, side, packet);
                BlockUtil.mc.field_71439_g.func_184609_a(hand);
                BlockUtil.mc.field_71467_ac = 4;
                break;
            }
        }
    }
    
    public static boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.func_177972_a(side);
        final EnumFacing opposite = side.func_176734_d();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        final Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }
    
    public static boolean placeBlockSmartRotate(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.func_177972_a(side);
        final EnumFacing opposite = side.func_176734_d();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        final Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            CreepyWare.rotationManager.lookAtVec3d(hitVec);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }
    
    public static void placeBlockStopSneaking(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        final boolean sneaking = placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);
        if (!isSneaking && sneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    public static Vec3d[] getHelpingBlocks(final Vec3d vec3d) {
        return new Vec3d[] { new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0, vec3d.field_72449_c), new Vec3d((vec3d.field_72450_a != 0.0) ? (vec3d.field_72450_a * 2.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a != 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c * 2.0)), new Vec3d((vec3d.field_72450_a == 0.0) ? (vec3d.field_72450_a + 1.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c + 1.0)), new Vec3d((vec3d.field_72450_a == 0.0) ? (vec3d.field_72450_a - 1.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c - 1.0)), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0, vec3d.field_72449_c) };
    }
    
    public static List<BlockPos> possiblePlacePositions(final float placeRange) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter((Predicate<? super Object>)BlockUtil::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.func_177958_n();
        final int cy = pos.func_177956_o();
        final int cz = pos.func_177952_p();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = (float)y;
                    final float f2 = sphere ? (cy + r) : ((float)(cy + h));
                    if (f >= f2) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    public static List<BlockPos> getDisc(final BlockPos pos, final float r) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.func_177958_n();
        final int cy = pos.func_177956_o();
        final int cz = pos.func_177952_p();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < r * r) {
                    final BlockPos position = new BlockPos(x, cy, z);
                    circleblocks.add(position);
                }
            }
        }
        return circleblocks;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        final BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            return (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static List<BlockPos> possiblePlacePositions(final float placeRange, final boolean specialEntityCheck, final boolean oneDot15) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck, final boolean oneDot15) {
        final BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        final BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                return false;
            }
            if ((!oneDot15 && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) || BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                return false;
            }
            for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost))) {
                if (!entity.field_70128_L) {
                    if (specialEntityCheck && entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            if (!oneDot15) {
                for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2))) {
                    if (!entity.field_70128_L) {
                        if (specialEntityCheck && entity instanceof EntityEnderCrystal) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).func_176209_a(getState(pos), false);
    }
    
    private static Block getBlock(final BlockPos pos) {
        return getState(pos).func_177230_c();
    }
    
    private static IBlockState getState(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos);
    }
    
    public static boolean isBlockAboveEntitySolid(final Entity entity) {
        if (entity != null) {
            final BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u + 2.0, entity.field_70161_v);
            return isBlockSolid(pos);
        }
        return false;
    }
    
    public static void debugPos(final String message, final BlockPos pos) {
        Command.sendMessage(message + pos.func_177958_n() + "x, " + pos.func_177956_o() + "y, " + pos.func_177952_p() + "z");
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand, final boolean swing, final boolean exactHand, final boolean silent) {
        final RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5, pos.func_177956_o() - 0.5, pos.func_177952_p() + 0.5));
        final EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
        final int old = BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        final int crystal = InventoryUtil.getItemHotbar(Items.field_185158_cP);
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(crystal));
        }
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(old));
        }
        if (swing) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }
    
    public static BlockPos[] toBlockPos(final Vec3d[] vec3ds) {
        final BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }
    
    public static Vec3d posToVec3d(final BlockPos pos) {
        return new Vec3d((Vec3i)pos);
    }
    
    public static BlockPos vec3dToPos(final Vec3d vec3d) {
        return new BlockPos(vec3d);
    }
    
    public static Boolean isPosInFov(final BlockPos pos) {
        final int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0 && pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c < 0.0) {
            return false;
        }
        if (dirnumber == 1 && pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a > 0.0) {
            return false;
        }
        if (dirnumber == 2 && pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c > 0.0) {
            return false;
        }
        return dirnumber != 3 || pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a >= 0.0;
    }
    
    public static boolean isBlockBelowEntitySolid(final Entity entity) {
        if (entity != null) {
            final BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v);
            return isBlockSolid(pos);
        }
        return false;
    }
    
    public static boolean isBlockSolid(final BlockPos pos) {
        return !isBlockUnSolid(pos);
    }
    
    public static boolean isBlockUnSolid(final BlockPos pos) {
        return isBlockUnSolid(BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c());
    }
    
    public static boolean isBlockUnSolid(final Block block) {
        return BlockUtil.unSolidBlocks.contains(block);
    }
    
    public static Vec3d[] convertVec3ds(final Vec3d vec3d, final Vec3d[] input) {
        final Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }
    
    public static Vec3d[] convertVec3ds(final EntityPlayer entity, final Vec3d[] input) {
        return convertVec3ds(entity.func_174791_d(), input);
    }
    
    public static boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        final Block block = blockState.func_177230_c();
        return block.func_176195_g(blockState, (World)BlockUtil.mc.field_71441_e, pos) != -1.0f;
    }
    
    public static boolean isValidBlock(final BlockPos pos) {
        final Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return !(block instanceof BlockLiquid) && block.func_149688_o((IBlockState)null) != Material.field_151579_a;
    }
    
    public static boolean isScaffoldPos(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_175623_d(pos) || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150431_aC || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150329_H || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid;
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)(pos.func_177956_o() + height), (double)pos.func_177952_p()), false, true, false) == null;
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck) {
        return rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos) {
        return rayTracePlaceCheck(pos, true);
    }
    
    public static boolean isInHole() {
        final BlockPos blockPos = new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u, BlockUtil.mc.field_71439_g.field_70161_v);
        final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(blockPos);
        return isBlockValid(blockState, blockPos);
    }
    
    public static double getNearestBlockBelow() {
        for (double y = BlockUtil.mc.field_71439_g.field_70163_u; y > 0.0; y -= 0.001) {
            if (!(BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, y, BlockUtil.mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockSlab) && BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, y, BlockUtil.mc.field_71439_g.field_70161_v)).func_177230_c().func_176223_P().func_185890_d((IBlockAccess)BlockUtil.mc.field_71441_e, new BlockPos(0, 0, 0)) != null) {
                return y;
            }
        }
        return -1.0;
    }
    
    public static boolean isBlockValid(final IBlockState blockState, final BlockPos blockPos) {
        return blockState.func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71439_g.func_174818_b(blockPos) >= 1.0 && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a && (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos) || isElseHole(blockPos));
    }
    
    public static boolean isObbyHole(final BlockPos blockPos) {
        for (final BlockPos pos : getTouchingBlocks(blockPos)) {
            final IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150343_Z) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBedrockHole(final BlockPos blockPos) {
        for (final BlockPos pos : getTouchingBlocks(blockPos)) {
            final IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBothHole(final BlockPos blockPos) {
        for (final BlockPos pos : getTouchingBlocks(blockPos)) {
            final IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150350_a || (touchingState.func_177230_c() != Blocks.field_150357_h && touchingState.func_177230_c() != Blocks.field_150343_Z)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isElseHole(final BlockPos blockPos) {
        for (final BlockPos pos : getTouchingBlocks(blockPos)) {
            final IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150350_a || !touchingState.func_185913_b()) {
                return false;
            }
        }
        return true;
    }
    
    public static BlockPos[] getTouchingBlocks(final BlockPos blockPos) {
        return new BlockPos[] { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
    }
    
    static {
        blackList = Arrays.asList(Blocks.field_150477_bB, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, (Block)Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn);
        shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
        BlockUtil.unSolidBlocks = Arrays.asList((Block)Blocks.field_150356_k, Blocks.field_150457_bL, Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, (Block)Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, (Block)Blocks.field_150479_bC, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150468_ap, (Block)Blocks.field_150441_bU, (Block)Blocks.field_150455_bV, (Block)Blocks.field_150413_aR, (Block)Blocks.field_150416_aS, Blocks.field_150437_az, Blocks.field_150429_aA, (Block)Blocks.field_150488_af, Blocks.field_150350_a, (Block)Blocks.field_150427_aO, Blocks.field_150384_bq, (Block)Blocks.field_150355_j, (Block)Blocks.field_150358_i, (Block)Blocks.field_150353_l, (Block)Blocks.field_150356_k, Blocks.field_150345_g, (Block)Blocks.field_150328_O, (Block)Blocks.field_150327_N, (Block)Blocks.field_150338_P, (Block)Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, (Block)Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, (Block)Blocks.field_150329_H, (Block)Blocks.field_150330_I, Blocks.field_150395_bd, (Block)Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa);
    }
}
