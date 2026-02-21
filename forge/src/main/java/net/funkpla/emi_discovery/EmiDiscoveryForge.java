package net.funkpla.emi_discovery;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class EmiDiscoveryForge {

  public EmiDiscoveryForge() {
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    if (FMLEnvironment.dist.isClient()) {
      EmiDiscoveryClientForge.init(bus);
    }
    CommonClass.init();
  }
}
