package net.funkpla.emi_discovery;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.io.IOUtils;

public class KnownItems {
  private static final Set<Item> knownItems = new HashSet<>();
  private static final File PRE_DISCOVERED = new File("config/dei_pre_discovered.json");
  private static final Gson gson = new Gson();

  public static void clear() {
    knownItems.clear();
  }

  public static boolean isKnown(ItemStack stack) {
    return knownItems.contains(stack.getItem());
  }

  public static void addKnown(ItemStack stack) {
    if (knownItems.add(stack.getItem())) {
      saveToDisk();
    }
  }

  public static void loadFromDisk() {
    clear();

    new File("dei/").mkdirs(); // make sure the folder exists

    Path discoveredPath = Path.of("dei", getWorldName().replace('/', '_') + ".json");
    File worldDiscovered = discoveredPath.toFile();

    if (!worldDiscovered.exists() && PRE_DISCOVERED.exists()) { // add pre discovered entries
      try {
        Path path = worldDiscovered.toPath();
        Files.copy(PRE_DISCOVERED.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

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

  public static void saveToDisk() {
    Path discoveredPath = Path.of("dei", getWorldName().replace('/', '_') + ".json");
    File worldDiscovered = discoveredPath.toFile();
    JsonWriter writer = null;
    try {
      writer = gson.newJsonWriter(new FileWriter(worldDiscovered));
      writer.setIndent("    ");
      gson.toJson(discoveredToJson(), writer);
    } catch (Exception e) {
      Constants.LOG.error("Couldn't save discovered");
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  public static String getWorldName() {
    Minecraft minecraft = Minecraft.getInstance();
    ServerData serverData = minecraft.getCurrentServer();
    if (serverData != null) {
      return serverData.name;
    } else {
      IntegratedServer integratedServer = minecraft.getSingleplayerServer();
      return integratedServer.getMotd();
    }
  }
}
