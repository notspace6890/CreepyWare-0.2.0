// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.modules.combat.AutoCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import dev.fxcte.creepyware.event.events.PacketEvent;
import java.util.Iterator;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.server.SPacketSoundEffect;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.util.SoundEvent;
import java.util.Set;
import dev.fxcte.creepyware.features.modules.Module;

public class NoSoundLag extends Module
{
    private static final Set<SoundEvent> BLACKLIST;
    private static NoSoundLag instance;
    public Setting<Boolean> crystals;
    public Setting<Boolean> armor;
    public Setting<Float> soundRange;
    
    public NoSoundLag() {
        super("NoSoundLag", "Prevents Lag through sound spam.", Category.MISC, true, false, false);
        this.crystals = (Setting<Boolean>)this.register(new Setting("Speed", "Crystals", 0.0, 0.0, (T)true, 0));
        this.armor = (Setting<Boolean>)this.register(new Setting("Speed", "Armor", 0.0, 0.0, (T)true, 0));
        this.soundRange = (Setting<Float>)this.register(new Setting("SoundRange", (T)12.0f, (T)0.0f, (T)12.0f));
        NoSoundLag.instance = this;
    }
    
    public static NoSoundLag getInstance() {
        if (NoSoundLag.instance == null) {
            NoSoundLag.instance = new NoSoundLag();
        }
        return NoSoundLag.instance;
    }
    
    public static void removeEntities(final SPacketSoundEffect packet, final float range) {
        final BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
        final ArrayList<Entity> toRemove = new ArrayList<Entity>();
        if (fullNullCheck()) {
            return;
        }
        for (final Entity entity : NoSoundLag.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityEnderCrystal) {
                if (entity.func_174818_b(pos) > MathUtil.square(range)) {
                    continue;
                }
                toRemove.add(entity);
            }
        }
        for (final Entity entity : toRemove) {
            entity.func_70106_y();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (event != null && event.getPacket() != null && NoSoundLag.mc.field_71439_g != null && NoSoundLag.mc.field_71441_e != null && event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = event.getPacket();
            if (this.crystals.getValue() && packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB && (AutoCrystal.getInstance().isOff() || (!AutoCrystal.getInstance().sound.getValue() && AutoCrystal.getInstance().threadMode.getValue() != AutoCrystal.ThreadMode.SOUND))) {
                removeEntities(packet, this.soundRange.getValue());
            }
            if (NoSoundLag.BLACKLIST.contains(packet.func_186978_a()) && this.armor.getValue()) {
                event.setCanceled(true);
            }
        }
    }
    
    static {
        BLACKLIST = Sets.newHashSet((Object[])new SoundEvent[] { SoundEvents.field_187719_p, SoundEvents.field_191258_p, SoundEvents.field_187716_o, SoundEvents.field_187725_r, SoundEvents.field_187722_q, SoundEvents.field_187713_n, SoundEvents.field_187728_s });
    }
}
