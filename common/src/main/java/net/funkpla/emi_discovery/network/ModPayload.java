package net.funkpla.emi_discovery.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ModPayload<MSG extends ModPacket>(MSG msg) implements CustomPacketPayload {
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return new Type<>(PacketHandler.packet(msg.getClass()));
    }
}
