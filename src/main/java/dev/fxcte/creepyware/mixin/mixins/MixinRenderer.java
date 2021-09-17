// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin.mixins;

import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.Entity;

@Mixin({ Render.class })
abstract class MixinRenderer<T extends Entity>
{
    @Shadow
    protected boolean field_188301_f;
    @Shadow
    @Final
    protected RenderManager field_76990_c;
    
    @Shadow
    protected abstract boolean func_180548_c(final T p0);
    
    @Shadow
    protected abstract int func_188298_c(final T p0);
}
