// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import dev.fxcte.creepyware.CreepyWare;
import dev.fxcte.creepyware.util.Timer;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class TimerSpeed extends Module
{
    public Setting<Boolean> autoOff;
    public Setting<Integer> timeLimit;
    public Setting<TimerMode> mode;
    public Setting<Float> timerSpeed;
    public Setting<Float> fastSpeed;
    public Setting<Integer> fastTime;
    public Setting<Integer> slowTime;
    public Setting<Boolean> startFast;
    public float speed;
    private final Timer timer;
    private final Timer turnOffTimer;
    private boolean fast;
    
    public TimerSpeed() {
        super("Timer", "Will speed up the game.", Category.PLAYER, false, false, false);
        this.autoOff = (Setting<Boolean>)this.register(new Setting("Speed", "AutoOff", 0.0, 0.0, (T)false, 0));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Limit", (T)250, (T)1, (T)2500, v -> this.autoOff.getValue()));
        this.mode = (Setting<TimerMode>)this.register(new Setting("Speed", "Mode", 0.0, 0.0, (T)TimerMode.NORMAL, 0));
        this.timerSpeed = (Setting<Float>)this.register(new Setting("Speed", (T)4.0f, (T)0.1f, (T)20.0f));
        this.fastSpeed = (Setting<Float>)this.register(new Setting("Fast", (T)10.0f, (T)0.1f, (T)100.0f, v -> this.mode.getValue() == TimerMode.SWITCH, "Fast Speed for switch."));
        this.fastTime = (Setting<Integer>)this.register(new Setting("FastTime", (T)20, (T)1, (T)500, v -> this.mode.getValue() == TimerMode.SWITCH, "How long you want to go fast.(ms * 10)"));
        this.slowTime = (Setting<Integer>)this.register(new Setting("SlowTime", (T)20, (T)1, (T)500, v -> this.mode.getValue() == TimerMode.SWITCH, "Recover from too fast.(ms * 10)"));
        this.startFast = (Setting<Boolean>)this.register(new Setting("StartFast", (T)false, v -> this.mode.getValue() == TimerMode.SWITCH));
        this.speed = 1.0f;
        this.timer = new Timer();
        this.turnOffTimer = new Timer();
        this.fast = false;
    }
    
    @Override
    public void onEnable() {
        this.turnOffTimer.reset();
        this.speed = this.timerSpeed.getValue();
        if (!this.startFast.getValue()) {
            this.timer.reset();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.autoOff.getValue() && this.turnOffTimer.passedMs(this.timeLimit.getValue())) {
            this.disable();
            return;
        }
        if (this.mode.getValue() == TimerMode.NORMAL) {
            this.speed = this.timerSpeed.getValue();
            return;
        }
        if (!this.fast && this.timer.passedDms(this.slowTime.getValue())) {
            this.fast = true;
            this.speed = this.fastSpeed.getValue();
            this.timer.reset();
        }
        if (this.fast && this.timer.passedDms(this.fastTime.getValue())) {
            this.fast = false;
            this.speed = this.timerSpeed.getValue();
            this.timer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        this.speed = 1.0f;
        CreepyWare.timerManager.reset();
        this.fast = false;
    }
    
    @Override
    public String getDisplayInfo() {
        return this.timerSpeed.getValueAsString();
    }
    
    public enum TimerMode
    {
        NORMAL, 
        SWITCH;
    }
}
