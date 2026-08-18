package net.funkpla.emi_discovery.mixin;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import net.funkpla.emi_discovery.network.client.S2CItemStackPacket;
import net.funkpla.emi_discovery.platform.Services;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixinNeoForge {

  @Unique private final WeakHashMap<ServerPlayer, Set<Item>> emi_discovery$cache = new WeakHashMap<>();

  @Inject(
      method =
          "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
      at = @At(value = "HEAD"))
  private void sendPacket(
      ServerPlayer pPlayer, Inventory pInventory, ItemStack pStack, CallbackInfo ci) {
    emi_discovery$cache.computeIfAbsent(pPlayer, player -> new HashSet<>());
    if (!emi_discovery$cache.get(pPlayer).contains(pStack.getItem())) {
      Services.PLATFORM.sendToClient(new S2CItemStackPacket(pStack), pPlayer);
      emi_discovery$cache.get(pPlayer).add(pStack.getItem());
    }
  }
}
