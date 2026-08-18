package net.funkpla.emi_discovery.mixin.remi;

import com.evandev.remi.integration.emi.StackManager;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.emi.api.stack.EmiStack;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(StackManager.class)
public class StackManagerMixin {
    /**
     * Filter unknown items out of REMI's displayed-stack list (pretty clean eh?).
     */
    @ModifyReturnValue(method = "buildDisplayedStacks", at = @At("RETURN"))
    private static List<EmiStack> filterDisplayedStacks(List<EmiStack> original) {
        return original.stream().filter(KnownItems::shouldStackDisplay).toList();
    }
}
