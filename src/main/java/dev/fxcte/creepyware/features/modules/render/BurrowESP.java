// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import java.util.function.Consumer;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import java.awt.Color;
import net.minecraft.init.Blocks;
import dev.fxcte.creepyware.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.CreepyWare;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class BurrowESP extends Module
{
    private final Setting<Integer> boxRed;
    private final Setting<Integer> outlineGreen;
    private final Setting<Integer> boxGreen;
    private final Setting<Boolean> box;
    private final Setting<Boolean> cOutline;
    private final Setting<Integer> outlineBlue;
    private final Setting<Boolean> name;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> outlineWidth;
    private final Setting<Integer> outlineRed;
    private final Setting<Boolean> outline;
    private final Setting<Integer> boxBlue;
    private final Map<EntityPlayer, BlockPos> burrowedPlayers;
    private final Setting<Integer> outlineAlpha;
    
    public BurrowESP() {
        super("BurrowESP", "Shows gay people.", Category.RENDER, true, false, false);
        this.name = (Setting<Boolean>)this.register(new Setting("Speed", "Name", 0.0, 0.0, (T)false, 0));
        this.box = new Setting<Boolean>("Speed", "Box", 0.0, 0.0, true, 0);
        this.boxRed = (Setting<Integer>)this.register(new Setting("BoxRed", (T)255, (T)0, (T)255, v -> this.box.getValue()));
        this.boxGreen = (Setting<Integer>)this.register(new Setting("BoxGreen", (T)255, (T)0, (T)255, v -> this.box.getValue()));
        this.boxBlue = (Setting<Integer>)this.register(new Setting("BoxBlue", (T)255, (T)0, (T)255, v -> this.box.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Speed", "Outline", 0.0, 0.0, (T)true, 0));
        this.outlineWidth = (Setting<Float>)this.register(new Setting("OutlineWidth", (T)1.0f, (T)0.0f, (T)5.0f, v -> this.outline.getValue()));
        this.cOutline = (Setting<Boolean>)this.register(new Setting("CustomOutline", (T)false, v -> this.outline.getValue()));
        this.outlineRed = (Setting<Integer>)this.register(new Setting("OutlineRed", (T)255, (T)0, (T)255, v -> this.cOutline.getValue()));
        this.outlineGreen = (Setting<Integer>)this.register(new Setting("OutlineGreen", (T)255, (T)0, (T)255, v -> this.cOutline.getValue()));
        this.outlineBlue = (Setting<Integer>)this.register(new Setting("OutlineBlue", (T)255, (T)0, (T)255, v -> this.cOutline.getValue()));
        this.outlineAlpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", (T)255, (T)0, (T)255, v -> this.cOutline.getValue()));
        this.burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();
    }
    
    private void getPlayers() {
        for (final EntityPlayer entityPlayer : BurrowESP.mc.field_71441_e.field_73010_i) {
            if (entityPlayer != BurrowESP.mc.field_71439_g && !CreepyWare.friendManager.isFriend(entityPlayer.func_70005_c_()) && EntityUtil.isLiving((Entity)entityPlayer)) {
                if (!this.isBurrowed(entityPlayer)) {
                    continue;
                }
                this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.field_70165_t, entityPlayer.field_70163_u, entityPlayer.field_70161_v));
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.burrowedPlayers.clear();
    }
    
    private void lambda$onRender3D$8(final Map.Entry entry) {
        this.renderBurrowedBlock(entry.getValue());
        if (this.name.getValue()) {
            RenderUtil.drawText(new AxisAlignedBB((BlockPos)entry.getValue()), ((EntityPlayer)entry.getKey()).func_146103_bH().getName());
        }
    }
    
    private boolean isBurrowed(final EntityPlayer entityPlayer) {
        final BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.field_70165_t), Math.floor(entityPlayer.field_70163_u + 0.2), Math.floor(entityPlayer.field_70161_v));
        return BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150477_bB || BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z || BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150486_ae || BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150467_bQ;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }
    
    private void renderBurrowedBlock(final BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.boxAlpha.getValue()), true, new Color(this.outlineRed.getValue(), this.outlineGreen.getValue(), this.outlineBlue.getValue(), this.outlineAlpha.getValue()), this.outlineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
    }
    
    @Override
    public void onRender3D(final Render3DEvent render3DEvent) {
        if (!this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.entrySet().forEach(this::lambda$onRender3D$8);
        }
    }
}
