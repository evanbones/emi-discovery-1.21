package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.recipe.EmiTagRecipe;
import net.funkpla.emi_discovery.Constants;
import net.funkpla.emi_discovery.KnownItems;
import net.funkpla.emi_discovery.mixin.emi.accessor.PageSlotWidgetAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "dev.emi.emi.api.recipe.EmiIngredientRecipe$PageSlotWidget")
public abstract class PageSlotWidgetMixin {

  @WrapOperation(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target = "Ldev/emi/emi/api/stack/EmiIngredient;isEmpty()" + "Z",
              remap = false))
  private boolean filterPageSlots(EmiIngredient instance, Operation<Boolean> original) {
    var ingredient = ((PageSlotWidgetAccessor) this).emi_discovery$getIngredientStack();
    var recipe = ((PageSlotWidgetAccessor) this).emi_discovery$getRecipe();
    if (recipe instanceof EmiTagRecipe) Constants.LOG.info("Poo");
    return !KnownItems.isKnown(ingredient);
  }
}
