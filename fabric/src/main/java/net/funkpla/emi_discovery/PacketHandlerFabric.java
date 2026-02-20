package net.funkpla.emi_discovery;

import java.util.function.Function;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PacketHandlerFabric {
  public static <MSG extends C2SModPacket> ServerPlayNetworking.PlayChannelHandler wrapC2S(
      Function<FriendlyByteBuf, MSG> decodeFunction) {
    return new ServerHandler<>(decodeFunction);
  }

  public record ServerHandler<MSG extends C2SModPacket>(
      Function<FriendlyByteBuf, MSG> packetDecoder)
      implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(
        MinecraftServer server,
        ServerPlayer player,
        ServerGamePacketListenerImpl handler,
        FriendlyByteBuf buf,
        PacketSender responseSender) {
      MSG decode = packetDecoder.apply(buf);
      server.execute(() -> decode.handleServer(player));
    }
  }
}
