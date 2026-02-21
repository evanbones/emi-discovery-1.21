package net.funkpla.emi_discovery.mixin.emi;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiApi.class)
public abstract class EmiApiMixin {

  /**
   * Cancel getting a stack if there are no recipes that can be crafted from known ingredients. This
   * will prevent the tab for the category from being drawn.
   */
  @Inject(
      remap = false,
      method = "displayRecipes(Ldev/emi/emi/api/stack/EmiIngredient;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"),
      cancellable = true)
  private static void stopEmptyRecipeTabs(EmiIngredient stack, CallbackInfo ci) {
    if (EmiApi.getRecipeManager().getRecipesByOutput(stack.getEmiStacks().get(0)).stream()
        .noneMatch(KnownItems::areAllKnown)) ci.cancel();
  }
}
