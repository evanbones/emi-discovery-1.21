package net.funkpla.emi_discovery;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandlerForge {

  public static SimpleChannel INSTANCE =
      NetworkRegistry.newSimpleChannel(
          CommonClass.locate("packet"), () -> "1.0", s -> true, s -> true);

  public static <MSG extends S2CModPacket>
      BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapS2C() {
    return ((msg, contextSupplier) -> {
      contextSupplier.get().enqueueWork(msg::handleClient);
      contextSupplier.get().setPacketHandled(true);
    });
  }

  public static <MSG extends C2SModPacket>
      BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapC2S() {
    return ((msg, contextSupplier) -> {
      ServerPlayer player = contextSupplier.get().getSender();
      contextSupplier.get().enqueueWork(() -> msg.handleServer(player));
      contextSupplier.get().setPacketHandled(true);
    });
  }

  public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
    INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static <MSG> void sendToServer(MSG packet) {
    INSTANCE.sendToServer(packet);
  }
}
