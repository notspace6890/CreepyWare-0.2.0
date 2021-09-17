// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.combat;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.fxcte.creepyware.util.BlockUtil;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import dev.fxcte.creepyware.event.events.PacketEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class GodModule extends Module
{
    public Setting<Integer> rotations;
    public Setting<Boolean> rotate;
    public Setting<Boolean> render;
    public Setting<Boolean> antiIllegal;
    public Setting<Boolean> checkPos;
    public Setting<Boolean> oneDot15;
    public Setting<Boolean> entitycheck;
    public Setting<Integer> attacks;
    public Setting<Integer> delay;
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private int highestID;
    
    public GodModule() {
        super("GodModule", "Wow", Category.COMBAT, true, false, false);
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", (T)1, (T)1, (T)20));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Speed", "Rotate", 0.0, 0.0, (T)false, 0));
        this.render = (Setting<Boolean>)this.register(new Setting("Speed", "Render", 0.0, 0.0, (T)false, 0));
        this.antiIllegal = (Setting<Boolean>)this.register(new Setting("Speed", "AntiIllegal", 0.0, 0.0, (T)true, 0));
        this.checkPos = (Setting<Boolean>)this.register(new Setting("Speed", "CheckPos", 0.0, 0.0, (T)true, 0));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("Speed", "1.15", 0.0, 0.0, (T)false, 0));
        this.entitycheck = (Setting<Boolean>)this.register(new Setting("Speed", "EntityCheck", 0.0, 0.0, (T)false, 0));
        this.attacks = (Setting<Integer>)this.register(new Setting("Attacks", (T)1, (T)1, (T)10));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)50));
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.highestID = -100000;
    }
    
    @Override
    public void onToggle() {
        this.resetFields();
        if (GodModule.mc.field_71441_e != null) {
            this.updateEntityID();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.render.getValue()) {
            for (final Entity entity : GodModule.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                entity.func_96094_a(String.valueOf(entity.field_145783_c));
                entity.func_174805_g(true);
            }
        }
    }
    
    @Override
    public void onLogout() {
        this.resetFields();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (GodModule.mc.field_71439_g.func_184586_b(packet.field_187027_c).func_77973_b() instanceof ItemEndCrystal) {
                if ((this.checkPos.getValue() && !BlockUtil.canPlaceCrystal(packet.field_179725_b, this.entitycheck.getValue(), this.oneDot15.getValue())) || this.checkPlayers()) {
                    return;
                }
                this.updateEntityID();
                for (int i = 1; i < this.attacks.getValue(); ++i) {
                    this.attackID(packet.field_179725_b, this.highestID + i);
                }
            }
        }
        if (event.getStage() == 0 && this.rotating && this.rotate.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet2 = event.getPacket();
            packet2.field_149476_e = this.yaw;
            packet2.field_149473_f = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }
    
    private void attackID(final BlockPos pos, final int id) {
        final Entity entity = GodModule.mc.field_71441_e.func_73045_a(id);
        if (entity == null || entity instanceof EntityEnderCrystal) {
            final AttackThread attackThread = new AttackThread(id, pos, this.delay.getValue(), this);
            attackThread.start();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            this.checkID(event.getPacket().func_149001_c());
        }
        else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.checkID(event.getPacket().func_148985_c());
        }
        else if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.checkID(event.getPacket().func_148943_d());
        }
        else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
            this.checkID(event.getPacket().func_149052_c());
        }
        else if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.checkID(event.getPacket().func_148965_c());
        }
        else if (event.getPacket() instanceof SPacketSpawnMob) {
            this.checkID(event.getPacket().func_149024_d());
        }
    }
    
    private void checkID(final int id) {
        if (id > this.highestID) {
            this.highestID = id;
        }
    }
    
    public void updateEntityID() {
        for (final Entity entity : GodModule.mc.field_71441_e.field_72996_f) {
            if (entity.func_145782_y() <= this.highestID) {
                continue;
            }
            this.highestID = entity.func_145782_y();
        }
    }
    
    private boolean checkPlayers() {
        if (this.antiIllegal.getValue()) {
            for (final EntityPlayer player : GodModule.mc.field_71441_e.field_73010_i) {
                if (!this.checkItem(player.func_184614_ca()) && !this.checkItem(player.func_184592_cb())) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean checkItem(final ItemStack stack) {
        return stack.func_77973_b() instanceof ItemBow || stack.func_77973_b() instanceof ItemExpBottle || stack.func_77973_b() == Items.field_151007_F;
    }
    
    public void rotateTo(final BlockPos pos) {
        final float[] angle = MathUtil.calcAngle(GodModule.mc.field_71439_g.func_174824_e(Util.mc.func_184121_ak()), new Vec3d((Vec3i)pos));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }
    
    private void resetFields() {
        this.rotating = false;
        this.highestID = -1000000;
    }
    
    public static class AttackThread extends Thread
    {
        private final BlockPos pos;
        private final int id;
        private final int delay;
        private final GodModule godModule;
        
        public AttackThread(final int idIn, final BlockPos posIn, final int delayIn, final GodModule godModuleIn) {
            this.id = idIn;
            this.pos = posIn;
            this.delay = delayIn;
            this.godModule = godModuleIn;
        }
        
        @Override
        public void run() {
            try {
                this.wait(this.delay);
                final CPacketUseEntity attack = new CPacketUseEntity();
                attack.field_149567_a = this.id;
                attack.field_149566_b = CPacketUseEntity.Action.ATTACK;
                this.godModule.rotateTo(this.pos.func_177984_a());
                Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)attack);
                Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
