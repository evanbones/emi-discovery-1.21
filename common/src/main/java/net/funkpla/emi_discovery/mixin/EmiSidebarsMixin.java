package net.funkpla.emi_discovery.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiSidebars;
import java.util.List;
import net.funkpla.emi_discovery.KnownItems;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmiSidebars.class)
public class EmiSidebarsMixin {
  @WrapOperation(
      remap = false,
      method = "getStacks",
      at =
          @At(
              value = "FIELD",
              opcode = Opcodes.GETSTATIC,
              target = "Ldev/emi/emi/registry/EmiStackList;filteredStacks:Ljava/util/List;"))
  private static List<EmiStack> filterFiltered(Operation<List<EmiStack>> operation) {
    return EmiStackList.filteredStacks.stream()
        .filter(stack -> KnownItems.isKnown(stack.getItemStack()))
        .toList();
  }
}
