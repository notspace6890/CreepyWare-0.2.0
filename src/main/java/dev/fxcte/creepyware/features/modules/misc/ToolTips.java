// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import dev.fxcte.creepyware.util.ColorUtil;
import java.awt.Color;
import dev.fxcte.creepyware.util.RenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.storage.MapData;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import dev.fxcte.creepyware.util.Util;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import dev.fxcte.creepyware.event.events.Render2DEvent;
import java.util.Iterator;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.inventory.GuiContainer;
import dev.fxcte.creepyware.features.Feature;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.world.World;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntityShulkerBox;
import java.util.concurrent.ConcurrentHashMap;
import dev.fxcte.creepyware.util.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import dev.fxcte.creepyware.features.setting.Bind;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.util.ResourceLocation;
import dev.fxcte.creepyware.features.modules.Module;

public class ToolTips extends Module
{
    private static final ResourceLocation MAP;
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static ToolTips INSTANCE;
    public Setting<Boolean> maps;
    public Setting<Boolean> shulkers;
    public Setting<Bind> peek;
    public Setting<Boolean> shulkerSpy;
    public Setting<Boolean> render;
    public Setting<Boolean> own;
    public Setting<Integer> cooldown;
    public Setting<Boolean> textColor;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public Setting<Boolean> offsets;
    private final Setting<Integer> yPerPlayer;
    private final Setting<Integer> xOffset;
    private final Setting<Integer> yOffset;
    private final Setting<Integer> trOffset;
    public Setting<Integer> invH;
    public Map<EntityPlayer, ItemStack> spiedPlayers;
    public Map<EntityPlayer, Timer> playerTimers;
    private int textRadarY;
    
    public ToolTips() {
        super("ToolTips", "Several tweaks for tooltips.", Category.MISC, true, false, false);
        this.maps = (Setting<Boolean>)this.register(new Setting("Speed", "Maps", 0.0, 0.0, (T)true, 0));
        this.shulkers = (Setting<Boolean>)this.register(new Setting("Speed", "ShulkerViewer", 0.0, 0.0, (T)true, 0));
        this.peek = (Setting<Bind>)this.register(new Setting("Speed", "Peek", 0.0, 0.0, (T)new Bind(-1), 0));
        this.shulkerSpy = (Setting<Boolean>)this.register(new Setting("Speed", "ShulkerSpy", 0.0, 0.0, (T)true, 0));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true, v -> this.shulkerSpy.getValue()));
        this.own = (Setting<Boolean>)this.register(new Setting("OwnShulker", (T)true, v -> this.shulkerSpy.getValue()));
        this.cooldown = (Setting<Integer>)this.register(new Setting("ShowForS", (T)2, (T)0, (T)5, v -> this.shulkerSpy.getValue()));
        this.textColor = (Setting<Boolean>)this.register(new Setting("TextColor", (T)false, v -> this.shulkers.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255, v -> this.textColor.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255, v -> this.textColor.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> this.textColor.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255, v -> this.textColor.getValue()));
        this.offsets = (Setting<Boolean>)this.register(new Setting("Speed", "Offsets", 0.0, 0.0, (T)false, 0));
        this.yPerPlayer = (Setting<Integer>)this.register(new Setting("Y/Player", (T)18, v -> this.offsets.getValue()));
        this.xOffset = (Setting<Integer>)this.register(new Setting("XOffset", (T)4, v -> this.offsets.getValue()));
        this.yOffset = (Setting<Integer>)this.register(new Setting("YOffset", (T)2, v -> this.offsets.getValue()));
        this.trOffset = (Setting<Integer>)this.register(new Setting("TROffset", (T)2, v -> this.offsets.getValue()));
        this.invH = (Setting<Integer>)this.register(new Setting("InvH", (T)3, v -> this.offsets.getValue()));
        this.spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
        this.playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.textRadarY = 0;
        this.setInstance();
    }
    
    public static ToolTips getInstance() {
        if (ToolTips.INSTANCE == null) {
            ToolTips.INSTANCE = new ToolTips();
        }
        return ToolTips.INSTANCE;
    }
    
    public static void displayInv(final ItemStack stack, final String name) {
        try {
            final Item item = stack.func_77973_b();
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            final ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.field_145854_h = shulker.func_179223_d();
            entityBox.func_145834_a((World)ToolTips.mc.field_71441_e);
            ItemStackHelper.func_191283_b(stack.func_77978_p().func_74775_l("BlockEntityTag"), entityBox.field_190596_f);
            entityBox.func_145839_a(stack.func_77978_p().func_74775_l("BlockEntityTag"));
            entityBox.func_190575_a((name == null) ? stack.func_82833_r() : name);
            final IInventory inventory;
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException ex) {}
                ToolTips.mc.field_71439_g.func_71007_a(inventory);
            }).start();
        }
        catch (Exception ex2) {}
    }
    
    private void setInstance() {
        ToolTips.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck() || !this.shulkerSpy.getValue()) {
            return;
        }
        final Slot slot;
        final ItemStack stack;
        if (this.peek.getValue().getKey() != -1 && ToolTips.mc.field_71462_r instanceof GuiContainer && Keyboard.isKeyDown(this.peek.getValue().getKey()) && (slot = ((GuiContainer)ToolTips.mc.field_71462_r).getSlotUnderMouse()) != null && (stack = slot.func_75211_c()) != null && stack.func_77973_b() instanceof ItemShulkerBox) {
            displayInv(stack, null);
        }
        for (final EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
            if (player != null && player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox) {
                if (!this.own.getValue() && ToolTips.mc.field_71439_g.equals((Object)player)) {
                    continue;
                }
                final ItemStack stack2 = player.func_184614_ca();
                this.spiedPlayers.put(player, stack2);
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Feature.fullNullCheck() || !this.shulkerSpy.getValue() || !this.render.getValue()) {
            return;
        }
        final int x = -4 + this.xOffset.getValue();
        int y = 10 + this.yOffset.getValue();
        this.textRadarY = 0;
        for (final EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
            if (this.spiedPlayers.get(player) == null) {
                continue;
            }
            if (player.func_184614_ca() == null || !(player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox)) {
                final Timer playerTimer = this.playerTimers.get(player);
                if (playerTimer == null) {
                    final Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                }
                else if (playerTimer.passedS(this.cooldown.getValue())) {
                    continue;
                }
            }
            else {
                final Timer playerTimer;
                if (player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                    playerTimer.reset();
                    this.playerTimers.put(player, playerTimer);
                }
            }
            final ItemStack stack = this.spiedPlayers.get(player);
            this.renderShulkerToolTip(stack, x, y, player.func_70005_c_());
            this.textRadarY = (y += this.yPerPlayer.getValue() + 60) - 10 - this.yOffset.getValue() + this.trOffset.getValue();
        }
    }
    
    public int getTextRadarY() {
        return this.textRadarY;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent event) {
    }
    
    @SubscribeEvent
    public void renderTooltip(final RenderTooltipEvent.PostText event) {
        final MapData mapData;
        if (this.maps.getValue() && !event.getStack().func_190926_b() && event.getStack().func_77973_b() instanceof ItemMap && (mapData = Items.field_151098_aY.func_77873_a(event.getStack(), (World)ToolTips.mc.field_71441_e)) != null) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
            RenderHelper.func_74518_a();
            Util.mc.func_110434_K().func_110577_a(ToolTips.MAP);
            final Tessellator instance = Tessellator.func_178181_a();
            final BufferBuilder buffer = instance.func_178180_c();
            final int n = 7;
            final float n2 = 135.0f;
            final float n3 = 0.5f;
            GlStateManager.func_179109_b((float)event.getX(), event.getY() - n2 * n3 - 5.0f, 0.0f);
            GlStateManager.func_179152_a(n3, n3, n3);
            buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
            buffer.func_181662_b((double)(-n), (double)n2, 0.0).func_187315_a(0.0, 1.0).func_181675_d();
            buffer.func_181662_b((double)n2, (double)n2, 0.0).func_187315_a(1.0, 1.0).func_181675_d();
            buffer.func_181662_b((double)n2, (double)(-n), 0.0).func_187315_a(1.0, 0.0).func_181675_d();
            buffer.func_181662_b((double)(-n), (double)(-n), 0.0).func_187315_a(0.0, 0.0).func_181675_d();
            instance.func_78381_a();
            ToolTips.mc.field_71460_t.func_147701_i().func_148250_a(mapData, false);
            GlStateManager.func_179145_e();
            GlStateManager.func_179121_F();
        }
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.func_77978_p();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.func_150297_b("BlockEntityTag", 10) && (blockEntityTag = tagCompound.func_74775_l("BlockEntityTag")).func_150297_b("Items", 9)) {
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Util.mc.func_110434_K().func_110577_a(ToolTips.SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.func_179097_i();
            Color color = new Color(0, 0, 0, 255);
            if (this.textColor.getValue()) {
                color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
            }
            this.renderer.drawStringWithShadow((name == null) ? stack.func_82833_r() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
            GlStateManager.func_179126_j();
            RenderHelper.func_74520_c();
            GlStateManager.func_179091_B();
            GlStateManager.func_179142_g();
            GlStateManager.func_179145_e();
            final NonNullList nonnulllist = NonNullList.func_191197_a(27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b(blockEntityTag, nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                ToolTips.mc.func_175599_af().field_77023_b = 501.0f;
                RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
                RenderUtil.itemRender.func_180453_a(ToolTips.mc.field_71466_p, itemStack, iX, iY, (String)null);
                ToolTips.mc.func_175599_af().field_77023_b = 0.0f;
            }
            GlStateManager.func_179140_f();
            GlStateManager.func_179084_k();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    static {
        MAP = new ResourceLocation("textures/map/map_background.png");
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        ToolTips.INSTANCE = new ToolTips();
    }
}
