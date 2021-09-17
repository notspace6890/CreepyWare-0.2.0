// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import dev.fxcte.creepyware.event.events.StepEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class Step extends Module
{
    private static Step instance;
    public Setting<Boolean> vanilla;
    public Setting<Integer> stepHeight;
    public Setting<Boolean> turnOff;
    
    public Step() {
        super("Step", "Allows you to step up blocks", Category.MOVEMENT, true, false, false);
        this.vanilla = (Setting<Boolean>)this.register(new Setting("Speed", "Vanilla", 0.0, 0.0, (T)false, 0));
        this.stepHeight = (Setting<Integer>)this.register(new Setting("Height", (T)2, (T)1, (T)2));
        this.turnOff = (Setting<Boolean>)this.register(new Setting("Speed", "Disable", 0.0, 0.0, (T)false, 0));
        Step.instance = this;
    }
    
    public static Step getInstance() {
        if (Step.instance == null) {
            Step.instance = new Step();
        }
        return Step.instance;
    }
    
    @SubscribeEvent
    public void onStep(final StepEvent event) {
        if (Step.mc.field_71439_g.field_70122_E && !Step.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !Step.mc.field_71439_g.func_70055_a(Material.field_151587_i) && Step.mc.field_71439_g.field_70124_G && Step.mc.field_71439_g.field_70143_R == 0.0f && !Step.mc.field_71474_y.field_74314_A.field_74513_e && !Step.mc.field_71439_g.func_70617_f_()) {
            event.setHeight(this.stepHeight.getValue());
            final double rheight = Step.mc.field_71439_g.func_174813_aQ().field_72338_b - Step.mc.field_71439_g.field_70163_u;
            if (rheight >= 0.625) {
                if (!this.vanilla.getValue()) {
                    this.ncpStep(rheight);
                }
                if (this.turnOff.getValue()) {
                    this.disable();
                }
            }
        }
        else {
            event.setHeight(0.6f);
        }
    }
    
    private void ncpStep(final double height) {
        final double posX = Step.mc.field_71439_g.field_70165_t;
        final double posZ = Step.mc.field_71439_g.field_70161_v;
        double y = Step.mc.field_71439_g.field_70163_u;
        if (height >= 1.1) {
            if (height < 1.6) {
                final double[] array;
                final double[] offset = array = new double[] { 0.42, 0.33, 0.24, 0.083, -0.078 };
                for (final double off : array) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y += off, posZ, false));
                }
            }
            else if (height < 2.1) {
                final double[] array2;
                final double[] heights = array2 = new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869 };
                for (final double off : array2) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
            else {
                final double[] array3;
                final double[] heights = array3 = new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                for (final double off : array3) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
        }
        else {
            double first = 0.42;
            double second = 0.75;
            if (height != 1.0) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + first, posZ, false));
            if (y + second < y + height) {
                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + second, posZ, false));
            }
        }
    }
}
