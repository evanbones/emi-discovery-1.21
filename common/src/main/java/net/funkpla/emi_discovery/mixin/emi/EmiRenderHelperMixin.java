package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import net.funkpla.emi_discovery.KnownItems;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmiRenderHelper.class)
public class EmiRenderHelperMixin {
  @WrapOperation(
      method = "renderRecipe",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Ldev/emi/emi/api/widget/Widget;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
  private static void filterDrawnWidgets(
      Widget widget,
      GuiGraphics guiGraphics,
      int x,
      int y,
      float v,
      Operation<Void> original) {
    if (widget instanceof SlotWidget slotWidget) {
      if (!KnownItems.isKnown(slotWidget.getStack())) {
        return;
      }
    }
    original.call(widget, guiGraphics, x, y, v);
  }
}
