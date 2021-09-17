// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.movement;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.fxcte.creepyware.event.events.PacketEvent;
import net.minecraft.client.gui.GuiChat;
import dev.fxcte.creepyware.event.events.KeyEvent;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.GuiOptions;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import dev.fxcte.creepyware.features.modules.Module;

public class NoSlowDown extends Module
{
    private static NoSlowDown INSTANCE;
    private static final KeyBinding[] keys;
    public final Setting<Double> webHorizontalFactor;
    public final Setting<Double> webVerticalFactor;
    public Setting<Boolean> guiMove;
    public Setting<Boolean> noSlow;
    public Setting<Boolean> soulSand;
    public Setting<Boolean> strict;
    public Setting<Boolean> sneakPacket;
    public Setting<Boolean> endPortal;
    public Setting<Boolean> webs;
    private boolean sneaking;
    
    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from getting slowed down.", Category.MOVEMENT, true, false, false);
        this.webHorizontalFactor = (Setting<Double>)this.register(new Setting("WebHSpeed", (T)2.0, (T)0.0, (T)100.0));
        this.webVerticalFactor = (Setting<Double>)this.register(new Setting("WebVSpeed", (T)2.0, (T)0.0, (T)100.0));
        this.guiMove = (Setting<Boolean>)this.register(new Setting("Speed", "GuiMove", 0.0, 0.0, (T)true, 0));
        this.noSlow = (Setting<Boolean>)this.register(new Setting("Speed", "NoSlow", 0.0, 0.0, (T)true, 0));
        this.soulSand = (Setting<Boolean>)this.register(new Setting("Speed", "SoulSand", 0.0, 0.0, (T)true, 0));
        this.strict = (Setting<Boolean>)this.register(new Setting("Speed", "Strict", 0.0, 0.0, (T)false, 0));
        this.sneakPacket = (Setting<Boolean>)this.register(new Setting("Speed", "SneakPacket", 0.0, 0.0, (T)false, 0));
        this.endPortal = (Setting<Boolean>)this.register(new Setting("Speed", "EndPortal", 0.0, 0.0, (T)false, 0));
        this.webs = (Setting<Boolean>)this.register(new Setting("Speed", "Webs", 0.0, 0.0, (T)false, 0));
        this.sneaking = false;
        this.setInstance();
    }
    
    public static NoSlowDown getInstance() {
        if (NoSlowDown.INSTANCE == null) {
            NoSlowDown.INSTANCE = new NoSlowDown();
        }
        return NoSlowDown.INSTANCE;
    }
    
    private void setInstance() {
        NoSlowDown.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.guiMove.getValue()) {
            if (NoSlowDown.mc.field_71462_r instanceof GuiOptions || NoSlowDown.mc.field_71462_r instanceof GuiVideoSettings || NoSlowDown.mc.field_71462_r instanceof GuiScreenOptionsSounds || NoSlowDown.mc.field_71462_r instanceof GuiContainer || NoSlowDown.mc.field_71462_r instanceof GuiIngameMenu) {
                for (final KeyBinding bind : NoSlowDown.keys) {
                    KeyBinding.func_74510_a(bind.func_151463_i(), Keyboard.isKeyDown(bind.func_151463_i()));
                }
            }
            else if (NoSlowDown.mc.field_71462_r == null) {
                for (final KeyBinding bind : NoSlowDown.keys) {
                    if (!Keyboard.isKeyDown(bind.func_151463_i())) {
                        KeyBinding.func_74510_a(bind.func_151463_i(), false);
                    }
                }
            }
        }
        if (this.webs.getValue() && NoSlowDown.mc.field_71439_g.field_70134_J) {
            final EntityPlayerSP field_71439_g = NoSlowDown.mc.field_71439_g;
            field_71439_g.field_70159_w *= this.webHorizontalFactor.getValue();
            final EntityPlayerSP field_71439_g2 = NoSlowDown.mc.field_71439_g;
            field_71439_g2.field_70179_y *= this.webHorizontalFactor.getValue();
            final EntityPlayerSP field_71439_g3 = NoSlowDown.mc.field_71439_g;
            field_71439_g3.field_70181_x *= this.webVerticalFactor.getValue();
        }
        final Item item = NoSlowDown.mc.field_71439_g.func_184607_cu().func_77973_b();
        if (this.sneaking && !NoSlowDown.mc.field_71439_g.func_184587_cr() && this.sneakPacket.getValue()) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)NoSlowDown.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.sneaking = false;
        }
    }
    
    @SubscribeEvent
    public void onUseItem(final PlayerInteractEvent.RightClickItem event) {
        final Item item = NoSlowDown.mc.field_71439_g.func_184586_b(event.getHand()).func_77973_b();
        if ((item instanceof ItemFood || item instanceof ItemBow || (item instanceof ItemPotion && this.sneakPacket.getValue())) && !this.sneaking) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)NoSlowDown.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            this.sneaking = true;
        }
    }
    
    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (this.noSlow.getValue() && NoSlowDown.mc.field_71439_g.func_184587_cr() && !NoSlowDown.mc.field_71439_g.func_184218_aH()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.field_78902_a *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.field_192832_b *= 5.0f;
        }
    }
    
    @SubscribeEvent
    public void onKeyEvent(final KeyEvent event) {
        if (this.guiMove.getValue() && event.getStage() == 0 && !(NoSlowDown.mc.field_71462_r instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.strict.getValue() && this.noSlow.getValue() && NoSlowDown.mc.field_71439_g.func_184587_cr() && !NoSlowDown.mc.field_71439_g.func_184218_aH()) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(NoSlowDown.mc.field_71439_g.field_70165_t), Math.floor(NoSlowDown.mc.field_71439_g.field_70163_u), Math.floor(NoSlowDown.mc.field_71439_g.field_70161_v)), EnumFacing.DOWN));
        }
    }
    
    static {
        NoSlowDown.INSTANCE = new NoSlowDown();
        keys = new KeyBinding[] { NoSlowDown.mc.field_71474_y.field_74351_w, NoSlowDown.mc.field_71474_y.field_74368_y, NoSlowDown.mc.field_71474_y.field_74370_x, NoSlowDown.mc.field_71474_y.field_74366_z, NoSlowDown.mc.field_71474_y.field_74314_A, NoSlowDown.mc.field_71474_y.field_151444_V };
    }
}
