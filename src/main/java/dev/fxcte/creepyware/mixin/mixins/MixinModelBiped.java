// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.fxcte.creepyware.features.modules.render.NoRender;
import net.minecraft.entity.monster.EntityPigZombie;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelBiped.class })
public class MixinModelBiped
{
    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        if (entityIn instanceof EntityPigZombie && NoRender.getInstance().pigmen.getValue()) {
            ci.cancel();
        }
    }
}
