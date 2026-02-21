package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.screen.RecipeScreen;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeScreen.class)
public class RecipeScreenMixin {
  /**
   * Intercept the call to Map.entrySet() to filter out recipes with unknown ingredients before
   * building recipe page tabs.
   *
   * @param recipeCategoryListMap the entry set of the category -> recipe list map
   * @param original the original operation (unused)
   * @return a filtered copy of the original entry set
   */
  @WrapOperation(
      method = "init",
      at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
  private Set<Map.Entry<EmiRecipeCategory, List<EmiRecipe>>> filterRecipes(
      Map<EmiRecipeCategory, List<EmiRecipe>> recipeCategoryListMap,
      Operation<Set<Map.Entry<EmiRecipeCategory, List<EmiRecipe>>>> original) {
    return KnownItems.filterRecipes(recipeCategoryListMap);
  }

  /**
   * Intercept the call to getWorkstations() to filter out unknown workstations from the recipe
   * display.
   *
   * @param recipeManager the recipe manager to get the workstations
   * @param emiRecipeCategory the category to filter
   * @param original original operation (unused)
   * @return a filtered list of workstations
   */
  @WrapOperation(
      remap = false,
      method = "setPage",
      at =
          @At(
              target =
                  "Ldev/emi/emi/api/recipe/EmiRecipeManager;getWorkstations(Ldev/emi/emi/api/recipe/EmiRecipeCategory;)Ljava/util/List;",
              value = "INVOKE"))
  public List<EmiIngredient> filterUnknownWorkstations(
      EmiRecipeManager recipeManager,
      EmiRecipeCategory emiRecipeCategory,
      Operation<List<EmiIngredient>> original) {

    List<EmiIngredient> workstations = recipeManager.getWorkstations(emiRecipeCategory);
    return workstations.stream().filter(KnownItems::isKnown).toList();
  }
}
