package net.funkpla.emi_discovery;

import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

public class EmiDiscoveryClientNeoForge {

  public static void init(IEventBus ignoredBus, ModContainer modContainer) {
    NeoForge.EVENT_BUS.addListener(EmiDiscoveryClientNeoForge::joinWorld);
    NeoForge.EVENT_BUS.addListener(EmiDiscoveryClientNeoForge::leaveWorld);
    modContainer.registerExtensionPoint(
        IConfigScreenFactory.class,
        (container, parent) -> AutoConfig.getConfigScreen(EmiDiscoveryConfig.class, parent).get());
  }

  static void joinWorld(ClientPlayerNetworkEvent.LoggingIn event) {
    EmiDiscoveryClient.joinWorld();
  }

  static void leaveWorld(ClientPlayerNetworkEvent.LoggingOut event) {
    EmiDiscoveryClient.leaveWorld();
  }
}
