// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command.commands;

import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.command.Command;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        CreepyWare.reload();
    }
}
