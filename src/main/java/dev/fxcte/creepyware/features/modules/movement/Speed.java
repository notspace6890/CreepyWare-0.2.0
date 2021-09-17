// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.util.MovementInput;
import dev.fxcte.creepyware.event.events.MoveEvent;
import dev.fxcte.creepyware.event.events.ClientEvent;
import java.util.Random;
import dev.fxcte.creepyware.util.BlockUtil;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Speed extends Module
{
    private static Speed INSTANCE;
    public Setting<Mode> mode;
    public Setting<Boolean> strafeJump;
    public Setting<Boolean> noShake;
    public Setting<Boolean> useTimer;
    public Setting<Double> zeroSpeed;
    public Setting<Double> speed;
    public Setting<Double> blocked;
    public Setting<Double> unblocked;
    public double startY;
    public boolean antiShake;
    public double minY;
    public boolean changeY;
    private double highChainVal;
    private double lowChainVal;
    private boolean oneTime;
    private double bounceHeight;
    private float move;
    private int vanillaCounter;
    
    public Speed() {
        super("Speed", "Makes you faster", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.INSTANT, 0));
        this.strafeJump = (Setting<Boolean>)this.register(new Setting("Jump", (T)false, v -> this.mode.getValue() == Mode.INSTANT));
        this.noShake = (Setting<Boolean>)this.register(new Setting("NoShake", (T)true, v -> this.mode.getValue() != Mode.INSTANT));
        this.useTimer = (Setting<Boolean>)this.register(new Setting("UseTimer", (T)false, v -> this.mode.getValue() != Mode.INSTANT));
        this.zeroSpeed = (Setting<Double>)this.register(new Setting("0-Speed", (T)0.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)10.0, (T)0.1, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.blocked = (Setting<Double>)this.register(new Setting("Blocked", (T)10.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.unblocked = (Setting<Double>)this.register(new Setting("Unblocked", (T)10.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.startY = 0.0;
        this.antiShake = false;
        this.minY = 0.0;
        this.changeY = false;
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.oneTime = false;
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        this.vanillaCounter = 0;
        this.setInstance();
    }
    
    public static Speed getInstance() {
        if (Speed.INSTANCE == null) {
            Speed.INSTANCE = new Speed();
        }
        return Speed.INSTANCE;
    }
    
    private void setInstance() {
        Speed.INSTANCE = this;
    }
    
    private boolean shouldReturn() {
        return CreepyWare.moduleManager.isModuleEnabled("Freecam") || CreepyWare.moduleManager.isModuleEnabled("Phase") || CreepyWare.moduleManager.isModuleEnabled("ElytraFlight") || CreepyWare.moduleManager.isModuleEnabled("Strafe") || CreepyWare.moduleManager.isModuleEnabled("Flight");
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldReturn() || Speed.mc.field_71439_g.func_70093_af() || Speed.mc.field_71439_g.func_70090_H() || Speed.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                this.doBoost();
                break;
            }
            case ACCEL: {
                this.doAccel();
                break;
            }
            case ONGROUND: {
                this.doOnground();
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() != Mode.VANILLA || Feature.nullCheck()) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                this.vanillaCounter = (this.vanilla() ? (++this.vanillaCounter) : 0);
                if (this.vanillaCounter != 4) {
                    break;
                }
                this.changeY = true;
                this.minY = Speed.mc.field_71439_g.func_174813_aQ().field_72338_b + (Speed.mc.field_71441_e.func_180495_p(Speed.mc.field_71439_g.func_180425_c()).func_185904_a().func_76230_c() ? (-this.blocked.getValue() / 10.0) : (this.unblocked.getValue() / 10.0)) + this.getJumpBoostModifier();
            }
            case 1: {
                if (this.vanillaCounter == 3) {
                    final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
                    field_71439_g.field_70159_w *= this.zeroSpeed.getValue() / 10.0;
                    final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
                    field_71439_g2.field_70179_y *= this.zeroSpeed.getValue() / 10.0;
                    break;
                }
                if (this.vanillaCounter != 4) {
                    break;
                }
                final EntityPlayerSP field_71439_g3 = Speed.mc.field_71439_g;
                field_71439_g3.field_70159_w /= this.speed.getValue() / 10.0;
                final EntityPlayerSP field_71439_g4 = Speed.mc.field_71439_g;
                field_71439_g4.field_70179_y /= this.speed.getValue() / 10.0;
                this.vanillaCounter = 2;
                break;
            }
        }
    }
    
    private double getJumpBoostModifier() {
        double boost = 0.0;
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j)).func_76458_c();
            boost *= 1.0 + 0.2 * amplifier;
        }
        return boost;
    }
    
    private boolean vanillaCheck() {
        if (Speed.mc.field_71439_g.field_70122_E) {}
        return false;
    }
    
    private boolean vanilla() {
        return Speed.mc.field_71439_g.field_70122_E;
    }
    
    private void doBoost() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.field_71439_g.func_184187_bx() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.15f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.2f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.225f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.25f;
                }
                if (this.lowChainVal >= 7.0) {
                    this.move = 0.27895f;
                }
                if (this.useTimer.getValue()) {
                    CreepyWare.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.325f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.4f;
                }
                if (this.highChainVal >= 6.0) {
                    this.move = 0.43395f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        CreepyWare.timerManager.setTimer(1.3f);
                    }
                    else {
                        CreepyWare.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        }
        else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.antiShake = false;
            this.speedOff();
        }
    }
    
    private void doAccel() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.field_71439_g.func_184187_bx() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue()) {
                    CreepyWare.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.175f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        CreepyWare.timerManager.setTimer(1.3f);
                    }
                    else {
                        CreepyWare.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        }
        else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }
    
    private void doOnground() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.field_71439_g.func_184187_bx() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue()) {
                    CreepyWare.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.2f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        CreepyWare.timerManager.setTimer(1.3f);
                    }
                    else {
                        CreepyWare.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        }
        else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.ONGROUND || this.mode.getValue() == Mode.BOOST) {
            Speed.mc.field_71439_g.field_70181_x = -0.1;
        }
        this.changeY = false;
        CreepyWare.timerManager.setTimer(1.0f);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.antiShake = false;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
            Speed.mc.field_71439_g.field_70181_x = -0.1;
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    @SubscribeEvent
    public void onMode(final MoveEvent event) {
        if (!this.shouldReturn() && event.getStage() == 0 && this.mode.getValue() == Mode.INSTANT && !Feature.nullCheck() && !Speed.mc.field_71439_g.func_70093_af() && !Speed.mc.field_71439_g.func_70090_H() && !Speed.mc.field_71439_g.func_180799_ab() && (Speed.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || Speed.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f)) {
            if (Speed.mc.field_71439_g.field_70122_E && this.strafeJump.getValue()) {
                event.setY(Speed.mc.field_71439_g.field_70181_x = 0.4);
            }
            final MovementInput movementInput = Speed.mc.field_71439_g.field_71158_b;
            float moveForward = movementInput.field_192832_b;
            float moveStrafe = movementInput.field_78902_a;
            float rotationYaw = Speed.mc.field_71439_g.field_70177_z;
            if (moveForward == 0.0 && moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else {
                if (moveForward != 0.0) {
                    if (moveStrafe > 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? -45 : 45);
                    }
                    else if (moveStrafe < 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    if (moveForward != 0.0f) {
                        moveForward = ((moveForward > 0.0) ? 1.0f : -1.0f);
                    }
                }
                moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0) ? 1.0f : -1.0f));
                event.setX(moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ(moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }
    
    private void speedOff() {
        final float yaw = (float)Math.toRadians(Speed.mc.field_71439_g.field_70177_z);
        if (BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g)) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
                field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.15;
                final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
                field_71439_g2.field_70179_y += MathUtil.cos(yaw) * 0.15;
            }
        }
        else if (Speed.mc.field_71439_g.field_70123_F) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                final EntityPlayerSP field_71439_g3 = Speed.mc.field_71439_g;
                field_71439_g3.field_70159_w -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP field_71439_g4 = Speed.mc.field_71439_g;
                field_71439_g4.field_70179_y += MathUtil.cos(yaw) * 0.03;
            }
        }
        else if (!BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                final EntityPlayerSP field_71439_g5 = Speed.mc.field_71439_g;
                field_71439_g5.field_70159_w -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP field_71439_g6 = Speed.mc.field_71439_g;
                field_71439_g6.field_70179_y += MathUtil.cos(yaw) * 0.03;
            }
        }
        else {
            Speed.mc.field_71439_g.field_70159_w = 0.0;
            Speed.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    static {
        Speed.INSTANCE = new Speed();
    }
    
    public enum Mode
    {
        INSTANT, 
        ONGROUND, 
        ACCEL, 
        BOOST, 
        VANILLA;
    }
}
