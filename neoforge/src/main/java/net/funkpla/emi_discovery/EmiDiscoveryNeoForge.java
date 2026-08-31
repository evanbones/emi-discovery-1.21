package net.funkpla.emi_discovery;

import net.funkpla.emi_discovery.command.DiscoveryCommand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class EmiDiscoveryNeoForge {

    public EmiDiscoveryNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(PacketHandlerNeoForge::register);
        if (FMLEnvironment.dist.isClient()) {
            EmiDiscoveryClientNeoForge.init(modBus, modContainer);
        }
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        CommonClass.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DiscoveryCommand.register(event.getDispatcher());
    }
}
