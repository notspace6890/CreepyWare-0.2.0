// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import java.util.function.BiFunction;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.modules.client.ModuleTools;
import java.util.Iterator;
import dev.fxcte.creepyware.features.command.Command;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import dev.fxcte.creepyware.features.modules.client.Notifications;
import dev.fxcte.creepyware.features.Feature;

public class TotemPopManager extends Feature
{
    private Notifications notifications;
    private Map<EntityPlayer, Integer> poplist;
    private final Set<EntityPlayer> toAnnounce;
    
    public TotemPopManager() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.toAnnounce = new HashSet<EntityPlayer>();
    }
    
    public void onUpdate() {
        if (this.notifications.totemAnnounce.passedMs(this.notifications.delay.getValue()) && this.notifications.isOn() && this.notifications.totemPops.getValue()) {
            for (final EntityPlayer player : this.toAnnounce) {
                if (player == null) {
                    continue;
                }
                int playerNumber = 0;
                for (final char character : player.func_70005_c_().toCharArray()) {
                    playerNumber += character;
                    playerNumber *= 10;
                }
                Command.sendOverwriteMessage(this.pop(player), playerNumber, this.notifications.totemNoti.getValue());
                this.toAnnounce.remove(player);
                this.notifications.totemAnnounce.reset();
                break;
            }
        }
    }
    
    public String pop(final EntityPlayer player) {
        if (this.getTotemPops(player) == 1) {
            if (!ModuleTools.getInstance().isEnabled()) {
                return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem.";
            }
            switch (ModuleTools.getInstance().popNotifier.getValue()) {
                case FUTURE: {
                    final String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.func_70005_c_() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totem.";
                    return text;
                }
                case PHOBOS: {
                    final String text = ChatFormatting.GOLD + player.func_70005_c_() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totem.";
                    return text;
                }
                case NONE: {
                    return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem.";
                }
            }
        }
        else {
            if (!ModuleTools.getInstance().isEnabled()) {
                return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems.";
            }
            switch (ModuleTools.getInstance().popNotifier.getValue()) {
                case FUTURE: {
                    final String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.func_70005_c_() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totems.";
                    return text;
                }
                case PHOBOS: {
                    final String text = ChatFormatting.GOLD + player.func_70005_c_() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totems.";
                    return text;
                }
                case NONE: {
                    return ChatFormatting.WHITE + player.func_70005_c_() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems.";
                }
            }
        }
        return "";
    }
    
    public void onLogout() {
        this.onOwnLogout(this.notifications.clearOnLogout.getValue());
    }
    
    public void init() {
        this.notifications = CreepyWare.moduleManager.getModuleByClass(Notifications.class);
    }
    
    public void onTotemPop(final EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)TotemPopManager.mc.field_71439_g)) {
            this.toAnnounce.add(player);
            this.notifications.totemAnnounce.reset();
        }
    }
    
    public String death1(final EntityPlayer player) {
        if (this.getTotemPops(player) == 1) {
            if (!ModuleTools.getInstance().isEnabled()) {
                return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem!";
            }
            switch (ModuleTools.getInstance().popNotifier.getValue()) {
                case FUTURE: {
                    final String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.func_70005_c_() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totem.";
                    return text;
                }
                case PHOBOS: {
                    final String text = ChatFormatting.GOLD + player.func_70005_c_() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totem.";
                    return text;
                }
                case NONE: {
                    return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem!";
                }
            }
        }
        else {
            if (!ModuleTools.getInstance().isEnabled()) {
                return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems!";
            }
            switch (ModuleTools.getInstance().popNotifier.getValue()) {
                case FUTURE: {
                    final String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.func_70005_c_() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totems.";
                    return text;
                }
                case PHOBOS: {
                    final String text = ChatFormatting.GOLD + player.func_70005_c_() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totems.";
                    return text;
                }
                case NONE: {
                    return CreepyWare.commandManager.getClientMessage() + ChatFormatting.WHITE + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems!";
                }
            }
        }
        return null;
    }
    
    public void onDeath(final EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals((Object)TotemPopManager.mc.field_71439_g) && this.notifications.isOn() && this.notifications.totemPops.getValue()) {
            int playerNumber = 0;
            for (final char character : player.func_70005_c_().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            Command.sendOverwriteMessage(this.death1(player), playerNumber, this.notifications.totemNoti.getValue());
            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }
    
    public void onLogout(final EntityPlayer player, final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }
    
    public void onOwnLogout(final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }
    
    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }
    
    public void resetPops(final EntityPlayer player) {
        this.setTotemPops(player, 0);
    }
    
    public void popTotem(final EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }
    
    public void setTotemPops(final EntityPlayer player, final int amount) {
        this.poplist.put(player, amount);
    }
    
    public int getTotemPops(final EntityPlayer player) {
        final Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }
    
    public String getTotemPopString(final EntityPlayer player) {
        return "§f" + ((this.getTotemPops(player) <= 0) ? "" : ("-" + this.getTotemPops(player) + " "));
    }
}
