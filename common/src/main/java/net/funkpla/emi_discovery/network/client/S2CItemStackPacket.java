package net.funkpla.emi_discovery.network.client;

import dev.emi.emi.screen.EmiScreenManager;
import net.funkpla.emi_discovery.KnownItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record S2CItemStackPacket(ItemStack stack) implements S2CModPacket {

  public S2CItemStackPacket(FriendlyByteBuf buf) {
    this(buf.readItem());
  }

  @Override
  public void handleClient() {
    KnownItems.addKnown(stack);
    //Fake a search update to update the index view.
    EmiScreenManager.search.update();
  }

  @Override
  public void write(FriendlyByteBuf to) {
    to.writeItem(stack);
  }
}
