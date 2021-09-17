// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command.commands;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import dev.fxcte.creepyware.features.command.Command;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)Util.mc.func_147118_V(), new String[] { "sndManager", "field_147694_f" });
            sndManager.func_148596_a();
            Command.sendMessage("§aReloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println("Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            Command.sendMessage("§cCouldnt Reload Sound System!");
        }
    }
}
