// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command.commands;

import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import dev.fxcte.creepyware.features.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new String[] { "<char>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("§cSpecify a new prefix.");
            return;
        }
        CreepyWare.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to §a" + CreepyWare.commandManager.getPrefix());
    }
}
