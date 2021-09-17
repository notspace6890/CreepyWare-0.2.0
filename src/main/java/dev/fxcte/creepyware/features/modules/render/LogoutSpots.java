// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import dev.fxcte.creepyware.util.ColorUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.fxcte.creepyware.util.Util;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import dev.fxcte.creepyware.features.command.Command;
import dev.fxcte.creepyware.event.events.ConnectionEvent;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.entity.Entity;
import dev.fxcte.creepyware.features.Feature;
import net.minecraft.util.math.AxisAlignedBB;
import java.awt.Color;
import dev.fxcte.creepyware.features.modules.client.Colors;
import dev.fxcte.creepyware.util.RenderUtil;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class LogoutSpots extends Module
{
    private final Setting<Boolean> colorSync;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Boolean> scaleing;
    private final Setting<Float> scaling;
    private final Setting<Float> factor;
    private final Setting<Boolean> smartScale;
    private final Setting<Boolean> rect;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> notification;
    private final List<LogoutPos> spots;
    public Setting<Float> range;
    public Setting<Boolean> message;
    
    public LogoutSpots() {
        super("LogoutSpots", "Renders LogoutSpots", Category.RENDER, true, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Speed", "Sync", 0.0, 0.0, (T)false, 0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.scaleing = (Setting<Boolean>)this.register(new Setting("Speed", "Scale", 0.0, 0.0, (T)false, 0));
        this.scaling = (Setting<Float>)this.register(new Setting("Size", (T)4.0f, (T)0.1f, (T)20.0f));
        this.factor = (Setting<Float>)this.register(new Setting("Factor", (T)0.3f, (T)0.1f, (T)1.0f, v -> this.scaleing.getValue()));
        this.smartScale = (Setting<Boolean>)this.register(new Setting("SmartScale", (T)false, v -> this.scaleing.getValue()));
        this.rect = (Setting<Boolean>)this.register(new Setting("Speed", "Rectangle", 0.0, 0.0, (T)true, 0));
        this.coords = (Setting<Boolean>)this.register(new Setting("Speed", "Coords", 0.0, 0.0, (T)true, 0));
        this.notification = (Setting<Boolean>)this.register(new Setting("Speed", "Notification", 0.0, 0.0, (T)true, 0));
        this.spots = new CopyOnWriteArrayList<LogoutPos>();
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)300.0f, (T)50.0f, (T)500.0f));
        this.message = (Setting<Boolean>)this.register(new Setting("Speed", "Message", 0.0, 0.0, (T)false, 0));
    }
    
    @Override
    public void onLogout() {
        this.spots.clear();
    }
    
    @Override
    public void onDisable() {
        this.spots.clear();
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            final List<LogoutPos> list = this.spots;
            synchronized (list) {
                AxisAlignedBB interpolateAxis;
                AxisAlignedBB bb;
                Color currentColor = null;
                double x;
                double y;
                double z;
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        bb = (interpolateAxis = RenderUtil.interpolateAxis(spot.getEntity().func_174813_aQ()));
                        if (this.colorSync.getValue()) {
                            currentColor = Colors.INSTANCE.getCurrentColor();
                        }
                        else {
                            // new(java.awt.Color.class)
                            new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
                        }
                        RenderUtil.drawBlockOutline(interpolateAxis, currentColor, 1.0f);
                        x = this.interpolate(spot.getEntity().field_70142_S, spot.getEntity().field_70165_t, event.getPartialTicks()) - LogoutSpots.mc.func_175598_ae().field_78725_b;
                        y = this.interpolate(spot.getEntity().field_70137_T, spot.getEntity().field_70163_u, event.getPartialTicks()) - LogoutSpots.mc.func_175598_ae().field_78726_c;
                        z = this.interpolate(spot.getEntity().field_70136_U, spot.getEntity().field_70161_v, event.getPartialTicks()) - LogoutSpots.mc.func_175598_ae().field_78723_d;
                        this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!Feature.fullNullCheck()) {
            this.spots.removeIf(spot -> LogoutSpots.mc.field_71439_g.func_70068_e((Entity)spot.getEntity()) >= MathUtil.square(this.range.getValue()));
        }
    }
    
    @SubscribeEvent
    public void onConnection(final ConnectionEvent event) {
        if (event.getStage() == 0) {
            final UUID uuid = event.getUuid();
            final EntityPlayer entity = LogoutSpots.mc.field_71441_e.func_152378_a(uuid);
            if (entity != null && this.message.getValue()) {
                Command.sendMessage("§a" + entity.func_70005_c_() + " just logged in" + (this.coords.getValue() ? (" at (" + (int)entity.field_70165_t + ", " + (int)entity.field_70163_u + ", " + (int)entity.field_70161_v + ")!") : "!"), this.notification.getValue());
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        }
        else if (event.getStage() == 1) {
            final EntityPlayer entity2 = event.getEntity();
            final UUID uuid2 = event.getUuid();
            final String name = event.getName();
            if (this.message.getValue()) {
                Command.sendMessage("§c" + event.getName() + " just logged out" + (this.coords.getValue() ? (" at (" + (int)entity2.field_70165_t + ", " + (int)entity2.field_70163_u + ", " + (int)entity2.field_70161_v + ")!") : "!"), this.notification.getValue());
            }
            if (name != null && entity2 != null && uuid2 != null) {
                this.spots.add(new LogoutPos(name, uuid2, entity2));
            }
        }
    }
    
    private void renderNameTag(final String name, final double x, final double yi, final double z, final float delta, final double xPos, final double yPos, final double zPos) {
        final double y = yi + 0.7;
        final Entity camera = Util.mc.func_175606_aa();
        assert camera != null;
        final double originalPositionX = camera.field_70165_t;
        final double originalPositionY = camera.field_70163_u;
        final double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, delta);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, delta);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, delta);
        final String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
        final double distance = camera.func_70011_f(x + LogoutSpots.mc.func_175598_ae().field_78730_l, y + LogoutSpots.mc.func_175598_ae().field_78731_m, z + LogoutSpots.mc.func_175598_ae().field_78728_n);
        final int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + this.scaling.getValue() * (distance * this.factor.getValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            scale = this.scaling.getValue() / 100.0;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)x, (float)y + 1.4f, (float)z);
        GlStateManager.func_179114_b(-LogoutSpots.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(LogoutSpots.mc.func_175598_ae().field_78732_j, (LogoutSpots.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179147_l();
        if (this.rect.getValue()) {
            RenderUtil.drawRect((float)(-width - 2), (float)(-(this.renderer.getFontHeight() + 1)), width + 2.0f, 1.5f, 1426063360);
        }
        GlStateManager.func_179084_k();
        this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue())));
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a(1.0f, 1500000.0f);
        GlStateManager.func_179121_F();
    }
    
    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }
    
    private static class LogoutPos
    {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;
        
        public LogoutPos(final String name, final UUID uuid, final EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.field_70165_t;
            this.y = entity.field_70163_u;
            this.z = entity.field_70161_v;
        }
        
        public String getName() {
            return this.name;
        }
        
        public UUID getUuid() {
            return this.uuid;
        }
        
        public EntityPlayer getEntity() {
            return this.entity;
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public double getZ() {
            return this.z;
        }
    }
}
