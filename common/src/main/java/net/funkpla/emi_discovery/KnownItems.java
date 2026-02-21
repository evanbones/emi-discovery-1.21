package net.funkpla.emi_discovery;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.authlib.GameProfile;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.funkpla.emi_discovery.mixin.MinecraftServerStorageSourceAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.IOUtils;

public class KnownItems {
  private static final Set<Item> knownItems = new HashSet<>();
  private static final File PRE_DISCOVERED =
      new File("config", "emi_discovery_pre_discovered.json");
  private static final Path DATA_DIR = Path.of(".","emi_discovery");

  private static final Gson gson = new Gson();

  public static void clear() {
    knownItems.clear();
  }

  public static boolean isKnown(ItemStack stack) {
    return knownItems.contains(stack.getItem());
  }

  public static boolean isKnown(EmiStack stack) {
    return isKnown(stack.getItemStack());
  }

  public static boolean isKnown(EmiIngredient ingredient) {
    return ingredient.getEmiStacks().stream().anyMatch(KnownItems::isKnown);
  }

  public static boolean areAllKnown(EmiIngredient ingredient) {
    return ingredient.getEmiStacks().stream().allMatch(KnownItems::isKnown);
  }

  public static boolean areAllKnown(EmiRecipe recipe) {
    return recipe.getInputs().stream().allMatch(KnownItems::isKnown);
  }

  public static Set<Map.Entry<EmiRecipeCategory, List<EmiRecipe>>> filterRecipes(
      Map<EmiRecipeCategory, List<EmiRecipe>> categoryListMap) {

    return categoryListMap.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    entry.getValue().stream()
                        .filter(
                            emiRecipe ->
                                emiRecipe.getInputs().stream().allMatch(KnownItems::isKnown))
                        .toList()))
        .entrySet();
  }

  public static void addKnown(ItemStack stack) {
    if (knownItems.add(stack.getItem())) {
      saveToDisk();
    }
  }

  public static void loadFromDisk() {
    clear();
    if (DATA_DIR.toFile().exists() && !DATA_DIR.toFile().mkdirs()) {
      throw new RuntimeException("Could not create data directory.");
    }

    File worldDiscovered = getKnownItemsFile();

    if (!worldDiscovered.exists() && PRE_DISCOVERED.exists()) { // add pre discovered entries
      try {
        Files.copy(
            PRE_DISCOVERED.toPath(), worldDiscovered.toPath(), StandardCopyOption.REPLACE_EXISTING);

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    if (worldDiscovered.exists()) { // load existing discoveries
      Reader reader = null;
      try {
        reader = new FileReader(worldDiscovered);
        JsonReader jsonReader = new JsonReader(reader);
        Constants.LOG.info("Loading existing discoveries");
        JsonArray json = gson.fromJson(jsonReader, JsonArray.class);

        for (JsonElement element : json) {
          knownItems.add(BuiltInRegistries.ITEM.get(new ResourceLocation(element.getAsString())));
        }

      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        IOUtils.closeQuietly(reader);
      }
    }

    /*
    if (TOOLTIPS.exists()) {
        Reader reader = null;
        try {
            reader = new FileReader(TOOLTIPS);
            JsonReader jsonReader = new JsonReader(reader);
            DiscoveredEnoughItems.LOG.info("Loading tooltips");
            JsonObject json = gson.fromJson(jsonReader, JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(entry.getKey()));
                Component component = Component.Serializer.fromJson(entry.getValue().getAsString());
                tooltips.put(item, component);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
     */
  }

  static JsonArray discoveredToJson() {
    JsonArray array = new JsonArray();
    for (Item item : knownItems) {
      array.add(BuiltInRegistries.ITEM.getKey(item).toString());
    }
    return array;
  }

  public static Path getKnownItemsPath() {
    return DATA_DIR.resolve(getWorldName().replace('/', '_') + ".json");
  }

  public static File getKnownItemsFile() {
    return getKnownItemsPath().toFile();
  }

  public static void saveToDisk() {
    JsonWriter writer = null;
    try {
      writer = gson.newJsonWriter(new FileWriter(getKnownItemsFile()));
      writer.setIndent("    ");
      gson.toJson(discoveredToJson(), writer);
    } catch (Exception e) {
      Constants.LOG.error("Couldn't save discovered");
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  public static Path getDataDir() {
    Minecraft client = Minecraft.getInstance();
    if (client.isLocalServer() && client.getSingleplayerServer() != null) {
      IntegratedServer server = client.getSingleplayerServer();
      return server.getWorldPath(LevelResource.ROOT).normalize().toAbsolutePath().resolve(DATA_DIR);
    } else if (client.getCurrentServer() != null) {
      return DATA_DIR.resolve(client.getCurrentServer().name);
    }
    return DATA_DIR;
  }

  public static String getWorldName() {
    Minecraft client = Minecraft.getInstance();
    if (client.isLocalServer() && client.getSingleplayerServer() != null) {
      IntegratedServer server = client.getSingleplayerServer();
      GameProfile profile = server.getSingleplayerProfile();
      String levelId =
          ((MinecraftServerStorageSourceAccessor) server).getStorageSource().getLevelId();
      return profile != null ? profile.getName() + " - " + levelId : levelId;
    } else {
      ServerData serverdata = client.getCurrentServer();
      return serverdata != null ? serverdata.name : "unknown";
    }
  }
}
