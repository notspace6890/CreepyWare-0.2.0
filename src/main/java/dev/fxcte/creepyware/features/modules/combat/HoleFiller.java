// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import dev.fxcte.creepyware.features.modules.player.Freecam;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import dev.fxcte.creepyware.features.Feature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import dev.fxcte.creepyware.util.BlockUtil;
import dev.fxcte.creepyware.util.MathUtil;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import java.util.HashMap;
import dev.fxcte.creepyware.features.setting.Bind;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.util.InventoryUtil;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class HoleFiller extends Module
{
    private static HoleFiller INSTANCE;
    private final Setting<Boolean> server;
    private final Setting<Double> range;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> raytrace;
    private final Setting<Boolean> disable;
    private final Setting<Integer> disableTime;
    private final Setting<Boolean> offhand;
    private final Setting<InventoryUtil.Switch> switchMode;
    private final Setting<Boolean> onlySafe;
    private final Setting<Boolean> webSelf;
    private final Setting<Boolean> highWeb;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> midSafeHoles;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> onGroundCheck;
    private final Timer offTimer;
    private final Timer timer;
    private final Map<BlockPos, Integer> retries;
    private final Timer retryTimer;
    public Setting<Mode> mode;
    public Setting<PlaceMode> placeMode;
    private final Setting<Double> smartRange;
    public Setting<Bind> obbyBind;
    public Setting<Bind> webBind;
    public Mode currentMode;
    private boolean accessedViaBind;
    private int targetSlot;
    private int blocksThisTick;
    private Offhand.Mode offhandMode;
    private Offhand.Mode2 offhandMode2;
    private boolean isSneaking;
    private boolean hasOffhand;
    private boolean placeHighWeb;
    private int lastHotbarSlot;
    private boolean switchedItem;
    
    public HoleFiller() {
        super("HoleFiller", "Fills holes around you.", Category.COMBAT, true, false, true);
        this.server = (Setting<Boolean>)this.register(new Setting("Speed", "Server", 0.0, 0.0, (T)false, 0));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", (T)6.0, (T)0.0, (T)10.0));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay/Place", (T)50, (T)0, (T)250));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("Block/Place", (T)8, (T)1, (T)20));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)true, 0));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Speed", "Raytrace", 0.0, 0.0, (T)false, 0));
        this.disable = (Setting<Boolean>)this.register(new Setting("Speed", "Disable", 0.0, 0.0, (T)true, 0));
        this.disableTime = (Setting<Integer>)this.register(new Setting("Ms/Disable", (T)200, (T)1, (T)250));
        this.offhand = (Setting<Boolean>)this.register(new Setting("Speed", "OffHand", 0.0, 0.0, (T)true, 0));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Speed", "Switch", 0.0, 0.0, (T)InventoryUtil.Switch.NORMAL, 0));
        this.onlySafe = (Setting<Boolean>)this.register(new Setting("OnlySafe", (T)true, v -> this.offhand.getValue()));
        this.webSelf = (Setting<Boolean>)this.register(new Setting("Speed", "SelfWeb", 0.0, 0.0, (T)false, 0));
        this.highWeb = (Setting<Boolean>)this.register(new Setting("Speed", "HighWeb", 0.0, 0.0, (T)false, 0));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Speed", "Freecam", 0.0, 0.0, (T)false, 0));
        this.midSafeHoles = (Setting<Boolean>)this.register(new Setting("Speed", "MidSafe", 0.0, 0.0, (T)false, 0));
        this.packet = (Setting<Boolean>)this.register(new Setting("Speed", "Packet", 0.0, 0.0, (T)false, 0));
        this.onGroundCheck = (Setting<Boolean>)this.register(new Setting("Speed", "OnGroundCheck", 0.0, 0.0, (T)false, 0));
        this.offTimer = new Timer();
        this.timer = new Timer();
        this.retries = new HashMap<BlockPos, Integer>();
        this.retryTimer = new Timer();
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.OBSIDIAN, 0));
        this.placeMode = (Setting<PlaceMode>)this.register(new Setting("Speed", "PlaceMode", 0.0, 0.0, (T)PlaceMode.ALL, 0));
        this.smartRange = (Setting<Double>)this.register(new Setting("SmartRange", (T)6.0, (T)0.0, (T)10.0, v -> this.placeMode.getValue() == PlaceMode.SMART));
        this.obbyBind = (Setting<Bind>)this.register(new Setting("Speed", "Obsidian", 0.0, 0.0, (T)new Bind(-1), 0));
        this.webBind = (Setting<Bind>)this.register(new Setting("Speed", "Webs", 0.0, 0.0, (T)new Bind(-1), 0));
        this.currentMode = Mode.OBSIDIAN;
        this.accessedViaBind = false;
        this.targetSlot = -1;
        this.blocksThisTick = 0;
        this.offhandMode = Offhand.Mode.CRYSTALS;
        this.offhandMode2 = Offhand.Mode2.CRYSTALS;
        this.hasOffhand = false;
        this.placeHighWeb = false;
        this.lastHotbarSlot = -1;
        this.switchedItem = false;
        this.setInstance();
    }
    
    public static HoleFiller getInstance() {
        if (HoleFiller.INSTANCE == null) {
            HoleFiller.INSTANCE = new HoleFiller();
        }
        return HoleFiller.INSTANCE;
    }
    
    private void setInstance() {
        HoleFiller.INSTANCE = this;
    }
    
    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue();
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        if (!HoleFiller.mc.field_71439_g.field_70122_E && this.onGroundCheck.getValue()) {
            return;
        }
        if (this.shouldServer()) {
            HoleFiller.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            HoleFiller.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module HoleFiller set Enabled true"));
            return;
        }
        this.lastHotbarSlot = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
        if (!this.accessedViaBind) {
            this.currentMode = this.mode.getValue();
        }
        final Offhand module = CreepyWare.moduleManager.getModuleByClass(Offhand.class);
        this.offhandMode = module.mode;
        this.offhandMode2 = module.currentMode;
        if (this.offhand.getValue() && (EntityUtil.isSafe((Entity)HoleFiller.mc.field_71439_g) || !this.onlySafe.getValue())) {
            if (module.type.getValue() == Offhand.Type.NEW) {
                if (this.currentMode == Mode.WEBS) {
                    module.setSwapToTotem(false);
                    module.setMode(Offhand.Mode.WEBS);
                }
                else {
                    module.setSwapToTotem(false);
                    module.setMode(Offhand.Mode.OBSIDIAN);
                }
            }
            else {
                if (this.currentMode == Mode.WEBS) {
                    module.setMode(Offhand.Mode2.WEBS);
                }
                else {
                    module.setMode(Offhand.Mode2.OBSIDIAN);
                }
                if (!module.didSwitchThisTick) {
                    module.doOffhand();
                }
            }
        }
        CreepyWare.holeManager.update();
        this.offTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue() != 1 || !this.rotate.getValue())) {
            this.doHoleFill();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue() == 1 && this.rotate.getValue()) {
            this.doHoleFill();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.offhand.getValue()) {
            CreepyWare.moduleManager.getModuleByClass(Offhand.class).setMode(this.offhandMode);
            CreepyWare.moduleManager.getModuleByClass(Offhand.class).setMode(this.offhandMode2);
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.retries.clear();
        this.accessedViaBind = false;
        this.hasOffhand = false;
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.obbyBind.getValue().getKey() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.OBSIDIAN;
                this.toggle();
            }
            if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.WEBS;
                this.toggle();
            }
        }
    }
    
    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        if (this.placeHighWeb) {
            final BlockPos pos = new BlockPos(HoleFiller.mc.field_71439_g.field_70165_t, HoleFiller.mc.field_71439_g.field_70163_u + 1.0, HoleFiller.mc.field_71439_g.field_70161_v);
            this.placeBlock(pos);
            this.placeHighWeb = false;
        }
        if (this.midSafeHoles.getValue()) {
            final Object object = CreepyWare.holeManager.getMidSafety();
            synchronized (object) {
                final ArrayList<BlockPos> targets = new ArrayList<BlockPos>(CreepyWare.holeManager.getMidSafety());
            }
        }
        final Object object = CreepyWare.holeManager.getHoles();
        ArrayList<BlockPos> targets;
        synchronized (object) {
            targets = new ArrayList<BlockPos>(CreepyWare.holeManager.getHoles());
        }
        for (final BlockPos position : targets) {
            if (HoleFiller.mc.field_71439_g.func_174818_b(position) <= MathUtil.square(this.range.getValue())) {
                if (this.placeMode.getValue() == PlaceMode.SMART && !this.isPlayerInRange(position)) {
                    continue;
                }
                if (position.equals((Object)new BlockPos(HoleFiller.mc.field_71439_g.func_174791_d()))) {
                    if (this.currentMode != Mode.WEBS) {
                        continue;
                    }
                    if (!this.webSelf.getValue()) {
                        continue;
                    }
                    if (this.highWeb.getValue()) {
                        this.placeHighWeb = true;
                    }
                }
                final int placeability;
                if ((placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue())) == 1 && (this.currentMode == Mode.WEBS || (this.switchMode.getValue() == InventoryUtil.Switch.SILENT && (this.currentMode == Mode.WEBS || this.retries.get(position) == null || this.retries.get(position) < 4)))) {
                    this.placeBlock(position);
                    if (this.currentMode == Mode.WEBS) {
                        continue;
                    }
                    this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                }
                else {
                    if (placeability != 3) {
                        continue;
                    }
                    this.placeBlock(position);
                }
            }
        }
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.blocksThisTick < this.blocksPerTick.getValue() && this.switchItem(false)) {
            final boolean bl;
            final boolean smartRotate = bl = (this.blocksPerTick.getValue() == 1 && this.rotate.getValue());
            this.isSneaking = (smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking));
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }
    
    private boolean isPlayerInRange(final BlockPos pos) {
        for (final EntityPlayer player : HoleFiller.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, this.smartRange.getValue())) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    private boolean check() {
        if (Feature.fullNullCheck() || (this.disable.getValue() && this.offTimer.passedMs(this.disableTime.getValue()))) {
            this.disable();
            return true;
        }
        if (HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock((Class)((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class))) {
            this.lastHotbarSlot = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        this.switchItem(true);
        if (!this.freecam.getValue() && CreepyWare.moduleManager.isModuleEnabled(Freecam.class)) {
            return true;
        }
        this.blocksThisTick = 0;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        switch (this.currentMode) {
            case WEBS: {
                this.hasOffhand = InventoryUtil.isBlock(HoleFiller.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
                this.targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
                break;
            }
            case OBSIDIAN: {
                this.hasOffhand = InventoryUtil.isBlock(HoleFiller.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
                this.targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                break;
            }
        }
        if (this.onlySafe.getValue() && !EntityUtil.isSafe((Entity)HoleFiller.mc.field_71439_g)) {
            this.disable();
            return true;
        }
        return (!this.hasOffhand && this.targetSlot == -1 && (!this.offhand.getValue() || (!EntityUtil.isSafe((Entity)HoleFiller.mc.field_71439_g) && this.onlySafe.getValue()))) || (this.offhand.getValue() && !this.hasOffhand) || !this.timer.passedMs(this.delay.getValue());
    }
    
    private boolean switchItem(final boolean back) {
        if (this.offhand.getValue()) {
            return true;
        }
        final boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), (Class)((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class));
        this.switchedItem = value[0];
        return value[1];
    }
    
    static {
        HoleFiller.INSTANCE = new HoleFiller();
    }
    
    public enum PlaceMode
    {
        SMART, 
        ALL;
    }
    
    public enum Mode
    {
        WEBS, 
        OBSIDIAN;
    }
}
