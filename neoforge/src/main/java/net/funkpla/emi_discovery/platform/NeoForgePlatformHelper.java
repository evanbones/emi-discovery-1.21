package net.funkpla.emi_discovery.platform;

import net.funkpla.emi_discovery.PacketHandlerNeoForge;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.funkpla.emi_discovery.platform.services.IPlatformHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.nio.file.Path;
import java.util.function.Function;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path getGameDir() {
        return FMLLoader.getGamePath();
    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(
            Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerNeoForge.registerClientPacket(packetLocation, reader);
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(
            Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerNeoForge.registerServerPacket(packetLocation, reader);
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerNeoForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerNeoForge.sendToServer(msg);
    }
}
