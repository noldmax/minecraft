package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
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
        // In MC 1.21.11 the LOOT_TABLE registry is data-driven and not present in
        // registryAccess(). We probe for the correct API via reflection and log
        // everything needed to write a proper compile-time call next iteration.
        List<ResourceKey<LootTable>> pool = new ArrayList<>();
        enumerateChestLootTables(server, pool);

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

        // ── Probes ────────────────────────────────────────────────────────────
        probeIdentifierClass();
        probeReloadableRegistries(server, pool);
    }

    @SuppressWarnings("unchecked")
    private static void enumerateChestLootTables(MinecraftServer server, List<ResourceKey<LootTable>> pool) {
        // Attempt 1: registryAccess (works for built-in registries, probably not loot tables)
        try {
            var lookup = server.registryAccess().lookup(Registries.LOOT_TABLE);
            if (lookup.isPresent()) {
                lookup.get().listElementIds()
                      .filter(key -> key.toString().contains("chests/"))
                      .sorted(Comparator.comparing(Object::toString))
                      .forEach(pool::add);
                if (!pool.isEmpty()) {
                    RandomizerMod.LOGGER.info("[Randomizer] Chest: registryAccess found {} entries", pool.size());
                    return;
                }
            }
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer] registryAccess attempt: {}", e.getMessage());
        }

        // Attempt 2: reloadableRegistries (data-driven registries) — via reflection
        // to avoid compile-time dependency on the exact return type.
        try {
            Object reloadable = server.getClass().getMethod("reloadableRegistries").invoke(server);

            // The holder may expose the registry via compositeAccess(), get(), or similar.
            // Try each until we find something that has lookup(ResourceKey).
            for (String accessorName : new String[]{"compositeAccess", "get", "access", "registries"}) {
                try {
                    Method accessor = reloadable.getClass().getMethod(accessorName);
                    Object registryAccess = accessor.invoke(reloadable);
                    Method lookupMethod = registryAccess.getClass().getMethod("lookup", ResourceKey.class);
                    Optional<?> opt = (Optional<?>) lookupMethod.invoke(registryAccess, Registries.LOOT_TABLE);
                    if (opt.isPresent()) {
                        Object reg = opt.get();
                        Method listIds = reg.getClass().getMethod("listElementIds");
                        @SuppressWarnings("rawtypes")
                        java.util.stream.Stream ids = (java.util.stream.Stream) listIds.invoke(reg);
                        ids.filter(key -> key.toString().contains("chests/"))
                           .sorted(Comparator.comparing(Object::toString))
                           .forEach(key -> pool.add((ResourceKey<LootTable>) key));
                        if (!pool.isEmpty()) {
                            RandomizerMod.LOGGER.info("[Randomizer] Chest: reloadableRegistries.{}() found {} entries",
                                    accessorName, pool.size());
                            return;
                        }
                    }
                } catch (NoSuchMethodException ignored) {
                } catch (Exception e) {
                    RandomizerMod.LOGGER.warn("[Randomizer] reloadableRegistries.{}() attempt: {}", accessorName, e.getMessage());
                }
            }
        } catch (NoSuchMethodException e) {
            RandomizerMod.LOGGER.warn("[Randomizer] server.reloadableRegistries() not found");
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer] reloadableRegistries attempt: {}", e.getMessage());
        }
    }

    private static void probeIdentifierClass() {
        try {
            Object sampleId = ResourceKey.class.getMethod("identifier").invoke(Registries.LOOT_TABLE);
            Class<?> idClass = sampleId.getClass();
            RandomizerMod.LOGGER.info("[Randomizer/Probe] Identifier class: {}", idClass.getName());
            RandomizerMod.LOGGER.info("[Randomizer/Probe] Sample Identifier value: {}", sampleId);
            for (Method m : idClass.getDeclaredMethods()) {
                if (java.lang.reflect.Modifier.isPublic(m.getModifiers())
                        && java.lang.reflect.Modifier.isStatic(m.getModifiers())
                        && m.getParameterCount() <= 2) {
                    RandomizerMod.LOGGER.info("[Randomizer/Probe] Identifier.static.{}({}) -> {}",
                            m.getName(),
                            Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                                  .collect(Collectors.joining(", ")),
                            m.getReturnType().getSimpleName());
                }
            }
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer/Probe] Identifier probe failed: {}", e.getMessage());
        }
    }

    private static void probeReloadableRegistries(MinecraftServer server, List<ResourceKey<LootTable>> pool) {
        if (!pool.isEmpty()) return; // Skip if we already have the pool
        try {
            Object reloadable = server.getClass().getMethod("reloadableRegistries").invoke(server);
            RandomizerMod.LOGGER.info("[Randomizer/Probe] reloadableRegistries type: {}",
                    reloadable.getClass().getName());
            for (Method m : reloadable.getClass().getMethods()) {
                String n = m.getName().toLowerCase();
                if (m.getParameterCount() <= 1 &&
                        (n.contains("access") || n.contains("lookup") || n.contains("get")
                                || n.contains("registry") || n.contains("composite"))) {
                    RandomizerMod.LOGGER.info("[Randomizer/Probe] Reloadable.{}({}) -> {}",
                            m.getName(),
                            Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                                  .collect(Collectors.joining(", ")),
                            m.getReturnType().getSimpleName());
                }
            }
        } catch (Exception e) {
            RandomizerMod.LOGGER.warn("[Randomizer/Probe] reloadableRegistries probe: {}", e.getMessage());
        }
    }

    public static ResourceKey<LootTable> getMappedKey(ResourceKey<LootTable> original) {
        return mapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !mapping.isEmpty();
    }
}
