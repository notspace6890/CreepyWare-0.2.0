// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class MCP extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> stopRotation;
    private final Setting<Boolean> antiFriend;
    private final Setting<Integer> rotation;
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)Mode.MIDDLECLICK, 0));
        this.stopRotation = (Setting<Boolean>)this.register(new Setting("Speed", "Rotation", 0.0, 0.0, (T)true, 0));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("Speed", "AntiFriend", 0.0, 0.0, (T)true, 0));
        this.rotation = (Setting<Integer>)this.register(new Setting("Delay", (T)10, (T)0, (T)100, v -> this.stopRotation.getValue()));
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
            this.throwPearl();
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.MIDDLECLICK) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked) {
                    this.throwPearl();
                }
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
    
    private void throwPearl() {
        final RayTraceResult result;
        final Entity entity;
        if (this.antiFriend.getValue() && (result = MCP.mc.field_71476_x) != null && result.field_72313_a == RayTraceResult.Type.ENTITY && (entity = result.field_72308_g) instanceof EntityPlayer) {
            return;
        }
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean bl;
        final boolean offhand = bl = (MCP.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.field_71439_g.field_71071_by.field_70461_c;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.field_71442_b.func_187101_a((EntityPlayer)MCP.mc.field_71439_g, (World)MCP.mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public enum Mode
    {
        TOGGLE, 
        MIDDLECLICK;
    }
}
