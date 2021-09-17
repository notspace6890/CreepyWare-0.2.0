// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware;

import dev.fxcte.creepyware.features.modules.misc.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;
    private static int index;
    
    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordPresence.rpc.Discord_Initialize("866117154675097600", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = ((Minecraft.func_71410_x().field_71462_r instanceof GuiMainMenu) ? "Main menu" : ("Playing " + ((Minecraft.func_71410_x().field_71422_O != null) ? (RPC.INSTANCE.showIP.getValue() ? ("on " + Minecraft.func_71410_x().field_71422_O.field_78845_b + ".") : " multiplayer") : " singleplayer")));
        DiscordPresence.presence.state = "Donbass activity";
        DiscordPresence.presence.largeImageKey = "creepy";
        DiscordPresence.presence.largeImageText = "b0.2.0";
        DiscordPresence.presence.smallImageKey = "logo";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
        DiscordRichPresence presence;
        String string;
        String string2;
        final StringBuilder sb;
        (DiscordPresence.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordPresence.rpc.Discord_RunCallbacks();
                presence = DiscordPresence.presence;
                if (Minecraft.func_71410_x().field_71462_r instanceof GuiMainMenu) {
                    string = "Main menu";
                }
                else {
                    new StringBuilder().append("Playing ");
                    if (Minecraft.func_71410_x().field_71422_O != null) {
                        if (RPC.INSTANCE.showIP.getValue()) {
                            string2 = "on " + Minecraft.func_71410_x().field_71422_O.field_78845_b + ".";
                        }
                        else {
                            string2 = " multiplayer";
                        }
                    }
                    else {
                        string2 = " singleplayer";
                    }
                    string = sb.append(string2).toString();
                }
                presence.details = string;
                DiscordPresence.presence.state = "Eating kids";
                if (RPC.INSTANCE.users.getValue()) {
                    if (DiscordPresence.index == 6) {
                        DiscordPresence.index = 1;
                    }
                    DiscordPresence.presence.smallImageKey = "user" + DiscordPresence.index;
                    if (++DiscordPresence.index == 2) {
                        DiscordPresence.presence.smallImageText = "BlackBro4";
                    }
                    if (DiscordPresence.index == 3) {
                        DiscordPresence.presence.smallImageText = "rianix";
                    }
                    if (DiscordPresence.index == 4) {
                        DiscordPresence.presence.smallImageText = "Sudmarin";
                    }
                    if (DiscordPresence.index == 5) {
                        DiscordPresence.presence.smallImageText = "Ziasan";
                    }
                }
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler")).start();
    }
    
    public static void stop() {
        if (DiscordPresence.thread != null && !DiscordPresence.thread.isInterrupted()) {
            DiscordPresence.thread.interrupt();
        }
        DiscordPresence.rpc.Discord_Shutdown();
    }
    
    static {
        DiscordPresence.index = 1;
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
    }
}
