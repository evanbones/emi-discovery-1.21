package net.funkpla.emi_discovery.mixin;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiTagRecipe;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EmiTagRecipe.class)
public class EmiTagRecipeMixin {
    @Shadow
    @Final
    private List<EmiStack> stacks;

    @Inject(
      remap = false,
      method = "getStacks()Ljava/util/List;",
      at = @At(value = "HEAD"),
      cancellable = true)
  protected void pruneStacks(CallbackInfoReturnable<List<EmiStack>> cir) {
    cir.setReturnValue(
       this.stacks.stream().filter(stack -> KnownItems.isKnown(stack.getItemStack())).toList());
  }
}
