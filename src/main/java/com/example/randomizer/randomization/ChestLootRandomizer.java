package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
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
 * In MC 1.21.11, loot tables are data-driven and are not present in
 * server.registryAccess(). They live in the reloadable registries, accessible
 * via server.reloadableRegistries().lookup() which returns a HolderLookup.Provider.
 */
public final class ChestLootRandomizer {

    private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mapping = new HashMap<>();

    public static void initialize(MinecraftServer server, long seed) {
        mapping.clear();

        List<ResourceKey<LootTable>> pool = new ArrayList<>();
        try {
            server.reloadableRegistries().lookup()
                  .lookup(Registries.LOOT_TABLE)
                  .ifPresent(reg ->
                      reg.listElementIds()
                         .filter(key -> key.toString().contains("chests/"))
                         .sorted(Comparator.comparing(Object::toString))
                         .forEach(pool::add)
                  );
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
