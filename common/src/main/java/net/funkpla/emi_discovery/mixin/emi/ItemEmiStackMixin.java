package net.funkpla.emi_discovery.mixin.emi;

import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.ItemEmiStack;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.Component;
import java.util.ArrayList;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.runtime.EmiDrawContext;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemEmiStack.class)
public class ItemEmiStackMixin {

  /** Suppress original numeric amount rendering when blackout is enabled and stack is undiscovered */
  @WrapOperation(
      remap = false,
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target = "Ldev/emi/emi/EmiRenderHelper;renderAmount(Ldev/emi/emi/runtime/EmiDrawContext;IILnet/minecraft/network/chat/Component;)V"))
  private void suppressRenderAmount(
      EmiDrawContext context, int x, int y, Component amount, Operation<Void> original) {
    if (KnownItems.shouldBlackoutRecipes()
        && !KnownItems.isKnown((ItemEmiStack) (Object) this)) {
      return;
    }
    original.call(context, x, y, amount);
  }
}
