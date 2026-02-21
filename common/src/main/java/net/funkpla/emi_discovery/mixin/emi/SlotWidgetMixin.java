package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlotWidget.class)
public class SlotWidgetMixin {
  /** Wrap the drawSlotHighlight method so that we can override it in subclasses. */
  @WrapMethod(method = "drawSlotHighlight")
  protected void overrideDrawSlotHighlight(
      GuiGraphics draw, Bounds bounds, Operation<Void> original) {
    original.call(draw, bounds);
  }
}
