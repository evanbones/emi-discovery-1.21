package net.funkpla.emi_discovery.mixin.emixx;

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

  /**
   * Get the internal list of displayed stacks from the StackManager and filter out stacks with no
   * known items.
   *
   * @return the filtered list
   */
  @Unique
  private synchronized List<EmiStack> getFilteredStacks() {
    return ((EMIxxStackManagerAccessor) this)
        .getInternalDisplayedStacks().stream()
            .filter(
                emiStack -> {
                  if (emiStack instanceof EmiGroupStack groupStack)
                    return groupStack.getItems().stream()
                        .anyMatch(item -> KnownItems.isKnown(item.getRealStack()));
                  return KnownItems.isKnown(emiStack);
                })
            .toList();
  }

  /**
   * Replace the return value of StackManager.displayedStacks with a list filtered for known items.
   *
   * @param returnable to set the return value
   */
  @Inject(
      remap = false,
      method = "getDisplayedStacks$emixx_common",
      at = @At("HEAD"),
      cancellable = true)
  private void filterStacks(CallbackInfoReturnable<List<EmiStack>> returnable) {
    returnable.setReturnValue(getFilteredStacks());
  }
}
