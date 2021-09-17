// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import dev.fxcte.creepyware.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.io.IOException;
import java.io.FileWriter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.io.File;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class StashLogger extends Module
{
    private final Setting<Boolean> chests;
    private final Setting<Integer> chestsValue;
    private final Setting<Boolean> Shulkers;
    private final Setting<Integer> shulkersValue;
    private final Setting<Boolean> writeToFile;
    File mainFolder;
    final Iterator<NBTTagCompound> iterator;
    
    public StashLogger() {
        super("StashLogger", "Logs stashes", Category.MISC, true, false, false);
        this.chests = (Setting<Boolean>)this.register(new Setting("Speed", "Chests", 0.0, 0.0, (T)true, 0));
        this.chestsValue = (Setting<Integer>)this.register(new Setting("ChestsValue", (T)4, (T)1, (T)30, v -> this.chests.getValue()));
        this.Shulkers = (Setting<Boolean>)this.register(new Setting("Speed", "Shulkers", 0.0, 0.0, (T)true, 0));
        this.shulkersValue = (Setting<Integer>)this.register(new Setting("ShulkersValue", (T)4, (T)1, (T)30, v -> this.Shulkers.getValue()));
        this.writeToFile = (Setting<Boolean>)this.register(new Setting("Speed", "CoordsSaver", 0.0, 0.0, (T)true, 0));
        this.mainFolder = new File(Minecraft.func_71410_x().field_71412_D + File.separator + "legacy");
        this.iterator = null;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketChunkData) {
            final SPacketChunkData l_Packet = event.getPacket();
            int l_ChestsCount = 0;
            int shulkers = 0;
            for (final NBTTagCompound l_Tag : l_Packet.func_189554_f()) {
                final String l_Id = l_Tag.func_74779_i("id");
                if (l_Id.equals("minecraft:chest") && this.chests.getValue()) {
                    ++l_ChestsCount;
                }
                else {
                    if (!l_Id.equals("minecraft:shulker_box")) {
                        continue;
                    }
                    if (!this.Shulkers.getValue()) {
                        continue;
                    }
                    ++shulkers;
                }
            }
            if (l_ChestsCount >= this.chestsValue.getValue()) {
                this.SendMessage(String.format("%s chests located at X: %s, Z: %s", l_ChestsCount, l_Packet.func_149273_e() * 16, l_Packet.func_149271_f() * 16), true);
            }
            if (shulkers >= this.shulkersValue.getValue()) {
                this.SendMessage(String.format("%s shulker boxes at X: %s, Z: %s", shulkers, l_Packet.func_149273_e() * 16, l_Packet.func_149271_f() * 16), true);
            }
        }
    }
    
    private void SendMessage(final String message, final boolean save) {
        final String string;
        final String server = string = (Minecraft.func_71410_x().func_71356_B() ? "singleplayer".toUpperCase() : StashLogger.mc.func_147104_D().field_78845_b);
        if (this.writeToFile.getValue() && save) {
            try {
                final FileWriter writer = new FileWriter(this.mainFolder + "/stashes.txt", true);
                writer.write("[" + server + "]: " + message + "\n");
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        StashLogger.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_194007_a(SoundEvents.field_187604_bf, 1.0f, 1.0f));
        Command.sendMessage(ChatFormatting.GREEN + message);
    }
}
