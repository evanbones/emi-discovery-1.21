package net.funkpla.emi_discovery.network.client;


import net.funkpla.emi_discovery.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}
