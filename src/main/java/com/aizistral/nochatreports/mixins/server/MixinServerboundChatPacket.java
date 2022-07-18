package com.aizistral.nochatreports.mixins.server;

import net.minecraft.Util;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.util.Crypt.SaltSignaturePair;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.core.NoReportsConfig;

import java.time.Instant;
import java.util.UUID;

@Mixin(ServerboundChatPacket.class)
public class MixinServerboundChatPacket {

	@Shadow @Final private Instant timeStamp;

	/**
	 * @reason Strip signatures before relaying messages to other clients, if they arrive signed
	 * This is important for mod to be able to operate in server-only mode without
	 * {@link NoReportsConfig#convertToGameMessage()} enabled.
	 * @author Aizistral
	 */

	@Inject(method = "signature", at = @At("RETURN"), cancellable = true)
	private void onSignature(CallbackInfoReturnable<MessageSignature> info) {
		info.setReturnValue(MessageSignature.EMPTY);
	}

}
