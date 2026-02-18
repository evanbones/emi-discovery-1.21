package net.funkpla.emi_discovery.mixin;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiTagRecipe;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EmiTagRecipe.class)
public class EmiTagRecipeMixin {
  @Final @Shadow private List<EmiStack> stacks;

  @Inject(
      method = "getStacks()Ljava/util/List;",
      at = @At(value = "HEAD"), cancellable = true, remap=false)
  protected void bob(CallbackInfoReturnable<List<EmiStack>> cir) {
    cir.setReturnValue(
        stacks.stream().filter(stack -> KnownItems.isKnown(stack.getItemStack())).toList());
  }
}
