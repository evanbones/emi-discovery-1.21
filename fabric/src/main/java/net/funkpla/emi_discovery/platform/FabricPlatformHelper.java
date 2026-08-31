package net.funkpla.emi_discovery.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.funkpla.emi_discovery.network.ModPacket;
import net.funkpla.emi_discovery.network.ModPayload;
import net.funkpla.emi_discovery.network.PacketHandler;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.funkpla.emi_discovery.platform.services.IPlatformHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    private static <MSG extends ModPacket> StreamCodec<FriendlyByteBuf, ModPayload<MSG>> streamCodec(
            Function<FriendlyByteBuf, MSG> reader) {
        return StreamCodec.ofMember(
                (payload, buf) -> payload.msg().write(buf), buf -> new ModPayload<>(reader.apply(buf)));
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(
            Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        CustomPacketPayload.Type<ModPayload<MSG>> type =
                new CustomPacketPayload.Type<>(PacketHandler.packet(packetLocation));
        PayloadTypeRegistry.playS2C().register(type, streamCodec(reader));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(
                    type, (payload, context) -> context.client().execute(payload.msg()::handleClient));
        }
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(
            Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        CustomPacketPayload.Type<ModPayload<MSG>> type =
                new CustomPacketPayload.Type<>(PacketHandler.packet(packetLocation));
        PayloadTypeRegistry.playC2S().register(type, streamCodec(reader));
        ServerPlayNetworking.registerGlobalReceiver(
                type,
                (payload, context) ->
                        context.server().execute(() -> payload.msg().handleServer(context.player())));
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        ServerPlayNetworking.send(player, new ModPayload<>(msg));
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        ClientPlayNetworking.send(new ModPayload<>(msg));
    }
}
