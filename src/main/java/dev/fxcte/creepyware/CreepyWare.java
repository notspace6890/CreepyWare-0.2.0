// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware;

import org.apache.logging.log4j.LogManager;
import dev.fxcte.creepyware.util.TitleUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import java.io.InputStream;
import org.lwjgl.opengl.Display;
import dev.fxcte.creepyware.util.IconUtils;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import dev.fxcte.creepyware.features.modules.misc.RPC;
import dev.fxcte.creepyware.manager.NoStopManager;
import dev.fxcte.creepyware.features.gui.custom.GuiCustomMainScreen;
import dev.fxcte.creepyware.manager.SafetyManager;
import dev.fxcte.creepyware.manager.NotificationManager;
import dev.fxcte.creepyware.manager.HoleManager;
import dev.fxcte.creepyware.manager.TotemPopManager;
import dev.fxcte.creepyware.manager.ReloadManager;
import dev.fxcte.creepyware.manager.PacketManager;
import dev.fxcte.creepyware.manager.TimerManager;
import dev.fxcte.creepyware.manager.InventoryManager;
import dev.fxcte.creepyware.manager.PotionManager;
import dev.fxcte.creepyware.manager.ServerManager;
import dev.fxcte.creepyware.manager.ColorManager;
import dev.fxcte.creepyware.manager.TextManager;
import dev.fxcte.creepyware.manager.FriendManager;
import dev.fxcte.creepyware.manager.FileManager;
import dev.fxcte.creepyware.manager.ConfigManager;
import dev.fxcte.creepyware.manager.EventManager;
import dev.fxcte.creepyware.manager.CommandManager;
import dev.fxcte.creepyware.manager.RotationManager;
import dev.fxcte.creepyware.manager.PositionManager;
import dev.fxcte.creepyware.manager.SpeedManager;
import dev.fxcte.creepyware.manager.ModuleManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "creepyware", name = "Creepyware", version = "b0.2.0")
public class CreepyWare
{
    public static final String MODID = "creepyware";
    public static final String MODNAME = "Creepyware";
    public static final String MODVER = "b0.2.0";
    public static final Logger LOGGER;
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static TimerManager timerManager;
    public static PacketManager packetManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    public static GuiCustomMainScreen customMainScreen;
    public static NoStopManager baritoneManager;
    @Mod.Instance
    public static CreepyWare INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        CreepyWare.LOGGER.info("\n\nLoading Creepyware by Cr33pyWare Dev Team");
        CreepyWare.unloaded = false;
        if (CreepyWare.reloadManager != null) {
            CreepyWare.reloadManager.unload();
            CreepyWare.reloadManager = null;
        }
        CreepyWare.baritoneManager = new NoStopManager();
        CreepyWare.totemPopManager = new TotemPopManager();
        CreepyWare.timerManager = new TimerManager();
        CreepyWare.packetManager = new PacketManager();
        CreepyWare.serverManager = new ServerManager();
        CreepyWare.colorManager = new ColorManager();
        CreepyWare.textManager = new TextManager();
        CreepyWare.moduleManager = new ModuleManager();
        CreepyWare.speedManager = new SpeedManager();
        CreepyWare.rotationManager = new RotationManager();
        CreepyWare.positionManager = new PositionManager();
        CreepyWare.commandManager = new CommandManager();
        CreepyWare.eventManager = new EventManager();
        CreepyWare.configManager = new ConfigManager();
        CreepyWare.fileManager = new FileManager();
        CreepyWare.friendManager = new FriendManager();
        CreepyWare.potionManager = new PotionManager();
        CreepyWare.inventoryManager = new InventoryManager();
        CreepyWare.holeManager = new HoleManager();
        CreepyWare.notificationManager = new NotificationManager();
        CreepyWare.safetyManager = new SafetyManager();
        CreepyWare.LOGGER.info("Initialized Managers");
        CreepyWare.moduleManager.init();
        CreepyWare.LOGGER.info("Modules loaded.");
        CreepyWare.configManager.init();
        CreepyWare.eventManager.init();
        CreepyWare.LOGGER.info("EventManager loaded.");
        CreepyWare.textManager.init(true);
        CreepyWare.moduleManager.onLoad();
        CreepyWare.totemPopManager.init();
        CreepyWare.timerManager.init();
        if (CreepyWare.moduleManager.getModuleByClass(RPC.class).isEnabled()) {
            DiscordPresence.start();
        }
        CreepyWare.LOGGER.info("Creepyware successfully loaded!\n");
    }
    
    public static String getVersion() {
        return getVersion();
    }
    
    public static void unload(final boolean unload) {
        CreepyWare.LOGGER.info("\n\nUnloading Creepyware by Cr33pyWare Dev Team");
        if (unload) {
            (CreepyWare.reloadManager = new ReloadManager()).init((CreepyWare.commandManager != null) ? CreepyWare.commandManager.getPrefix() : ".");
        }
        if (CreepyWare.baritoneManager != null) {
            CreepyWare.baritoneManager.stop();
        }
        onUnload();
        CreepyWare.eventManager = null;
        CreepyWare.holeManager = null;
        CreepyWare.timerManager = null;
        CreepyWare.moduleManager = null;
        CreepyWare.totemPopManager = null;
        CreepyWare.serverManager = null;
        CreepyWare.colorManager = null;
        CreepyWare.textManager = null;
        CreepyWare.speedManager = null;
        CreepyWare.rotationManager = null;
        CreepyWare.positionManager = null;
        CreepyWare.commandManager = null;
        CreepyWare.configManager = null;
        CreepyWare.fileManager = null;
        CreepyWare.friendManager = null;
        CreepyWare.potionManager = null;
        CreepyWare.inventoryManager = null;
        CreepyWare.notificationManager = null;
        CreepyWare.safetyManager = null;
        CreepyWare.LOGGER.info("Creepyware unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!CreepyWare.unloaded) {
            CreepyWare.eventManager.onUnload();
            CreepyWare.moduleManager.onUnload();
            CreepyWare.configManager.saveConfig(CreepyWare.configManager.config.replaceFirst("creepyware/", ""));
            CreepyWare.moduleManager.onUnloadPost();
            CreepyWare.timerManager.unload();
            CreepyWare.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        CreepyWare.LOGGER.info("CREEPY IS THE BEST PVP IN 2021 - FXCTE");
    }
    
    public static void setWindowIcon() {
        if (Util.func_110647_a() != Util.EnumOS.OSX) {
            try (final InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/creepy/icons/creepyware-16x.png");
                 final InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/creepy/icons/creepyware-32x.png")) {
                final ByteBuffer[] icons = { IconUtils.INSTANCE.readImageToBuffer(inputStream16x), IconUtils.INSTANCE.readImageToBuffer(inputStream32x) };
                Display.setIcon((ByteBuffer[])icons);
            }
            catch (Exception e) {
                CreepyWare.LOGGER.error("Couldn't set Windows Icon", (Throwable)e);
            }
        }
    }
    
    private void setWindowsIcon() {
        setWindowIcon();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        this.setWindowsIcon();
        CreepyWare.customMainScreen = new GuiCustomMainScreen();
        MinecraftForge.EVENT_BUS.register((Object)new TitleUtils());
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("Creepyware");
        CreepyWare.unloaded = false;
    }
}
