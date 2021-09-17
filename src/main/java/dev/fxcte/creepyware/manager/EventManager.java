// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import dev.fxcte.creepyware.features.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import dev.fxcte.creepyware.event.events.Render2DEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import dev.fxcte.creepyware.util.GLUProjection;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.UUID;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import dev.fxcte.creepyware.event.events.ConnectionEvent;
import com.google.common.base.Strings;
import java.util.function.Predicate;
import java.util.Objects;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.fml.common.eventhandler.Event;
import dev.fxcte.creepyware.event.events.TotemPopEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import dev.fxcte.creepyware.event.events.PacketEvent;
import dev.fxcte.creepyware.event.events.UpdateWalkingPlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import dev.fxcte.creepyware.features.modules.combat.AutoCrystal;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import dev.fxcte.creepyware.event.events.ClientEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.features.modules.client.Managers;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.atomic.AtomicBoolean;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.Feature;

public class EventManager extends Feature
{
    private final Timer timer;
    private final Timer logoutTimer;
    private final Timer switchTimer;
    private boolean keyTimeout;
    private final AtomicBoolean tickOngoing;
    
    public EventManager() {
        this.timer = new Timer();
        this.logoutTimer = new Timer();
        this.switchTimer = new Timer();
        this.tickOngoing = new AtomicBoolean(false);
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (!Feature.fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals((Object)EventManager.mc.field_71439_g)) {
            CreepyWare.potionManager.update();
            CreepyWare.totemPopManager.onUpdate();
            CreepyWare.inventoryManager.update();
            CreepyWare.holeManager.update();
            CreepyWare.safetyManager.onUpdate();
            CreepyWare.moduleManager.onUpdate();
            CreepyWare.timerManager.update();
            if (this.timer.passedMs(Managers.getInstance().moduleListUpdates.getValue())) {
                CreepyWare.moduleManager.sortModules(true);
                CreepyWare.moduleManager.alphabeticallySortModules();
                this.timer.reset();
            }
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && EventManager.mc.func_147114_u() != null && ServerModule.getInstance().isConnected() && EventManager.mc.field_71441_e != null) {
            final String command = "@Server" + ServerModule.getInstance().getServerPrefix() + "module " + event.getSetting().getFeature().getName() + " set " + event.getSetting().getName() + " " + event.getSetting().getPlannedValue().toString();
            final CPacketChatMessage cPacketChatMessage = new CPacketChatMessage(command);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTickHighest(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.tickOngoing.set(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTickLowest(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tickOngoing.set(false);
            AutoCrystal.getInstance().postTick();
        }
    }
    
    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }
    
    @SubscribeEvent
    public void onClientConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        CreepyWare.moduleManager.onLogin();
    }
    
    @SubscribeEvent
    public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        CreepyWare.moduleManager.onLogout();
        CreepyWare.totemPopManager.onLogout();
        CreepyWare.potionManager.onLogout();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        CreepyWare.moduleManager.onTick();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            CreepyWare.baritoneManager.onUpdateWalkingPlayer();
            CreepyWare.speedManager.updateValues();
            CreepyWare.rotationManager.updateRotations();
            CreepyWare.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            CreepyWare.rotationManager.restoreRotations();
            CreepyWare.positionManager.restorePosition();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.switchTimer.reset();
        }
    }
    
    public boolean isOnSwitchCoolDown() {
        return !this.switchTimer.passedMs(500L);
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        CreepyWare.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = event.getPacket();
            if (packet.func_149160_c() == 35 && packet.func_149161_a((World)EventManager.mc.field_71441_e) instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)EventManager.mc.field_71441_e);
                MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
                CreepyWare.totemPopManager.onTotemPop(player);
                CreepyWare.potionManager.onTotemPop(player);
            }
        }
        else if (event.getPacket() instanceof SPacketPlayerListItem && !Feature.fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            final SPacketPlayerListItem packet2 = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet2.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet2.func_179768_b())) {
                return;
            }
            final UUID id;
            final SPacketPlayerListItem sPacketPlayerListItem;
            final String name;
            final EntityPlayer entity;
            String logoutName;
            packet2.func_179767_a().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.func_179962_a().getName()) || data.func_179962_a().getId() != null).forEach(data -> {
                id = data.func_179962_a().getId();
                switch (sPacketPlayerListItem.func_179768_b()) {
                    case ADD_PLAYER: {
                        name = data.func_179962_a().getName();
                        MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        entity = EventManager.mc.field_71441_e.func_152378_a(id);
                        if (entity != null) {
                            logoutName = entity.func_70005_c_();
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        else {
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, null));
                            break;
                        }
                        break;
                    }
                }
            });
        }
        else if (event.getPacket() instanceof SPacketTimeUpdate) {
            CreepyWare.serverManager.update();
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.field_71424_I.func_76320_a("creepyware");
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179097_i();
        GlStateManager.func_187441_d(1.0f);
        final Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        final GLUProjection projection = GLUProjection.getInstance();
        final IntBuffer viewPort = GLAllocation.func_74527_f(16);
        final FloatBuffer modelView = GLAllocation.func_74529_h(16);
        final FloatBuffer projectionPort = GLAllocation.func_74529_h(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projectionPort);
        GL11.glGetInteger(2978, viewPort);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        projection.updateMatrices(viewPort, modelView, projectionPort, scaledResolution.func_78326_a() / (double)Minecraft.func_71410_x().field_71443_c, scaledResolution.func_78328_b() / (double)Minecraft.func_71410_x().field_71440_d);
        CreepyWare.moduleManager.onRender3D(render3dEvent);
        GlStateManager.func_187441_d(1.0f);
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        EventManager.mc.field_71424_I.func_76319_b();
    }
    
    @SubscribeEvent
    public void renderHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            CreepyWare.textManager.updateResolution();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            final ScaledResolution resolution = new ScaledResolution(EventManager.mc);
            final Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            CreepyWare.moduleManager.onRender2D(render2DEvent);
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    CreepyWare.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage("§cAn error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }
}
