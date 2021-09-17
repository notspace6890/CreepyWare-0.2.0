// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import dev.fxcte.creepyware.features.gui.CreepyWareGui;
import org.lwjgl.input.Keyboard;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import dev.fxcte.creepyware.event.events.Render3DEvent;
import dev.fxcte.creepyware.event.events.Render2DEvent;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import java.util.Arrays;
import java.util.Iterator;
import dev.fxcte.creepyware.features.modules.render.PenisESP;
import dev.fxcte.creepyware.features.modules.render.Fullbright;
import dev.fxcte.creepyware.features.modules.render.SmallShield;
import dev.fxcte.creepyware.features.modules.render.NoRender;
import dev.fxcte.creepyware.features.modules.render.CrystalScale;
import dev.fxcte.creepyware.features.modules.render.VoidESP;
import dev.fxcte.creepyware.features.modules.render.XRay;
import dev.fxcte.creepyware.features.modules.render.LogoutSpots;
import dev.fxcte.creepyware.features.modules.render.Trajectories;
import dev.fxcte.creepyware.features.modules.render.BlockHighlight;
import dev.fxcte.creepyware.features.modules.render.PopChams;
import dev.fxcte.creepyware.features.modules.render.HoleESP;
import dev.fxcte.creepyware.features.modules.render.ESP;
import dev.fxcte.creepyware.features.modules.render.Chams;
import dev.fxcte.creepyware.features.modules.render.CameraClip;
import dev.fxcte.creepyware.features.modules.render.StorageESP;
import dev.fxcte.creepyware.features.modules.render.ViewModel;
import dev.fxcte.creepyware.features.modules.render.ItemPhysics;
import dev.fxcte.creepyware.features.modules.render.Aspect;
import dev.fxcte.creepyware.features.modules.render.HandChams;
import dev.fxcte.creepyware.features.modules.render.BurrowESP;
import dev.fxcte.creepyware.features.modules.render.BreakingESP;
import dev.fxcte.creepyware.features.modules.render.Nametags;
import dev.fxcte.creepyware.features.modules.player.EchestBP;
import dev.fxcte.creepyware.features.modules.player.SilentXP;
import dev.fxcte.creepyware.features.modules.player.MCP;
import dev.fxcte.creepyware.features.modules.player.Replenish;
import dev.fxcte.creepyware.features.modules.player.XCarry;
import dev.fxcte.creepyware.features.modules.player.MultiTask;
import dev.fxcte.creepyware.features.modules.player.Speedmine;
import dev.fxcte.creepyware.features.modules.player.Freecam;
import dev.fxcte.creepyware.features.modules.player.FastPlace;
import dev.fxcte.creepyware.features.modules.player.TimerSpeed;
import dev.fxcte.creepyware.features.modules.player.LiquidInteract;
import dev.fxcte.creepyware.features.modules.player.StashLogger;
import dev.fxcte.creepyware.features.modules.player.FakePlayer;
import dev.fxcte.creepyware.features.modules.player.EntityNotifier;
import dev.fxcte.creepyware.features.modules.player.NoEntityTrace;
import dev.fxcte.creepyware.features.modules.movement.VanillaSpeed;
import dev.fxcte.creepyware.features.modules.movement.Sprint;
import dev.fxcte.creepyware.features.modules.movement.AntiVoid;
import dev.fxcte.creepyware.features.modules.movement.Velocity;
import dev.fxcte.creepyware.features.modules.movement.Step;
import dev.fxcte.creepyware.features.modules.movement.Speed;
import dev.fxcte.creepyware.features.modules.movement.StairSpeed;
import dev.fxcte.creepyware.features.modules.movement.NoSlowDown;
import dev.fxcte.creepyware.features.modules.movement.ElytraFlight;
import dev.fxcte.creepyware.features.modules.movement.Strafe;
import dev.fxcte.creepyware.features.modules.movement.ReverseStep;
import dev.fxcte.creepyware.features.modules.movement.Anchor;
import dev.fxcte.creepyware.features.modules.misc.FxcteSexDupe;
import dev.fxcte.creepyware.features.modules.misc.NoHandShake;
import dev.fxcte.creepyware.features.modules.misc.AutoGG;
import dev.fxcte.creepyware.features.modules.misc.RPC;
import dev.fxcte.creepyware.features.modules.misc.Blink;
import dev.fxcte.creepyware.features.modules.misc.Tracker;
import dev.fxcte.creepyware.features.modules.misc.AutoReconnect;
import dev.fxcte.creepyware.features.modules.misc.AutoLog;
import dev.fxcte.creepyware.features.modules.misc.NoRotate;
import dev.fxcte.creepyware.features.modules.misc.ToolTips;
import dev.fxcte.creepyware.features.modules.misc.AutoRespawn;
import dev.fxcte.creepyware.features.modules.misc.BuildHeight;
import dev.fxcte.creepyware.features.modules.misc.MCF;
import dev.fxcte.creepyware.features.modules.misc.ChatModifier;
import dev.fxcte.creepyware.features.modules.misc.FriendSettings;
import dev.fxcte.creepyware.features.modules.misc.ExtraTab;
import dev.fxcte.creepyware.features.modules.combat.Quiver;
import dev.fxcte.creepyware.features.modules.combat.AntiTrap;
import dev.fxcte.creepyware.features.modules.combat.GodModule;
import dev.fxcte.creepyware.features.modules.combat.Webaura;
import dev.fxcte.creepyware.features.modules.combat.BedAura;
import dev.fxcte.creepyware.features.modules.combat.Selftrap;
import dev.fxcte.creepyware.features.modules.combat.HoleFiller;
import dev.fxcte.creepyware.features.modules.combat.Killaura;
import dev.fxcte.creepyware.features.modules.combat.Criticals;
import dev.fxcte.creepyware.features.modules.combat.AutoCrystal;
import dev.fxcte.creepyware.features.modules.combat.AutoTrap;
import dev.fxcte.creepyware.features.modules.combat.Surround;
import dev.fxcte.creepyware.features.modules.combat.AutoTotem;
import dev.fxcte.creepyware.features.modules.combat.Offhand;
import dev.fxcte.creepyware.features.modules.combat.BowAim;
import dev.fxcte.creepyware.features.modules.combat.BowSpam;
import dev.fxcte.creepyware.features.modules.combat.Burrow;
import dev.fxcte.creepyware.features.modules.client.MainMenu;
import dev.fxcte.creepyware.features.modules.client.Media;
import dev.fxcte.creepyware.features.modules.client.HUD;
import dev.fxcte.creepyware.features.modules.client.Notifications;
import dev.fxcte.creepyware.features.modules.client.ServerModule;
import dev.fxcte.creepyware.features.modules.client.ModuleTools;
import dev.fxcte.creepyware.features.modules.client.Colors;
import dev.fxcte.creepyware.features.modules.client.Managers;
import dev.fxcte.creepyware.features.modules.client.ClickGui;
import dev.fxcte.creepyware.features.modules.client.FontMod;
import java.util.HashMap;
import java.awt.Color;
import java.util.Map;
import java.util.List;
import dev.fxcte.creepyware.features.modules.Module;
import java.util.ArrayList;
import dev.fxcte.creepyware.features.Feature;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<Module> alphabeticallySortedModules;
    public Map<Module, Color> moduleColorMap;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.sortedModules = new ArrayList<Module>();
        this.alphabeticallySortedModules = new ArrayList<Module>();
        this.moduleColorMap = new HashMap<Module, Color>();
    }
    
    public void init() {
        this.modules.add(new FontMod());
        this.modules.add(new ClickGui());
        this.modules.add(new Managers());
        this.modules.add(new Colors());
        this.modules.add(new ModuleTools());
        this.modules.add(new ServerModule());
        this.modules.add(new Notifications());
        this.modules.add(new HUD());
        this.modules.add(new Media());
        this.modules.add(new MainMenu());
        this.modules.add(new Burrow());
        this.modules.add(new BowSpam());
        this.modules.add(new BowAim());
        this.modules.add(new Offhand());
        this.modules.add(new AutoTotem());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoCrystal());
        this.modules.add(new Criticals());
        this.modules.add(new Killaura());
        this.modules.add(new HoleFiller());
        this.modules.add(new Selftrap());
        this.modules.add(new BedAura());
        this.modules.add(new Webaura());
        this.modules.add(new GodModule());
        this.modules.add(new AntiTrap());
        this.modules.add(new Quiver());
        this.modules.add(new ExtraTab());
        this.modules.add(new FriendSettings());
        this.modules.add(new ChatModifier());
        this.modules.add(new MCF());
        this.modules.add(new BuildHeight());
        this.modules.add(new AutoRespawn());
        this.modules.add(new ToolTips());
        this.modules.add(new NoRotate());
        this.modules.add(new AutoLog());
        this.modules.add(new AutoReconnect());
        this.modules.add(new Tracker());
        this.modules.add(new Blink());
        this.modules.add(new RPC());
        this.modules.add(new AutoGG());
        this.modules.add(new NoHandShake());
        this.modules.add(new FxcteSexDupe());
        this.modules.add(new Anchor());
        this.modules.add(new ReverseStep());
        this.modules.add(new Strafe());
        this.modules.add(new ElytraFlight());
        this.modules.add(new NoSlowDown());
        this.modules.add(new StairSpeed());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new Velocity());
        this.modules.add(new AntiVoid());
        this.modules.add(new Sprint());
        this.modules.add(new VanillaSpeed());
        this.modules.add(new NoEntityTrace());
        this.modules.add(new EntityNotifier());
        this.modules.add(new FakePlayer());
        this.modules.add(new StashLogger());
        this.modules.add(new LiquidInteract());
        this.modules.add(new TimerSpeed());
        this.modules.add(new FastPlace());
        this.modules.add(new Freecam());
        this.modules.add(new Speedmine());
        this.modules.add(new MultiTask());
        this.modules.add(new XCarry());
        this.modules.add(new Replenish());
        this.modules.add(new MCP());
        this.modules.add(new SilentXP());
        this.modules.add(new EchestBP());
        this.modules.add(new Nametags());
        this.modules.add(new BreakingESP());
        this.modules.add(new BurrowESP());
        this.modules.add(new HandChams());
        this.modules.add(new Aspect());
        this.modules.add(new ItemPhysics());
        this.modules.add(new ViewModel());
        this.modules.add(new StorageESP());
        this.modules.add(new CameraClip());
        this.modules.add(new Chams());
        this.modules.add(new ESP());
        this.modules.add(new HoleESP());
        this.modules.add(new PopChams());
        this.modules.add(new BlockHighlight());
        this.modules.add(new Trajectories());
        this.modules.add(new LogoutSpots());
        this.modules.add(new XRay());
        this.modules.add(new VoidESP());
        this.modules.add(new CrystalScale());
        this.modules.add(new NoRender());
        this.modules.add(new SmallShield());
        this.modules.add(new Fullbright());
        this.modules.add(new PenisESP());
        this.moduleColorMap.put(this.getModuleByClass(BurrowESP.class), new Color(96, 138, 92));
        this.moduleColorMap.put(this.getModuleByClass(AntiTrap.class), new Color(128, 53, 69));
        this.moduleColorMap.put(this.getModuleByClass(AutoCrystal.class), new Color(255, 15, 43));
        this.moduleColorMap.put(this.getModuleByClass(AutoTrap.class), new Color(193, 49, 244));
        this.moduleColorMap.put(this.getModuleByClass(Criticals.class), new Color(204, 151, 184));
        this.moduleColorMap.put(this.getModuleByClass(HoleFiller.class), new Color(166, 55, 110));
        this.moduleColorMap.put(this.getModuleByClass(Killaura.class), new Color(255, 37, 0));
        this.moduleColorMap.put(this.getModuleByClass(Offhand.class), new Color(185, 212, 144));
        this.moduleColorMap.put(this.getModuleByClass(Selftrap.class), new Color(22, 127, 145));
        this.moduleColorMap.put(this.getModuleByClass(Surround.class), new Color(100, 0, 150));
        this.moduleColorMap.put(this.getModuleByClass(Webaura.class), new Color(11, 161, 121));
        this.moduleColorMap.put(this.getModuleByClass(AutoGG.class), new Color(240, 49, 110));
        this.moduleColorMap.put(this.getModuleByClass(AutoLog.class), new Color(176, 176, 176));
        this.moduleColorMap.put(this.getModuleByClass(AutoReconnect.class), new Color(17, 85, 153));
        this.moduleColorMap.put(this.getModuleByClass(BuildHeight.class), new Color(64, 136, 199));
        this.moduleColorMap.put(this.getModuleByClass(ChatModifier.class), new Color(255, 59, 216));
        this.moduleColorMap.put(this.getModuleByClass(MCF.class), new Color(17, 85, 255));
        this.moduleColorMap.put(this.getModuleByClass(NoRotate.class), new Color(69, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass(RPC.class), new Color(0, 64, 255));
        this.moduleColorMap.put(this.getModuleByClass(ToolTips.class), new Color(209, 125, 156));
        this.moduleColorMap.put(this.getModuleByClass(Tracker.class), new Color(0, 255, 225));
        this.moduleColorMap.put(this.getModuleByClass(BlockHighlight.class), new Color(103, 182, 224));
        this.moduleColorMap.put(this.getModuleByClass(CameraClip.class), new Color(247, 169, 107));
        this.moduleColorMap.put(this.getModuleByClass(Chams.class), new Color(34, 152, 34));
        this.moduleColorMap.put(this.getModuleByClass(ESP.class), new Color(255, 27, 155));
        this.moduleColorMap.put(this.getModuleByClass(Fullbright.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass(HoleESP.class), new Color(95, 83, 130));
        this.moduleColorMap.put(this.getModuleByClass(LogoutSpots.class), new Color(2, 135, 134));
        this.moduleColorMap.put(this.getModuleByClass(Nametags.class), new Color(98, 82, 223));
        this.moduleColorMap.put(this.getModuleByClass(NoRender.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass(SmallShield.class), new Color(145, 223, 187));
        this.moduleColorMap.put(this.getModuleByClass(StorageESP.class), new Color(97, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass(Trajectories.class), new Color(98, 18, 223));
        this.moduleColorMap.put(this.getModuleByClass(VoidESP.class), new Color(68, 178, 142));
        this.moduleColorMap.put(this.getModuleByClass(XRay.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass(ElytraFlight.class), new Color(55, 161, 201));
        this.moduleColorMap.put(this.getModuleByClass(NoSlowDown.class), new Color(61, 204, 78));
        this.moduleColorMap.put(this.getModuleByClass(Speed.class), new Color(55, 161, 196));
        this.moduleColorMap.put(this.getModuleByClass(AntiVoid.class), new Color(86, 53, 98));
        this.moduleColorMap.put(this.getModuleByClass(Step.class), new Color(144, 212, 203));
        this.moduleColorMap.put(this.getModuleByClass(Strafe.class), new Color(0, 204, 255));
        this.moduleColorMap.put(this.getModuleByClass(Velocity.class), new Color(115, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(ReverseStep.class), new Color(1, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(FakePlayer.class), new Color(37, 192, 170));
        this.moduleColorMap.put(this.getModuleByClass(FastPlace.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass(Freecam.class), new Color(206, 232, 128));
        this.moduleColorMap.put(this.getModuleByClass(LiquidInteract.class), new Color(85, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass(MCP.class), new Color(153, 68, 170));
        this.moduleColorMap.put(this.getModuleByClass(MultiTask.class), new Color(17, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass(Replenish.class), new Color(153, 223, 235));
        this.moduleColorMap.put(this.getModuleByClass(Speedmine.class), new Color(152, 166, 113));
        this.moduleColorMap.put(this.getModuleByClass(TimerSpeed.class), new Color(255, 133, 18));
        this.moduleColorMap.put(this.getModuleByClass(XCarry.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass(ClickGui.class), new Color(26, 81, 135));
        this.moduleColorMap.put(this.getModuleByClass(Colors.class), new Color(135, 133, 26));
        this.moduleColorMap.put(this.getModuleByClass(FontMod.class), new Color(135, 26, 88));
        this.moduleColorMap.put(this.getModuleByClass(HUD.class), new Color(110, 26, 135));
        this.moduleColorMap.put(this.getModuleByClass(Managers.class), new Color(26, 90, 135));
        this.moduleColorMap.put(this.getModuleByClass(Notifications.class), new Color(170, 153, 255));
        this.moduleColorMap.put(this.getModuleByClass(ServerModule.class), new Color(60, 110, 175));
        this.moduleColorMap.put(this.getModuleByClass(Media.class), new Color(138, 45, 13));
        this.moduleColorMap.put(this.getModuleByClass(HandChams.class), new Color(96, 138, 92));
        for (final Module module : this.modules) {
            module.animation.start();
        }
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class clazz) {
        final Object module = this.getModuleByClass((Class<Object>)clazz);
        if (module != null) {
            ((Module)module).enable();
        }
    }
    
    public void disableModule(final Class clazz) {
        final Object module = this.getModuleByClass((Class<Object>)clazz);
        if (module != null) {
            ((Module)module).disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class clazz) {
        final Object module = this.getModuleByClass((Class<Object>)clazz);
        return module != null && ((Module)module).isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled() && !module.isSliding()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Module::getDisplayName)).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof CreepyWareGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    public List<Module> getAnimationModules(final Module.Category category) {
        final ArrayList<Module> animationModules = new ArrayList<Module>();
        for (final Module module : this.getEnabledModules()) {
            if (module.getCategory() == category && !module.isDisabled() && module.isSliding()) {
                if (!module.isDrawn()) {
                    continue;
                }
                animationModules.add(module);
            }
        }
        return animationModules;
    }
}
