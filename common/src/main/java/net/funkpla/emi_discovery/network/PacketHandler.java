package net.funkpla.emi_discovery.network;

import java.util.Locale;

import net.funkpla.emi_discovery.CommonClass;
import net.funkpla.emi_discovery.network.client.S2CItemStackPacket;
import net.funkpla.emi_discovery.platform.Services;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerClientPacket(S2CItemStackPacket.class, S2CItemStackPacket::new);

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return CommonClass.locate(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
