// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command.commands;

import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.util.Util;
import dev.fxcte.creepyware.features.modules.misc.FriendSettings;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.UUID;
import java.util.Map;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.command.Command;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (CreepyWare.friendManager.getFriends().isEmpty()) {
                Command.sendMessage("You currently dont have any friends added.");
            }
            else {
                Command.sendMessage("Friends: ");
                for (final Map.Entry<String, UUID> entry : CreepyWare.friendManager.getFriends().entrySet()) {
                    Command.sendMessage(entry.getKey());
                }
            }
            return;
        }
        if (commands.length != 2) {
            if (commands.length >= 2) {
                final String s = commands[0];
                switch (s) {
                    case "add": {
                        CreepyWare.friendManager.addFriend(commands[1]);
                        Command.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                        if (FriendSettings.getInstance().notify.getValue()) {
                            Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/w " + commands[1] + " I just added you to my friends list on Charlie dana hack!"));
                        }
                    }
                    case "del": {
                        CreepyWare.friendManager.removeFriend(commands[1]);
                        if (FriendSettings.getInstance().notify.getValue()) {
                            Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/w " + commands[1] + " I just removed you from my friends list on Charlie dana hack!"));
                        }
                        Command.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    }
                    default: {
                        Command.sendMessage("Unknown Command, try friend add/del (name)");
                        break;
                    }
                }
            }
            return;
        }
        final String s2 = commands[0];
        switch (s2) {
            case "reset": {
                CreepyWare.friendManager.onLoad();
                Command.sendMessage("Friends got reset.");
            }
            default: {
                Command.sendMessage(commands[0] + (CreepyWare.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            }
        }
    }
}
