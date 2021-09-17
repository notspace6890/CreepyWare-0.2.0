// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import dev.fxcte.creepyware.features.modules.render.PopChams;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.fxcte.creepyware.util.EntityUtil;
import java.awt.Color;
import dev.fxcte.creepyware.util.RenderUtil;
import dev.fxcte.creepyware.features.modules.client.Colors;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.features.modules.render.Chams;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    @Shadow
    protected ModelBase field_77045_g;
    private static final ResourceLocation glint;
    
    public MixinRenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
    }
    
    @Redirect(method = { "renderModel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final boolean cancel = false;
        if (Chams.getInstance().isEnabled() && entityIn instanceof EntityPlayer && Chams.getInstance().colored.getValue() && !Chams.getInstance().textured.getValue()) {
            if (!Chams.getInstance().textured.getValue()) {
                GL11.glPushAttrib(1048575);
                GL11.glDisable(3008);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glLineWidth(1.5f);
                GL11.glEnable(2960);
                if (Chams.getInstance().rainbow.getValue()) {
                    final Color rainbowColor1 = Chams.getInstance().colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(Chams.getInstance().speed.getValue() * 100, 0, Chams.getInstance().saturation.getValue() / 100.0f, Chams.getInstance().brightness.getValue() / 100.0f));
                    final Color rainbowColor2 = EntityUtil.getColor(entityIn, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glEnable(10754);
                    GL11.glColor4f(rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, Chams.getInstance().alpha.getValue() / 255.0f);
                    modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                else if (Chams.getInstance().xqz.getValue()) {
                    final Color hiddenColor = Chams.getInstance().colorSync.getValue() ? EntityUtil.getColor(entityIn, Chams.getInstance().hiddenRed.getValue(), Chams.getInstance().hiddenGreen.getValue(), Chams.getInstance().hiddenBlue.getValue(), Chams.getInstance().hiddenAlpha.getValue(), true) : EntityUtil.getColor(entityIn, Chams.getInstance().hiddenRed.getValue(), Chams.getInstance().hiddenGreen.getValue(), Chams.getInstance().hiddenBlue.getValue(), Chams.getInstance().hiddenAlpha.getValue(), true);
                    final Color visibleColor2 = Chams.getInstance().colorSync.getValue() ? EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true) : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glEnable(10754);
                    GL11.glColor4f(hiddenColor.getRed() / 255.0f, hiddenColor.getGreen() / 255.0f, hiddenColor.getBlue() / 255.0f, Chams.getInstance().alpha.getValue() / 255.0f);
                    modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glColor4f(visibleColor2.getRed() / 255.0f, visibleColor2.getGreen() / 255.0f, visibleColor2.getBlue() / 255.0f, Chams.getInstance().alpha.getValue() / 255.0f);
                    modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }
                else {
                    final Color visibleColor3 = Chams.getInstance().colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glEnable(10754);
                    GL11.glColor4f(visibleColor3.getRed() / 255.0f, visibleColor3.getGreen() / 255.0f, visibleColor3.getBlue() / 255.0f, Chams.getInstance().alpha.getValue() / 255.0f);
                    modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glEnable(3042);
                GL11.glEnable(2896);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glPopAttrib();
            }
        }
        else if (Chams.getInstance().textured.getValue()) {
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            final Color visibleColor3 = Chams.getInstance().colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
            GL11.glColor4f(visibleColor3.getRed() / 255.0f, visibleColor3.getGreen() / 255.0f, visibleColor3.getBlue() / 255.0f, Chams.getInstance().alpha.getValue() / 255.0f);
            modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
        }
        else if (!cancel) {
            modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    public void doRenderPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && !Chams.getInstance().colored.getValue() && entity != null) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    public void doRenderPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && !Chams.getInstance().colored.getValue() && entity != null) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
    
    @Inject(method = { "renderModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V") }, cancellable = true)
    private void renderModel(final EntityLivingBase entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final CallbackInfo info) {
        if (PopChams.pops.containsKey(entity.func_145782_y())) {
            if (PopChams.pops.get(entity.func_145782_y()) == 0) {
                Minecraft.func_71410_x().field_71441_e.func_73028_b(entity.field_145783_c);
            }
            else if (PopChams.pops.get(entity.func_145782_y()) < 0) {
                if (PopChams.pops.get(entity.func_145782_y()) < -5) {
                    PopChams.pops.remove(entity.func_145782_y());
                }
                return;
            }
            GlStateManager.func_187408_a(GlStateManager.Profile.TRANSPARENT_MODEL);
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glDisable(2929);
            GL11.glEnable(10754);
            GL11.glPolygonMode(1032, 6914);
            new Color(PopChams.INSTANCE.red.getValue(), PopChams.INSTANCE.green.getValue(), PopChams.INSTANCE.blue.getValue(), PopChams.pops.get(entity.func_145782_y()));
            this.field_77045_g.func_78088_a((Entity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable(2929);
            GL11.glEnable(3553);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
            PopChams.pops.computeIfPresent(entity.func_145782_y(), (key, oldValue) -> oldValue - 1);
        }
    }
    
    static {
        glint = new ResourceLocation("textures/shinechams.png");
    }
}
