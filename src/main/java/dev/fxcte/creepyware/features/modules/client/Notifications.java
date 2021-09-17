// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import net.minecraft.util.text.ITextComponent;
import dev.fxcte.creepyware.event.events.ClientEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketSpawnObject;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.manager.FileManager;
import java.util.Iterator;
import net.minecraft.init.SoundEvents;
import dev.fxcte.creepyware.CreepyWare;
import java.util.Collection;
import dev.fxcte.creepyware.features.command.Command;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.util.Timer;
import java.util.List;
import dev.fxcte.creepyware.features.modules.Module;

public class Notifications extends Module
{
    private static final String fileName = "creepyware/util/ModuleMessage_List.txt";
    private static final List<String> modules;
    private static Notifications INSTANCE;
    private final Timer timer;
    public Setting<Boolean> totemPops;
    public Setting<Boolean> totemNoti;
    public Setting<Integer> delay;
    public Setting<Boolean> clearOnLogout;
    public Setting<Boolean> moduleMessage;
    public Setting<Boolean> list;
    public Setting<Boolean> watermark;
    public Setting<Boolean> visualRange;
    public Setting<Boolean> VisualRangeSound;
    public Setting<Boolean> coords;
    public Setting<Boolean> leaving;
    public Setting<Boolean> pearls;
    public Setting<Boolean> crash;
    public Setting<Boolean> popUp;
    public Timer totemAnnounce;
    private final Setting<Boolean> readfile;
    private List<EntityPlayer> knownPlayers;
    private boolean check;
    
    public Notifications() {
        super("Notifications", "Sends Messages.", Category.CLIENT, true, false, false);
        this.timer = new Timer();
        this.totemPops = (Setting<Boolean>)this.register(new Setting("Speed", "TotemPops", 0.0, 0.0, (T)false, 0));
        this.totemNoti = (Setting<Boolean>)this.register(new Setting("TotemNoti", (T)true, v -> this.totemPops.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)2000, (T)0, (T)5000, v -> this.totemPops.getValue(), "Delays messages."));
        this.clearOnLogout = (Setting<Boolean>)this.register(new Setting("Speed", "LogoutClear", 0.0, 0.0, (T)false, 0));
        this.moduleMessage = (Setting<Boolean>)this.register(new Setting("Speed", "ModuleMessage", 0.0, 0.0, (T)false, 0));
        this.list = (Setting<Boolean>)this.register(new Setting("List", (T)false, v -> this.moduleMessage.getValue()));
        this.watermark = (Setting<Boolean>)this.register(new Setting("Watermark", (T)true, v -> this.moduleMessage.getValue()));
        this.visualRange = (Setting<Boolean>)this.register(new Setting("Speed", "VisualRange", 0.0, 0.0, (T)false, 0));
        this.VisualRangeSound = (Setting<Boolean>)this.register(new Setting("Speed", "VisualRangeSound", 0.0, 0.0, (T)false, 0));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (T)true, v -> this.visualRange.getValue()));
        this.leaving = (Setting<Boolean>)this.register(new Setting("Leaving", (T)false, v -> this.visualRange.getValue()));
        this.pearls = (Setting<Boolean>)this.register(new Setting("Speed", "PearlNotifs", 0.0, 0.0, (T)false, 0));
        this.crash = (Setting<Boolean>)this.register(new Setting("Speed", "Crash", 0.0, 0.0, (T)false, 0));
        this.popUp = (Setting<Boolean>)this.register(new Setting("Speed", "PopUpVisualRange", 0.0, 0.0, (T)false, 0));
        this.totemAnnounce = new Timer();
        this.readfile = (Setting<Boolean>)this.register(new Setting("LoadFile", (T)false, v -> this.moduleMessage.getValue()));
        this.knownPlayers = new ArrayList<EntityPlayer>();
        this.setInstance();
    }
    
    public static Notifications getInstance() {
        if (Notifications.INSTANCE == null) {
            Notifications.INSTANCE = new Notifications();
        }
        return Notifications.INSTANCE;
    }
    
    public static void displayCrash(final Exception e) {
        Command.sendMessage("§cException caught: " + e.getMessage());
    }
    
    private void setInstance() {
        Notifications.INSTANCE = this;
    }
    
    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }
    
    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<EntityPlayer>();
        if (!this.check) {
            this.loadFile();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.readfile.getValue()) {
            if (!this.check) {
                Command.sendMessage("Loading File...");
                this.timer.reset();
                this.loadFile();
            }
            this.check = true;
        }
        if (this.check && this.timer.passedMs(750L)) {
            this.readfile.setValue(false);
            this.check = false;
        }
        if (this.visualRange.getValue()) {
            final ArrayList<EntityPlayer> tickPlayerList = new ArrayList<EntityPlayer>(Notifications.mc.field_71441_e.field_73010_i);
            if (tickPlayerList.size() > 0) {
                for (final EntityPlayer player : tickPlayerList) {
                    if (!player.func_70005_c_().equals(Notifications.mc.field_71439_g.func_70005_c_())) {
                        if (this.knownPlayers.contains(player)) {
                            continue;
                        }
                        this.knownPlayers.add(player);
                        if (CreepyWare.friendManager.isFriend(player)) {
                            Command.sendMessage("Player §a" + player.func_70005_c_() + "§r entered your visual range" + (this.coords.getValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), this.popUp.getValue());
                        }
                        else {
                            Command.sendMessage("Player §c" + player.func_70005_c_() + "§r entered your visual range" + (this.coords.getValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), this.popUp.getValue());
                        }
                        if (this.VisualRangeSound.getValue()) {
                            Notifications.mc.field_71439_g.func_184185_a(SoundEvents.field_187689_f, 1.0f, 1.0f);
                        }
                        return;
                    }
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (final EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains(player)) {
                        continue;
                    }
                    this.knownPlayers.remove(player);
                    if (this.leaving.getValue()) {
                        if (CreepyWare.friendManager.isFriend(player)) {
                            Command.sendMessage("Player §a" + player.func_70005_c_() + "§r left your visual range" + (this.coords.getValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), this.popUp.getValue());
                        }
                        else {
                            Command.sendMessage("Player §c" + player.func_70005_c_() + "§r left your visual range" + (this.coords.getValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), this.popUp.getValue());
                        }
                    }
                }
            }
        }
    }
    
    public void loadFile() {
        final List<String> fileInput = FileManager.readTextFileAllLines("creepyware/util/ModuleMessage_List.txt");
        final Iterator<String> i = fileInput.iterator();
        Notifications.modules.clear();
        while (i.hasNext()) {
            final String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) {
                continue;
            }
            Notifications.modules.add(s);
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.pearls.getValue()) {
            final SPacketSpawnObject packet = event.getPacket();
            final EntityPlayer player = Notifications.mc.field_71441_e.func_184137_a(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e(), 1.0, false);
            if (player == null) {
                return;
            }
            if (packet.func_149001_c() == 85) {
                Command.sendMessage("§cPearl thrown by " + player.func_70005_c_() + " at X:" + (int)packet.func_186880_c() + " Y:" + (int)packet.func_186882_d() + " Z:" + (int)packet.func_186881_e());
            }
        }
    }
    
    public TextComponentString getNotifierOn(final Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case FUTURE: {
                    final TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.GREEN + "on" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case PHOBOS: {
                    final TextComponentString text = new TextComponentString(CreepyWare.commandManager.getClientMessage() + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.GREEN + " enabled.");
                    return text;
                }
            }
        }
        final TextComponentString text = new TextComponentString(CreepyWare.commandManager.getClientMessage() + ChatFormatting.GREEN + module.getDisplayName() + " toggled on.");
        return text;
    }
    
    public TextComponentString getNotifierOff(final Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case FUTURE: {
                    final TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.RED + "off" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case PHOBOS: {
                    final TextComponentString text = new TextComponentString(CreepyWare.commandManager.getClientMessage() + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.RED + " disabled.");
                    return text;
                }
            }
        }
        final TextComponentString text = new TextComponentString(CreepyWare.commandManager.getClientMessage() + ChatFormatting.RED + module.getDisplayName() + " toggled off.");
        return text;
    }
    
    @SubscribeEvent
    public void onToggleModule(final ClientEvent event) {
        if (!this.moduleMessage.getValue()) {
            return;
        }
        Module module;
        if (event.getStage() == 0 && !(module = (Module)event.getFeature()).equals(this) && (Notifications.modules.contains(module.getDisplayName()) || !this.list.getValue())) {
            int moduleNumber = 0;
            for (final char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)this.getNotifierOff(module), moduleNumber);
        }
        if (event.getStage() == 1 && (Notifications.modules.contains((module = (Module)event.getFeature()).getDisplayName()) || !this.list.getValue())) {
            int moduleNumber = 0;
            for (final char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)this.getNotifierOn(module), moduleNumber);
        }
    }
    
    static {
        modules = new ArrayList<String>();
        Notifications.INSTANCE = new Notifications();
    }
}
