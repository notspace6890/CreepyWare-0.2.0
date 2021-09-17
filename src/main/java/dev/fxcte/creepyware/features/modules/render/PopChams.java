// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.awt.Color;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.ArrayList;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.event.events.TotemPopEvent;
import java.util.concurrent.ConcurrentHashMap;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class PopChams extends Module
{
    public static PopChams INSTANCE;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public static ConcurrentHashMap<Integer, Integer> pops;
    
    public PopChams() {
        super("PopChams", "1 origianl module from kambing", Category.RENDER, false, false, false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
    }
    
    public static PopChams getINSTANCE() {
        if (PopChams.INSTANCE == null) {
            PopChams.INSTANCE = new PopChams();
        }
        return PopChams.INSTANCE;
    }
    
    public void setInstance() {
        PopChams.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPopLol(final TotemPopEvent event) {
        final Color color = EntityUtil.getColor((Entity)event.getEntity(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), false);
        if (event.getEntity() != PopChams.mc.field_71439_g) {
            final Entity ee = (Entity)event.getEntity();
            Command.sendMessage("PopEventLol");
            final ArrayList<Integer> idList = new ArrayList<Integer>();
            for (final Entity e : PopChams.mc.field_71441_e.field_72996_f) {
                idList.add(e.field_145783_c);
            }
            final EntityOtherPlayerMP popCham = new EntityOtherPlayerMP((World)PopChams.mc.field_71441_e, event.getEntity().func_146103_bH());
            popCham.func_82149_j(ee);
            popCham.field_70759_as = ee.func_70079_am();
            popCham.field_70177_z = ee.field_70177_z;
            popCham.field_70125_A = ee.field_70125_A;
            popCham.func_71033_a(GameType.CREATIVE);
            popCham.func_70606_j(20.0f);
            for (int i = 0; i > -10000; --i) {
                if (!idList.contains(i)) {
                    PopChams.mc.field_71441_e.func_73027_a(i, (Entity)popCham);
                    PopChams.pops.put(i, color.getAlpha());
                    break;
                }
            }
        }
    }
    
    static {
        PopChams.INSTANCE = new PopChams();
        PopChams.pops = new ConcurrentHashMap<Integer, Integer>();
    }
}
