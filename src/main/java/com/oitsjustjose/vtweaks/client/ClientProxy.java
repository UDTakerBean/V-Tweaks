package com.oitsjustjose.vtweaks.client;

import com.mojang.math.Vector3f;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.network.packet.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientProxy extends CommonProxy {
    Minecraft mc;

    @Override
    public void init() {
        mc = Minecraft.getInstance();
        CommonProxy.networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode, ArmorBreakPacket::decode, ArmorBreakPacket::handleClient);
        networkManager.networkWrapper.registerMessage(1, ChallengerParticlePacket.class, ChallengerParticlePacket::encode, ChallengerParticlePacket::decode, ChallengerParticlePacket::handleClient);
    }

    @Override
    public void playSound(Player player) {
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.SHIELD_BREAK, 1F, 1F);
        }
    }

    @Override
    public void addParticle(float r, float g, float b, double x, double y, double z) {
        showDustParticle(r, g, b, x, y, z);
    }

    public static void showDustParticle(float r, float g, float b, double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        Entity view = mc.getCameraEntity();

        if (view != null) {
            Vec3 position = view.getForward();
            Vec3 particlePos = new Vec3(x, y, z);

            if (mc.player != null && mc.player.shouldRenderAtSqrDistance(position.distanceTo(particlePos))) {
                ParticleOptions p = new DustParticleOptions(new Vector3f(r, g, b), 1.0F);
                mc.player.clientLevel.addParticle(p, false, x, y, z, 0D, 0D, 0D);
            }
        }
    }
}