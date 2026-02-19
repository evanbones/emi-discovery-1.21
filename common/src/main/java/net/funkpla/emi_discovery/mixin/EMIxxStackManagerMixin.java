package net.funkpla.emi_discovery.mixin;

import concerrox.emixx.content.StackManager;
import concerrox.emixx.content.stackgroup.EmiGroupStack;
import dev.emi.emi.api.stack.EmiStack;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StackManager.class)
public class EMIxxStackManagerMixin {

  @Unique
  private List<EmiStack> getFilteredStacks() {
    return ((EMIxxStackManagerAccessor) this)
        .getInternalDisplayedStacks().stream()
            .filter(
                stack -> {
                  if (stack instanceof EmiGroupStack groupStack) {
                    if (groupStack.getItems().stream()
                        .filter(item -> KnownItems.isKnown(item.getRealStack().getItemStack()))
                        .toList()
                        .isEmpty()) {
                      return false;
                    }
                  }
                  return KnownItems.isKnown(stack.getItemStack());
                })
            .toList();
  }

  @Inject(
      remap = false,
      method = "getDisplayedStacks$emixx_common",
      at = @At("HEAD"),
      cancellable = true)
  private void filterStacks(CallbackInfoReturnable<List<EmiStack>> cir) {
    cir.setReturnValue(getFilteredStacks());
  }
}
