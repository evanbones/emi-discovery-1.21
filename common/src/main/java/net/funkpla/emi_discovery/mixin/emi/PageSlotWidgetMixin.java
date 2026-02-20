package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiIngredient;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "dev.emi.emi.api.recipe.EmiIngredientRecipe$PageSlotWidget")
public abstract class PageSlotWidgetMixin {

  /**
   * Finagle our way into isEmpty(), used to filter out empty recipes, so we can also remove recipes
   * with unknown ingredients.
   *
   * @param ingredient the ingredient to test
   * @param original original operation (unused)
   * @return false if none of the items in the ingredient are known
   */
  @WrapOperation(
      method = "render",
      at = @At(value = "INVOKE", target = "Ldev/emi/emi/api/stack/EmiIngredient;isEmpty()Z"))
  private boolean filterPageSlots(EmiIngredient ingredient, Operation<Boolean> original) {
    return !KnownItems.isKnown(ingredient);
  }
}
