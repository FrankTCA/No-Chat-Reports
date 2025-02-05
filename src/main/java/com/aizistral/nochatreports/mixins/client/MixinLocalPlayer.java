package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.brigadier.ParseResults;

import net.minecraft.Util;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.*;
import net.minecraft.util.Crypt;
import net.minecraft.util.Crypt.SaltSignaturePair;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

import javax.annotation.Nullable;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

	/**
	 * @reason Never sign messages, so that neither server nor other clients have
	 * proof of them being sent from your account.
	 * @author Aizistral
	 */

	@Inject(method = "signMessage", at = @At("HEAD"), cancellable = true)
	private void onSignMessage(MessageSigner messageSigner, ChatMessageContent chatMessageContent, LastSeenMessages lastSeenMessages, CallbackInfoReturnable<MessageSignature> info) {
		if (!ServerSafetyState.forceSignedMessages()) {
			info.setReturnValue(MessageSignature.EMPTY); // I hope this works
		}
	}

	/**
	 * @reason Same as above, except commands mostly concern only server.
	 * @author Aizistral
	 */

	@Inject(method = "signCommandArguments", at = @At("HEAD"), cancellable = true)
	private void onSignCommand(MessageSigner messageSigner, ParseResults<SharedSuggestionProvider> parseResults, Component component, LastSeenMessages lastSeenMessages, CallbackInfoReturnable<ArgumentSignatures> info) {
		if (!ServerSafetyState.forceSignedMessages()) {
			info.setReturnValue(ArgumentSignatures.EMPTY);
		}
	}

}
