package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.recipe.EmiIngredientRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.recipe.EmiTagRecipe;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import net.funkpla.emi_discovery.mixin.emi.accessor.EmiTagRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmiIngredientRecipe.class)
public class EmiIngredientRecipeMixin {
  @WrapOperation(
      remap = false,
      method = "getInputs",
      at =
          @At(
              target = "Ldev/emi/emi/api/recipe/EmiIngredientRecipe;getStacks()Ljava/util/List;",
              value = "INVOKE"))
  private List<EmiIngredient> blurp(
      EmiIngredientRecipe instance, Operation<List<EmiIngredient>> original) {
    if (instance instanceof EmiTagRecipe tagRecipe) {
      List<EmiIngredient> bob =
          List.of(new ListEmiIngredient(((EmiTagRecipeAccessor) tagRecipe).getStacks(), 1L));
      return bob.stream()
          .filter(
              tagIngredient -> {
                if (tagIngredient.getEmiStacks().size() == 1) {
                  return KnownItems.isKnown(tagIngredient.getEmiStacks().get(0).getItemStack());
                }
                return false;
              })
          .toList();
    }
    return original.call(instance);
  }
}
