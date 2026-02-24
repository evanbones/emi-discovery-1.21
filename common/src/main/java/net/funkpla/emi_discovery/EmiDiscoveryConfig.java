package net.funkpla.emi_discovery;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Constants.MOD_ID)
public class EmiDiscoveryConfig implements ConfigData {
    @ConfigEntry.Category("main")
    public boolean displayCraftableInIndex = false;
}