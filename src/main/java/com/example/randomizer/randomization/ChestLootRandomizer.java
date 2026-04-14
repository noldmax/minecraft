package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates and stores the per-world chest loot-table mapping.
 * All loot tables whose path begins with "chests/" are shuffled together
 * so every chest type yields loot from a different (but consistent) chest.
 * Same seed = same shuffle.
 */
public final class ChestLootRandomizer {

    private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mapping = new HashMap<>();

    public static void initialize(MinecraftServer server, long seed) {
        mapping.clear();

        // ── Enumerate chest loot tables ───────────────────────────────────────
        // In MC 1.21.11, ResourceKey.location() was renamed to identifier().
        List<ResourceKey<LootTable>> pool = new ArrayList<>();
        try {
            var registryLookup = server.registryAccess().lookup(Registries.LOOT_TABLE);
            if (registryLookup.isEmpty()) {
                RandomizerMod.LOGGER.warn("[Randomizer] Chest: LOOT_TABLE registry not found in registryAccess!");
            } else {
                var reg = registryLookup.get();
                reg.listElementIds()
                   .filter(key -> key.identifier().toString().contains("chests/"))
                   .sorted(Comparator.comparing((ResourceKey<LootTable> key) -> key.identifier().toString()))
                   .forEach(pool::add);
                RandomizerMod.LOGGER.info("[Randomizer] Chest: found {} chest loot table(s)", pool.size());
                if (pool.isEmpty()) {
                    // Log samples to diagnose filter mismatch
                    RandomizerMod.LOGGER.info("[Randomizer/Probe] Sample loot table identifiers:");
                    reg.listElementIds().limit(10).forEach(k ->
                        RandomizerMod.LOGGER.info("[Randomizer/Probe]   {}", k.identifier()));
                }
            }
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer] Could not enumerate chest loot tables: {}", e.getMessage(), e);
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

        // ── Probe: discover correct method names ──────────────────────────────
        // Remove once confirmed.
        RandomizerMod.LOGGER.info("[Randomizer/Probe] === ResourceKey public methods ===");
        for (Method m : ResourceKey.class.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isPublic(m.getModifiers())) {
                RandomizerMod.LOGGER.info("[Randomizer/Probe] ResourceKey.{}({}) -> {}",
                        m.getName(),
                        Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                              .collect(Collectors.joining(", ")),
                        m.getReturnType().getSimpleName());
            }
        }
        RandomizerMod.LOGGER.info("[Randomizer/Probe] === ALL RandomizableContainerBlockEntity declared methods ===");
        for (Method m : RandomizableContainerBlockEntity.class.getDeclaredMethods()) {
            RandomizerMod.LOGGER.info("[Randomizer/Probe] RCBE.{}({}) -> {}",
                    m.getName(),
                    Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                          .collect(Collectors.joining(", ")),
                    m.getReturnType().getSimpleName());
        }
    }

    public static ResourceKey<LootTable> getMappedKey(ResourceKey<LootTable> original) {
        return mapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !mapping.isEmpty();
    }
}
