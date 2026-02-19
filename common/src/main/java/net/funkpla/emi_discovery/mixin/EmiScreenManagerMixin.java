package net.funkpla.emi_discovery.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.screen.EmiScreenManager;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmiScreenManager.class)
public class EmiScreenManagerMixin {
  @WrapOperation(
      remap = false,
      method =
          "stackInteraction(Ldev/emi/emi/api/stack/EmiStackInteraction;Ljava/util/function/Function;)Z",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Ldev/emi/emi/api/EmiApi;displayUses(Ldev/emi/emi/api/stack/EmiIngredient;)V"))
  private static void stopUsages(EmiIngredient fav, Operation<Void> original) {
    List<EmiStack> stacks = fav.getEmiStacks();
    if (!(stacks.size() == 1 && !KnownItems.isKnown(stacks.get(0).getItemStack()))) {
      original.call(fav);
    }
  }

  @WrapOperation(
      remap = false,
      method =
          "stackInteraction(Ldev/emi/emi/api/stack/EmiStackInteraction;Ljava/util/function/Function;)Z",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Ldev/emi/emi/api/EmiApi;displayRecipes(Ldev/emi/emi/api/stack/EmiIngredient;)V"))
  private static void stopRecipes(EmiIngredient fav, Operation<Void> original) {
    List<EmiStack> stacks = fav.getEmiStacks();
    if (!(stacks.size() == 1 && !KnownItems.isKnown(stacks.get(0).getItemStack()))) {
      original.call(fav);
    }
  }
  /*
   @WrapOperation(
       method = "renderCurrentTooltip",
       at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z"))
   private static boolean interceptAddTooltip(
       List<ClientTooltipComponent> instance,
       Collection<? extends ClientTooltipComponent> es,
       Operation<Boolean> original,
       @Local(name = "hov") EmiIngredient hov) {
     var stacks = hov.getEmiStacks();
     if (stacks.size() == 1
         && !(stacks.get(0).getItemStack() == ItemStack.EMPTY)
         && !KnownItems.isKnown(stacks.get(0).getItemStack())) {
       instance.clear();
       instance.add(
           ClientTooltipComponent.create(FormattedCharSequence.forward("???", Style.EMPTY)));
       return true;
     } else return original.call(instance, es);
   }
  */
}
