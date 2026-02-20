package net.funkpla.emi_discovery.mixin.emi.accessor;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "dev.emi.emi.api.recipe.EmiIngredientRecipe$PageSlotWidget")
public interface PageSlotWidgetAccessor {
  @Invoker(value = "getRecipe", remap = false)
  EmiRecipe emi_discovery$getRecipe();

  @Invoker(value = "getStack", remap = false)
  EmiIngredient emi_discovery$getIngredientStack();
}
