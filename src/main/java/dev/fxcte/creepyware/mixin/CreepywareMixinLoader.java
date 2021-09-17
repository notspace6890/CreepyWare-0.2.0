// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class CreepywareMixinLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public CreepywareMixinLoader() {
        CreepyWare.LOGGER.info("CreepyWare mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.creepyware.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        CreepyWare.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        CreepywareMixinLoader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        CreepywareMixinLoader.isObfuscatedEnvironment = false;
    }
}
