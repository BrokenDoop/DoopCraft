package brokendoop.doopmod.mixin;

import brokendoop.doopmod.DoopMod;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Vertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Polygon.class, remap = false)
public abstract class PolygonMixin {
	@Inject(method = "<init>([Lnet/minecraft/client/render/Vertex;IIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Vertex;remap(FF)Lnet/minecraft/client/render/Vertex;", ordinal = 0))
	public void changeOffset(Vertex[] vertices, int minU, int minV, int maxU, int maxV, int texWidth, int texHeight, CallbackInfo ci,
							 @Local(name = "offsetU") LocalFloatRef offsetU, @Local(name = "offsetV") LocalFloatRef offsetV)
	{
		if (DoopMod.modPadding) {
			offsetU.set(1/1280F);
			offsetV.set(1/1280F);
		}
	}
}
