package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.recipe.EmiIngredientRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.recipe.EmiTagRecipe;
import java.util.ArrayList;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import net.funkpla.emi_discovery.mixin.emi.accessor.EmiTagRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmiIngredientRecipe.class)
public class EmiIngredientRecipeMixin {
  @SuppressWarnings("UnstableApiUsage")
  @WrapOperation(
      remap = false,
      method = "getInputs",
      at =
          @At(
              target = "Ldev/emi/emi/api/recipe/EmiIngredientRecipe;getStacks()Ljava/util/List;",
              value = "INVOKE"))
  private List<EmiIngredient> filterInputs(
      EmiIngredientRecipe instance, Operation<List<EmiIngredient>> original) {
    if (instance instanceof EmiTagRecipe tagRecipe) {
      List<EmiIngredient> emiIngredients = new ArrayList<>();
      emiIngredients.add(new ListEmiIngredient(((EmiTagRecipeAccessor) tagRecipe).getStacks(), 1L));
      return emiIngredients.stream().filter(KnownItems::isKnown).toList();
    }
    return original.call(instance);
  }
}
