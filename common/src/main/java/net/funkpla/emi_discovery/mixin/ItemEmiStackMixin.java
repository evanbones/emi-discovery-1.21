package net.funkpla.emi_discovery.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.ItemEmiStack;
import java.util.ArrayList;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEmiStack.class)
public class ItemEmiStackMixin {

  @Shadow @Final private ItemStack stack;

  @Inject(method = "getTooltip", at = @At(value = "HEAD"),cancellable = true, remap = false)
  private void beep(CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
    if (!KnownItems.isKnown(stack)) {
      List<ClientTooltipComponent> list = new ArrayList<>();
      list.add(ClientTooltipComponent.create(FormattedCharSequence.forward("???", Style.EMPTY)));
      cir.setReturnValue(list);
    }
  }

  @Inject(
      method = "renderForBatch",
      at =
          @At(
              target =
                  "Lnet/minecraft/client/Minecraft;getItemRenderer()"
                      + "Lnet/minecraft/client/renderer/entity/ItemRenderer;",
              shift = At.Shift.AFTER,
              value = "INVOKE"),
      cancellable = true)
  private void interceptBatchRender(
      MultiBufferSource vcp,
      GuiGraphics draw,
      int x,
      int y,
      int z,
      float delta,
      CallbackInfo ci,
      @Local(name = "stack") ItemStack stack) {
    if (!KnownItems.isKnown(stack)) {
      ci.cancel();
    }
  }

  @WrapOperation(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/GuiGraphics;renderItem"
                      + "(Lnet/minecraft/world/item/ItemStack;II)V"))
  private void interceptRender(
      GuiGraphics instance, ItemStack stack, int x, int y, Operation<Void> original) {
    if (!KnownItems.isKnown(stack)) {
      instance.fill(x, y, x + 16, y + 16, 0xff000000);
    } else original.call(instance, stack, x, y);
  }
}
