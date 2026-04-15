package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

/**
 * Generates and stores the per-world chest loot-table mapping.
 * All loot tables whose path begins with "chests/" are shuffled together
 * so every chest type yields loot from a different (but consistent) chest.
 * Same seed = same shuffle.
 *
 * In MC 1.21.11 there is no Registry<LootTable> accessible through either
 * registryAccess() or reloadableRegistries(). Instead we enumerate loot table
 * JSON files directly from the server's resource manager and reconstruct the
 * ResourceKeys from the file paths.
 */
public final class ChestLootRandomizer {

    private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mapping = new HashMap<>();

    public static void initialize(MinecraftServer server, long seed) {
        mapping.clear();

        // Chest loot tables live at data/<namespace>/loot_tables/chests/...json
        // listResources("loot_tables/chests", ...) returns identifiers of the form
        // "namespace:loot_tables/chests/name.json". Strip the "loot_tables/" prefix
        // and ".json" suffix to get the actual loot-table key path "chests/name".
        List<ResourceKey<LootTable>> pool = new ArrayList<>();
        try {
            server.getResourceManager()
                  .listResources("loot_tables/chests", id -> id.toString().endsWith(".json"))
                  .keySet()
                  .stream()
                  .sorted(Comparator.comparing(Object::toString))
                  .forEach(id -> {
                      String full = id.toString(); // "namespace:loot_tables/chests/name.json"
                      int colon = full.indexOf(':');
                      String namespace   = full.substring(0, colon);
                      String resourcePath = full.substring(colon + 1); // "loot_tables/chests/name.json"
                      String tablePath   = resourcePath.substring(
                              "loot_tables/".length(),
                              resourcePath.length() - ".json".length()); // "chests/name"
                      pool.add(ResourceKey.create(Registries.LOOT_TABLE,
                               Identifier.fromNamespaceAndPath(namespace, tablePath)));
                  });
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer] Could not enumerate chest loot tables: {}", e.getMessage());
        }

        if (pool.isEmpty()) {
            RandomizerMod.LOGGER.warn("[Randomizer] Chest loot pool is empty — chest randomization disabled");
        } else {
            List<ResourceKey<LootTable>> shuffled = new ArrayList<>(pool);
            Collections.shuffle(shuffled, new Random(seed));
            for (int i = 0; i < pool.size(); i++) {
                mapping.put(pool.get(i), shuffled.get(i));
            }
            RandomizerMod.LOGGER.info("[Randomizer] Chest loot mapping initialized: {} tables, seed {}",
                    pool.size(), seed);
        }
    }

    public static ResourceKey<LootTable> getMappedKey(ResourceKey<LootTable> original) {
        return mapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !mapping.isEmpty();
    }
}
