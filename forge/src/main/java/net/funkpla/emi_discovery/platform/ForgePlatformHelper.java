package net.funkpla.emi_discovery.platform;

import java.nio.file.Path;
import java.util.function.Function;
import net.funkpla.emi_discovery.PacketHandlerForge;
import net.funkpla.emi_discovery.network.client.S2CModPacket;
import net.funkpla.emi_discovery.network.server.C2SModPacket;
import net.funkpla.emi_discovery.platform.services.IPlatformHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

  int i;

  @Override
  public String getPlatformName() {

    return "Forge";
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
    PacketHandlerForge.INSTANCE.registerMessage(
        i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
  }

  @Override
  public <MSG extends C2SModPacket> void registerServerPacket(
      Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
    PacketHandlerForge.INSTANCE.registerMessage(
        i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
  }

  @Override
  public void sendToClient(S2CModPacket msg, ServerPlayer player) {
    PacketHandlerForge.sendToClient(msg, player);
  }

  @Override
  public void sendToServer(C2SModPacket msg) {
    PacketHandlerForge.sendToServer(msg);
  }
}
