// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.inventory.ClickType;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.item.ItemSword;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.util.DamageUtil;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.EntityUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.util.Timer;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.features.modules.Module;

public class Killaura extends Module
{
    public static Entity target;
    private final Timer timer;
    public Setting<Float> range;
    public Setting<Boolean> autoSwitch;
    public Setting<Boolean> delay;
    public Setting<Boolean> rotate;
    public Setting<Boolean> stay;
    public Setting<Boolean> armorBreak;
    public Setting<Boolean> eating;
    public Setting<Boolean> onlySharp;
    public Setting<Boolean> teleport;
    public Setting<Float> raytrace;
    public Setting<Float> teleportRange;
    public Setting<Boolean> lagBack;
    public Setting<Boolean> teekaydelay;
    public Setting<Integer> time32k;
    public Setting<Integer> multi;
    public Setting<Boolean> multi32k;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    public Setting<Boolean> tps;
    public Setting<Boolean> packet;
    public Setting<Boolean> swing;
    public Setting<Boolean> sneak;
    public Setting<Boolean> info;
    private final Setting<TargetMode> targetMode;
    public Setting<Float> health;
    
    public Killaura() {
        super("Killaura", "Kills aura.", Category.COMBAT, true, false, false);
        this.timer = new Timer();
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)6.0f, (T)0.1f, (T)7.0f));
        this.autoSwitch = (Setting<Boolean>)this.register(new Setting("Speed", "AutoSwitch", 0.0, 0.0, (T)false, 0));
        this.delay = (Setting<Boolean>)this.register(new Setting("Speed", "Delay", 0.0, 0.0, (T)true, 0));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)true, 0));
        this.stay = (Setting<Boolean>)this.register(new Setting("Stay", (T)true, v -> this.rotate.getValue()));
        this.armorBreak = (Setting<Boolean>)this.register(new Setting("Speed", "ArmorBreak", 0.0, 0.0, (T)false, 0));
        this.eating = (Setting<Boolean>)this.register(new Setting("Speed", "Eating", 0.0, 0.0, (T)true, 0));
        this.onlySharp = (Setting<Boolean>)this.register(new Setting("Speed", "Axe/Sword", 0.0, 0.0, (T)true, 0));
        this.teleport = (Setting<Boolean>)this.register(new Setting("Speed", "Teleport", 0.0, 0.0, (T)false, 0));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", (T)6.0f, (T)0.1f, (T)7.0f, v -> !this.teleport.getValue(), "Wall Range."));
        this.teleportRange = (Setting<Float>)this.register(new Setting("TpRange", (T)15.0f, (T)0.1f, (T)50.0f, v -> this.teleport.getValue(), "Teleport Range."));
        this.lagBack = (Setting<Boolean>)this.register(new Setting("LagBack", (T)true, v -> this.teleport.getValue()));
        this.teekaydelay = (Setting<Boolean>)this.register(new Setting("Speed", "32kDelay", 0.0, 0.0, (T)false, 0));
        this.time32k = (Setting<Integer>)this.register(new Setting("32kTime", (T)5, (T)1, (T)50));
        this.multi = (Setting<Integer>)this.register(new Setting("32kPackets", (T)2, v -> !this.teekaydelay.getValue()));
        this.multi32k = (Setting<Boolean>)this.register(new Setting("Speed", "Multi32k", 0.0, 0.0, (T)false, 0));
        this.players = (Setting<Boolean>)this.register(new Setting("Speed", "Players", 0.0, 0.0, (T)true, 0));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Speed", "Mobs", 0.0, 0.0, (T)false, 0));
        this.animals = (Setting<Boolean>)this.register(new Setting("Speed", "Animals", 0.0, 0.0, (T)false, 0));
        this.vehicles = (Setting<Boolean>)this.register(new Setting("Speed", "Entities", 0.0, 0.0, (T)false, 0));
        this.projectiles = (Setting<Boolean>)this.register(new Setting("Speed", "Projectiles", 0.0, 0.0, (T)false, 0));
        this.tps = (Setting<Boolean>)this.register(new Setting("Speed", "TpsSync", 0.0, 0.0, (T)true, 0));
        this.packet = (Setting<Boolean>)this.register(new Setting("Speed", "Packet", 0.0, 0.0, (T)false, 0));
        this.swing = (Setting<Boolean>)this.register(new Setting("Speed", "Swing", 0.0, 0.0, (T)true, 0));
        this.sneak = (Setting<Boolean>)this.register(new Setting("Speed", "State", 0.0, 0.0, (T)false, 0));
        this.info = (Setting<Boolean>)this.register(new Setting("Speed", "Info", 0.0, 0.0, (T)true, 0));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Speed", "Target", 0.0, 0.0, (T)TargetMode.CLOSEST, 0));
        this.health = (Setting<Float>)this.register(new Setting("Health", (T)6.0f, (T)0.1f, (T)36.0f, v -> this.targetMode.getValue() == TargetMode.SMART));
    }
    
    @Override
    public void onTick() {
        if (!this.rotate.getValue()) {
            this.doKillaura();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue()) {
            if (this.stay.getValue() && Killaura.target != null) {
                CreepyWare.rotationManager.lookAtEntity(Killaura.target);
            }
            this.doKillaura();
        }
    }
    
    private void doKillaura() {
        if (this.onlySharp.getValue() && !EntityUtil.holdingWeapon((EntityPlayer)Killaura.mc.field_71439_g)) {
            Killaura.target = null;
            return;
        }
        int n = 0;
        if (this.delay.getValue() && (!EntityUtil.holding32k((EntityPlayer)Killaura.mc.field_71439_g) || this.teekaydelay.getValue())) {
            n = (int)(DamageUtil.getCooldownByWeapon((EntityPlayer)Killaura.mc.field_71439_g) * (this.tps.getValue() ? CreepyWare.serverManager.getTpsFactor() : 1.0f));
        }
        final int wait = n;
        if (!this.timer.passedMs(wait) || (!this.eating.getValue() && Killaura.mc.field_71439_g.func_184587_cr() && (!Killaura.mc.field_71439_g.func_184592_cb().func_77973_b().equals(Items.field_185159_cQ) || Killaura.mc.field_71439_g.func_184600_cs() != EnumHand.OFF_HAND))) {
            return;
        }
        if (this.targetMode.getValue() != TargetMode.FOCUS || Killaura.target == null || (Killaura.mc.field_71439_g.func_70068_e(Killaura.target) >= MathUtil.square(this.range.getValue()) && (!this.teleport.getValue() || Killaura.mc.field_71439_g.func_70068_e(Killaura.target) >= MathUtil.square(this.teleportRange.getValue()))) || (!Killaura.mc.field_71439_g.func_70685_l(Killaura.target) && !EntityUtil.canEntityFeetBeSeen(Killaura.target) && Killaura.mc.field_71439_g.func_70068_e(Killaura.target) >= MathUtil.square(this.raytrace.getValue()) && !this.teleport.getValue())) {
            Killaura.target = this.getTarget();
        }
        if (Killaura.target == null) {
            return;
        }
        final int sword;
        if (this.autoSwitch.getValue() && (sword = InventoryUtil.findHotbarBlock(ItemSword.class)) != -1) {
            InventoryUtil.switchToHotbarSlot(sword, false);
        }
        if (this.rotate.getValue()) {
            CreepyWare.rotationManager.lookAtEntity(Killaura.target);
        }
        if (this.teleport.getValue()) {
            CreepyWare.positionManager.setPositionPacket(Killaura.target.field_70165_t, EntityUtil.canEntityFeetBeSeen(Killaura.target) ? Killaura.target.field_70163_u : (Killaura.target.field_70163_u + Killaura.target.func_70047_e()), Killaura.target.field_70161_v, true, true, !this.lagBack.getValue());
        }
        if (EntityUtil.holding32k((EntityPlayer)Killaura.mc.field_71439_g) && !this.teekaydelay.getValue()) {
            if (this.multi32k.getValue()) {
                for (final EntityPlayer player : Killaura.mc.field_71441_e.field_73010_i) {
                    if (!EntityUtil.isValid((Entity)player, this.range.getValue())) {
                        continue;
                    }
                    this.teekayAttack((Entity)player);
                }
            }
            else {
                this.teekayAttack(Killaura.target);
            }
            this.timer.reset();
            return;
        }
        if (this.armorBreak.getValue()) {
            Killaura.mc.field_71442_b.func_187098_a(Killaura.mc.field_71439_g.field_71069_bz.field_75152_c, 9, Killaura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)Killaura.mc.field_71439_g);
            EntityUtil.attackEntity(Killaura.target, this.packet.getValue(), this.swing.getValue());
            Killaura.mc.field_71442_b.func_187098_a(Killaura.mc.field_71439_g.field_71069_bz.field_75152_c, 9, Killaura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)Killaura.mc.field_71439_g);
            EntityUtil.attackEntity(Killaura.target, this.packet.getValue(), this.swing.getValue());
        }
        else {
            final boolean sneaking = Killaura.mc.field_71439_g.func_70093_af();
            final boolean sprint = Killaura.mc.field_71439_g.func_70051_ag();
            if (this.sneak.getValue()) {
                if (sneaking) {
                    Killaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Killaura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                if (sprint) {
                    Killaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Killaura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SPRINTING));
                }
            }
            EntityUtil.attackEntity(Killaura.target, this.packet.getValue(), this.swing.getValue());
            if (this.sneak.getValue()) {
                if (sprint) {
                    Killaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Killaura.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (sneaking) {
                    Killaura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Killaura.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
        }
        this.timer.reset();
    }
    
    private void teekayAttack(final Entity entity) {
        for (int i = 0; i < this.multi.getValue(); ++i) {
            this.startEntityAttackThread(entity, i * this.time32k.getValue());
        }
    }
    
    private void startEntityAttackThread(final Entity entity, final int time) {
        final Timer timer;
        new Thread(() -> {
            timer = new Timer();
            timer.reset();
            try {
                Thread.sleep(time);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            EntityUtil.attackEntity(entity, true, this.swing.getValue());
        }).start();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (double)(this.teleport.getValue() ? this.teleportRange.getValue() : ((double)(float)this.range.getValue()));
        double maxHealth = 36.0;
        for (final Entity entity : Killaura.mc.field_71441_e.field_72996_f) {
            if (((this.players.getValue() && entity instanceof EntityPlayer) || (this.animals.getValue() && EntityUtil.isPassive(entity)) || (this.mobs.getValue() && EntityUtil.isMobAggressive(entity)) || (this.vehicles.getValue() && EntityUtil.isVehicle(entity)) || (this.projectiles.getValue() && EntityUtil.isProjectile(entity))) && (!(entity instanceof EntityLivingBase) || !EntityUtil.isntValid(entity, distance))) {
                if (!this.teleport.getValue() && !Killaura.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && Killaura.mc.field_71439_g.func_70068_e(entity) > MathUtil.square(this.raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (this.targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < this.health.getValue()) {
                        target = entity;
                        break;
                    }
                    if (this.targetMode.getValue() != TargetMode.HEALTH && Killaura.mc.field_71439_g.func_70068_e(entity) < distance) {
                        target = entity;
                        distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (this.targetMode.getValue() != TargetMode.HEALTH) {
                        continue;
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && Killaura.target instanceof EntityPlayer) {
            return Killaura.target.func_70005_c_();
        }
        return null;
    }
    
    public enum TargetMode
    {
        FOCUS, 
        CLOSEST, 
        HEALTH, 
        SMART;
    }
}
