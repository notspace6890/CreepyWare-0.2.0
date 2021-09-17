// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import java.util.Iterator;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import org.lwjgl.input.Mouse;
import net.minecraft.item.ItemEndCrystal;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.item.ItemBow;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class BowSpam extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> bowbomb;
    public Setting<Boolean> allowOffhand;
    public Setting<Integer> ticks;
    public Setting<Integer> delay;
    public Setting<Boolean> tpsSync;
    public Setting<Boolean> autoSwitch;
    public Setting<Boolean> onlyWhenSave;
    public Setting<Target> targetMode;
    public Setting<Float> range;
    public Setting<Float> health;
    public Setting<Float> ownHealth;
    private final Timer timer;
    private boolean offhand;
    private boolean switched;
    private int lastHotbarSlot;
    
    public BowSpam() {
        super("BowSpam", "Spams your bow", Category.COMBAT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.FAST, 0));
        this.bowbomb = (Setting<Boolean>)this.register(new Setting("BowBomb", (T)false, v -> this.mode.getValue() != Mode.BOWBOMB));
        this.allowOffhand = (Setting<Boolean>)this.register(new Setting("Offhand", (T)true, v -> this.mode.getValue() != Mode.AUTORELEASE));
        this.ticks = (Setting<Integer>)this.register(new Setting("Ticks", (T)3, (T)0, (T)20, v -> this.mode.getValue() == Mode.BOWBOMB || this.mode.getValue() == Mode.FAST, "Speed"));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)50, (T)0, (T)500, v -> this.mode.getValue() == Mode.AUTORELEASE, "Speed"));
        this.tpsSync = (Setting<Boolean>)this.register(new Setting("Speed", "TpsSync", 0.0, 0.0, (T)true, 0));
        this.autoSwitch = (Setting<Boolean>)this.register(new Setting("Speed", "AutoSwitch", 0.0, 0.0, (T)false, 0));
        this.onlyWhenSave = (Setting<Boolean>)this.register(new Setting("OnlyWhenSave", (T)true, v -> this.autoSwitch.getValue()));
        this.targetMode = (Setting<Target>)this.register(new Setting("Target", (T)Target.LOWEST, v -> this.autoSwitch.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)3.0f, (T)0.0f, (T)6.0f, v -> this.autoSwitch.getValue(), "Range of the target"));
        this.health = (Setting<Float>)this.register(new Setting("Lethal", (T)6.0f, (T)0.1f, (T)36.0f, v -> this.autoSwitch.getValue(), "When should it switch?"));
        this.ownHealth = (Setting<Float>)this.register(new Setting("OwnHealth", (T)20.0f, (T)0.1f, (T)36.0f, v -> this.autoSwitch.getValue(), "Own Health."));
        this.timer = new Timer();
        this.offhand = false;
        this.switched = false;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onEnable() {
        this.lastHotbarSlot = BowSpam.mc.field_71439_g.field_71071_by.field_70461_c;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.autoSwitch.getValue() && InventoryUtil.findHotbarBlock(ItemBow.class) != -1 && this.ownHealth.getValue() <= EntityUtil.getHealth((Entity)BowSpam.mc.field_71439_g) && (!this.onlyWhenSave.getValue() || EntityUtil.isSafe((Entity)BowSpam.mc.field_71439_g))) {
            final EntityPlayer target = this.getTarget();
            final AutoCrystal crystal;
            if (target != null && (!(crystal = CreepyWare.moduleManager.getModuleByClass(AutoCrystal.class)).isOn() || !InventoryUtil.holdingItem(ItemEndCrystal.class))) {
                final Vec3d pos = target.func_174791_d();
                final double xPos = pos.field_72450_a;
                double yPos = pos.field_72448_b;
                final double zPos = pos.field_72449_c;
                if (BowSpam.mc.field_71439_g.func_70685_l((Entity)target)) {
                    yPos += target.eyeHeight;
                }
                else {
                    if (!EntityUtil.canEntityFeetBeSeen((Entity)target)) {
                        return;
                    }
                    yPos += 0.1;
                }
                if (!(BowSpam.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow)) {
                    this.lastHotbarSlot = BowSpam.mc.field_71439_g.field_71071_by.field_70461_c;
                    InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
                    BowSpam.mc.field_71474_y.field_74313_G.field_74513_e = true;
                    this.switched = true;
                }
                CreepyWare.rotationManager.lookAtVec3d(xPos, yPos, zPos);
                if (BowSpam.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow) {
                    this.switched = true;
                }
            }
        }
        else if (event.getStage() == 0 && this.switched && this.lastHotbarSlot != -1) {
            InventoryUtil.switchToHotbarSlot(this.lastHotbarSlot, false);
            BowSpam.mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
            this.switched = false;
        }
        else {
            BowSpam.mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
        }
        if (this.mode.getValue() == Mode.FAST && (this.offhand || BowSpam.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && BowSpam.mc.field_71439_g.func_184587_cr()) {
            final float f = (float)BowSpam.mc.field_71439_g.func_184612_cw();
            final float f2 = this.ticks.getValue();
            final float f3 = this.tpsSync.getValue() ? CreepyWare.serverManager.getTpsFactor() : 1.0f;
            if (f >= f2 * f3) {
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, BowSpam.mc.field_71439_g.func_174811_aO()));
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                BowSpam.mc.field_71439_g.func_184597_cx();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        this.offhand = (BowSpam.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151031_f && this.allowOffhand.getValue());
        switch (this.mode.getValue()) {
            case AUTORELEASE: {
                if (!this.offhand && !(BowSpam.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow)) {
                    break;
                }
                if (!this.timer.passedMs((int)(this.delay.getValue() * (this.tpsSync.getValue() ? CreepyWare.serverManager.getTpsFactor() : 1.0f)))) {
                    break;
                }
                BowSpam.mc.field_71442_b.func_78766_c((EntityPlayer)BowSpam.mc.field_71439_g);
                this.timer.reset();
                break;
            }
            case BOWBOMB: {
                if (!this.offhand && !(BowSpam.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow)) {
                    break;
                }
                if (!BowSpam.mc.field_71439_g.func_184587_cr()) {
                    break;
                }
                final float f = (float)BowSpam.mc.field_71439_g.func_184612_cw();
                final float f2 = this.ticks.getValue();
                final float f3 = this.tpsSync.getValue() ? CreepyWare.serverManager.getTpsFactor() : 1.0f;
                if (f < f2 * f3) {
                    break;
                }
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, BowSpam.mc.field_71439_g.func_174811_aO()));
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(BowSpam.mc.field_71439_g.field_70165_t, BowSpam.mc.field_71439_g.field_70163_u - 0.0624, BowSpam.mc.field_71439_g.field_70161_v, BowSpam.mc.field_71439_g.field_70177_z, BowSpam.mc.field_71439_g.field_70125_A, false));
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(BowSpam.mc.field_71439_g.field_70165_t, BowSpam.mc.field_71439_g.field_70163_u - 999.0, BowSpam.mc.field_71439_g.field_70161_v, BowSpam.mc.field_71439_g.field_70177_z, BowSpam.mc.field_71439_g.field_70125_A, true));
                BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                BowSpam.mc.field_71439_g.func_184597_cx();
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketPlayerDigging packet;
        if (event.getStage() == 0 && this.bowbomb.getValue() && this.mode.getValue() != Mode.BOWBOMB && event.getPacket() instanceof CPacketPlayerDigging && (packet = event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && (this.offhand || BowSpam.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && BowSpam.mc.field_71439_g.func_184612_cw() >= 20 && !BowSpam.mc.field_71439_g.field_70122_E) {
            BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BowSpam.mc.field_71439_g.field_70165_t, BowSpam.mc.field_71439_g.field_70163_u - 0.10000000149011612, BowSpam.mc.field_71439_g.field_70161_v, false));
            BowSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BowSpam.mc.field_71439_g.field_70165_t, BowSpam.mc.field_71439_g.field_70163_u - 10000.0, BowSpam.mc.field_71439_g.field_70161_v, true));
        }
    }
    
    private EntityPlayer getTarget() {
        double maxHealth = 36.0;
        EntityPlayer target = null;
        for (final EntityPlayer player : BowSpam.mc.field_71441_e.field_73010_i) {
            if (player != null && !EntityUtil.isDead((Entity)player) && EntityUtil.getHealth((Entity)player) <= this.health.getValue() && !player.equals((Object)BowSpam.mc.field_71439_g) && !CreepyWare.friendManager.isFriend(player) && BowSpam.mc.field_71439_g.func_70068_e((Entity)player) <= MathUtil.square(this.range.getValue())) {
                if (!BowSpam.mc.field_71439_g.func_70685_l((Entity)player) && !EntityUtil.canEntityFeetBeSeen((Entity)player)) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    maxHealth = EntityUtil.getHealth((Entity)player);
                }
                if (this.targetMode.getValue() == Target.CLOSEST && BowSpam.mc.field_71439_g.func_70068_e((Entity)player) < BowSpam.mc.field_71439_g.func_70068_e((Entity)target)) {
                    target = player;
                    maxHealth = EntityUtil.getHealth((Entity)player);
                }
                if (this.targetMode.getValue() != Target.LOWEST) {
                    continue;
                }
                if (EntityUtil.getHealth((Entity)player) >= maxHealth) {
                    continue;
                }
                target = player;
                maxHealth = EntityUtil.getHealth((Entity)player);
            }
        }
        return target;
    }
    
    public enum Mode
    {
        FAST, 
        AUTORELEASE, 
        BOWBOMB;
    }
    
    public enum Target
    {
        CLOSEST, 
        LOWEST;
    }
}
