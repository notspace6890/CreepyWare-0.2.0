// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import dev.fxcte.creepyware.event.events.DeathEvent;
import dev.fxcte.creepyware.event.events.ConnectionEvent;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Objects;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import dev.fxcte.creepyware.features.modules.combat.AutoCrystal;
import dev.fxcte.creepyware.features.modules.combat.AntiTrap;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.network.play.server.SPacketChat;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.event.events.PacketEvent;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.TextUtil;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.modules.Module;

public class Tracker extends Module
{
    private static Tracker instance;
    private final Timer timer;
    private final Set<BlockPos> manuallyPlaced;
    public Setting<TextUtil.Color> color;
    public Setting<Boolean> autoEnable;
    public Setting<Boolean> autoDisable;
    private EntityPlayer trackedPlayer;
    private int usedExp;
    private int usedStacks;
    private int usedCrystals;
    private int usedCStacks;
    private boolean shouldEnable;
    
    public Tracker() {
        super("Tracker", "Tracks players in 1v1s. Only good in duels tho!", Category.MISC, true, false, true);
        this.timer = new Timer();
        this.manuallyPlaced = new HashSet<BlockPos>();
        this.color = (Setting<TextUtil.Color>)this.register(new Setting("Speed", "Color", 0.0, 0.0, (T)TextUtil.Color.RED, 0));
        this.autoEnable = (Setting<Boolean>)this.register(new Setting("Speed", "AutoEnable", 0.0, 0.0, (T)false, 0));
        this.autoDisable = (Setting<Boolean>)this.register(new Setting("Speed", "AutoDisable", 0.0, 0.0, (T)true, 0));
        this.usedExp = 0;
        this.usedStacks = 0;
        this.usedCrystals = 0;
        this.usedCStacks = 0;
        this.shouldEnable = false;
        Tracker.instance = this;
    }
    
    public static Tracker getInstance() {
        if (Tracker.instance == null) {
            Tracker.instance = new Tracker();
        }
        return Tracker.instance;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (!Feature.fullNullCheck() && (this.autoEnable.getValue() || this.autoDisable.getValue()) && event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = event.getPacket();
            final String message = packet.func_148915_c().func_150254_d();
            if (this.autoEnable.getValue() && (message.contains("has accepted your duel request") || message.contains("Accepted the duel request from")) && !message.contains("<")) {
                Command.sendMessage("Tracker will enable in 5 seconds.");
                this.timer.reset();
                this.shouldEnable = true;
            }
            else if (this.autoDisable.getValue() && message.contains("has defeated") && message.contains(Tracker.mc.field_71439_g.func_70005_c_()) && !message.contains("<")) {
                this.disable();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (!Feature.fullNullCheck() && this.isOn() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (Tracker.mc.field_71439_g.func_184586_b(packet.field_187027_c).func_77973_b() == Items.field_185158_cP && !AntiTrap.placedPos.contains(packet.field_179725_b) && !AutoCrystal.placedPos.contains(packet.field_179725_b)) {
                this.manuallyPlaced.add(packet.field_179725_b);
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.shouldEnable && this.timer.passedS(5.0) && this.isOff()) {
            this.enable();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.isOff()) {
            return;
        }
        if (this.trackedPlayer == null) {
            this.trackedPlayer = EntityUtil.getClosestEnemy(1000.0);
        }
        else {
            if (this.usedStacks != this.usedExp / 64) {
                this.usedStacks = this.usedExp / 64;
                Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedStacks + " Stacks of EXP.", this.color.getValue()));
            }
            if (this.usedCStacks != this.usedCrystals / 64) {
                this.usedCStacks = this.usedCrystals / 64;
                Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedCStacks + " Stacks of Crystals.", this.color.getValue()));
            }
        }
    }
    
    public void onSpawnEntity(final Entity entity) {
        if (this.isOff()) {
            return;
        }
        if (entity instanceof EntityExpBottle && Objects.equals(Tracker.mc.field_71441_e.func_72890_a(entity, 3.0), this.trackedPlayer)) {
            ++this.usedExp;
        }
        if (entity instanceof EntityEnderCrystal) {
            if (AntiTrap.placedPos.contains(entity.func_180425_c().func_177977_b())) {
                AntiTrap.placedPos.remove(entity.func_180425_c().func_177977_b());
            }
            else if (this.manuallyPlaced.contains(entity.func_180425_c().func_177977_b())) {
                this.manuallyPlaced.remove(entity.func_180425_c().func_177977_b());
            }
            else if (!AutoCrystal.placedPos.contains(entity.func_180425_c().func_177977_b())) {
                ++this.usedCrystals;
            }
        }
    }
    
    @SubscribeEvent
    public void onConnection(final ConnectionEvent event) {
        if (this.isOff() || event.getStage() != 1) {
            return;
        }
        final String name = event.getName();
        if (this.trackedPlayer != null && name != null && name.equals(this.trackedPlayer.func_70005_c_()) && this.autoDisable.getValue()) {
            Command.sendMessage(name + " logged, Tracker disableing.");
            this.disable();
        }
    }
    
    @Override
    public void onToggle() {
        this.manuallyPlaced.clear();
        AntiTrap.placedPos.clear();
        this.shouldEnable = false;
        this.trackedPlayer = null;
        this.usedExp = 0;
        this.usedStacks = 0;
        this.usedCrystals = 0;
        this.usedCStacks = 0;
    }
    
    @Override
    public void onLogout() {
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onDeath(final DeathEvent event) {
        if (this.isOn() && (event.player.equals((Object)this.trackedPlayer) || event.player.equals((Object)Tracker.mc.field_71439_g))) {
            this.usedExp = 0;
            this.usedStacks = 0;
            this.usedCrystals = 0;
            this.usedCStacks = 0;
            if (this.autoDisable.getValue()) {
                this.disable();
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.trackedPlayer != null) {
            return this.trackedPlayer.func_70005_c_();
        }
        return null;
    }
}
