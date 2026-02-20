package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.screen.RecipeScreen;
import dev.emi.emi.screen.RecipeTab;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeScreen.class)
public class RecipeScreenMixin {
  @WrapOperation(
      remap = false,
      method = "init",
      at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
  private Set<Map.Entry<EmiRecipeCategory, List<EmiRecipe>>> filterRecipes(
      Map<EmiRecipeCategory, List<EmiRecipe>> recipeCategoryListMap,
      Operation<Set<Map.Entry<EmiRecipeCategory, List<EmiRecipe>>>> original) {
    return KnownItems.filterRecipes(recipeCategoryListMap);
  }

  @WrapOperation(
      remap = false,
      method = "setPage",
      at =
          @At(
              target =
                  "Ldev/emi/emi/api/recipe/EmiRecipeManager;getWorkstations(Ldev/emi/emi/api/recipe/EmiRecipeCategory;)Ljava/util/List;",
              value = "INVOKE"))
  public List<EmiIngredient> filterUnknownWorkstations(
      EmiRecipeManager instance,
      EmiRecipeCategory emiRecipeCategory,
      Operation<List<EmiIngredient>> original,
      @Local(name = "tab") RecipeTab tab) {

    List<EmiIngredient> workstations = EmiApi.getRecipeManager().getWorkstations(tab.category);
    return workstations.stream().filter(KnownItems::isKnown).toList();
  }
}
