// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.features.Feature;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.MathUtil;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.util.math.MathHelper;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.item.ItemElytra;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import dev.fxcte.creepyware.event.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.modules.Module;

public class ElytraFlight extends Module
{
    private static ElytraFlight INSTANCE;
    private final Timer timer;
    private final Timer bypassTimer;
    public Setting<Mode> mode;
    public Setting<Integer> devMode;
    public Setting<Float> speed;
    public Setting<Float> vSpeed;
    public Setting<Float> hSpeed;
    public Setting<Float> glide;
    public Setting<Float> tooBeeSpeed;
    public Setting<Boolean> autoStart;
    public Setting<Boolean> disableInLiquid;
    public Setting<Boolean> infiniteDura;
    public Setting<Boolean> noKick;
    public Setting<Boolean> allowUp;
    public Setting<Boolean> lockPitch;
    private boolean vertical;
    private Double posX;
    private Double flyHeight;
    private Double posZ;
    
    public ElytraFlight() {
        super("ElytraFlight", "Makes Elytra Flight better.", Category.MOVEMENT, true, false, false);
        this.timer = new Timer();
        this.bypassTimer = new Timer();
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.FLY, 0));
        this.devMode = (Setting<Integer>)this.register(new Setting("Type", (T)2, (T)1, (T)3, v -> this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER, "EventMode"));
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)1.0f, (T)0.0f, (T)10.0f, v -> this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE, "The Speed."));
        this.vSpeed = (Setting<Float>)this.register(new Setting("VSpeed", (T)0.3f, (T)0.0f, (T)10.0f, v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE, "Vertical Speed"));
        this.hSpeed = (Setting<Float>)this.register(new Setting("HSpeed", (T)1.0f, (T)0.0f, (T)10.0f, v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE, "Horizontal Speed"));
        this.glide = (Setting<Float>)this.register(new Setting("Glide", (T)1.0E-4f, (T)0.0f, (T)0.2f, v -> this.mode.getValue() == Mode.BETTER, "Glide Speed"));
        this.tooBeeSpeed = (Setting<Float>)this.register(new Setting("TooBeeSpeed", (T)1.8000001f, (T)1.0f, (T)2.0f, v -> this.mode.getValue() == Mode.TOOBEE, "Speed for flight on 2b2t"));
        this.autoStart = (Setting<Boolean>)this.register(new Setting("Speed", "AutoStart", 0.0, 0.0, (T)true, 0));
        this.disableInLiquid = (Setting<Boolean>)this.register(new Setting("Speed", "NoLiquid", 0.0, 0.0, (T)true, 0));
        this.infiniteDura = (Setting<Boolean>)this.register(new Setting("Speed", "InfiniteDura", 0.0, 0.0, (T)false, 0));
        this.noKick = (Setting<Boolean>)this.register(new Setting("NoKick", (T)false, v -> this.mode.getValue() == Mode.PACKET));
        this.allowUp = (Setting<Boolean>)this.register(new Setting("AllowUp", (T)true, v -> this.mode.getValue() == Mode.BETTER));
        this.lockPitch = (Setting<Boolean>)this.register(new Setting("Speed", "LockPitch", 0.0, 0.0, (T)false, 0));
        this.setInstance();
    }
    
    public static ElytraFlight getInstance() {
        if (ElytraFlight.INSTANCE == null) {
            ElytraFlight.INSTANCE = new ElytraFlight();
        }
        return ElytraFlight.INSTANCE;
    }
    
    private void setInstance() {
        ElytraFlight.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.BETTER && !this.autoStart.getValue() && this.devMode.getValue() == 1) {
            ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.flyHeight = null;
        this.posX = null;
        this.posZ = null;
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 1 && ElytraFlight.mc.field_71439_g.func_184613_cA()) {
            ElytraFlight.mc.field_71439_g.field_70159_w = 0.0;
            ElytraFlight.mc.field_71439_g.field_70181_x = -1.0E-4;
            ElytraFlight.mc.field_71439_g.field_70179_y = 0.0;
            final double forwardInput = ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b;
            final double strafeInput = ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a;
            final double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlight.mc.field_71439_g.field_70177_z);
            final double forward = result[0];
            final double strafe = result[1];
            final double yaw = result[2];
            if (forwardInput != 0.0 || strafeInput != 0.0) {
                ElytraFlight.mc.field_71439_g.field_70159_w = forward * this.speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * this.speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0));
                ElytraFlight.mc.field_71439_g.field_70179_y = forward * this.speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0)) - strafe * this.speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0));
            }
            if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70181_x = -1.0;
            }
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
            final CPacketPlayer packet = event.getPacket();
            if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {}
        }
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEEBYPASS) {
            final CPacketPlayer packet = event.getPacket();
            if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {}
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.mode.getValue() == Mode.OHARE) {
            final ItemStack itemstack = ElytraFlight.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST);
            if (itemstack.func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d(itemstack) && ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                event.setY(ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d() ? ((double)this.vSpeed.getValue()) : (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d() ? (-this.vSpeed.getValue()) : 0.0));
                ElytraFlight.mc.field_71439_g.func_70024_g(0.0, ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d() ? ((double)this.vSpeed.getValue()) : (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d() ? (-this.vSpeed.getValue()) : 0.0), 0.0);
                ElytraFlight.mc.field_71439_g.field_184835_a = 0.0f;
                ElytraFlight.mc.field_71439_g.field_184836_b = 0.0f;
                ElytraFlight.mc.field_71439_g.field_184837_c = 0.0f;
                ElytraFlight.mc.field_71439_g.field_70701_bs = (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d() ? this.vSpeed.getValue() : (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d() ? (-this.vSpeed.getValue()) : 0.0f));
                double forward = ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b;
                double strafe = ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a;
                float yaw = ElytraFlight.mc.field_71439_g.field_70177_z;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += ((forward > 0.0) ? -45 : 45);
                        }
                        else if (strafe < 0.0) {
                            yaw += ((forward > 0.0) ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        }
                        else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * this.hSpeed.getValue() * cos + strafe * this.hSpeed.getValue() * sin);
                    event.setZ(forward * this.hSpeed.getValue() * sin - strafe * this.hSpeed.getValue() * cos);
                }
            }
        }
        else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 3) {
            if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                event.setX(0.0);
                event.setY(-1.0E-4);
                event.setZ(0.0);
                final double forwardInput = ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b;
                final double strafeInput = ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a;
                final double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlight.mc.field_71439_g.field_70177_z);
                final double forward2 = result[0];
                final double strafe2 = result[1];
                final double yaw2 = result[2];
                if (forwardInput != 0.0 || strafeInput != 0.0) {
                    event.setX(forward2 * this.speed.getValue() * Math.cos(Math.toRadians(yaw2 + 90.0)) + strafe2 * this.speed.getValue() * Math.sin(Math.toRadians(yaw2 + 90.0)));
                    event.setY(forward2 * this.speed.getValue() * Math.sin(Math.toRadians(yaw2 + 90.0)) - strafe2 * this.speed.getValue() * Math.cos(Math.toRadians(yaw2 + 90.0)));
                }
                if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    event.setY(-1.0);
                }
            }
        }
        else if (this.mode.getValue() == Mode.TOOBEE) {
            if (!ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                return;
            }
            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78901_c) {
                return;
            }
            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78899_d) {
                ElytraFlight.mc.field_71439_g.field_70181_x = -(this.tooBeeSpeed.getValue() / 2.0f);
                event.setY(-(this.speed.getValue() / 2.0f));
            }
            else if (event.getY() != -1.01E-4) {
                event.setY(-1.01E-4);
                ElytraFlight.mc.field_71439_g.field_70181_x = -1.01E-4;
            }
            this.setMoveSpeed(event, this.tooBeeSpeed.getValue());
        }
        else if (this.mode.getValue() == Mode.TOOBEEBYPASS) {
            if (!ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                return;
            }
            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78901_c) {
                return;
            }
            if (this.lockPitch.getValue()) {
                ElytraFlight.mc.field_71439_g.field_70125_A = 4.0f;
            }
            if (CreepyWare.speedManager.getSpeedKpH() > 180.0) {
                return;
            }
            final double yaw3 = Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
            final EntityPlayerSP field_71439_g = ElytraFlight.mc.field_71439_g;
            field_71439_g.field_70159_w -= ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b * Math.sin(yaw3) * 0.04;
            final EntityPlayerSP field_71439_g2 = ElytraFlight.mc.field_71439_g;
            field_71439_g2.field_70179_y += ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b * Math.cos(yaw3) * 0.04;
        }
    }
    
    private void setMoveSpeed(final MoveEvent event, final double speed) {
        double forward = ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b;
        double strafe = ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = ElytraFlight.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
            ElytraFlight.mc.field_71439_g.field_70159_w = 0.0;
            ElytraFlight.mc.field_71439_g.field_70179_y = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            final double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            event.setX(x);
            event.setZ(z);
            ElytraFlight.mc.field_71439_g.field_70159_w = x;
            ElytraFlight.mc.field_71439_g.field_70179_y = z;
        }
    }
    
    @Override
    public void onTick() {
        if (!ElytraFlight.mc.field_71439_g.func_184613_cA()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFlight.mc.field_71439_g.func_70090_H()) {
                    Util.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    final EntityPlayerSP field_71439_g = ElytraFlight.mc.field_71439_g;
                    field_71439_g.field_70181_x += 0.08;
                }
                else if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    final EntityPlayerSP field_71439_g2 = ElytraFlight.mc.field_71439_g;
                    field_71439_g2.field_70181_x -= 0.04;
                }
                if (ElytraFlight.mc.field_71474_y.field_74351_w.func_151470_d()) {
                    final float yaw = (float)Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
                    final EntityPlayerSP field_71439_g3 = ElytraFlight.mc.field_71439_g;
                    field_71439_g3.field_70159_w -= MathHelper.func_76126_a(yaw) * 0.05f;
                    final EntityPlayerSP field_71439_g4 = ElytraFlight.mc.field_71439_g;
                    field_71439_g4.field_70179_y += MathHelper.func_76134_b(yaw) * 0.05f;
                    break;
                }
                if (!ElytraFlight.mc.field_71474_y.field_74368_y.func_151470_d()) {
                    break;
                }
                final float yaw = (float)Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
                final EntityPlayerSP field_71439_g5 = ElytraFlight.mc.field_71439_g;
                field_71439_g5.field_70159_w += MathHelper.func_76126_a(yaw) * 0.05f;
                final EntityPlayerSP field_71439_g6 = ElytraFlight.mc.field_71439_g;
                field_71439_g6.field_70179_y -= MathHelper.func_76134_b(yaw) * 0.05f;
                break;
            }
            case FLY: {
                ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (ElytraFlight.mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() != Items.field_185160_cR) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                if (this.disableInLiquid.getValue() && (ElytraFlight.mc.field_71439_g.func_70090_H() || ElytraFlight.mc.field_71439_g.func_180799_ab())) {
                    if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                        Util.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    return;
                }
                if (this.autoStart.getValue() && ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d() && !ElytraFlight.mc.field_71439_g.func_184613_cA() && ElytraFlight.mc.field_71439_g.field_70181_x < 0.0 && this.timer.passedMs(250L)) {
                    Util.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.timer.reset();
                }
                if (this.mode.getValue() == Mode.BETTER) {
                    final double[] dir = MathUtil.directionSpeed((this.devMode.getValue() == 1) ? ((double)this.speed.getValue()) : ((double)this.hSpeed.getValue()));
                    switch (this.devMode.getValue()) {
                        case 1: {
                            ElytraFlight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                            ElytraFlight.mc.field_71439_g.field_70747_aH = this.speed.getValue();
                            if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                final EntityPlayerSP field_71439_g = ElytraFlight.mc.field_71439_g;
                                field_71439_g.field_70181_x += this.speed.getValue();
                            }
                            if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                                final EntityPlayerSP field_71439_g2 = ElytraFlight.mc.field_71439_g;
                                field_71439_g2.field_70181_x -= this.speed.getValue();
                            }
                            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                                ElytraFlight.mc.field_71439_g.field_70159_w = dir[0];
                                ElytraFlight.mc.field_71439_g.field_70179_y = dir[1];
                                break;
                            }
                            ElytraFlight.mc.field_71439_g.field_70159_w = 0.0;
                            ElytraFlight.mc.field_71439_g.field_70179_y = 0.0;
                            break;
                        }
                        case 2: {
                            if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                                if (this.flyHeight == null) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u;
                                }
                                if (this.noKick.getValue()) {
                                    this.flyHeight -= (Double)this.glide.getValue();
                                }
                                this.posX = 0.0;
                                this.posZ = 0.0;
                                if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                                    this.posX = dir[0];
                                    this.posZ = dir[1];
                                }
                                if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u + this.vSpeed.getValue();
                                }
                                if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u - this.vSpeed.getValue();
                                }
                                ElytraFlight.mc.field_71439_g.func_70107_b(ElytraFlight.mc.field_71439_g.field_70165_t + this.posX, (double)this.flyHeight, ElytraFlight.mc.field_71439_g.field_70161_v + this.posZ);
                                ElytraFlight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                                break;
                            }
                            this.flyHeight = null;
                            return;
                        }
                        case 3: {
                            if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                                if (this.flyHeight == null || this.posX == null || this.posX == 0.0 || this.posZ == null || this.posZ == 0.0) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u;
                                    this.posX = ElytraFlight.mc.field_71439_g.field_70165_t;
                                    this.posZ = ElytraFlight.mc.field_71439_g.field_70161_v;
                                }
                                if (this.noKick.getValue()) {
                                    this.flyHeight -= (Double)this.glide.getValue();
                                }
                                if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                                    this.posX += dir[0];
                                    this.posZ += dir[1];
                                }
                                if (this.allowUp.getValue() && ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u + this.vSpeed.getValue() / 10.0f;
                                }
                                if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                                    this.flyHeight = ElytraFlight.mc.field_71439_g.field_70163_u - this.vSpeed.getValue() / 10.0f;
                                }
                                ElytraFlight.mc.field_71439_g.func_70107_b((double)this.posX, (double)this.flyHeight, (double)this.posZ);
                                ElytraFlight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                                break;
                            }
                            this.flyHeight = null;
                            this.posX = null;
                            this.posZ = null;
                            return;
                        }
                    }
                }
                final double rotationYaw = Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
                if (ElytraFlight.mc.field_71439_g.func_184613_cA()) {
                    switch (this.mode.getValue()) {
                        case VANILLA: {
                            final float speedScaled = this.speed.getValue() * 0.05f;
                            if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                final EntityPlayerSP field_71439_g3 = ElytraFlight.mc.field_71439_g;
                                field_71439_g3.field_70181_x += speedScaled;
                            }
                            if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                                final EntityPlayerSP field_71439_g4 = ElytraFlight.mc.field_71439_g;
                                field_71439_g4.field_70181_x -= speedScaled;
                            }
                            if (ElytraFlight.mc.field_71474_y.field_74351_w.func_151470_d()) {
                                final EntityPlayerSP field_71439_g5 = ElytraFlight.mc.field_71439_g;
                                field_71439_g5.field_70159_w -= Math.sin(rotationYaw) * speedScaled;
                                final EntityPlayerSP field_71439_g6 = ElytraFlight.mc.field_71439_g;
                                field_71439_g6.field_70179_y += Math.cos(rotationYaw) * speedScaled;
                            }
                            if (!ElytraFlight.mc.field_71474_y.field_74368_y.func_151470_d()) {
                                break;
                            }
                            final EntityPlayerSP field_71439_g7 = ElytraFlight.mc.field_71439_g;
                            field_71439_g7.field_70159_w += Math.sin(rotationYaw) * speedScaled;
                            final EntityPlayerSP field_71439_g8 = ElytraFlight.mc.field_71439_g;
                            field_71439_g8.field_70179_y -= Math.cos(rotationYaw) * speedScaled;
                            break;
                        }
                        case PACKET: {
                            this.freezePlayer((EntityPlayer)ElytraFlight.mc.field_71439_g);
                            this.runNoKick((EntityPlayer)ElytraFlight.mc.field_71439_g);
                            final double[] directionSpeedPacket = MathUtil.directionSpeed(this.speed.getValue());
                            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78901_c) {
                                ElytraFlight.mc.field_71439_g.field_70181_x = this.speed.getValue();
                            }
                            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78899_d) {
                                ElytraFlight.mc.field_71439_g.field_70181_x = -this.speed.getValue();
                            }
                            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                                ElytraFlight.mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
                                ElytraFlight.mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
                            }
                            Util.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                            Util.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                            break;
                        }
                        case BYPASS: {
                            if (this.devMode.getValue() != 3) {
                                break;
                            }
                            if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                ElytraFlight.mc.field_71439_g.field_70181_x = 0.019999999552965164;
                            }
                            if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                                ElytraFlight.mc.field_71439_g.field_70181_x = -0.20000000298023224;
                            }
                            if (ElytraFlight.mc.field_71439_g.field_70173_aa % 8 == 0 && ElytraFlight.mc.field_71439_g.field_70163_u <= 240.0) {
                                ElytraFlight.mc.field_71439_g.field_70181_x = 0.019999999552965164;
                            }
                            ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                            ElytraFlight.mc.field_71439_g.field_71075_bZ.func_75092_a(0.025f);
                            final double[] directionSpeedBypass = MathUtil.directionSpeed(0.5199999809265137);
                            if (ElytraFlight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || ElytraFlight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                                ElytraFlight.mc.field_71439_g.field_70159_w = directionSpeedBypass[0];
                                ElytraFlight.mc.field_71439_g.field_70179_y = directionSpeedBypass[1];
                                break;
                            }
                            ElytraFlight.mc.field_71439_g.field_70159_w = 0.0;
                            ElytraFlight.mc.field_71439_g.field_70179_y = 0.0;
                            break;
                        }
                    }
                }
                if (!this.infiniteDura.getValue()) {
                    break;
                }
                ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                break;
            }
            case 1: {
                if (!this.infiniteDura.getValue()) {
                    break;
                }
                ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
                break;
            }
        }
    }
    
    private double[] forwardStrafeYaw(final double forward, final double strafe, final double yaw) {
        final double[] result = { forward, strafe, yaw };
        if ((forward != 0.0 || strafe != 0.0) && forward != 0.0) {
            if (strafe > 0.0) {
                result[2] += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                result[2] += ((forward > 0.0) ? 45 : -45);
            }
            result[1] = 0.0;
            if (forward > 0.0) {
                result[0] = 1.0;
            }
            else if (forward < 0.0) {
                result[0] = -1.0;
            }
        }
        return result;
    }
    
    private void freezePlayer(final EntityPlayer player) {
        player.field_70159_w = 0.0;
        player.field_70181_x = 0.0;
        player.field_70179_y = 0.0;
    }
    
    private void runNoKick(final EntityPlayer player) {
        if (this.noKick.getValue() && !player.func_184613_cA() && player.field_70173_aa % 4 == 0) {
            player.field_70181_x = -0.03999999910593033;
        }
    }
    
    @Override
    public void onDisable() {
        if (Feature.fullNullCheck() || ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75098_d) {
            return;
        }
        ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
    }
    
    static {
        ElytraFlight.INSTANCE = new ElytraFlight();
    }
    
    public enum Mode
    {
        VANILLA, 
        PACKET, 
        BOOST, 
        FLY, 
        BYPASS, 
        BETTER, 
        OHARE, 
        TOOBEE, 
        TOOBEEBYPASS;
    }
}
