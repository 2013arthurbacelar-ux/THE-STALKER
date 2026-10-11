package com.stalker.mixin.client;

import com.stalker.client.StalkerClientState;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Screen shake: depois que a câmera é posicionada a cada frame (Camera.setup), somamos um pequeno
 * desvio de yaw/pitch. Isso só afeta o que é RENDERIZADO — a mira real do jogador não muda.
 * Usamos o nome do método sem descritor para tolerar mudanças de assinatura entre versões.
 */
@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow protected abstract void setRotation(float yRot, float xRot);
	@Shadow public abstract float getYRot();
	@Shadow public abstract float getXRot();

	@Inject(method = "setup", at = @At("RETURN"))
	private void stalker$applyShake(CallbackInfo ci) {
		if (!StalkerClientState.active()) return;
		float[] shake = StalkerClientState.cameraShake();
		this.setRotation(this.getYRot() + shake[0], this.getXRot() + shake[1]);
	}
}
