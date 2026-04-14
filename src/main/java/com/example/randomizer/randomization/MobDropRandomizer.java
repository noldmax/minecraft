package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates and stores the per-world mob drop mapping.
 * Every entity type that has a loot table is shuffled in one pool.
 * Same seed → same shuffle.
 */
public final class MobDropRandomizer {

    private static final Map<EntityType<?>, EntityType<?>> dropMapping = new HashMap<>();

    public static void initialize(long seed) {
        dropMapping.clear();
        Random random = new Random(seed);

        List<EntityType<?>> pool = BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(e -> e.getDefaultLootTable().isPresent())
                .collect(Collectors.toCollection(ArrayList::new));

        List<EntityType<?>> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);

        for (int i = 0; i < pool.size(); i++) {
            dropMapping.put(pool.get(i), shuffled.get(i));
        }

        RandomizerMod.LOGGER.info("[Randomizer] Mob drop mapping initialized: {} entity types, seed {}",
                pool.size(), seed);
    }

    public static EntityType<?> getMappedEntityType(EntityType<?> original) {
        return dropMapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !dropMapping.isEmpty();
    }
}
