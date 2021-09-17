// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import java.util.HashSet;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Comparator;
import dev.fxcte.creepyware.util.EntityUtil;
import java.util.Collection;
import java.util.Collections;
import dev.fxcte.creepyware.util.BlockUtil;
import java.util.ArrayList;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.util.Timer;
import net.minecraft.util.math.Vec3d;
import dev.fxcte.creepyware.util.InventoryUtil;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import dev.fxcte.creepyware.features.modules.Module;

public class AntiTrap extends Module
{
    public static Set<BlockPos> placedPos;
    private final Setting<Integer> coolDown;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Vec3d[] surroundTargets;
    private final Timer timer;
    public Setting<Rotate> rotate;
    public Setting<Boolean> sortY;
    private int lastHotbarSlot;
    private boolean switchedItem;
    private boolean offhand;
    
    public AntiTrap() {
        super("AntiTrap", "Places a crystal to prevent you getting trapped.", Category.COMBAT, true, false, false);
        this.coolDown = (Setting<Integer>)this.register(new Setting("CoolDown", (T)400, (T)0, (T)1000));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Speed", "Switch", 0.0, 0.0, (T)InventoryUtil.Switch.NORMAL, 0));
        this.surroundTargets = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, 1.0) };
        this.timer = new Timer();
        this.rotate = (Setting<Rotate>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)Rotate.NORMAL, 0));
        this.sortY = (Setting<Boolean>)this.register(new Setting("Speed", "SortY", 0.0, 0.0, (T)true, 0));
        this.lastHotbarSlot = -1;
        this.offhand = false;
    }
    
    @Override
    public void onEnable() {
        if (Feature.fullNullCheck() || !this.timer.passedMs(this.coolDown.getValue())) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = AntiTrap.mc.field_71439_g.field_71071_by.field_70461_c;
    }
    
    @Override
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        this.switchItem(true);
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (!Feature.fullNullCheck() && event.getStage() == 0) {
            this.doAntiTrap();
        }
    }
    
    public void doAntiTrap() {
        final boolean offhand = AntiTrap.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        this.offhand = offhand;
        final boolean bl = offhand;
        if (!this.offhand && InventoryUtil.findHotbarBlock(ItemEndCrystal.class) == -1) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = AntiTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        final ArrayList<Vec3d> targets = new ArrayList<Vec3d>();
        Collections.addAll(targets, BlockUtil.convertVec3ds(AntiTrap.mc.field_71439_g.func_174791_d(), this.surroundTargets));
        final EntityPlayer closestPlayer = EntityUtil.getClosestEnemy(6.0);
        if (closestPlayer != null) {
            final EntityPlayer entityPlayer;
            targets.sort((vec3d, vec3d2) -> Double.compare(entityPlayer.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), entityPlayer.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
            if (this.sortY.getValue()) {
                targets.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
            }
        }
        for (final Vec3d vec3d3 : targets) {
            final BlockPos pos = new BlockPos(vec3d3);
            if (!BlockUtil.canPlaceCrystal(pos)) {
                continue;
            }
            this.placeCrystal(pos);
            this.disable();
            break;
        }
    }
    
    private void placeCrystal(final BlockPos pos) {
        final boolean bl;
        final boolean mainhand = bl = (AntiTrap.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
        if (!mainhand && !this.offhand && !this.switchItem(false)) {
            this.disable();
            return;
        }
        final RayTraceResult result = AntiTrap.mc.field_71441_e.func_72933_a(new Vec3d(AntiTrap.mc.field_71439_g.field_70165_t, AntiTrap.mc.field_71439_g.field_70163_u + AntiTrap.mc.field_71439_g.func_70047_e(), AntiTrap.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5, pos.func_177956_o() - 0.5, pos.func_177952_p() + 0.5));
        final EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
        final float[] angle = MathUtil.calcAngle(AntiTrap.mc.field_71439_g.func_174824_e(Util.mc.func_184121_ak()), new Vec3d((double)(pos.func_177958_n() + 0.5f), (double)(pos.func_177956_o() - 0.5f), (double)(pos.func_177952_p() + 0.5f)));
        switch (this.rotate.getValue()) {
            case NORMAL: {
                CreepyWare.rotationManager.setPlayerRotations(angle[0], angle[1]);
                break;
            }
            case PACKET: {
                AntiTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.func_180184_b((int)angle[1], 360), AntiTrap.mc.field_71439_g.field_70122_E));
                break;
            }
        }
        AntiTrap.placedPos.add(pos);
        AntiTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        AntiTrap.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        this.timer.reset();
    }
    
    private boolean switchItem(final boolean back) {
        if (this.offhand) {
            return true;
        }
        final boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), Items.field_185158_cP);
        this.switchedItem = value[0];
        return value[1];
    }
    
    static {
        AntiTrap.placedPos = new HashSet<BlockPos>();
    }
    
    public enum Rotate
    {
        NONE, 
        NORMAL, 
        PACKET;
    }
}
