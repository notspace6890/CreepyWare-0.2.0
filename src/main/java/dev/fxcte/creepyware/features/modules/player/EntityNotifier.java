// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import java.util.Iterator;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.init.SoundEvents;
import dev.fxcte.creepyware.features.command.Command;
import net.minecraft.entity.monster.EntityGhast;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.util.HashSet;
import dev.fxcte.creepyware.features.setting.Setting;
import java.awt.TrayIcon;
import java.awt.Image;
import net.minecraft.entity.Entity;
import java.util.Set;
import dev.fxcte.creepyware.features.modules.Module;

public class EntityNotifier extends Module
{
    private final Set<Entity> ghasts;
    private final Set<Entity> donkeys;
    private final Set<Entity> mules;
    private final Set<Entity> llamas;
    private final Image image;
    private final TrayIcon icon;
    public Setting<Boolean> Chat;
    public Setting<Boolean> Sound;
    public Setting<Boolean> Desktop;
    public Setting<Boolean> Ghasts;
    public Setting<Boolean> Donkeys;
    public Setting<Boolean> Mules;
    public Setting<Boolean> Llamas;
    
    public EntityNotifier() {
        super("EntityNotifier", "Helps you find certain things", Category.PLAYER, true, false, false);
        this.ghasts = new HashSet<Entity>();
        this.donkeys = new HashSet<Entity>();
        this.mules = new HashSet<Entity>();
        this.llamas = new HashSet<Entity>();
        this.image = Toolkit.getDefaultToolkit().getImage("resources/creepy.png");
        this.icon = new TrayIcon(this.image, "Creepyware");
        this.Chat = (Setting<Boolean>)this.register(new Setting("Speed", "Chat", 0.0, 0.0, (T)true, 0));
        this.Sound = (Setting<Boolean>)this.register(new Setting("Speed", "Sound", 0.0, 0.0, (T)true, 0));
        this.Desktop = (Setting<Boolean>)this.register(new Setting("Speed", "DesktopNotifs", 0.0, 0.0, (T)true, 0));
        this.Ghasts = (Setting<Boolean>)this.register(new Setting("Speed", "Ghasts", 0.0, 0.0, (T)true, 0));
        this.Donkeys = (Setting<Boolean>)this.register(new Setting("Speed", "Donkeys", 0.0, 0.0, (T)true, 0));
        this.Mules = (Setting<Boolean>)this.register(new Setting("Speed", "Mules", 0.0, 0.0, (T)true, 0));
        this.Llamas = (Setting<Boolean>)this.register(new Setting("Speed", "Llamas", 0.0, 0.0, (T)true, 0));
        this.icon.setImageAutoSize(true);
        try {
            final SystemTray tray = SystemTray.getSystemTray();
            tray.add(this.icon);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public void onEnable() {
        this.ghasts.clear();
        this.donkeys.clear();
        this.mules.clear();
        this.llamas.clear();
    }
    
    @Override
    public void onUpdate() {
        if (this.Ghasts.getValue()) {
            for (final Entity entity : EntityNotifier.mc.field_71441_e.func_72910_y()) {
                if (entity instanceof EntityGhast) {
                    if (this.ghasts.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Ghast Detected at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.");
                    }
                    this.ghasts.add(entity);
                    if (!this.Desktop.getValue()) {
                        continue;
                    }
                    this.icon.displayMessage("Creepyware", "I found a ghast at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.", TrayIcon.MessageType.INFO);
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0f, 1.0f);
                }
            }
        }
        if (this.Donkeys.getValue()) {
            for (final Entity entity : EntityNotifier.mc.field_71441_e.func_72910_y()) {
                if (entity instanceof EntityDonkey) {
                    if (this.donkeys.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Donkey Detected at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.");
                    }
                    this.donkeys.add(entity);
                    if (!this.Desktop.getValue()) {
                        continue;
                    }
                    this.icon.displayMessage("Creepyware", "I found a donkey at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.", TrayIcon.MessageType.INFO);
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0f, 1.0f);
                }
            }
        }
        if (this.Mules.getValue()) {
            for (final Entity entity : EntityNotifier.mc.field_71441_e.func_72910_y()) {
                if (entity instanceof EntityMule) {
                    if (this.mules.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Mule Detected at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.");
                    }
                    this.mules.add(entity);
                    if (!this.Desktop.getValue()) {
                        continue;
                    }
                    this.icon.displayMessage("Creepyware", "I found a mule at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.", TrayIcon.MessageType.INFO);
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0f, 1.0f);
                }
            }
        }
        if (this.Llamas.getValue()) {
            for (final Entity entity : EntityNotifier.mc.field_71441_e.func_72910_y()) {
                if (entity instanceof EntityLlama) {
                    if (this.llamas.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Llama Detected at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.");
                    }
                    this.llamas.add(entity);
                    if (!this.Desktop.getValue()) {
                        continue;
                    }
                    this.icon.displayMessage("Creepyware", "I found a llama at: " + Math.round((float)entity.func_180425_c().func_177958_n()) + "x, " + Math.round((float)entity.func_180425_c().func_177956_o()) + "y, " + Math.round((float)entity.func_180425_c().func_177952_p()) + "z.", TrayIcon.MessageType.INFO);
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0f, 1.0f);
                }
            }
        }
    }
}
