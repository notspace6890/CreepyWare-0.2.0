// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.features.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Random;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import dev.fxcte.creepyware.features.setting.Setting;
import dev.fxcte.creepyware.features.modules.Module;

public class FakePlayer extends Module
{
    public Setting<Boolean> hollow;
    private EntityOtherPlayerMP otherPlayer;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns fake player", Category.PLAYER, false, false, false);
        this.hollow = (Setting<Boolean>)this.register(new Setting("Speed", "Move", 0.0, 0.0, (T)false, 0));
    }
    
    @Override
    public void onTick() {
        if (this.otherPlayer != null) {
            final Random random = new Random();
            this.otherPlayer.field_191988_bg = FakePlayer.mc.field_71439_g.field_191988_bg + random.nextInt(5) / 10.0f;
            this.otherPlayer.field_70702_br = FakePlayer.mc.field_71439_g.field_70702_br + random.nextInt(5) / 10.0f;
            if (this.hollow.getValue()) {
                this.travel(this.otherPlayer.field_70702_br, this.otherPlayer.field_70701_bs, this.otherPlayer.field_191988_bg);
            }
        }
    }
    
    public void travel(final float strafe, final float vertical, final float forward) {
        final double d0 = this.otherPlayer.field_70163_u;
        float f1 = 0.8f;
        float f2 = 0.02f;
        float f3 = (float)EnchantmentHelper.func_185294_d((EntityLivingBase)this.otherPlayer);
        if (f3 > 3.0f) {
            f3 = 3.0f;
        }
        if (!this.otherPlayer.field_70122_E) {
            f3 *= 0.5f;
        }
        if (f3 > 0.0f) {
            f1 += (0.54600006f - f1) * f3 / 3.0f;
            f2 += (this.otherPlayer.func_70689_ay() - f2) * f3 / 4.0f;
        }
        this.otherPlayer.func_191958_b(strafe, vertical, forward, f2);
        this.otherPlayer.func_70091_d(MoverType.SELF, this.otherPlayer.field_70159_w, this.otherPlayer.field_70181_x, this.otherPlayer.field_70179_y);
        final EntityOtherPlayerMP otherPlayer = this.otherPlayer;
        otherPlayer.field_70159_w *= f1;
        final EntityOtherPlayerMP otherPlayer2 = this.otherPlayer;
        otherPlayer2.field_70181_x *= 0.800000011920929;
        final EntityOtherPlayerMP otherPlayer3 = this.otherPlayer;
        otherPlayer3.field_70179_y *= f1;
        if (!this.otherPlayer.func_189652_ae()) {
            final EntityOtherPlayerMP otherPlayer4 = this.otherPlayer;
            otherPlayer4.field_70181_x -= 0.02;
        }
        if (this.otherPlayer.field_70123_F && this.otherPlayer.func_70038_c(this.otherPlayer.field_70159_w, this.otherPlayer.field_70181_x + 0.6000000238418579 - this.otherPlayer.field_70163_u + d0, this.otherPlayer.field_70179_y)) {
            this.otherPlayer.field_70181_x = 0.30000001192092896;
        }
    }
    
    @Override
    public void onEnable() {
        if (FakePlayer.mc.field_71441_e == null || FakePlayer.mc.field_71439_g == null) {
            this.toggle();
            return;
        }
        if (this.otherPlayer == null) {
            (this.otherPlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.field_71441_e, new GameProfile(UUID.randomUUID(), "kuro_noob"))).func_82149_j((Entity)FakePlayer.mc.field_71439_g);
            this.otherPlayer.field_71071_by.func_70455_b(FakePlayer.mc.field_71439_g.field_71071_by);
        }
        FakePlayer.mc.field_71441_e.func_72838_d((Entity)this.otherPlayer);
    }
    
    @Override
    public void onDisable() {
        if (this.otherPlayer != null) {
            FakePlayer.mc.field_71441_e.func_72900_e((Entity)this.otherPlayer);
            this.otherPlayer = null;
        }
    }
}
