package net.funkpla.emi_discovery.mixin.emixx;

import concerrox.emixx.content.StackManager;
import dev.emi.emi.api.stack.EmiStack;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StackManager.class)
public interface EMIxxStackManagerAccessor {
  /** Allow access to the private internal value of displayedStacks. Bah, Kotlin! */
  @Accessor(value = "displayedStacks", remap = false)
  List<EmiStack> getInternalDisplayedStacks();

  @Accessor(value = "displayedStacks", remap = false)
  void setInternalDisplayedStacks(List<EmiStack> stacks);
}
