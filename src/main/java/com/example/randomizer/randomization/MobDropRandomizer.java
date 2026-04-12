package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Method;
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

        // Probe: log loot/drop-related methods so we can find the correct mixin target.
        // Remove this block once the right method name is confirmed.
        RandomizerMod.LOGGER.info("[Randomizer/Probe] === LivingEntity drop/loot methods ===");
        for (Method m : LivingEntity.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("drop") || n.contains("loot")) {
                RandomizerMod.LOGGER.info("[Randomizer/Probe] LivingEntity.{}({}) -> {}",
                        m.getName(),
                        Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                              .collect(Collectors.joining(", ")),
                        m.getReturnType().getSimpleName());
            }
        }
        RandomizerMod.LOGGER.info("[Randomizer/Probe] === EntityType loot/table methods ===");
        for (Method m : EntityType.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("loot") || n.contains("table")) {
                RandomizerMod.LOGGER.info("[Randomizer/Probe] EntityType.{}({}) -> {}",
                        m.getName(),
                        Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName)
                              .collect(Collectors.joining(", ")),
                        m.getReturnType().getSimpleName());
            }
        }
    }

    public static EntityType<?> getMappedEntityType(EntityType<?> original) {
        return dropMapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !dropMapping.isEmpty();
    }
}
