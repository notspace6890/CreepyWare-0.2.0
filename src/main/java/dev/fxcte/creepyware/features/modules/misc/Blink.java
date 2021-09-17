// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import dev.fxcte.creepyware.features.Feature;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.network.Packet;
import java.util.Queue;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.modules.Module;

public class Blink extends Module
{
    private static Blink INSTANCE;
    private final Timer timer;
    private final Queue<Packet<?>> packets;
    public Setting<Boolean> cPacketPlayer;
    public Setting<Mode> autoOff;
    public Setting<Integer> timeLimit;
    public Setting<Integer> packetLimit;
    public Setting<Float> distance;
    private EntityOtherPlayerMP entity;
    private int packetsCanceled;
    private BlockPos startPos;
    
    public Blink() {
        super("Blink", "Fakelag.", Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.cPacketPlayer = (Setting<Boolean>)this.register(new Setting("Speed", "CPacketPlayer", 0.0, 0.0, (T)true, 0));
        this.autoOff = (Setting<Mode>)this.register(new Setting("Speed", "AutoOff", 0.0, 0.0, (T)Mode.MANUAL, 0));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", (T)20, (T)1, (T)500, v -> this.autoOff.getValue() == Mode.TIME));
        this.packetLimit = (Setting<Integer>)this.register(new Setting("Packets", (T)20, (T)1, (T)500, v -> this.autoOff.getValue() == Mode.PACKETS));
        this.distance = (Setting<Float>)this.register(new Setting("Distance", (T)10.0f, (T)1.0f, (T)100.0f, v -> this.autoOff.getValue() == Mode.DISTANCE));
        this.setInstance();
    }
    
    public static Blink getInstance() {
        if (Blink.INSTANCE == null) {
            Blink.INSTANCE = new Blink();
        }
        return Blink.INSTANCE;
    }
    
    private void setInstance() {
        Blink.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            (this.entity = new EntityOtherPlayerMP((World)Blink.mc.field_71441_e, Blink.mc.field_71449_j.func_148256_e())).func_82149_j((Entity)Blink.mc.field_71439_g);
            this.entity.field_70177_z = Blink.mc.field_71439_g.field_70177_z;
            this.entity.field_70759_as = Blink.mc.field_71439_g.field_70759_as;
            this.entity.field_71071_by.func_70455_b(Blink.mc.field_71439_g.field_71071_by);
            Blink.mc.field_71441_e.func_73027_a(6942069, (Entity)this.entity);
            this.startPos = Blink.mc.field_71439_g.func_180425_c();
        }
        else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }
    
    @Override
    public void onUpdate() {
        if (Feature.nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.field_71439_g.func_174818_b(this.startPos) >= MathUtil.square(this.distance.getValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue())) {
            this.disable();
        }
    }
    
    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && Blink.mc.field_71441_e != null && !Blink.mc.func_71356_B()) {
            final Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            Blink.mc.field_71441_e.func_72900_e((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.field_71439_g.field_71174_a.func_147297_a((Packet)this.packets.poll());
            }
        }
        this.startPos = null;
    }
    
    static {
        Blink.INSTANCE = new Blink();
    }
    
    public enum Mode
    {
        MANUAL, 
        TIME, 
        DISTANCE, 
        PACKETS;
    }
}
