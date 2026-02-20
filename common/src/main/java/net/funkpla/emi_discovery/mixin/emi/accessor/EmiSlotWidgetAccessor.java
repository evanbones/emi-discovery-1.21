package net.funkpla.emi_discovery.mixin.emi.accessor;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlotWidget.class)
public interface EmiSlotWidgetAccessor {
  @Accessor(value = "recipe", remap = false)
  EmiRecipe getRecipe();

  @Accessor(value = "stack", remap = false)
  EmiIngredient getIngredientStack();
}
