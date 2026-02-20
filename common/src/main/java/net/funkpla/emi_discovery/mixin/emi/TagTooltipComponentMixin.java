package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.tooltip.TagTooltipComponent;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TagTooltipComponent.class)
public class TagTooltipComponentMixin {

  @WrapOperation(
      remap = false,
      method =
          "drawTooltip(Ldev/emi/emi/runtime/EmiDrawContext;Ldev/emi/emi/screen/tooltip/EmiTooltipComponent$TooltipRenderData;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Ldev/emi/emi/runtime/EmiDrawContext;drawStack"
                      + "(Ldev/emi/emi/api/stack/EmiIngredient;III)V"))
  private void pruneTagTooltip(
      EmiDrawContext instance,
      EmiIngredient stack,
      int x,
      int y,
      int flags,
      Operation<Void> original) {
    if (!KnownItems.isKnown(stack)) instance.fill(x, y, 16, 16, 0x0FFFFFFF);
    else original.call(instance, stack, x, y, flags);
  }

  /*
  @Inject(
      method = "Ldev/emi/emi/screen/tooltip/TagTooltipComponent;getStackWidth()I",
      at = @At("HEAD"),
      remap = false,
      cancellable = true)
  private void fup(CallbackInfoReturnable<Integer> cir) {
    int result;
    int count =
        stacks.stream()
            .filter(
                stack -> {
                  if (stack.getEmiStacks().size() == 1) {
                    return KnownItems.isKnown(stack.getEmiStacks().get(0).getItemStack());
                  }
                  return false;
                })
            .toList()
            .size();
    if (count < 4) {
      result = count;
    } else if (count > 16) {
      result = 8;
    } else {
      result = 4;
    }
    cir.setReturnValue(result);
  }

   */
}
