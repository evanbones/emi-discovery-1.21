package net.funkpla.emi_discovery.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerStorageSourceAccessor {
  /**
   * Allow access to the private storageSource so we can get the unique path to the save directory.
   */
  @Accessor(value = "storageSource")
  LevelStorageSource.LevelStorageAccess getStorageSource();
}
