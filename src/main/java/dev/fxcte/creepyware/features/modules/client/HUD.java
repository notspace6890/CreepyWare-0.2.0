// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.client;

import net.minecraft.client.gui.ScaledResolution;
import dev.fxcte.creepyware.util.RenderUtil;
import java.util.function.ToIntFunction;
import net.minecraft.init.Items;
import dev.fxcte.creepyware.util.EntityUtil;
import dev.fxcte.creepyware.manager.TextManager;
import net.minecraft.client.Minecraft;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.potion.PotionEffect;
import dev.fxcte.creepyware.util.MathUtil;
import net.minecraft.client.gui.GuiChat;
import dev.fxcte.creepyware.features.modules.misc.ToolTips;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.fxcte.creepyware.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.fxcte.creepyware.features.Feature;
import dev.fxcte.creepyware.event.events.Render2DEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.fxcte.creepyware.event.events.ClientEvent;
import java.util.Iterator;
import dev.fxcte.creepyware.CreepyWare;
import net.minecraft.init.MobEffects;
import java.util.HashMap;
import java.awt.Color;
import net.minecraft.potion.Potion;
import java.util.Map;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.setting.Setting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import dev.fxcte.creepyware.features.modules.Module;

public class HUD extends Module
{
    private static final ItemStack totem;
    private static final ResourceLocation codHitmarker;
    private static final ResourceLocation csgoHitmarker;
    private static HUD INSTANCE;
    private final Setting<Boolean> renderingUp;
    private final Setting<WaterMark> watermark;
    private final Setting<Boolean> modeVer;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> moduleColors;
    private final Setting<Boolean> alphabeticalSorting;
    private final Setting<Boolean> serverBrand;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> direction;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    private final Setting<Boolean> altPotionsColors;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> durability;
    private final Setting<Boolean> percent;
    private final Setting<Boolean> totems;
    private final Setting<Greeter> greeter;
    private final Setting<String> spoofGreeter;
    private final Setting<LagNotify> lag;
    private final Setting<Boolean> hitMarkers;
    private final Setting<Boolean> grayNess;
    private final Timer timer;
    private final Timer moduleTimer;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> rainbow;
    public Setting<Integer> factor;
    public Setting<Boolean> rolling;
    public Setting<Integer> rainbowSpeed;
    public Setting<Integer> rainbowSaturation;
    public Setting<Integer> rainbowBrightness;
    public Setting<Boolean> potionIcons;
    public Setting<Boolean> shadow;
    public Setting<Integer> animationHorizontalTime;
    public Setting<Integer> animationVerticalTime;
    public Setting<Boolean> textRadar;
    public Setting<Boolean> time;
    public Setting<Integer> hudRed;
    public Setting<Integer> hudGreen;
    public Setting<Integer> hudBlue;
    public Setting<Boolean> potions1;
    public Setting<Boolean> MS;
    public Map<Module, Float> moduleProgressMap;
    public Map<Integer, Integer> colorMap;
    private Map<String, Integer> players;
    private final Map<Potion, Color> potionColorMap;
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    
    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Category.CLIENT, true, false, false);
        this.renderingUp = (Setting<Boolean>)this.register(new Setting("RenderingUp", (T)false, "Orientation of the HUD-Elements."));
        this.watermark = (Setting<WaterMark>)this.register(new Setting("Logo", (T)WaterMark.NONE, "WaterMark"));
        this.modeVer = (Setting<Boolean>)this.register(new Setting("Version", (T)false, v -> this.watermark.getValue() != WaterMark.NONE));
        this.arrayList = (Setting<Boolean>)this.register(new Setting("ActiveModules", (T)false, "Lists the active modules."));
        this.moduleColors = (Setting<Boolean>)this.register(new Setting("ModuleColors", (T)false, v -> this.arrayList.getValue()));
        this.alphabeticalSorting = (Setting<Boolean>)this.register(new Setting("AlphabeticalSorting", (T)false, v -> this.arrayList.getValue()));
        this.serverBrand = (Setting<Boolean>)this.register(new Setting("ServerBrand", (T)false, "Brand of the server you are on."));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (T)false, "Your response time to the server."));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", (T)false, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", (T)false, "Your frames per second."));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (T)false, "Your current coordinates"));
        this.direction = (Setting<Boolean>)this.register(new Setting("Direction", (T)false, "The Direction you are facing."));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", (T)false, "Your Speed"));
        this.potions = (Setting<Boolean>)this.register(new Setting("Potions", (T)false, "Active potion effects"));
        this.altPotionsColors = (Setting<Boolean>)this.register(new Setting("AltPotionColors", (T)false, v -> this.potions.getValue()));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (T)false, "ArmorHUD"));
        this.durability = (Setting<Boolean>)this.register(new Setting("Durability", (T)false, "Durability"));
        this.percent = (Setting<Boolean>)this.register(new Setting("Percent", (T)true, v -> this.armor.getValue()));
        this.totems = (Setting<Boolean>)this.register(new Setting("Totems", (T)false, "TotemHUD"));
        this.greeter = (Setting<Greeter>)this.register(new Setting("Greeter", (T)Greeter.NONE, "Greets you."));
        this.spoofGreeter = (Setting<String>)this.register(new Setting("GreeterName", (T)"CharlesDana", v -> this.greeter.getValue() == Greeter.CUSTOM));
        this.lag = (Setting<LagNotify>)this.register(new Setting("Lag", (T)LagNotify.GRAY, "Lag Notifier"));
        this.hitMarkers = (Setting<Boolean>)this.register(new Setting("Speed", "HitMarkers", 0.0, 0.0, (T)true, 0));
        this.grayNess = (Setting<Boolean>)this.register(new Setting("Speed", "FutureColour", 0.0, 0.0, (T)true, 0));
        this.timer = new Timer();
        this.moduleTimer = new Timer();
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", (T)false, "Universal colors for hud."));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false, "Rainbow hud."));
        this.factor = (Setting<Integer>)this.register(new Setting("Factor", (T)1, (T)0, (T)20, v -> this.rainbow.getValue()));
        this.rolling = (Setting<Boolean>)this.register(new Setting("Rolling", (T)false, v -> this.rainbow.getValue()));
        this.rainbowSpeed = (Setting<Integer>)this.register(new Setting("RSpeed", (T)20, (T)0, (T)100, v -> this.rainbow.getValue()));
        this.rainbowSaturation = (Setting<Integer>)this.register(new Setting("Saturation", (T)255, (T)0, (T)255, v -> this.rainbow.getValue()));
        this.rainbowBrightness = (Setting<Integer>)this.register(new Setting("Brightness", (T)255, (T)0, (T)255, v -> this.rainbow.getValue()));
        this.potionIcons = (Setting<Boolean>)this.register(new Setting("PotionIcons", (T)true, "Draws Potion Icons."));
        this.shadow = (Setting<Boolean>)this.register(new Setting("Shadow", (T)false, "Draws the text with a shadow."));
        this.animationHorizontalTime = (Setting<Integer>)this.register(new Setting("AnimationHTime", (T)500, (T)1, (T)1000, v -> this.arrayList.getValue()));
        this.animationVerticalTime = (Setting<Integer>)this.register(new Setting("AnimationVTime", (T)50, (T)1, (T)500, v -> this.arrayList.getValue()));
        this.textRadar = (Setting<Boolean>)this.register(new Setting("TextRadar", (T)false, "A TextRadar"));
        this.time = (Setting<Boolean>)this.register(new Setting("Time", (T)false, "The time"));
        this.hudRed = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.hudGreen = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.hudBlue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.potions1 = (Setting<Boolean>)this.register(new Setting("LevelPotions", (T)false, v -> this.potions.getValue()));
        this.MS = (Setting<Boolean>)this.register(new Setting("ms", (T)false, v -> this.ping.getValue()));
        this.moduleProgressMap = new HashMap<Module, Float>();
        this.colorMap = new HashMap<Integer, Integer>();
        this.players = new HashMap<String, Integer>();
        this.potionColorMap = new HashMap<Potion, Color>();
        this.setInstance();
        this.potionColorMap.put(MobEffects.field_76424_c, new Color(124, 175, 198));
        this.potionColorMap.put(MobEffects.field_76421_d, new Color(90, 108, 129));
        this.potionColorMap.put(MobEffects.field_76422_e, new Color(217, 192, 67));
        this.potionColorMap.put(MobEffects.field_76419_f, new Color(74, 66, 23));
        this.potionColorMap.put(MobEffects.field_76420_g, new Color(147, 36, 35));
        this.potionColorMap.put(MobEffects.field_76432_h, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.field_76433_i, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.field_76430_j, new Color(34, 255, 76));
        this.potionColorMap.put(MobEffects.field_76431_k, new Color(85, 29, 74));
        this.potionColorMap.put(MobEffects.field_76428_l, new Color(205, 92, 171));
        this.potionColorMap.put(MobEffects.field_76429_m, new Color(153, 69, 58));
        this.potionColorMap.put(MobEffects.field_76426_n, new Color(228, 154, 58));
        this.potionColorMap.put(MobEffects.field_76427_o, new Color(46, 82, 153));
        this.potionColorMap.put(MobEffects.field_76441_p, new Color(127, 131, 146));
        this.potionColorMap.put(MobEffects.field_76440_q, new Color(31, 31, 35));
        this.potionColorMap.put(MobEffects.field_76439_r, new Color(31, 31, 161));
        this.potionColorMap.put(MobEffects.field_76438_s, new Color(88, 118, 83));
        this.potionColorMap.put(MobEffects.field_76437_t, new Color(72, 77, 72));
        this.potionColorMap.put(MobEffects.field_76436_u, new Color(78, 147, 49));
        this.potionColorMap.put(MobEffects.field_82731_v, new Color(53, 42, 39));
        this.potionColorMap.put(MobEffects.field_180152_w, new Color(248, 125, 35));
        this.potionColorMap.put(MobEffects.field_76444_x, new Color(37, 82, 165));
        this.potionColorMap.put(MobEffects.field_76443_y, new Color(248, 36, 35));
        this.potionColorMap.put(MobEffects.field_188423_x, new Color(148, 160, 97));
        this.potionColorMap.put(MobEffects.field_188424_y, new Color(206, 255, 255));
        this.potionColorMap.put(MobEffects.field_188425_z, new Color(51, 153, 0));
        this.potionColorMap.put(MobEffects.field_189112_A, new Color(192, 164, 77));
    }
    
    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }
    
    private void setInstance() {
        HUD.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        for (final Module module : CreepyWare.moduleManager.sortedModules) {
            if (module.isDisabled() && module.arrayListOffset == 0.0f) {
                module.sliding = true;
            }
        }
        if (this.timer.passedMs(Managers.getInstance().textRadarUpdates.getValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }
    
    @SubscribeEvent
    public void onModuleToggle(final ClientEvent event) {
        if (event.getFeature() instanceof Module) {
            if (event.getStage() == 0) {
                for (float i = 0.0f; i <= this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0f) {
                    if (this.moduleTimer.passedMs(1L)) {
                        this.moduleProgressMap.put((Module)event.getFeature(), this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i);
                    }
                    this.timer.reset();
                }
            }
            else if (event.getStage() == 1) {
                for (float i = 0.0f; i <= this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0f) {
                    if (this.moduleTimer.passedMs(1L)) {
                        this.moduleProgressMap.put((Module)event.getFeature(), this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i);
                    }
                    this.timer.reset();
                }
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Feature.fullNullCheck()) {
            return;
        }
        final int colorSpeed = 101 - this.rainbowSpeed.getValue();
        final float hue = this.colorSync.getValue() ? Colors.INSTANCE.hue : (System.currentTimeMillis() % (360 * colorSpeed) / (360.0f * colorSpeed));
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        float tempHue = hue;
        for (int i = 0; i <= height; ++i) {
            if (this.colorSync.getValue()) {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, Colors.INSTANCE.rainbowSaturation.getValue() / 255.0f, Colors.INSTANCE.rainbowBrightness.getValue() / 255.0f));
            }
            else {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, this.rainbowSaturation.getValue() / 255.0f, this.rainbowBrightness.getValue() / 255.0f));
            }
            tempHue += 1.0f / height * this.factor.getValue();
        }
        GlStateManager.func_179094_E();
        if (this.rainbow.getValue() && !this.rolling.getValue()) {
            this.color = (this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColorHex() : Color.HSBtoRGB(hue, this.rainbowSaturation.getValue() / 255.0f, this.rainbowBrightness.getValue() / 255.0f));
        }
        else if (!this.rainbow.getValue()) {
            this.color = (this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue()));
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        switch (this.watermark.getValue()) {
            case CreepyWare: {
                this.renderer.drawString("CreepyWare" + (this.modeVer.getValue() ? "b0.2.0" : ""), 2.0f, 2.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2)) : this.color, true);
                break;
            }
        }
        if (this.textRadar.getValue()) {
            this.drawTextRadar((ToolTips.getInstance().isOff() || !ToolTips.getInstance().shulkerSpy.getValue() || !ToolTips.getInstance().render.getValue()) ? 0 : ToolTips.getInstance().getTextRadarY());
        }
        int j = this.renderingUp.getValue() ? 0 : ((HUD.mc.field_71462_r instanceof GuiChat) ? 14 : 0);
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                for (int k = 0; k < (this.alphabeticalSorting.getValue() ? CreepyWare.moduleManager.alphabeticallySortedModules.size() : CreepyWare.moduleManager.sortedModules.size()); ++k) {
                    final Module module = this.alphabeticalSorting.getValue() ? CreepyWare.moduleManager.alphabeticallySortedModules.get(k) : CreepyWare.moduleManager.sortedModules.get(k);
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    final Color moduleColor = CreepyWare.moduleManager.moduleColorMap.get(module);
                    this.renderer.drawString(text, width - 2 - this.renderer.getStringWidth(text) + ((this.animationHorizontalTime.getValue() == 1) ? 0.0f : module.arrayListOffset), (float)(2 + j * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(MathUtil.clamp(2 + j * 10, 0, height))) : ((this.moduleColors.getValue() && moduleColor != null) ? moduleColor.getRGB() : this.color), true);
                    ++j;
                }
            }
            else {
                for (int k = 0; k < (this.alphabeticalSorting.getValue() ? CreepyWare.moduleManager.alphabeticallySortedModules.size() : CreepyWare.moduleManager.sortedModules.size()); ++k) {
                    final Module module = this.alphabeticalSorting.getValue() ? CreepyWare.moduleManager.alphabeticallySortedModules.get(CreepyWare.moduleManager.alphabeticallySortedModules.size() - 1 - k) : CreepyWare.moduleManager.sortedModules.get(k);
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    final Color moduleColor = CreepyWare.moduleManager.moduleColorMap.get(module);
                    final TextManager renderer = this.renderer;
                    final String text2 = text;
                    final float x = width - 2 - this.renderer.getStringWidth(text) + ((this.animationHorizontalTime.getValue() == 1) ? 0.0f : module.arrayListOffset);
                    final int n = height;
                    j += 10;
                    renderer.drawString(text2, x, (float)(n - j), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(MathUtil.clamp(height - j, 0, height))) : ((this.moduleColors.getValue() && moduleColor != null) ? moduleColor.getRGB() : this.color), true);
                }
            }
        }
        int k = this.renderingUp.getValue() ? ((HUD.mc.field_71462_r instanceof GuiChat) ? 0 : 0) : 0;
        if (this.renderingUp.getValue()) {
            if (this.serverBrand.getValue()) {
                final String text3 = grayString + "Server brand " + ChatFormatting.WHITE + CreepyWare.serverManager.getServerBrand();
                final TextManager renderer2 = this.renderer;
                final String text4 = text3;
                final float x2 = (float)(width - (this.renderer.getStringWidth(text3) + 2));
                final int n2 = height - 2;
                k += 10;
                renderer2.drawString(text4, x2, (float)(n2 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
            }
            if (this.potions.getValue()) {
                for (final PotionEffect effect : CreepyWare.potionManager.getOwnPotions()) {
                    final String text5 = this.altPotionsColors.getValue() ? CreepyWare.potionManager.getPotionString(effect) : CreepyWare.potionManager.getColoredPotionString(effect);
                    final TextManager renderer3 = this.renderer;
                    final String text6 = text5;
                    final float x3 = (float)(width - (this.renderer.getStringWidth(text5) + 2));
                    final int n3 = height - 2;
                    k += 10;
                    renderer3.drawString(text6, x3, (float)(n3 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : (this.altPotionsColors.getValue() ? this.potionColorMap.get(effect.func_188419_a()).getRGB() : this.color), true);
                }
            }
            if (this.speed.getValue()) {
                final String text3 = grayString + "Speed " + ChatFormatting.WHITE + CreepyWare.speedManager.getSpeedKpH() + " km/h";
                final TextManager renderer4 = this.renderer;
                final String text7 = text3;
                final float x4 = (float)(width - (this.renderer.getStringWidth(text3) + 2));
                final int n4 = height - 2;
                k += 10;
                renderer4.drawString(text7, x4, (float)(n4 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
            }
            if (this.time.getValue()) {
                final String text3 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                final TextManager renderer5 = this.renderer;
                final String text8 = text3;
                final float x5 = (float)(width - (this.renderer.getStringWidth(text3) + 2));
                final int n5 = height - 2;
                k += 10;
                renderer5.drawString(text8, x5, (float)(n5 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
            }
            if (this.durability.getValue()) {
                final int itemDamage = HUD.mc.field_71439_g.func_184614_ca().func_77958_k() - HUD.mc.field_71439_g.func_184614_ca().func_77952_i();
                if (itemDamage > 0) {
                    final String text = grayString + "Durability " + ChatFormatting.RESET + itemDamage;
                    final TextManager renderer6 = this.renderer;
                    final String text9 = text;
                    final float x6 = (float)(width - (this.renderer.getStringWidth(text) + 2));
                    final int n6 = height - 2;
                    k += 10;
                    renderer6.drawString(text9, x6, (float)(n6 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
                }
            }
            if (this.tps.getValue()) {
                final String text3 = grayString + "TPS " + ChatFormatting.WHITE + CreepyWare.serverManager.getTPS();
                final TextManager renderer7 = this.renderer;
                final String text10 = text3;
                final float x7 = (float)(width - (this.renderer.getStringWidth(text3) + 2));
                final int n7 = height - 2;
                k += 10;
                renderer7.drawString(text10, x7, (float)(n7 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.field_71470_ab;
            final String text = grayString + "Ping " + ChatFormatting.WHITE + (ServerModule.getInstance().isConnected() ? ServerModule.getInstance().getServerPing() : CreepyWare.serverManager.getPing()) + (this.MS.getValue() ? "ms" : "");
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    final TextManager renderer8 = this.renderer;
                    final String text11 = text;
                    final float x8 = (float)(width - (this.renderer.getStringWidth(text) + 2));
                    final int n8 = height - 2;
                    k += 10;
                    renderer8.drawString(text11, x8, (float)(n8 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
                }
                if (this.fps.getValue()) {
                    final TextManager renderer9 = this.renderer;
                    final String text12 = fpsText;
                    final float x9 = (float)(width - (this.renderer.getStringWidth(fpsText) + 2));
                    final int n9 = height - 2;
                    k += 10;
                    renderer9.drawString(text12, x9, (float)(n9 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
                }
            }
            else {
                if (this.fps.getValue()) {
                    final TextManager renderer10 = this.renderer;
                    final String text13 = fpsText;
                    final float x10 = (float)(width - (this.renderer.getStringWidth(fpsText) + 2));
                    final int n10 = height - 2;
                    k += 10;
                    renderer10.drawString(text13, x10, (float)(n10 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
                }
                if (this.ping.getValue()) {
                    final TextManager renderer11 = this.renderer;
                    final String text14 = text;
                    final float x11 = (float)(width - (this.renderer.getStringWidth(text) + 2));
                    final int n11 = height - 2;
                    k += 10;
                    renderer11.drawString(text14, x11, (float)(n11 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(height - k)) : this.color, true);
                }
            }
        }
        else {
            if (this.serverBrand.getValue()) {
                final String text3 = grayString + "Server brand " + ChatFormatting.WHITE + CreepyWare.serverManager.getServerBrand();
                this.renderer.drawString(text3, (float)(width - (this.renderer.getStringWidth(text3) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
            }
            if (this.potions.getValue()) {
                for (final PotionEffect effect : CreepyWare.potionManager.getOwnPotions()) {
                    final String text5 = this.altPotionsColors.getValue() ? CreepyWare.potionManager.getPotionString(effect) : CreepyWare.potionManager.getColoredPotionString(effect);
                    this.renderer.drawString(text5, (float)(width - (this.renderer.getStringWidth(text5) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : (this.altPotionsColors.getValue() ? this.potionColorMap.get(effect.func_188419_a()).getRGB() : this.color), true);
                }
            }
            if (this.speed.getValue()) {
                final String text3 = grayString + "Speed " + ChatFormatting.WHITE + CreepyWare.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(text3, (float)(width - (this.renderer.getStringWidth(text3) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
            }
            if (this.time.getValue()) {
                final String text3 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(text3, (float)(width - (this.renderer.getStringWidth(text3) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
            }
            if (this.durability.getValue()) {
                final int itemDamage = HUD.mc.field_71439_g.func_184614_ca().func_77958_k() - HUD.mc.field_71439_g.func_184614_ca().func_77952_i();
                if (itemDamage > 0) {
                    final String text = grayString + "Durability " + ChatFormatting.GREEN + itemDamage;
                    this.renderer.drawString(text, (float)(width - (this.renderer.getStringWidth(text) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
                }
            }
            if (this.tps.getValue()) {
                final String text3 = grayString + "TPS " + ChatFormatting.WHITE + CreepyWare.serverManager.getTPS();
                this.renderer.drawString(text3, (float)(width - (this.renderer.getStringWidth(text3) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.field_71470_ab;
            final String text = grayString + "Ping " + ChatFormatting.WHITE + CreepyWare.serverManager.getPing();
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(text, (float)(width - (this.renderer.getStringWidth(text) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - (this.renderer.getStringWidth(fpsText) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - (this.renderer.getStringWidth(fpsText) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(text, (float)(width - (this.renderer.getStringWidth(text) + 2)), (float)(2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2 + k * 10)) : this.color, true);
                }
            }
        }
        final boolean inHell = HUD.mc.field_71441_e.func_180494_b(HUD.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
        final int posX = (int)HUD.mc.field_71439_g.field_70165_t;
        final int posY = (int)HUD.mc.field_71439_g.field_70163_u;
        final int posZ = (int)HUD.mc.field_71439_g.field_70161_v;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.field_71439_g.field_70165_t * nether);
        final int hposZ = (int)(HUD.mc.field_71439_g.field_70161_v * nether);
        if (this.renderingUp.getValue()) {
            CreepyWare.notificationManager.handleNotifications(height - (k + 16));
        }
        else {
            CreepyWare.notificationManager.handleNotifications(height - (j + 16));
        }
        k = ((HUD.mc.field_71462_r instanceof GuiChat) ? 14 : 0);
        final String coordinates = String.valueOf(ChatFormatting.WHITE) + posX + ChatFormatting.GRAY + " [" + hposX + "], " + ChatFormatting.WHITE + posY + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + posZ + ChatFormatting.GRAY + " [" + hposZ + "]";
        final String text15 = (this.direction.getValue() ? (CreepyWare.rotationManager.getDirection4D(false) + " ") : "") + (this.coords.getValue() ? coordinates : "") + "";
        final TextManager renderer12 = this.renderer;
        final String text16 = text15;
        final float x12 = 2.0f;
        final int n12 = height;
        k += 10;
        final float y = (float)(n12 - k);
        int color;
        if (this.rolling.getValue() && this.rainbow.getValue()) {
            final Map<Integer, Integer> colorMap = this.colorMap;
            final int n13 = height;
            k += 10;
            color = colorMap.get(n13 - k);
        }
        else {
            color = this.color;
        }
        renderer12.drawString(text16, 2.0f, y, color, true);
        if (this.armor.getValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.NONE) {
            this.renderGreeter();
        }
        if (this.lag.getValue() != LagNotify.NONE) {
            this.renderLag();
        }
        if (this.hitMarkers.getValue() && this.hitMarkerTimer > 0) {
            this.drawHitMarkers();
        }
        GlStateManager.func_179121_F();
    }
    
    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }
    
    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        switch (this.greeter.getValue()) {
            case TIME: {
                text = text + MathUtil.getTimeOfDay() + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
            case CHRISTMAS: {
                text = text + "Merry Christmas " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            case LONG: {
                text = text + "Welcome to CreepyWare.eu " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            case CUSTOM: {
                text += this.spoofGreeter.getValue();
                break;
            }
            default: {
                text = text + "Welcome " + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
        }
        this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(2)) : this.color, true);
    }
    
    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (CreepyWare.serverManager.isServerNotResponding()) {
            final String text = ((this.lag.getValue() == LagNotify.GRAY) ? ChatFormatting.GRAY : ChatFormatting.RED) + "Server not responding: " + MathUtil.round(CreepyWare.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(20)) : this.color, true);
        }
    }
    
    public void renderArrayList() {
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += HUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(HUD.totem, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, HUD.totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }
    
    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
        for (final ItemStack is : HUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            final String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
            this.renderer.drawStringWithShadow(s, (float)(x + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!percent) {
                continue;
            }
            int dmg = 0;
            final int itemDurability = is.func_77958_k() - is.func_77952_i();
            final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
            final float red = 1.0f - green;
            if (percent) {
                dmg = 100 - (int)(red * 100.0f);
            }
            else {
                dmg = itemDurability;
            }
            this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }
    
    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HUD.mc);
        RenderUtil.drawLine(resolution.func_78326_a() / 2.0f - 4.0f, resolution.func_78328_b() / 2.0f - 4.0f, resolution.func_78326_a() / 2.0f - 8.0f, resolution.func_78328_b() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.func_78326_a() / 2.0f + 4.0f, resolution.func_78328_b() / 2.0f - 4.0f, resolution.func_78326_a() / 2.0f + 8.0f, resolution.func_78328_b() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.func_78326_a() / 2.0f - 4.0f, resolution.func_78328_b() / 2.0f + 4.0f, resolution.func_78326_a() / 2.0f - 8.0f, resolution.func_78328_b() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.func_78326_a() / 2.0f + 4.0f, resolution.func_78328_b() / 2.0f + 4.0f, resolution.func_78326_a() / 2.0f + 8.0f, resolution.func_78328_b() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }
    
    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float)y, (this.rolling.getValue() && this.rainbow.getValue()) ? ((int)this.colorMap.get(y)) : this.color, true);
                y += textheight;
            }
        }
    }
    
    static {
        totem = new ItemStack(Items.field_190929_cY);
        codHitmarker = new ResourceLocation("creepy", "cod_hitmarker");
        csgoHitmarker = new ResourceLocation("creepy", "csgo_hitmarker");
        HUD.INSTANCE = new HUD();
    }
    
    public enum Greeter
    {
        NONE, 
        NAME, 
        TIME, 
        CHRISTMAS, 
        LONG, 
        CUSTOM;
    }
    
    public enum LagNotify
    {
        NONE, 
        RED, 
        GRAY;
    }
    
    public enum WaterMark
    {
        NONE, 
        CreepyWare;
    }
    
    public enum Sound
    {
        NONE, 
        COD, 
        CSGO;
    }
}
