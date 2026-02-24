package net.funkpla.emi_discovery.mixin.emixx;

import concerrox.emixx.content.StackManager;
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
        .getInternalDisplayedStacks().stream().filter(KnownItems::shouldStackDisplay).toList();
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
