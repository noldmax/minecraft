package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.*;

/**
 * Generates and stores the per-world structure mapping.
 * All structures registered in the worldgen registry are shuffled together
 * so every structure type generates the physical form of a different one.
 * Same seed = same shuffle.
 */
public final class StructureRandomizer {

    private static final Map<Structure, Structure> mapping = new HashMap<>();
    private static boolean initialized = false;

    public static void initialize(MinecraftServer server, long seed) {
        mapping.clear();
        initialized = false;

        List<Structure> pool = new ArrayList<>();
        server.registryAccess()
              .lookup(Registries.STRUCTURE)
              .ifPresent(lookup -> lookup.listElements()
                      .forEach(ref -> pool.add(ref.value())));

        if (pool.isEmpty()) {
            RandomizerMod.LOGGER.warn("[Randomizer] Structure pool is empty — structure randomization disabled");
            return;
        }

        List<Structure> shuffled = new ArrayList<>(pool);
        // Use a distinct salt so the structure shuffle is independent from other randomizers.
        Collections.shuffle(shuffled, new Random(seed ^ 0x6A09E667F3BCC908L));

        for (int i = 0; i < pool.size(); i++) {
            mapping.put(pool.get(i), shuffled.get(i));
        }
        initialized = true;
        RandomizerMod.LOGGER.info("[Randomizer] Structure mapping initialized: {} structures, seed {}",
                pool.size(), seed);
    }

    public static Structure getMappedStructure(Structure original) {
        return mapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private StructureRandomizer() {}
}
