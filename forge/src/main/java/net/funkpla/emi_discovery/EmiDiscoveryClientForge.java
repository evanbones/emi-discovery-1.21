package net.funkpla.emi_discovery;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class EmiDiscoveryClientForge {

  public static void init(IEventBus bus) {
    MinecraftForge.EVENT_BUS.addListener(EmiDiscoveryClientForge::joinWorld);
    MinecraftForge.EVENT_BUS.addListener(EmiDiscoveryClientForge::leaveWorld);
  }

  static void joinWorld(ClientPlayerNetworkEvent.LoggingIn event) {
    EmiDiscoveryClient.joinWorld();
  }

  static void leaveWorld(ClientPlayerNetworkEvent.LoggingOut event) {
    EmiDiscoveryClient.leaveWorld();
  }
}
