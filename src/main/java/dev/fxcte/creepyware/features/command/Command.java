// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.features.Feature;

public abstract class Command extends Feature
{
    protected String name;
    protected String[] commands;
    
    public Command(final String name) {
        super(name);
        this.name = name;
        this.commands = new String[] { "" };
    }
    
    public Command(final String name, final String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }
    
    public static void sendMessage(final String message, final boolean notification) {
        sendSilentMessage(CreepyWare.commandManager.getClientMessage() + " §r" + message);
        if (notification) {
            CreepyWare.notificationManager.addNotification(message, 3000L);
        }
    }
    
    public static void sendMessage(final String message) {
        sendSilentMessage(CreepyWare.commandManager.getClientMessage() + " §r" + message);
    }
    
    public static void sendSilentMessage(final String message) {
        if (nullCheck()) {
            return;
        }
        Command.mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(message));
    }
    
    public static void sendOverwriteMessage(final String message, final int id, final boolean notification) {
        final TextComponentString component = new TextComponentString(message);
        Command.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)component, id);
        if (notification) {
            CreepyWare.notificationManager.addNotification(message, 3000L);
        }
    }
    
    public static void sendRainbowMessage(final String message) {
        final StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.insert(0, "§+");
        Command.mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(stringBuilder.toString()));
    }
    
    public static String getCommandPrefix() {
        return CreepyWare.commandManager.getPrefix();
    }
    
    public abstract void execute(final String[] p0);
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String[] getCommands() {
        return this.commands;
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        private final String text;
        
        public ChatMessage(final String text) {
            final Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher matcher = pattern.matcher(text);
            final StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                final String replacement = "§" + matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }
        
        public String func_150261_e() {
            return this.text;
        }
        
        public ITextComponent func_150259_f() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}
