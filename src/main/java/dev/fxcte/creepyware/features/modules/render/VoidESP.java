// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import net.minecraft.init.Blocks;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import dev.fxcte.creepyware.util.BlockUtil;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.util.EntityUtil;
import java.util.Iterator;
import dev.fxcte.creepyware.util.RenderUtil;
import java.awt.Color;
import dev.fxcte.creepyware.util.RotationUtil;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import dev.fxcte.creepyware.features.Feature;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class VoidESP extends Module
{
    private final Setting<Float> radius;
    private final Timer timer;
    public Setting<Boolean> air;
    public Setting<Boolean> noEnd;
    public Setting<Boolean> box;
    public Setting<Boolean> outline;
    public Setting<Boolean> colorSync;
    public Setting<Double> height;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> updates;
    private final Setting<Integer> voidCap;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private List<BlockPos> voidHoles;
    
    public VoidESP() {
        super("VoidEsp", "Esps the void", Category.RENDER, true, false, false);
        this.radius = (Setting<Float>)this.register(new Setting("Radius", (T)8.0f, (T)0.0f, (T)50.0f));
        this.timer = new Timer();
        this.air = (Setting<Boolean>)this.register(new Setting("Speed", "OnlyAir", 0.0, 0.0, (T)true, 0));
        this.noEnd = (Setting<Boolean>)this.register(new Setting("Speed", "NoEnd", 0.0, 0.0, (T)true, 0));
        this.box = (Setting<Boolean>)this.register(new Setting("Speed", "Box", 0.0, 0.0, (T)true, 0));
        this.outline = (Setting<Boolean>)this.register(new Setting("Speed", "Outline", 0.0, 0.0, (T)true, 0));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Speed", "Sync", 0.0, 0.0, (T)false, 0));
        this.height = (Setting<Double>)this.register(new Setting("Height", (T)0.0, (T)(-2.0), (T)2.0));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", (T)false, v -> this.outline.getValue()));
        this.updates = (Setting<Integer>)this.register(new Setting("Updates", (T)500, (T)0, (T)1000));
        this.voidCap = (Setting<Integer>)this.register(new Setting("VoidCap", (T)500, (T)0, (T)1000));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)0, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)0, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)255, (T)0, (T)255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.voidHoles = new CopyOnWriteArrayList<BlockPos>();
    }
    
    @Override
    public void onToggle() {
        this.timer.reset();
    }
    
    @Override
    public void onLogin() {
        this.timer.reset();
    }
    
    @Override
    public void onTick() {
        if (!Feature.fullNullCheck() && (!this.noEnd.getValue() || VoidESP.mc.field_71439_g.field_71093_bK != 1) && this.timer.passedMs(this.updates.getValue())) {
            this.voidHoles.clear();
            this.voidHoles = this.findVoidHoles();
            if (this.voidHoles.size() > this.voidCap.getValue()) {
                this.voidHoles.clear();
            }
            this.timer.reset();
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (Feature.fullNullCheck() || (this.noEnd.getValue() && VoidESP.mc.field_71439_g.field_71093_bK == 1)) {
            return;
        }
        for (final BlockPos pos : this.voidHoles) {
            if (!RotationUtil.isInFov(pos)) {
                continue;
            }
            RenderUtil.drawBoxESP(pos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), false, false, false, false, 0);
        }
    }
    
    private List<BlockPos> findVoidHoles() {
        final BlockPos playerPos = EntityUtil.getPlayerPos((EntityPlayer)VoidESP.mc.field_71439_g);
        return BlockUtil.getDisc(playerPos.func_177982_a(0, -playerPos.func_177956_o(), 0), this.radius.getValue()).stream().filter((Predicate<? super Object>)this::isVoid).collect((Collector<? super Object, ?, List<BlockPos>>)Collectors.toList());
    }
    
    private boolean isVoid(final BlockPos pos) {
        return (VoidESP.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a || (!this.air.getValue() && VoidESP.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150357_h)) && pos.func_177956_o() < 1 && pos.func_177956_o() >= 0;
    }
}
