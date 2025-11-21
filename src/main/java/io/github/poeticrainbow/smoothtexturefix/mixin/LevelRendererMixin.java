package io.github.poeticrainbow.smoothtexturefix.mixin;

import com.mojang.blaze3d.textures.FilterMode;
import io.github.poeticrainbow.smoothtexturefix.SmoothTextureFix;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    /**
     * Overrides the texture filtering for the level renderer. Might break due to synthetic method reference
     * @author PoeticRainbow
     */
    @ModifyArgs(method = "method_62214", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createSampler(Lcom/mojang/blaze3d/textures/AddressMode;Lcom/mojang/blaze3d/textures/AddressMode;Lcom/mojang/blaze3d/textures/FilterMode;Lcom/mojang/blaze3d/textures/FilterMode;ILjava/util/OptionalDouble;)Lcom/mojang/blaze3d/textures/GpuSampler;"))
    private static void smoothtexturefix$override_texture_filtering(Args args) {
        if (SmoothTextureFix.config.overrideTextureFilter()) {
            for (int i = 0; i < args.size(); i++) {
                var arg = args.get(i);
                if (arg instanceof FilterMode) args.set(i, FilterMode.NEAREST);
            }
        }
    }
}
