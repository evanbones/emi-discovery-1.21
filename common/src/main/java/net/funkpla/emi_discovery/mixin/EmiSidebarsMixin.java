package net.funkpla.emi_discovery.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiSidebars;
import net.funkpla.emi_discovery.KnownItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;



@Mixin(EmiSidebars.class)
public class EmiSidebarsMixin {
    @Unique
    private static final int emi_discovery$GETSTATIC = 178;
    @WrapOperation(
            method="getStacks",
            remap = false,
            at=@At(value="FIELD",  opcode = emi_discovery$GETSTATIC,
                    target= "Ldev/emi/emi/registry/EmiStackList;filteredStacks:Ljava/util/List;"
    ))
    private static List<EmiStack> filterFiltered(Operation<List<EmiStack>> operation){
        return EmiStackList.filteredStacks.stream().filter(stack->
            KnownItems.isKnown(stack.getItemStack())
        ).toList();
    }
}


