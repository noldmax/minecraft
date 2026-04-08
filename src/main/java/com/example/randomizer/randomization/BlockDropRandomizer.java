package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.world.level.block.Block;

import java.util.*;

/**
 * Generates and stores the per-world block drop mapping.
 * Each block maps to exactly one other block within the same category (bijection).
 * The mapping is deterministic: the same world seed always produces the same shuffle.
 */
public final class BlockDropRandomizer {

    private static final Map<Block, Block> dropMapping = new HashMap<>();

    /**
     * Initializes the mapping using the world seed.
     * Call this once when a world is loaded.
     */
    public static void initialize(long seed) {
        dropMapping.clear();
        Random random = new Random(seed);

        for (List<Block> category : BlockCategories.getCategories()) {
            List<Block> shuffled = new ArrayList<>(category);
            Collections.shuffle(shuffled, random);
            for (int i = 0; i < category.size(); i++) {
                dropMapping.put(category.get(i), shuffled.get(i));
            }
        }

        RandomizerMod.LOGGER.info("[Randomizer] Block drop mapping initialized with seed {}", seed);
    }

    /**
     * Returns the block whose loot table should be used when breaking the given block.
     * Returns the original block if it has no mapping (not in any category).
     */
    public static Block getMappedBlock(Block original) {
        return dropMapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !dropMapping.isEmpty();
    }
}
