package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates and stores the per-world block drop mapping.
 * All breakable blocks shuffle together in one pool — every block with an
 * obtainable item form is eligible. The mapping is deterministic: same seed
 * = same shuffle.
 */
public final class BlockDropRandomizer {

    private static final Map<Block, Block> dropMapping = new HashMap<>();

    /**
     * Blocks that exist in the registry and have item forms but should never
     * enter the shuffle pool — creative-only, admin, or internal-state blocks
     * that players cannot meaningfully break in survival.
     */
    private static final Set<Block> EXCLUDED = Set.of(
            Blocks.BEDROCK,
            Blocks.BARRIER,
            Blocks.LIGHT,
            Blocks.COMMAND_BLOCK,
            Blocks.CHAIN_COMMAND_BLOCK,
            Blocks.REPEATING_COMMAND_BLOCK,
            Blocks.STRUCTURE_BLOCK,
            Blocks.STRUCTURE_VOID,
            Blocks.JIGSAW,
            Blocks.MOVING_PISTON,
            Blocks.PISTON_HEAD
    );

    public static void initialize(long seed) {
        dropMapping.clear();
        Random random = new Random(seed);

        // Every block that has an obtainable item form and isn't admin-only
        List<Block> pool = BuiltInRegistries.BLOCK.stream()
                .filter(b -> b.asItem() != Items.AIR)
                .filter(b -> !EXCLUDED.contains(b))
                .collect(Collectors.toCollection(ArrayList::new));

        List<Block> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);

        for (int i = 0; i < pool.size(); i++) {
            dropMapping.put(pool.get(i), shuffled.get(i));
        }

        RandomizerMod.LOGGER.info("[Randomizer] Block drop mapping initialized: {} blocks, seed {}",
                pool.size(), seed);
    }

    /**
     * Returns the block whose loot table should be used when the given block is broken.
     * Returns the original block if it has no mapping.
     */
    public static Block getMappedBlock(Block original) {
        return dropMapping.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return !dropMapping.isEmpty();
    }
}
