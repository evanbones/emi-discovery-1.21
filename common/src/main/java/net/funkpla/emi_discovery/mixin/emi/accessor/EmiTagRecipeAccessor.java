package net.funkpla.emi_discovery.mixin.emi.accessor;

import dev.emi.emi.api.stack.EmiStack;
import java.util.List;

import dev.emi.emi.recipe.EmiTagRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EmiTagRecipe.class)
public interface EmiTagRecipeAccessor {
  @Accessor(value ="stacks", remap = false)
  List<EmiStack> getStacks();
}
