// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.event.events.RenderEntityModelEvent;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityXPOrb;
import dev.fxcte.creepyware.util.RenderUtil;
import java.awt.Color;
import net.minecraft.client.renderer.RenderGlobal;
import dev.fxcte.creepyware.features.modules.client.Colors;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class ESP extends Module
{
    private static ESP INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> colorSync;
    private final Setting<Boolean> players;
    private final Setting<Boolean> animals;
    private final Setting<Boolean> mobs;
    private final Setting<Boolean> items;
    private final Setting<Boolean> xporbs;
    private final Setting<Boolean> xpbottles;
    private final Setting<Boolean> pearl;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> boxAlpha;
    private final Setting<Integer> alpha;
    private final Setting<Float> lineWidth;
    private final Setting<Boolean> colorFriends;
    private final Setting<Boolean> self;
    private final Setting<Boolean> onTop;
    private final Setting<Boolean> invisibles;
    
    public ESP() {
        super("ESP", "Renders a nice ESP.", Category.RENDER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.OUTLINE, 0));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Speed", "Sync", 0.0, 0.0, (T)false, 0));
        this.players = (Setting<Boolean>)this.register(new Setting("Speed", "Players", 0.0, 0.0, (T)true, 0));
        this.animals = (Setting<Boolean>)this.register(new Setting("Speed", "Animals", 0.0, 0.0, (T)false, 0));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Speed", "Mobs", 0.0, 0.0, (T)false, 0));
        this.items = (Setting<Boolean>)this.register(new Setting("Speed", "Items", 0.0, 0.0, (T)false, 0));
        this.xporbs = (Setting<Boolean>)this.register(new Setting("Speed", "XpOrbs", 0.0, 0.0, (T)false, 0));
        this.xpbottles = (Setting<Boolean>)this.register(new Setting("Speed", "XpBottles", 0.0, 0.0, (T)false, 0));
        this.pearl = (Setting<Boolean>)this.register(new Setting("Speed", "Pearls", 0.0, 0.0, (T)false, 0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)120, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)2.0f, (T)0.1f, (T)5.0f));
        this.colorFriends = (Setting<Boolean>)this.register(new Setting("Speed", "Friends", 0.0, 0.0, (T)true, 0));
        this.self = (Setting<Boolean>)this.register(new Setting("Speed", "Self", 0.0, 0.0, (T)true, 0));
        this.onTop = (Setting<Boolean>)this.register(new Setting("Speed", "onTop", 0.0, 0.0, (T)true, 0));
        this.invisibles = (Setting<Boolean>)this.register(new Setting("Speed", "Invisibles", 0.0, 0.0, (T)false, 0));
        this.setInstance();
    }
    
    public static ESP getInstance() {
        if (ESP.INSTANCE == null) {
            ESP.INSTANCE = new ESP();
        }
        return ESP.INSTANCE;
    }
    
    private void setInstance() {
        ESP.INSTANCE = this;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.items.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityItem) {
                    if (ESP.mc.field_71439_g.func_70068_e(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, Util.mc.func_184121_ak());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? ((float)Colors.INSTANCE.getCurrentColor().getAlpha()) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityXPOrb) {
                    if (ESP.mc.field_71439_g.func_70068_e(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, Util.mc.func_184121_ak());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.pearl.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityEnderPearl) {
                    if (ESP.mc.field_71439_g.func_70068_e(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, Util.mc.func_184121_ak());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityExpBottle) {
                    if (ESP.mc.field_71439_g.func_70068_e(entity) >= 2500.0) {
                        continue;
                    }
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, Util.mc.func_184121_ak());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getRed() / 255.0f) : (this.red.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f) : (this.green.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f) : (this.blue.getValue() / 255.0f), ((boolean)this.colorSync.getValue()) ? (Colors.INSTANCE.getCurrentColor().getAlpha() / 255.0f) : (this.boxAlpha.getValue() / 255.0f));
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                    if (++i < 50) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (event.getStage() != 0 || event.entity == null || (event.entity.func_82150_aj() && !this.invisibles.getValue()) || (!this.self.getValue() && event.entity.equals((Object)ESP.mc.field_71439_g)) || (!this.players.getValue() && event.entity instanceof EntityPlayer) || (!this.animals.getValue() && EntityUtil.isPassive(event.entity)) || (!this.mobs.getValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer))) {
            return;
        }
        final Color color = this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        final boolean fancyGraphics = ESP.mc.field_71474_y.field_74347_j;
        ESP.mc.field_71474_y.field_74347_j = false;
        final float gamma = ESP.mc.field_71474_y.field_74333_Y;
        ESP.mc.field_71474_y.field_74333_Y = 10000.0f;
        if (this.onTop.getValue() && (!Chams.getInstance().isEnabled() || !Chams.getInstance().colored.getValue())) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue());
            RenderUtil.renderTwo();
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue());
            RenderUtil.renderFive();
        }
        else {
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode(1032, 6913);
            }
            else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179131_c(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!this.onTop.getValue() && (!Chams.getInstance().isEnabled() || !Chams.getInstance().colored.getValue())) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        try {
            ESP.mc.field_71474_y.field_74347_j = fancyGraphics;
            ESP.mc.field_71474_y.field_74333_Y = gamma;
        }
        catch (Exception ex) {}
        event.setCanceled(true);
    }
    
    static {
        ESP.INSTANCE = new ESP();
    }
    
    public enum Mode
    {
        WIREFRAME, 
        OUTLINE;
    }
}
