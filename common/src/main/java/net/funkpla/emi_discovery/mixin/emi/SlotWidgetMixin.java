package net.funkpla.emi_discovery.mixin.emi;

import dev.emi.emi.api.widget.SlotWidget;
import net.funkpla.emi_discovery.KnownItems;
import net.funkpla.emi_discovery.mixin.emi.accessor.EmiSlotWidgetAccessor;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotWidget.class)
public class SlotWidgetMixin {

  @Inject(method = "drawStack", at = @At("HEAD"), cancellable = true)
  private void maybeDrawStack(
      GuiGraphics draw, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    var stack = ((EmiSlotWidgetAccessor) this).getIngredientStack();

    if (stack.getEmiStacks().size() == 1
        && !KnownItems.isKnown(stack.getEmiStacks().get(0).getItemStack())) {
      ci.cancel();
    }
  }
}
