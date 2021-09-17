// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import dev.fxcte.creepyware.event.events.PushEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.init.Blocks;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Velocity extends Module
{
    private static Velocity INSTANCE;
    public Setting<Boolean> knockBack;
    public Setting<Boolean> noPush;
    public Setting<Float> horizontal;
    public Setting<Float> vertical;
    public Setting<Boolean> explosions;
    public Setting<Boolean> bobbers;
    public Setting<Boolean> water;
    public Setting<Boolean> blocks;
    public Setting<Boolean> ice;
    
    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Category.MOVEMENT, true, false, false);
        this.knockBack = (Setting<Boolean>)this.register(new Setting("Speed", "KnockBack", 0.0, 0.0, (T)true, 0));
        this.noPush = (Setting<Boolean>)this.register(new Setting("Speed", "NoPush", 0.0, 0.0, (T)true, 0));
        this.horizontal = (Setting<Float>)this.register(new Setting("Horizontal", (T)0.0f, (T)0.0f, (T)100.0f));
        this.vertical = (Setting<Float>)this.register(new Setting("Vertical", (T)0.0f, (T)0.0f, (T)100.0f));
        this.explosions = (Setting<Boolean>)this.register(new Setting("Speed", "Explosions", 0.0, 0.0, (T)true, 0));
        this.bobbers = (Setting<Boolean>)this.register(new Setting("Speed", "Bobbers", 0.0, 0.0, (T)true, 0));
        this.water = (Setting<Boolean>)this.register(new Setting("Speed", "Water", 0.0, 0.0, (T)false, 0));
        this.blocks = (Setting<Boolean>)this.register(new Setting("Speed", "Blocks", 0.0, 0.0, (T)false, 0));
        this.ice = (Setting<Boolean>)this.register(new Setting("Speed", "Ice", 0.0, 0.0, (T)false, 0));
        this.setInstance();
    }
    
    public static Velocity getINSTANCE() {
        if (Velocity.INSTANCE == null) {
            Velocity.INSTANCE = new Velocity();
        }
        return Velocity.INSTANCE;
    }
    
    private void setInstance() {
        Velocity.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.ice.getValue()) {
            Blocks.field_150432_aD.field_149765_K = 0.6f;
            Blocks.field_150403_cj.field_149765_K = 0.6f;
            Blocks.field_185778_de.field_149765_K = 0.6f;
        }
    }
    
    @Override
    public void onDisable() {
        Blocks.field_150432_aD.field_149765_K = 0.98f;
        Blocks.field_150403_cj.field_149765_K = 0.98f;
        Blocks.field_185778_de.field_149765_K = 0.98f;
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.field_71439_g != null) {
            final SPacketEntityVelocity velocity;
            if (this.knockBack.getValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = event.getPacket()).func_149412_c() == Velocity.mc.field_71439_g.field_145783_c) {
                if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                velocity.field_149415_b *= (int)(Object)this.horizontal.getValue();
                velocity.field_149416_c *= (int)(Object)this.vertical.getValue();
                velocity.field_149414_d *= (int)(Object)this.horizontal.getValue();
            }
            final SPacketEntityStatus packet;
            final Entity entity;
            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue() && (packet = event.getPacket()).func_149160_c() == 31 && (entity = packet.func_149161_a((World)Velocity.mc.field_71441_e)) instanceof EntityFishHook) {
                final EntityFishHook fishHook = (EntityFishHook)entity;
                if (fishHook.field_146043_c == Velocity.mc.field_71439_g) {
                    event.setCanceled(true);
                }
            }
            if (this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
                final SPacketExplosion sPacketExplosion;
                final SPacketExplosion velocity_ = sPacketExplosion = event.getPacket();
                sPacketExplosion.field_149152_f *= this.horizontal.getValue();
                final SPacketExplosion sPacketExplosion2 = velocity_;
                sPacketExplosion2.field_149153_g *= this.vertical.getValue();
                final SPacketExplosion sPacketExplosion3 = velocity_;
                sPacketExplosion3.field_149159_h *= this.horizontal.getValue();
            }
        }
    }
    
    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (event.getStage() == 0 && this.noPush.getValue() && event.entity.equals((Object)Velocity.mc.field_71439_g)) {
            if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }
            event.x = -event.x * this.horizontal.getValue();
            event.y = -event.y * this.vertical.getValue();
            event.z = -event.z * this.horizontal.getValue();
        }
        else if (event.getStage() == 1 && this.blocks.getValue()) {
            event.setCanceled(true);
        }
        else if (event.getStage() == 2 && this.water.getValue() && Velocity.mc.field_71439_g != null && Velocity.mc.field_71439_g.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
    
    static {
        Velocity.INSTANCE = new Velocity();
    }
}
