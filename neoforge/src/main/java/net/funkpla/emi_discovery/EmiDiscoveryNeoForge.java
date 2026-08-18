package net.funkpla.emi_discovery;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class EmiDiscoveryNeoForge {

  public EmiDiscoveryNeoForge(IEventBus modBus, ModContainer modContainer) {
    modBus.addListener(PacketHandlerNeoForge::register);
    if (FMLEnvironment.dist.isClient()) {
      EmiDiscoveryClientNeoForge.init(modBus, modContainer);
    }
    CommonClass.init();
  }
}
