package net.funkpla.emi_discovery.mixin.emi.accessor;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("UnstableApiUsage")
@Mixin(TagEmiIngredient.class)
public interface TagEmiIngredientAccessor {
  @Accessor(value ="stacks", remap = false)
  List<EmiStack> getStacks();
}
