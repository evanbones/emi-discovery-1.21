package net.funkpla.emi_discovery;

import net.fabricmc.api.ModInitializer;

public class EmiDiscoveryFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    Constants.LOG.info("EmiDiscovery is certainly not breaking things.");
    CommonClass.init();
  }
}
