// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.util.EnumFacing;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import net.minecraft.item.ItemSword;
import java.util.function.ToIntFunction;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockObsidian;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import dev.fxcte.creepyware.mixin.mixins.accessors.ISPacketSetSlot;
import dev.fxcte.creepyware.mixin.mixins.accessors.IContainer;
import net.minecraft.network.play.server.SPacketSetSlot;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import dev.fxcte.creepyware.features.setting.EnumConverter;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import dev.fxcte.creepyware.event.events.ProcessRightClickBlockEvent;
import dev.fxcte.creepyware.features.Feature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import java.util.concurrent.ConcurrentLinkedQueue;
import dev.fxcte.creepyware.features.setting.Bind;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.util.InventoryUtil;
import java.util.Queue;
import dev.fxcte.creepyware.features.modules.Module;

public class Offhand extends Module
{
    private static Offhand instance;
    private final Queue<InventoryUtil.Task> taskList;
    private final Timer timer;
    private final Timer secondTimer;
    private final Timer serverTimer;
    public Setting<Type> type;
    public Setting<Boolean> cycle;
    public Setting<Bind> cycleKey;
    public Setting<Bind> offHandGapple;
    public Setting<Float> gappleHealth;
    public Setting<Float> gappleHoleHealth;
    public Setting<Bind> offHandCrystal;
    public Setting<Float> crystalHealth;
    public Setting<Float> crystalHoleHealth;
    public Setting<Float> cTargetDistance;
    public Setting<Bind> obsidian;
    public Setting<Float> obsidianHealth;
    public Setting<Float> obsidianHoleHealth;
    public Setting<Bind> webBind;
    public Setting<Float> webHealth;
    public Setting<Float> webHoleHealth;
    public Setting<Boolean> holeCheck;
    public Setting<Boolean> crystalCheck;
    public Setting<Boolean> gapSwap;
    public Setting<Integer> updates;
    public Setting<Boolean> cycleObby;
    public Setting<Boolean> cycleWebs;
    public Setting<Boolean> crystalToTotem;
    public Setting<Boolean> absorption;
    public Setting<Boolean> autoGapple;
    public Setting<Boolean> onlyWTotem;
    public Setting<Boolean> unDrawTotem;
    public Setting<Boolean> noOffhandGC;
    public Setting<Boolean> retardOGC;
    public Setting<Boolean> returnToCrystal;
    public Setting<Integer> timeout;
    public Setting<Integer> timeout2;
    public Setting<Integer> actions;
    public Setting<NameMode> displayNameChange;
    public Setting<Boolean> guis;
    public Setting<Integer> serverTimeOut;
    public Setting<Boolean> bedcheck;
    public Mode mode;
    public Mode oldMode;
    public Mode2 currentMode;
    public int totems;
    public int crystals;
    public int gapples;
    public int obby;
    public int webs;
    public int lastTotemSlot;
    public int lastGappleSlot;
    public int lastCrystalSlot;
    public int lastObbySlot;
    public int lastWebSlot;
    public boolean holdingCrystal;
    public boolean holdingTotem;
    public boolean holdingGapple;
    public boolean holdingObby;
    public boolean holdingWeb;
    public boolean didSwitchThisTick;
    private int oldSlot;
    private boolean swapToTotem;
    private boolean eatingApple;
    private boolean oldSwapToTotem;
    private boolean autoGappleSwitch;
    private boolean second;
    private boolean switchedForHealthReason;
    
    public Offhand() {
        super("Offhand", "Allows you to switch up your Offhand.", Category.COMBAT, true, false, false);
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.timer = new Timer();
        this.secondTimer = new Timer();
        this.serverTimer = new Timer();
        this.type = (Setting<Type>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Type.NEW, 0));
        this.cycle = (Setting<Boolean>)this.register(new Setting("Cycle", (T)false, v -> this.type.getValue() == Type.OLD));
        this.cycleKey = (Setting<Bind>)this.register(new Setting("Key", (T)new Bind(-1), v -> this.cycle.getValue() && this.type.getValue() == Type.OLD));
        this.offHandGapple = (Setting<Bind>)this.register(new Setting("Speed", "Gapple", 0.0, 0.0, (T)new Bind(-1), 0));
        this.gappleHealth = (Setting<Float>)this.register(new Setting("G-Health", (T)13.0f, (T)0.1f, (T)36.0f));
        this.gappleHoleHealth = (Setting<Float>)this.register(new Setting("G-H-Health", (T)3.5f, (T)0.1f, (T)36.0f));
        this.offHandCrystal = (Setting<Bind>)this.register(new Setting("Speed", "Crystal", 0.0, 0.0, (T)new Bind(-1), 0));
        this.crystalHealth = (Setting<Float>)this.register(new Setting("C-Health", (T)13.0f, (T)0.1f, (T)36.0f));
        this.crystalHoleHealth = (Setting<Float>)this.register(new Setting("C-H-Health", (T)3.5f, (T)0.1f, (T)36.0f));
        this.cTargetDistance = (Setting<Float>)this.register(new Setting("C-Distance", (T)10.0f, (T)1.0f, (T)20.0f));
        this.obsidian = (Setting<Bind>)this.register(new Setting("Speed", "Obsidian", 0.0, 0.0, (T)new Bind(-1), 0));
        this.obsidianHealth = (Setting<Float>)this.register(new Setting("O-Health", (T)13.0f, (T)0.1f, (T)36.0f));
        this.obsidianHoleHealth = (Setting<Float>)this.register(new Setting("O-H-Health", (T)8.0f, (T)0.1f, (T)36.0f));
        this.webBind = (Setting<Bind>)this.register(new Setting("Speed", "Webs", 0.0, 0.0, (T)new Bind(-1), 0));
        this.webHealth = (Setting<Float>)this.register(new Setting("W-Health", (T)13.0f, (T)0.1f, (T)36.0f));
        this.webHoleHealth = (Setting<Float>)this.register(new Setting("W-H-Health", (T)8.0f, (T)0.1f, (T)36.0f));
        this.holeCheck = (Setting<Boolean>)this.register(new Setting("Speed", "Hole-Check", 0.0, 0.0, (T)true, 0));
        this.crystalCheck = (Setting<Boolean>)this.register(new Setting("Speed", "Crystal-Check", 0.0, 0.0, (T)false, 0));
        this.gapSwap = (Setting<Boolean>)this.register(new Setting("Speed", "Gap-Swap", 0.0, 0.0, (T)true, 0));
        this.updates = (Setting<Integer>)this.register(new Setting("Updates", (T)1, (T)1, (T)2));
        this.cycleObby = (Setting<Boolean>)this.register(new Setting("CycleObby", (T)false, v -> this.type.getValue() == Type.OLD));
        this.cycleWebs = (Setting<Boolean>)this.register(new Setting("CycleWebs", (T)false, v -> this.type.getValue() == Type.OLD));
        this.crystalToTotem = (Setting<Boolean>)this.register(new Setting("Crystal-Totem", (T)true, v -> this.type.getValue() == Type.OLD));
        this.absorption = (Setting<Boolean>)this.register(new Setting("Absorption", (T)false, v -> this.type.getValue() == Type.OLD));
        this.autoGapple = (Setting<Boolean>)this.register(new Setting("AutoGapple", (T)false, v -> this.type.getValue() == Type.OLD));
        this.onlyWTotem = (Setting<Boolean>)this.register(new Setting("OnlyWTotem", (T)true, v -> this.autoGapple.getValue() && this.type.getValue() == Type.OLD));
        this.unDrawTotem = (Setting<Boolean>)this.register(new Setting("DrawTotems", (T)true, v -> this.type.getValue() == Type.OLD));
        this.noOffhandGC = (Setting<Boolean>)this.register(new Setting("Speed", "NoOGC", 0.0, 0.0, (T)false, 0));
        this.retardOGC = (Setting<Boolean>)this.register(new Setting("Speed", "RetardOGC", 0.0, 0.0, (T)false, 0));
        this.returnToCrystal = (Setting<Boolean>)this.register(new Setting("Speed", "RecoverySwitch", 0.0, 0.0, (T)false, 0));
        this.timeout = (Setting<Integer>)this.register(new Setting("Timeout", (T)50, (T)0, (T)500));
        this.timeout2 = (Setting<Integer>)this.register(new Setting("Timeout2", (T)50, (T)0, (T)500));
        this.actions = (Setting<Integer>)this.register(new Setting("Actions", (T)4, (T)1, (T)4, v -> this.type.getValue() == Type.OLD));
        this.displayNameChange = (Setting<NameMode>)this.register(new Setting("Name", (T)NameMode.TOTEM, v -> this.type.getValue() == Type.OLD));
        this.guis = (Setting<Boolean>)this.register(new Setting("Speed", "Guis", 0.0, 0.0, (T)false, 0));
        this.serverTimeOut = (Setting<Integer>)this.register(new Setting("S-Timeout", (T)1000, (T)0, (T)5000));
        this.bedcheck = (Setting<Boolean>)this.register(new Setting("Speed", "BedCheck", 0.0, 0.0, (T)false, 0));
        this.mode = Mode.CRYSTALS;
        this.oldMode = Mode.CRYSTALS;
        this.currentMode = Mode2.TOTEMS;
        this.totems = 0;
        this.crystals = 0;
        this.gapples = 0;
        this.obby = 0;
        this.webs = 0;
        this.lastTotemSlot = -1;
        this.lastGappleSlot = -1;
        this.lastCrystalSlot = -1;
        this.lastObbySlot = -1;
        this.lastWebSlot = -1;
        this.holdingCrystal = false;
        this.holdingTotem = false;
        this.holdingGapple = false;
        this.holdingObby = false;
        this.holdingWeb = false;
        this.didSwitchThisTick = false;
        this.oldSlot = -1;
        this.swapToTotem = false;
        this.eatingApple = false;
        this.oldSwapToTotem = false;
        this.autoGappleSwitch = false;
        this.second = false;
        this.switchedForHealthReason = false;
        Offhand.instance = this;
    }
    
    public static Offhand getInstance() {
        if (Offhand.instance == null) {
            Offhand.instance = new Offhand();
        }
        return Offhand.instance;
    }
    
    public void onItemFinish(final ItemStack stack, final EntityLivingBase base) {
        if (this.noOffhandGC.getValue() && base.equals((Object)Offhand.mc.field_71439_g) && stack.func_77973_b() == Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()) {
            this.secondTimer.reset();
            this.second = true;
        }
    }
    
    @Override
    public void onTick() {
        if (Feature.nullCheck() || this.updates.getValue() == 1) {
            return;
        }
        this.doOffhand();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final ProcessRightClickBlockEvent event) {
        if (this.noOffhandGC.getValue() && event.hand == EnumHand.MAIN_HAND && event.stack.func_77973_b() == Items.field_185158_cP && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71476_x != null && event.pos == Offhand.mc.field_71476_x.func_178782_a()) {
            event.setCanceled(true);
            Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
            Offhand.mc.field_71442_b.func_187101_a((EntityPlayer)Offhand.mc.field_71439_g, (World)Offhand.mc.field_71441_e, EnumHand.OFF_HAND);
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.noOffhandGC.getValue() && this.retardOGC.getValue()) {
            if (this.timer.passedMs(this.timeout.getValue())) {
                if (Offhand.mc.field_71439_g != null && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Mouse.isButtonDown(1)) {
                    Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
                    Offhand.mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
                }
            }
            else if (Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
                Offhand.mc.field_71474_y.field_74313_G.field_74513_e = false;
            }
        }
        if (Feature.nullCheck() || this.updates.getValue() == 2) {
            return;
        }
        this.doOffhand();
        if (this.secondTimer.passedMs(this.timeout2.getValue()) && this.second) {
            this.second = false;
            this.timer.reset();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.type.getValue() == Type.NEW) {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.CRYSTALS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    }
                    else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.GAPPLES) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    }
                    else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.OBSIDIAN) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    }
                    else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.WEBS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    }
                    else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.WEBS);
                }
            }
            else if (this.cycle.getValue()) {
                if (this.cycleKey.getValue().getKey() == Keyboard.getEventKey()) {
                    Mode2 newMode = (Mode2)EnumConverter.increaseEnum(this.currentMode);
                    if ((newMode == Mode2.OBSIDIAN && !this.cycleObby.getValue()) || (newMode == Mode2.WEBS && !this.cycleWebs.getValue())) {
                        newMode = Mode2.TOTEMS;
                    }
                    this.setMode(newMode);
                }
            }
            else {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.WEBS);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.noOffhandGC.getValue() && !Feature.fullNullCheck() && Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                final CPacketPlayerTryUseItemOnBlock packet2 = event.getPacket();
                if (packet2.func_187022_c() == EnumHand.MAIN_HAND && !AutoCrystal.placedPos.contains(packet2.func_187023_a())) {
                    if (this.timer.passedMs(this.timeout.getValue())) {
                        Offhand.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
                        Offhand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            }
            else {
                final CPacketPlayerTryUseItem packet3;
                if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet3 = event.getPacket()).func_187028_a() == EnumHand.OFF_HAND && !this.timer.passedMs(this.timeout.getValue())) {
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        final SPacketSetSlot packet;
        if (ServerModule.getInstance().isConnected() && event.getPacket() instanceof SPacketSetSlot && (packet = event.getPacket()).func_149173_d() == -1 && packet.func_149175_c() != -1) {
            ((IContainer)Offhand.mc.field_71439_g.field_71070_bA).setTransactionID((short)packet.func_149175_c());
            ((ISPacketSetSlot)packet).setWindowId(-1);
            this.serverTimer.reset();
            this.switchedForHealthReason = true;
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.type.getValue() == Type.NEW) {
            return String.valueOf(this.getStackSize());
        }
        switch (this.displayNameChange.getValue()) {
            case MODE: {
                return EnumConverter.getProperName(this.currentMode);
            }
            case TOTEM: {
                if (this.currentMode == Mode2.TOTEMS) {
                    return this.totems + "";
                }
                return EnumConverter.getProperName(this.currentMode);
            }
            default: {
                switch (this.currentMode) {
                    case TOTEMS: {
                        return this.totems + "";
                    }
                    case GAPPLES: {
                        return this.gapples + "";
                    }
                    default: {
                        return this.crystals + "";
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public String getDisplayName() {
        if (this.type.getValue() == Type.NEW) {
            if (this.shouldTotem()) {
                return "AutoTotem" + (this.isSwapToTotem() ? "" : ("-" + this.getModeStr()));
            }
            switch (this.mode) {
                case GAPPLES: {
                    return "OffhandGapple";
                }
                case WEBS: {
                    return "OffhandWebs";
                }
                case OBSIDIAN: {
                    return "OffhandObby";
                }
                default: {
                    return "OffhandCrystal";
                }
            }
        }
        else {
            switch (this.displayNameChange.getValue()) {
                case MODE: {
                    return this.displayName.getValue();
                }
                case TOTEM: {
                    if (this.currentMode == Mode2.TOTEMS) {
                        return "AutoTotem";
                    }
                    return this.displayName.getValue();
                }
                default: {
                    switch (this.currentMode) {
                        case TOTEMS: {
                            return "AutoTotem";
                        }
                        case GAPPLES: {
                            return "OffhandGapple";
                        }
                        case WEBS: {
                            return "OffhandWebs";
                        }
                        case OBSIDIAN: {
                            return "OffhandObby";
                        }
                        default: {
                            return "OffhandCrystal";
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public void doOffhand() {
        if (!this.serverTimer.passedMs(this.serverTimeOut.getValue())) {
            return;
        }
        if (this.type.getValue() == Type.NEW) {
            if (Offhand.mc.field_71462_r instanceof GuiContainer && !this.guis.getValue() && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            if (this.gapSwap.getValue()) {
                if ((this.getSlot(Mode.GAPPLES) != -1 || Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao) && Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151153_ao && Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
                    this.setMode(Mode.GAPPLES);
                    this.eatingApple = true;
                    this.swapToTotem = false;
                }
                else if (this.eatingApple) {
                    this.setMode(this.oldMode);
                    this.swapToTotem = this.oldSwapToTotem;
                    this.eatingApple = false;
                }
                else {
                    this.oldMode = this.mode;
                    this.oldSwapToTotem = this.swapToTotem;
                }
            }
            if (!this.shouldTotem()) {
                if (Offhand.mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || !this.isItemInOffhand()) {
                    final int n;
                    final int slot = n = ((this.getSlot(this.mode) < 9) ? (this.getSlot(this.mode) + 36) : this.getSlot(this.mode));
                    if (this.getSlot(this.mode) != -1) {
                        if (this.oldSlot != -1) {
                            Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                            Offhand.mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        }
                        this.oldSlot = slot;
                        Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                        Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    }
                }
            }
            else if (!this.eatingApple && (Offhand.mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_190929_cY)) {
                final int n;
                final int slot = n = ((this.getTotemSlot() < 9) ? (this.getTotemSlot() + 36) : this.getTotemSlot());
                if (this.getTotemSlot() != -1) {
                    Offhand.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    Offhand.mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
                    this.oldSlot = -1;
                }
            }
        }
        else {
            if (!this.unDrawTotem.getValue()) {
                this.manageDrawn();
            }
            this.didSwitchThisTick = false;
            this.holdingCrystal = (Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
            this.holdingTotem = (Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY);
            this.holdingGapple = (Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao);
            this.holdingObby = InventoryUtil.isBlock(Offhand.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
            this.holdingWeb = InventoryUtil.isBlock(Offhand.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
            this.totems = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingTotem) {
                this.totems += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
            }
            this.crystals = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingCrystal) {
                this.crystals += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
            }
            this.gapples = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
            if (this.holdingGapple) {
                this.gapples += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
            }
            if (this.currentMode == Mode2.WEBS || this.currentMode == Mode2.OBSIDIAN) {
                this.obby = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
                if (this.holdingObby) {
                    this.obby += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
                }
                this.webs = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
                if (this.holdingWeb) {
                    this.webs += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
                }
            }
            this.doSwitch();
        }
    }
    
    private void manageDrawn() {
        if (this.currentMode == Mode2.TOTEMS && this.drawn.getValue()) {
            this.drawn.setValue(false);
        }
        if (this.currentMode != Mode2.TOTEMS && !this.drawn.getValue()) {
            this.drawn.setValue(true);
        }
    }
    
    public void doSwitch() {
        if (this.autoGapple.getValue()) {
            if (Offhand.mc.field_71474_y.field_74313_G.func_151470_d()) {
                if (Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (!this.onlyWTotem.getValue() || Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)) {
                    this.setMode(Mode.GAPPLES);
                    this.autoGappleSwitch = true;
                }
            }
            else if (this.autoGappleSwitch) {
                this.setMode(Mode2.TOTEMS);
                this.autoGappleSwitch = false;
            }
        }
        if ((this.currentMode == Mode2.GAPPLES && (((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.gappleHealth.getValue()) || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.gappleHoleHealth.getValue())) || (this.currentMode == Mode2.CRYSTALS && (((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.crystalHealth.getValue()) || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.crystalHoleHealth.getValue())) || (this.currentMode == Mode2.OBSIDIAN && (((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.obsidianHealth.getValue()) || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.obsidianHoleHealth.getValue())) || (this.currentMode == Mode2.WEBS && (((!EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) || this.bedPlaceable()) && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.webHealth.getValue()) || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) <= this.webHoleHealth.getValue()))) {
            if (this.returnToCrystal.getValue() && this.currentMode == Mode2.CRYSTALS) {
                this.switchedForHealthReason = true;
            }
            this.setMode(Mode2.TOTEMS);
        }
        if (this.switchedForHealthReason && ((EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g) && !this.bedPlaceable() && EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) > this.crystalHoleHealth.getValue()) || EntityUtil.getHealth((Entity)Offhand.mc.field_71439_g, this.absorption.getValue()) > this.crystalHealth.getValue())) {
            this.setMode(Mode2.CRYSTALS);
            this.switchedForHealthReason = false;
        }
        if (Offhand.mc.field_71462_r instanceof GuiContainer && !this.guis.getValue() && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        final Item currentOffhandItem = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b();
        switch (this.currentMode) {
            case TOTEMS: {
                if (this.totems <= 0) {
                    break;
                }
                if (this.holdingTotem) {
                    break;
                }
                this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.field_190929_cY, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastTotemSlot);
                this.putItemInOffhand(this.lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (this.gapples <= 0) {
                    break;
                }
                if (this.holdingGapple) {
                    break;
                }
                this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.field_151153_ao, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastGappleSlot);
                this.putItemInOffhand(this.lastGappleSlot, lastSlot);
                break;
            }
            case WEBS: {
                if (this.webs <= 0) {
                    break;
                }
                if (this.holdingWeb) {
                    break;
                }
                this.lastWebSlot = InventoryUtil.findInventoryBlock(BlockWeb.class, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastWebSlot);
                this.putItemInOffhand(this.lastWebSlot, lastSlot);
                break;
            }
            case OBSIDIAN: {
                if (this.obby <= 0) {
                    break;
                }
                if (this.holdingObby) {
                    break;
                }
                this.lastObbySlot = InventoryUtil.findInventoryBlock(BlockObsidian.class, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastObbySlot);
                this.putItemInOffhand(this.lastObbySlot, lastSlot);
                break;
            }
            default: {
                if (this.crystals <= 0) {
                    break;
                }
                if (this.holdingCrystal) {
                    break;
                }
                this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.field_185158_cP, false);
                final int lastSlot = this.getLastSlot(currentOffhandItem, this.lastCrystalSlot);
                this.putItemInOffhand(this.lastCrystalSlot, lastSlot);
                break;
            }
        }
        for (int i = 0; i < this.actions.getValue(); ++i) {
            final InventoryUtil.Task task = this.taskList.poll();
            if (task != null) {
                task.run();
                if (task.isSwitching()) {
                    this.didSwitchThisTick = true;
                }
            }
        }
    }
    
    private int getLastSlot(final Item item, final int slotIn) {
        if (item == Items.field_185158_cP) {
            return this.lastCrystalSlot;
        }
        if (item == Items.field_151153_ao) {
            return this.lastGappleSlot;
        }
        if (item == Items.field_190929_cY) {
            return this.lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return this.lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return this.lastWebSlot;
        }
        if (item == Items.field_190931_a) {
            return -1;
        }
        return slotIn;
    }
    
    private void putItemInOffhand(final int slotIn, final int slotOut) {
        if (slotIn != -1 && this.taskList.isEmpty()) {
            this.taskList.add(new InventoryUtil.Task(slotIn));
            this.taskList.add(new InventoryUtil.Task(45));
            this.taskList.add(new InventoryUtil.Task(slotOut));
            this.taskList.add(new InventoryUtil.Task());
        }
    }
    
    private boolean noNearbyPlayers() {
        return this.mode == Mode.CRYSTALS && Offhand.mc.field_71441_e.field_73010_i.stream().noneMatch(e -> e != Offhand.mc.field_71439_g && !CreepyWare.friendManager.isFriend(e) && Offhand.mc.field_71439_g.func_70032_d((Entity)e) <= this.cTargetDistance.getValue());
    }
    
    private boolean isItemInOffhand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao;
            }
            case CRYSTALS: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
            }
            case OBSIDIAN: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150343_Z;
            }
            case WEBS: {
                return Offhand.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150321_G;
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean isHeldInMainHand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao;
            }
            case CRYSTALS: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP;
            }
            case OBSIDIAN: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150343_Z;
            }
            case WEBS: {
                return Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150321_G;
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean shouldTotem() {
        if (this.isHeldInMainHand() || this.isSwapToTotem()) {
            return true;
        }
        if (this.holeCheck.getValue() && EntityUtil.isInHole((Entity)Offhand.mc.field_71439_g) && !this.bedPlaceable()) {
            return Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj() <= this.getHoleHealth() || Offhand.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || Offhand.mc.field_71439_g.field_70143_R >= 3.0f || this.noNearbyPlayers() || (this.crystalCheck.getValue() && this.isCrystalsAABBEmpty());
        }
        return Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj() <= this.getHealth() || Offhand.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || Offhand.mc.field_71439_g.field_70143_R >= 3.0f || this.noNearbyPlayers() || (this.crystalCheck.getValue() && this.isCrystalsAABBEmpty());
    }
    
    private boolean isNotEmpty(final BlockPos pos) {
        return Offhand.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof EntityEnderCrystal);
    }
    
    private float getHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHealth.getValue();
            }
            case GAPPLES: {
                return this.gappleHealth.getValue();
            }
            case OBSIDIAN: {
                return this.obsidianHealth.getValue();
            }
            default: {
                return this.webHealth.getValue();
            }
        }
    }
    
    private float getHoleHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHoleHealth.getValue();
            }
            case GAPPLES: {
                return this.gappleHoleHealth.getValue();
            }
            case OBSIDIAN: {
                return this.obsidianHoleHealth.getValue();
            }
            default: {
                return this.webHoleHealth.getValue();
            }
        }
    }
    
    private boolean isCrystalsAABBEmpty() {
        return this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 0)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 0)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 1)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -1)) || this.isNotEmpty(Offhand.mc.field_71439_g.func_180425_c());
    }
    
    int getStackSize() {
        int size = 0;
        if (this.shouldTotem()) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
                    size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
                }
            }
        }
        else if (this.mode == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) {
                    if (((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150343_Z) {
                        size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
                    }
                }
            }
        }
        else if (this.mode == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) {
                    if (((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150321_G) {
                        size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
                    }
                }
            }
        }
        else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == ((this.mode == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao)) {
                    size += Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
                }
            }
        }
        return size;
    }
    
    int getSlot(final Mode m) {
        int slot = -1;
        if (m == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150343_Z) {
                    slot = i;
                    break;
                }
            }
        }
        else if (m == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150321_G) {
                    slot = i;
                    break;
                }
            }
        }
        else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == ((m == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao)) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
    
    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }
    
    private String getModeStr() {
        switch (this.mode) {
            case GAPPLES: {
                return "G";
            }
            case WEBS: {
                return "W";
            }
            case OBSIDIAN: {
                return "O";
            }
            default: {
                return "C";
            }
        }
    }
    
    public void setMode(final Mode mode) {
        this.mode = mode;
    }
    
    public void setMode(final Mode2 mode) {
        this.currentMode = ((this.currentMode == mode) ? Mode2.TOTEMS : ((!this.cycle.getValue() && this.crystalToTotem.getValue() && (this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.OBSIDIAN || this.currentMode == Mode2.WEBS) && mode == Mode2.GAPPLES) ? Mode2.TOTEMS : mode));
    }
    
    public boolean isSwapToTotem() {
        return this.swapToTotem;
    }
    
    public void setSwapToTotem(final boolean swapToTotem) {
        this.swapToTotem = swapToTotem;
    }
    
    private boolean bedPlaceable() {
        if (!this.bedcheck.getValue()) {
            return false;
        }
        if (Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150324_C && Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        for (final EnumFacing facing : EnumFacing.values()) {
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN && (Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() == Blocks.field_150324_C || Offhand.mc.field_71441_e.func_180495_p(Offhand.mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() == Blocks.field_150350_a)) {
                return true;
            }
        }
        return false;
    }
    
    public enum NameMode
    {
        MODE, 
        TOTEM, 
        AMOUNT;
    }
    
    public enum Mode2
    {
        TOTEMS, 
        GAPPLES, 
        CRYSTALS, 
        OBSIDIAN, 
        WEBS;
    }
    
    public enum Type
    {
        OLD, 
        NEW;
    }
    
    public enum Mode
    {
        CRYSTALS, 
        GAPPLES, 
        OBSIDIAN, 
        WEBS;
    }
}
