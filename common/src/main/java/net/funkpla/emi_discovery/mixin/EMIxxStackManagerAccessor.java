package net.funkpla.emi_discovery.mixin;

import concerrox.emixx.content.StackManager;
import dev.emi.emi.api.stack.EmiStack;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StackManager.class)
public interface EMIxxStackManagerAccessor {
  @Accessor(value = "displayedStacks", remap = false)
  List<EmiStack> getInternalDisplayedStacks();
}
