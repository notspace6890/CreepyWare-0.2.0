// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.util.BlockUtil;
import net.minecraft.block.BlockEndPortalFrame;
import dev.fxcte.creepyware.event.events.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.client.CPacketAnimation;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.util.RenderUtil;
import java.awt.Color;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.modules.Module;

public class Speedmine extends Module
{
    private static Speedmine INSTANCE;
    public final Timer timer;
    private final Setting<Float> range;
    public Setting<Boolean> tweaks;
    public Setting<Mode> mode;
    public Setting<Boolean> reset;
    public Setting<Float> damage;
    public Setting<Boolean> noBreakAnim;
    public Setting<Boolean> noDelay;
    public Setting<Boolean> noSwing;
    public Setting<Boolean> allow;
    public Setting<Boolean> doubleBreak;
    public Setting<Boolean> webSwitch;
    public Setting<Boolean> silentSwitch;
    public Setting<Boolean> render;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    public final Setting<Float> lineWidth;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    public float breakTime;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    
    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)10.0f, (T)0.0f, (T)50.0f));
        this.tweaks = (Setting<Boolean>)this.register(new Setting("Speed", "Tweaks", 0.0, 0.0, (T)true, 0));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.PACKET, v -> this.tweaks.getValue()));
        this.reset = (Setting<Boolean>)this.register(new Setting("Speed", "Reset", 0.0, 0.0, (T)true, 0));
        this.damage = (Setting<Float>)this.register(new Setting("Damage", (T)0.7f, (T)0.0f, (T)1.0f, v -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue()));
        this.noBreakAnim = (Setting<Boolean>)this.register(new Setting("Speed", "NoBreakAnim", 0.0, 0.0, (T)false, 0));
        this.noDelay = (Setting<Boolean>)this.register(new Setting("Speed", "NoDelay", 0.0, 0.0, (T)false, 0));
        this.noSwing = (Setting<Boolean>)this.register(new Setting("Speed", "NoSwing", 0.0, 0.0, (T)false, 0));
        this.allow = (Setting<Boolean>)this.register(new Setting("Speed", "AllowMultiTask", 0.0, 0.0, (T)false, 0));
        this.doubleBreak = (Setting<Boolean>)this.register(new Setting("Speed", "DoubleBreak", 0.0, 0.0, (T)false, 0));
        this.webSwitch = (Setting<Boolean>)this.register(new Setting("Speed", "WebSwitch", 0.0, 0.0, (T)false, 0));
        this.silentSwitch = (Setting<Boolean>)this.register(new Setting("Speed", "SilentSwitch", 0.0, 0.0, (T)false, 0));
        this.render = (Setting<Boolean>)this.register(new Setting("Speed", "Render", 0.0, 0.0, (T)false, 0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)125, (T)0, (T)255, v -> this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255, v -> this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255, v -> this.render.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)Boolean.FALSE, v -> this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)85, (T)0, (T)255, v -> this.box.getValue() && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)Boolean.TRUE, v -> this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue() && this.render.getValue()));
        this.breakTime = -1.0f;
        this.setInstance();
    }
    
    public static Speedmine getInstance() {
        if (Speedmine.INSTANCE == null) {
            Speedmine.INSTANCE = new Speedmine();
        }
        return Speedmine.INSTANCE;
    }
    
    private void setInstance() {
        Speedmine.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine.mc.field_71439_g != null && Speedmine.mc.field_71439_g.func_174818_b(this.currentPos) > MathUtil.square(this.range.getValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }
            if (Speedmine.mc.field_71439_g != null && this.silentSwitch.getValue() && this.timer.passedMs((int)(2000.0f * CreepyWare.serverManager.getTpsFactor())) && this.getPickSlot() != -1) {
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.getPickSlot()));
            }
            if (Speedmine.mc.field_71439_g != null && this.silentSwitch.getValue() && this.timer.passedMs((int)(2200.0f * CreepyWare.serverManager.getTpsFactor()))) {
                final int oldSlot = Speedmine.mc.field_71439_g.field_71071_by.field_70461_c;
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
            }
            if (fullNullCheck()) {
                return;
            }
            if (!Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).func_177230_c() == Blocks.field_150350_a) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            else if (this.webSwitch.getValue() && this.currentBlockState.func_177230_c() == Blocks.field_150321_G && Speedmine.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue()) {
            Speedmine.mc.field_71442_b.field_78781_i = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue()) {
            Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue() && Speedmine.mc.field_71474_y.field_74313_G.func_151470_d() && !this.allow.getValue()) {
            Speedmine.mc.field_71442_b.field_78778_j = false;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent render3DEvent) {
        if (this.render.getValue() && this.currentPos != null) {
            final Color color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.boxAlpha.getValue());
            RenderUtil.boxESP(this.currentPos, color, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            if (this.noSwing.getValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            final CPacketPlayerDigging packet;
            if (this.noBreakAnim.getValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = event.getPacket()) != null) {
                packet.func_179715_a();
                try {
                    for (final Entity entity : Speedmine.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(packet.func_179715_a()))) {
                        if (!(entity instanceof EntityEnderCrystal)) {
                            continue;
                        }
                        this.showAnimation();
                        return;
                    }
                }
                catch (Exception ex) {}
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.func_179715_a(), packet.func_179714_b());
                }
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockEvent(final BlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.field_71441_e.func_180495_p(event.pos).func_177230_c() instanceof BlockEndPortalFrame) {
            Speedmine.mc.field_71441_e.func_180495_p(event.pos).func_177230_c().func_149711_c(50.0f);
        }
        if (event.getStage() == 3 && this.reset.getValue() && Speedmine.mc.field_71442_b.field_78770_f > 0.1f) {
            Speedmine.mc.field_71442_b.field_78778_j = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue()) {
            if (BlockUtil.canBreak(event.pos)) {
                if (this.reset.getValue()) {
                    Speedmine.mc.field_71442_b.field_78778_j = false;
                }
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.field_71441_e.func_180495_p(this.currentPos);
                            final ItemStack object = new ItemStack(Items.field_151046_w);
                            this.breakTime = object.func_150997_a(this.currentBlockState) / 3.71f;
                            this.timer.reset();
                        }
                        Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (Speedmine.mc.field_71442_b.field_78770_f < this.damage.getValue()) {
                            break;
                        }
                        Speedmine.mc.field_71442_b.field_78770_f = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71442_b.func_187103_a(event.pos);
                        Speedmine.mc.field_71441_e.func_175698_g(event.pos);
                        break;
                    }
                }
            }
            final BlockPos above;
            if (this.doubleBreak.getValue() && BlockUtil.canBreak(above = event.pos.func_177982_a(0, 1, 0)) && Speedmine.mc.field_71439_g.func_70011_f((double)above.func_177958_n(), (double)above.func_177956_o(), (double)above.func_177952_p()) <= 5.0) {
                Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71442_b.func_187103_a(above);
                Speedmine.mc.field_71441_e.func_175698_g(above);
            }
        }
    }
    
    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151046_w) {
                return i;
            }
        }
        return -1;
    }
    
    private void showAnimation(final boolean isMining, final BlockPos lastPos, final EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void showAnimation() {
        this.showAnimation(false, null, null);
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Speedmine.INSTANCE = new Speedmine();
    }
    
    public enum Mode
    {
        PACKET, 
        DAMAGE, 
        INSTANT;
    }
}
