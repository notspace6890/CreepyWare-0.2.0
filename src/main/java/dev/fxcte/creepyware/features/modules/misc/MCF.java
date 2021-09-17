// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import dev.fxcte.creepyware.features.command.Command;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.gui.CreepyWareGui;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import dev.fxcte.creepyware.features.setting.Bind;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class MCF extends Module
{
    private final Setting<Boolean> middleClick;
    private final Setting<Boolean> keyboard;
    private final Setting<Boolean> server;
    private final Setting<Bind> key;
    private boolean clicked;
    
    public MCF() {
        super("MCF", "Middleclick Friends.", Category.MISC, true, false, false);
        this.middleClick = (Setting<Boolean>)this.register(new Setting("Speed", "MiddleClick", 0.0, 0.0, (T)true, 0));
        this.keyboard = (Setting<Boolean>)this.register(new Setting("Speed", "Keyboard", 0.0, 0.0, (T)false, 0));
        this.server = (Setting<Boolean>)this.register(new Setting("Speed", "Server", 0.0, 0.0, (T)true, 0));
        this.key = (Setting<Bind>)this.register(new Setting("KeyBind", (T)new Bind(-1), v -> this.keyboard.getValue()));
        this.clicked = false;
    }
    
    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && this.middleClick.getValue() && MCF.mc.field_71462_r == null) {
                this.onClick();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (this.keyboard.getValue() && Keyboard.getEventKeyState() && !(MCF.mc.field_71462_r instanceof CreepyWareGui) && this.key.getValue().getKey() == Keyboard.getEventKey()) {
            this.onClick();
        }
    }
    
    private void onClick() {
        final RayTraceResult result = MCF.mc.field_71476_x;
        final Entity entity;
        if (result != null && result.field_72313_a == RayTraceResult.Type.ENTITY && (entity = result.field_72308_g) instanceof EntityPlayer) {
            if (CreepyWare.friendManager.isFriend(entity.func_70005_c_())) {
                CreepyWare.friendManager.removeFriend(entity.func_70005_c_());
                Command.sendMessage("§c" + entity.func_70005_c_() + "§r unfriended.");
                if (this.server.getValue() && ServerModule.getInstance().isConnected()) {
                    MCF.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                    MCF.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "friend del " + entity.func_70005_c_()));
                }
            }
            else {
                CreepyWare.friendManager.addFriend(entity.func_70005_c_());
                Command.sendMessage("§b" + entity.func_70005_c_() + "§r friended.");
                if (this.server.getValue() && ServerModule.getInstance().isConnected()) {
                    MCF.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
                    MCF.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "friend add " + entity.func_70005_c_()));
                }
            }
        }
        this.clicked = true;
    }
}
