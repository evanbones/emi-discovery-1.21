package net.funkpla.emi_discovery.mixin;


import net.funkpla.emi_discovery.EmiDiscoveryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixinFabric {

  @Inject(
      method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
  private void handleLogin(Screen screen, boolean keepResourcePacks, CallbackInfo ci) {
    EmiDiscoveryClient.leaveWorld();
  }
}
