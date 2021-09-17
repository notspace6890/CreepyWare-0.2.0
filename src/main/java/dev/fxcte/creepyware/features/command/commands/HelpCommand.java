// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command.commands;

import java.util.Iterator;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("commands");
    }
    
    @Override
    public void execute(final String[] commands) {
        Command.sendMessage("You can use following commands: ");
        for (final Command command : CreepyWare.commandManager.getCommands()) {
            Command.sendMessage(CreepyWare.commandManager.getPrefix() + command.getName());
        }
    }
}
