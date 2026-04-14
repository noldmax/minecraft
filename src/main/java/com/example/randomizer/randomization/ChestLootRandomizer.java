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
        List<ResourceKey<LootTable>> pool = new ArrayList<>();
        try {
            server.registryAccess().lookup(Registries.LOOT_TABLE).ifPresent(reg ->
                reg.listElementIds()
                   .filter(key -> key.location().getPath().startsWith("chests/"))
                   .sorted(Comparator.comparing(key -> key.location().toString()))
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

        // ── Probe: discover the correct unpack method name ────────────────────
        // Remove once the right method is confirmed.
        RandomizerMod.LOGGER.info("[Randomizer/Probe] === RandomizableContainerBlockEntity methods ===");
        for (Method m : RandomizableContainerBlockEntity.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("loot") || n.contains("unpack") || n.contains("load") || n.contains("fill")) {
                RandomizerMod.LOGGER.info("[Randomizer/Probe] RCBE.{}({}) -> {}",
                        m.getName(),
                        Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                              .collect(Collectors.joining(", ")),
                        m.getReturnType().getSimpleName());
            }
        }
    }

    public static ResourceKey<LootTable> getMappedKey(ResourceKey<LootTable> original) {
        return mapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !mapping.isEmpty();
    }
}
