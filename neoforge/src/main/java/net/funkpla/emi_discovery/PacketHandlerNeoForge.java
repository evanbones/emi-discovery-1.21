package net.funkpla.emi_discovery;

import net.funkpla.emi_discovery.network.ModPacket;
import net.funkpla.emi_discovery.network.PacketHandler;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PacketHandlerNeoForge {

    private static final List<Runnable> pendingRegistrations = new ArrayList<>();
    private static final PayloadRegistrar[] registrarHolder = new PayloadRegistrar[1];

    public static <MSG extends S2CModPacket> void registerClientPacket(
            Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        pendingRegistrations.add(
                () -> {
                    PayloadRegistrar registrar = registrarHolder[0];
                    CustomPacketPayload.Type<ModPayload<MSG>> type =
                            new CustomPacketPayload.Type<>(PacketHandler.packet(packetClass));
                    registrar.playToClient(
                            type,
                            streamCodec(reader),
                            (payload, context) -> context.enqueueWork(payload.msg()::handleClient));
                });
    }

    public static <MSG extends C2SModPacket> void registerServerPacket(
            Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        pendingRegistrations.add(
                () -> {
                    PayloadRegistrar registrar = registrarHolder[0];
                    CustomPacketPayload.Type<ModPayload<MSG>> type =
                            new CustomPacketPayload.Type<>(PacketHandler.packet(packetClass));
                    registrar.playToServer(
                            type,
                            streamCodec(reader),
                            (payload, context) ->
                                    context.enqueueWork(
                                            () -> payload.msg().handleServer((ServerPlayer) context.player())));
                });
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        registrarHolder[0] = event.registrar("1");
        pendingRegistrations.forEach(Runnable::run);
    }

    private static <MSG extends ModPacket> StreamCodec<FriendlyByteBuf, ModPayload<MSG>> streamCodec(
            Function<FriendlyByteBuf, MSG> reader) {
        return StreamCodec.ofMember(
                (payload, buf) -> payload.msg().write(buf), buf -> new ModPayload<>(reader.apply(buf)));
    }

    public static void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new ModPayload<>(msg));
    }

    public static void sendToServer(C2SModPacket msg) {
        PacketDistributor.sendToServer(new ModPayload<>(msg));
    }

    public record ModPayload<MSG extends ModPacket>(MSG msg) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return new Type<>(PacketHandler.packet(msg.getClass()));
        }
    }
}
