// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemMinecart;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import dev.fxcte.creepyware.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.util.math.BlockPos;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class FastPlace extends Module
{
    private final Setting<Boolean> all;
    private final Setting<Boolean> obby;
    private final Setting<Boolean> enderChests;
    private final Setting<Boolean> crystals;
    private final Setting<Boolean> exp;
    private final Setting<Boolean> Minecart;
    private final Setting<Boolean> feetExp;
    private final Setting<Boolean> fastCrystal;
    private BlockPos mousePos;
    
    public FastPlace() {
        super("FastPlace", "Fast everything.", Category.PLAYER, true, false, false);
        this.all = (Setting<Boolean>)this.register(new Setting("Speed", "All", 0.0, 0.0, (T)false, 0));
        this.obby = (Setting<Boolean>)this.register(new Setting("Obsidian", (T)false, v -> !this.all.getValue()));
        this.enderChests = (Setting<Boolean>)this.register(new Setting("EnderChests", (T)false, v -> !this.all.getValue()));
        this.crystals = (Setting<Boolean>)this.register(new Setting("Crystals", (T)false, v -> !this.all.getValue()));
        this.exp = (Setting<Boolean>)this.register(new Setting("Experience", (T)false, v -> !this.all.getValue()));
        this.Minecart = (Setting<Boolean>)this.register(new Setting("Minecarts", (T)false, v -> !this.all.getValue()));
        this.feetExp = (Setting<Boolean>)this.register(new Setting("Speed", "ExpFeet", 0.0, 0.0, (T)false, 0));
        this.fastCrystal = (Setting<Boolean>)this.register(new Setting("Speed", "PacketCrystal", 0.0, 0.0, (T)false, 0));
        this.mousePos = null;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.feetExp.getValue()) {
            final boolean mainHand = FastPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151062_by;
            final boolean bl;
            final boolean offHand = bl = (FastPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151062_by);
            if (FastPlace.mc.field_71474_y.field_74313_G.func_151470_d() && ((FastPlace.mc.field_71439_g.func_184600_cs() == EnumHand.MAIN_HAND && mainHand) || (FastPlace.mc.field_71439_g.func_184600_cs() == EnumHand.OFF_HAND && offHand))) {
                CreepyWare.rotationManager.lookAtVec3d(FastPlace.mc.field_71439_g.func_174791_d());
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class) && this.exp.getValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(BlockObsidian.class) && this.obby.getValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(BlockEnderChest.class) && this.enderChests.getValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemMinecart.class) && this.Minecart.getValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (this.all.getValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemEndCrystal.class) && (this.crystals.getValue() || this.all.getValue())) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (this.fastCrystal.getValue() && FastPlace.mc.field_71474_y.field_74313_G.func_151470_d()) {
            final boolean bl;
            final boolean offhand = bl = (FastPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
            if (offhand || FastPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
                final RayTraceResult result = FastPlace.mc.field_71476_x;
                if (result == null) {
                    return;
                }
                switch (result.field_72313_a) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = FastPlace.mc.field_71476_x.func_178782_a();
                        break;
                    }
                    case ENTITY: {
                        final Entity entity;
                        if (this.mousePos == null || (entity = result.field_72308_g) == null) {
                            break;
                        }
                        if (!this.mousePos.equals((Object)new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v))) {
                            break;
                        }
                        FastPlace.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        break;
                    }
                }
            }
        }
    }
}
