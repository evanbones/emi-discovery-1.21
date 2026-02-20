package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.recipe.EmiTagRecipe;
import net.funkpla.emi_discovery.Constants;
import net.funkpla.emi_discovery.KnownItems;
import net.funkpla.emi_discovery.mixin.emi.accessor.PageSlotWidgetAccessor;
import net.minecraft.client.gui.GuiGraphics;
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
  private boolean blubb(
      EmiIngredient instance,
      Operation<Boolean> original,
      @Local(argsOnly = true) GuiGraphics fish,
      @Local(ordinal = 0, argsOnly = true) int x,
      @Local(ordinal = 1, argsOnly = true) int y) {
    var ingredient = ((PageSlotWidgetAccessor) this).emi_discovery$getIngredientStack();
    var recipe = ((PageSlotWidgetAccessor) this).emi_discovery$getRecipe();
    if (recipe instanceof EmiTagRecipe) Constants.LOG.info("Poo");
    return ingredient.getEmiStacks().size() != 1
        || !KnownItems.isKnown(ingredient.getEmiStacks().get(0).getItemStack());
  }
}
