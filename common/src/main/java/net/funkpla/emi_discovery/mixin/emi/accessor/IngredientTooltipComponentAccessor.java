package net.funkpla.emi_discovery.mixin.emi.accessor;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.screen.tooltip.IngredientTooltipComponent;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(IngredientTooltipComponent.class)
public interface IngredientTooltipComponentAccessor {
  @Accessor(value ="ingredients", remap = false)
  List<EmiStack> getIngredients();
}
