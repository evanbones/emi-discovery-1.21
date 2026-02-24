package net.funkpla.emi_discovery;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;

public class EmiDiscoveryClientForge {

  public static void init(IEventBus ignoredBus) {
    MinecraftForge.EVENT_BUS.addListener(EmiDiscoveryClientForge::joinWorld);
    MinecraftForge.EVENT_BUS.addListener(EmiDiscoveryClientForge::leaveWorld);
    ModLoadingContext.get()
        .registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () ->
                new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) ->
                        AutoConfig.getConfigScreen(EmiDiscoveryConfig.class, parent).get()));
  }

  static void joinWorld(ClientPlayerNetworkEvent.LoggingIn event) {
    EmiDiscoveryClient.joinWorld();
  }

  static void leaveWorld(ClientPlayerNetworkEvent.LoggingOut event) {
    EmiDiscoveryClient.leaveWorld();
  }
}
