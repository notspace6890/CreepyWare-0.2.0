// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.render;

import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.glu.Cylinder;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class PenisESP extends Module
{
    private final Setting<Float> penisSize;
    
    public PenisESP() {
        super("PenisESP", "caution:ur pp is small", Category.RENDER, false, false, false);
        this.penisSize = (Setting<Float>)this.register(new Setting("PenisSize", (T)1.5f, (T)0.1f, (T)5.0f));
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final Object o : PenisESP.mc.field_71441_e.field_72996_f) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double n = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * PenisESP.mc.field_71428_T.field_194147_b;
                PenisESP.mc.func_175598_ae();
                final double x = n - PenisESP.mc.func_175598_ae().field_78725_b;
                final double n2 = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * PenisESP.mc.field_71428_T.field_194147_b;
                PenisESP.mc.func_175598_ae();
                final double y = n2 - PenisESP.mc.func_175598_ae().field_78726_c;
                final double n3 = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * PenisESP.mc.field_71428_T.field_194147_b;
                PenisESP.mc.func_175598_ae();
                final double z = n3 - PenisESP.mc.func_175598_ae().field_78723_d;
                GL11.glPushMatrix();
                RenderHelper.func_74518_a();
                this.esp(player, x, y, z);
                RenderHelper.func_74519_b();
                GL11.glPopMatrix();
            }
        }
    }
    
    public void esp(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.field_70177_z, 0.0f, player.field_70131_O, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.field_70131_O / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((double)(player.func_70093_af() ? 35 : 0), 1.0, 0.0, 0.0);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f * this.penisSize.getValue(), 0.11f, 0.4f, 25, 20);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f * this.penisSize.getValue(), 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f * this.penisSize.getValue(), 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f * this.penisSize.getValue(), 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
}
