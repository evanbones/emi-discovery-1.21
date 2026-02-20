package net.funkpla.emi_discovery;

import net.funkpla.emi_discovery.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;

public class CommonClass {

  public static void init() {
    PacketHandler.registerPackets();
  }

  public static ResourceLocation locate(String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}
