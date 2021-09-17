// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.features.modules.player.Freecam;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.event.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Strafe extends Module
{
    private static Strafe INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> limiter;
    private final Setting<Boolean> bhop2;
    private final Setting<Boolean> limiter2;
    private final Setting<Boolean> noLag;
    private final Setting<Integer> specialMoveSpeed;
    private final Setting<Integer> potionSpeed;
    private final Setting<Integer> potionSpeed2;
    private final Setting<Integer> dFactor;
    private final Setting<Integer> acceleration;
    private final Setting<Float> speedLimit;
    private final Setting<Float> speedLimit2;
    private final Setting<Integer> yOffset;
    private final Setting<Boolean> potion;
    private final Setting<Boolean> wait;
    private final Setting<Boolean> hopWait;
    private final Setting<Integer> startStage;
    private final Setting<Boolean> setPos;
    private final Setting<Boolean> setNull;
    private final Setting<Integer> setGroundLimit;
    private final Setting<Integer> groundFactor;
    private final Setting<Integer> step;
    private final Setting<Boolean> setGroundNoLag;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops;
    private boolean waitForGround;
    private final Timer timer;
    private int hops;
    
    public Strafe() {
        super("Strafe", "AirControl etc.", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.NCP, 0));
        this.limiter = (Setting<Boolean>)this.register(new Setting("Speed", "SetGround", 0.0, 0.0, (T)true, 0));
        this.bhop2 = (Setting<Boolean>)this.register(new Setting("Speed", "Hop", 0.0, 0.0, (T)true, 0));
        this.limiter2 = (Setting<Boolean>)this.register(new Setting("Speed", "Bhop", 0.0, 0.0, (T)false, 0));
        this.noLag = (Setting<Boolean>)this.register(new Setting("Speed", "NoLag", 0.0, 0.0, (T)false, 0));
        this.specialMoveSpeed = (Setting<Integer>)this.register(new Setting("Speed", (T)100, (T)0, (T)150));
        this.potionSpeed = (Setting<Integer>)this.register(new Setting("Speed1", (T)130, (T)0, (T)150));
        this.potionSpeed2 = (Setting<Integer>)this.register(new Setting("Speed2", (T)125, (T)0, (T)150));
        this.dFactor = (Setting<Integer>)this.register(new Setting("DFactor", (T)159, (T)100, (T)200));
        this.acceleration = (Setting<Integer>)this.register(new Setting("Accel", (T)2149, (T)1000, (T)2500));
        this.speedLimit = (Setting<Float>)this.register(new Setting("SpeedLimit", (T)35.0f, (T)20.0f, (T)60.0f));
        this.speedLimit2 = (Setting<Float>)this.register(new Setting("SpeedLimit2", (T)60.0f, (T)20.0f, (T)60.0f));
        this.yOffset = (Setting<Integer>)this.register(new Setting("YOffset", (T)400, (T)350, (T)500));
        this.potion = (Setting<Boolean>)this.register(new Setting("Speed", "Potion", 0.0, 0.0, (T)false, 0));
        this.wait = (Setting<Boolean>)this.register(new Setting("Speed", "Wait", 0.0, 0.0, (T)true, 0));
        this.hopWait = (Setting<Boolean>)this.register(new Setting("Speed", "HopWait", 0.0, 0.0, (T)true, 0));
        this.startStage = (Setting<Integer>)this.register(new Setting("Stage", (T)2, (T)0, (T)4));
        this.setPos = (Setting<Boolean>)this.register(new Setting("Speed", "SetPos", 0.0, 0.0, (T)true, 0));
        this.setNull = (Setting<Boolean>)this.register(new Setting("Speed", "SetNull", 0.0, 0.0, (T)false, 0));
        this.setGroundLimit = (Setting<Integer>)this.register(new Setting("GroundLimit", (T)138, (T)0, (T)1000));
        this.groundFactor = (Setting<Integer>)this.register(new Setting("GroundFactor", (T)13, (T)0, (T)50));
        this.step = (Setting<Integer>)this.register(new Setting("SetStep", (T)1, (T)0, (T)2, v -> this.mode.getValue() == Mode.BHOP));
        this.setGroundNoLag = (Setting<Boolean>)this.register(new Setting("Speed", "NoGroundLag", 0.0, 0.0, (T)true, 0));
        this.stage = 1;
        this.cooldownHops = 0;
        this.waitForGround = false;
        this.timer = new Timer();
        this.hops = 0;
        Strafe.INSTANCE = this;
    }
    
    public static Strafe getInstance() {
        if (Strafe.INSTANCE == null) {
            Strafe.INSTANCE = new Strafe();
        }
        return Strafe.INSTANCE;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Strafe.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = Objects.requireNonNull(Strafe.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }
        return baseSpeed;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        final BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    @Override
    public void onEnable() {
        if (!Strafe.mc.field_71439_g.field_70122_E) {
            this.waitForGround = true;
        }
        this.hops = 0;
        this.timer.reset();
        this.moveSpeed = getBaseMoveSpeed();
    }
    
    @Override
    public void onDisable() {
        this.hops = 0;
        this.moveSpeed = 0.0;
        this.stage = this.startStage.getValue();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Strafe.mc.field_71439_g.field_70165_t - Strafe.mc.field_71439_g.field_70169_q) * (Strafe.mc.field_71439_g.field_70165_t - Strafe.mc.field_71439_g.field_70169_q) + (Strafe.mc.field_71439_g.field_70161_v - Strafe.mc.field_71439_g.field_70166_s) * (Strafe.mc.field_71439_g.field_70161_v - Strafe.mc.field_71439_g.field_70166_s));
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (event.getStage() != 0 || this.shouldReturn()) {
            return;
        }
        if (!Strafe.mc.field_71439_g.field_70122_E) {
            if (this.wait.getValue() && this.waitForGround) {
                return;
            }
        }
        else {
            this.waitForGround = false;
        }
        if (this.mode.getValue() == Mode.NCP) {
            this.doNCP(event);
        }
        else if (this.mode.getValue() == Mode.BHOP) {
            float moveForward = Strafe.mc.field_71439_g.field_71158_b.field_192832_b;
            float moveStrafe = Strafe.mc.field_71439_g.field_71158_b.field_78902_a;
            float rotationYaw = Strafe.mc.field_71439_g.field_70177_z;
            if (this.step.getValue() == 1) {
                Strafe.mc.field_71439_g.field_70138_W = 0.6f;
            }
            if (this.limiter2.getValue() && Strafe.mc.field_71439_g.field_70122_E && CreepyWare.speedManager.getSpeedKpH() < this.speedLimit2.getValue()) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && round(Strafe.mc.field_71439_g.field_70163_u - (int)Strafe.mc.field_71439_g.field_70163_u, 3) == round(this.setGroundLimit.getValue() / 1000.0, 3) && (!this.setGroundNoLag.getValue() || EntityUtil.isEntityMoving((Entity)Strafe.mc.field_71439_g))) {
                if (this.setNull.getValue()) {
                    Strafe.mc.field_71439_g.field_70181_x = 0.0;
                }
                else {
                    final EntityPlayerSP field_71439_g = Strafe.mc.field_71439_g;
                    field_71439_g.field_70181_x -= this.groundFactor.getValue() / 100.0;
                    event.setY(event.getY() - this.groundFactor.getValue() / 100.0);
                    if (this.setPos.getValue()) {
                        final EntityPlayerSP field_71439_g2 = Strafe.mc.field_71439_g;
                        field_71439_g2.field_70163_u -= this.groundFactor.getValue() / 100.0;
                    }
                }
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
            }
            else if (this.stage == 2 && EntityUtil.isMoving()) {
                this.stage = 3;
                Strafe.mc.field_71439_g.field_70181_x = this.yOffset.getValue() / 1000.0;
                event.setY(this.yOffset.getValue() / 1000.0);
                if (this.cooldownHops > 0) {
                    --this.cooldownHops;
                }
                ++this.hops;
                final double accel = (this.acceleration.getValue() == 2149) ? 2.149802 : (this.acceleration.getValue() / 1000.0);
                this.moveSpeed *= accel;
            }
            else if (this.stage == 3) {
                this.stage = 4;
                final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                if (Strafe.mc.field_71441_e.func_184144_a((Entity)Strafe.mc.field_71439_g, Strafe.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Strafe.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || (Strafe.mc.field_71439_g.field_70124_G && this.stage > 0)) {
                    this.stage = (((!this.bhop2.getValue() || CreepyWare.speedManager.getSpeedKpH() < this.speedLimit.getValue()) && (Strafe.mc.field_71439_g.field_191988_bg != 0.0f || Strafe.mc.field_71439_g.field_70702_br != 0.0f)) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / this.dFactor.getValue();
            }
            this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
            if (this.hopWait.getValue() && this.limiter2.getValue() && this.hops < 2) {
                this.moveSpeed = EntityUtil.getMaxSpeed();
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
                event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
            }
            if (this.step.getValue() == 2) {
                Strafe.mc.field_71439_g.field_70138_W = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                this.timer.reset();
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    }
    
    private void doNCP(final MoveEvent event) {
        if (!this.limiter.getValue() && Strafe.mc.field_71439_g.field_70122_E) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (Strafe.mc.field_71439_g.field_191988_bg == 0.0f && Strafe.mc.field_71439_g.field_70702_br == 0.0f) {
                    break;
                }
                if (!Strafe.mc.field_71439_g.field_70122_E) {
                    break;
                }
                if (Strafe.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                    motionY += (Strafe.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1f;
                }
                event.setY(Strafe.mc.field_71439_g.field_70181_x = motionY);
                this.moveSpeed *= 2.149;
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - getBaseMoveSpeed());
                break;
            }
            default: {
                if (Strafe.mc.field_71441_e.func_184144_a((Entity)Strafe.mc.field_71439_g, Strafe.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Strafe.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || (Strafe.mc.field_71439_g.field_70124_G && this.stage > 0)) {
                    this.stage = (((!this.bhop2.getValue() || CreepyWare.speedManager.getSpeedKpH() < this.speedLimit.getValue()) && (Strafe.mc.field_71439_g.field_191988_bg != 0.0f || Strafe.mc.field_71439_g.field_70702_br != 0.0f)) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        double forward = Strafe.mc.field_71439_g.field_71158_b.field_192832_b;
        double strafe = Strafe.mc.field_71439_g.field_71158_b.field_78902_a;
        final double yaw = Strafe.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }
    
    private float getMultiplier() {
        float baseSpeed = this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Strafe.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = Objects.requireNonNull(Strafe.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c() + 1;
            baseSpeed = (float)((amplifier >= 2) ? this.potionSpeed2.getValue() : ((float)(int)this.potionSpeed.getValue()));
        }
        return baseSpeed / 100.0f;
    }
    
    private boolean shouldReturn() {
        return CreepyWare.moduleManager.isModuleEnabled(Freecam.class) || CreepyWare.moduleManager.isModuleEnabled(ElytraFlight.class);
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.noLag.getValue()) {
            this.stage = ((this.mode.getValue() == Mode.BHOP && (this.limiter2.getValue() || this.bhop2.getValue())) ? 1 : 4);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.NONE) {
            return null;
        }
        if (this.mode.getValue() == Mode.NCP) {
            return this.mode.currentEnumName().toUpperCase();
        }
        return this.mode.currentEnumName();
    }
    
    public enum Mode
    {
        NONE, 
        NCP, 
        BHOP;
    }
}
