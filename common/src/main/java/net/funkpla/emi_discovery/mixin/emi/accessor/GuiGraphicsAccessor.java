package net.funkpla.emi_discovery.mixin.emi.accessor;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
	@Invoker("renderTooltipInternal")
	void invokeDrawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y,
                           ClientTooltipPositioner positioner);
}
