// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.util.EnumHand;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.block.BlockWeb;
import java.util.Iterator;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.util.BlockUtil;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.util.InventoryUtil;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Webaura extends Module
{
    public static boolean isPlacing;
    private final Setting<Boolean> server;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerPlace;
    private final Setting<Double> targetRange;
    private final Setting<Double> range;
    private final Setting<TargetMode> targetMode;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> raytrace;
    private final Setting<Double> speed;
    private final Setting<Boolean> upperBody;
    private final Setting<Boolean> lowerbody;
    private final Setting<Boolean> ylower;
    private final Setting<Boolean> antiSelf;
    private final Setting<Integer> eventMode;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> info;
    private final Setting<Boolean> disable;
    private final Setting<Boolean> packet;
    private final Timer timer;
    public EntityPlayer target;
    private boolean didPlace;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements;
    private boolean smartRotate;
    private BlockPos startPos;
    
    public Webaura() {
        super("Webaura", "Traps other players in webs", Category.COMBAT, true, false, false);
        this.server = (Setting<Boolean>)this.register(new Setting("Speed", "Server", 0.0, 0.0, (T)false, 0));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay/Place", (T)50, (T)0, (T)250));
        this.blocksPerPlace = (Setting<Integer>)this.register(new Setting("Block/Place", (T)8, (T)1, (T)30));
        this.targetRange = (Setting<Double>)this.register(new Setting("TargetRange", (T)10.0, (T)0.0, (T)20.0));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", (T)6.0, (T)0.0, (T)10.0));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Speed", "Target", 0.0, 0.0, (T)TargetMode.CLOSEST, 0));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Speed", "Switch", 0.0, 0.0, (T)InventoryUtil.Switch.NORMAL, 0));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)true, 0));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Speed", "Raytrace", 0.0, 0.0, (T)false, 0));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)30.0, (T)0.0, (T)30.0));
        this.upperBody = (Setting<Boolean>)this.register(new Setting("Speed", "Upper", 0.0, 0.0, (T)false, 0));
        this.lowerbody = (Setting<Boolean>)this.register(new Setting("Speed", "Lower", 0.0, 0.0, (T)true, 0));
        this.ylower = (Setting<Boolean>)this.register(new Setting("Speed", "Y-1", 0.0, 0.0, (T)false, 0));
        this.antiSelf = (Setting<Boolean>)this.register(new Setting("Speed", "AntiSelf", 0.0, 0.0, (T)false, 0));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", (T)3, (T)1, (T)3));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Speed", "Freecam", 0.0, 0.0, (T)false, 0));
        this.info = (Setting<Boolean>)this.register(new Setting("Speed", "Info", 0.0, 0.0, (T)false, 0));
        this.disable = (Setting<Boolean>)this.register(new Setting("Speed", "TSelfMove", 0.0, 0.0, (T)false, 0));
        this.packet = (Setting<Boolean>)this.register(new Setting("Speed", "Packet", 0.0, 0.0, (T)false, 0));
        this.timer = new Timer();
        this.didPlace = false;
        this.placements = 0;
        this.smartRotate = false;
        this.startPos = null;
    }
    
    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue();
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Webaura.mc.field_71439_g);
        this.lastHotbarSlot = Webaura.mc.field_71439_g.field_71071_by.field_70461_c;
        if (this.shouldServer()) {
            Webaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            Webaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module Webaura set Enabled true"));
        }
    }
    
    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.smartRotate = (this.rotate.getValue() && this.blocksPerPlace.getValue() == 1);
            this.doTrap();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && this.target != null) {
            return this.target.func_70005_c_();
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        if (this.shouldServer()) {
            Webaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            Webaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module Webaura set Enabled false"));
            return;
        }
        Webaura.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }
    
    private void doTrap() {
        if (this.shouldServer() || this.check()) {
            return;
        }
        this.doWebTrap();
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void doWebTrap() {
        final List<Vec3d> placeTargets = this.getPlacements();
        this.placeList(placeTargets);
    }
    
    private List<Vec3d> getPlacements() {
        final ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        final Vec3d baseVec = this.target.func_174791_d();
        if (this.ylower.getValue()) {
            list.add(baseVec.func_72441_c(0.0, -1.0, 0.0));
        }
        if (this.lowerbody.getValue()) {
            list.add(baseVec);
        }
        if (this.upperBody.getValue()) {
            list.add(baseVec.func_72441_c(0.0, 1.0, 0.0));
        }
        return list;
    }
    
    private void placeList(final List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(Webaura.mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), Webaura.mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
        for (final Vec3d vec3d3 : list) {
            final BlockPos position = new BlockPos(vec3d3);
            final int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (placeability == 3 || placeability == 1) {
                if (this.antiSelf.getValue() && MathUtil.areVec3dsAligned(Webaura.mc.field_71439_g.func_174791_d(), vec3d3)) {
                    continue;
                }
                this.placeBlock(position);
            }
        }
    }
    
    private boolean check() {
        Webaura.isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue() && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Webaura.mc.field_71439_g))) {
            this.disable();
            return true;
        }
        if (obbySlot == -1) {
            if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
                if (this.info.getValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cYou are out of Webs.");
                }
                this.disable();
            }
            return true;
        }
        if (Webaura.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && Webaura.mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
            this.lastHotbarSlot = Webaura.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || (CreepyWare.moduleManager.isModuleEnabled("Freecam") && !this.freecam.getValue()) || !this.timer.passedMs(this.delay.getValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && Webaura.mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockWeb.class));
    }
    
    private EntityPlayer getTarget(final double range, final boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : Webaura.mc.field_71441_e.field_73010_i) {
            if (!EntityUtil.isntValid((Entity)player, range) && (!trapped || !player.field_70134_J) && (!EntityUtil.getRoundedBlockPos((Entity)Webaura.mc.field_71439_g).equals((Object)EntityUtil.getRoundedBlockPos((Entity)player)) || !this.antiSelf.getValue())) {
                if (CreepyWare.speedManager.getPlayerSpeed(player) > this.speed.getValue()) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = Webaura.mc.field_71439_g.func_70068_e((Entity)player);
                }
                else {
                    if (Webaura.mc.field_71439_g.func_70068_e((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = Webaura.mc.field_71439_g.func_70068_e((Entity)player);
                }
            }
        }
        return target;
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && Webaura.mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(this.range.getValue()) && this.switchItem(false)) {
            Webaura.isPlacing = true;
            this.isSneaking = (this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking));
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    private boolean switchItem(final boolean back) {
        final boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), BlockWeb.class);
        this.switchedItem = value[0];
        return value[1];
    }
    
    static {
        Webaura.isPlacing = false;
    }
    
    public enum TargetMode
    {
        CLOSEST, 
        UNTRAPPED;
    }
}
