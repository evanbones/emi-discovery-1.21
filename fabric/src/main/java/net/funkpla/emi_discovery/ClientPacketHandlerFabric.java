package net.funkpla.emi_discovery;

import java.util.function.Function;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.funkpla.emi_discovery.network.PacketHandler;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class ClientPacketHandlerFabric {

    public static <MSG extends S2CModPacket> void register(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        ClientPlayNetworking.registerGlobalReceiver(PacketHandler.packet(packetLocation), wrapS2C(reader));
    }

    public static <MSG extends S2CModPacket> ClientPlayNetworking.PlayChannelHandler wrapS2C(Function<FriendlyByteBuf,MSG> decodeFunction) {
        return new ClientHandler<>(decodeFunction);
    }


    public record ClientHandler<MSG extends S2CModPacket>(Function<FriendlyByteBuf, MSG> decodeFunction) implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            MSG decode = decodeFunction.apply(buf);
            client.execute(decode::handleClient);
        }
    }
}
