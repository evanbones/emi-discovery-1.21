package net.funkpla.emi_discovery.network.server;

import net.funkpla.emi_discovery.network.ModPacket;
import net.minecraft.server.level.ServerPlayer;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
