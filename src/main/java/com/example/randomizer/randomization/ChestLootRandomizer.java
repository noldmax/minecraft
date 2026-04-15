package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

/**
 * Generates and stores the per-world chest loot-table pool.
 * Instead of mapping chest types to each other, each individual chest is
 * assigned a loot table deterministically based on its block position and
 * the world seed. Two chests of the same type in different positions will
 * get different loot tables. Same seed + same position = same table always.
 */
public final class ChestLootRandomizer {

    private static final List<ResourceKey<LootTable>> pool = new ArrayList<>();
    private static long worldSeed;
    private static boolean initialized = false;

    /** All vanilla chest loot table paths as of MC 1.21.x. */
    private static final List<String> VANILLA_CHEST_PATHS = List.of(
            "chests/abandoned_mineshaft",
            "chests/bastion_bridge",
            "chests/bastion_hoglin_stable",
            "chests/bastion_other",
            "chests/bastion_treasure",
            "chests/buried_treasure",
            "chests/desert_pyramid",
            "chests/end_city_treasure",
            "chests/igloo_chest",
            "chests/jungle_temple",
            "chests/jungle_temple_dispenser",
            "chests/nether_bridge",
            "chests/pillager_outpost",
            "chests/ruined_portal",
            "chests/shipwreck_map",
            "chests/shipwreck_supply",
            "chests/shipwreck_treasure",
            "chests/simple_dungeon",
            "chests/spawn_bonus_chest",
            "chests/stronghold_corridor",
            "chests/stronghold_crossing",
            "chests/stronghold_library",
            "chests/underwater_ruin_big",
            "chests/underwater_ruin_small",
            "chests/village/village_armorer",
            "chests/village/village_butcher",
            "chests/village/village_cartographer",
            "chests/village/village_desert_house",
            "chests/village/village_fisher",
            "chests/village/village_fletcher",
            "chests/village/village_mason",
            "chests/village/village_plains_house",
            "chests/village/village_savanna_house",
            "chests/village/village_shepherd",
            "chests/village/village_snowy_house",
            "chests/village/village_taiga_house",
            "chests/village/village_tannery",
            "chests/village/village_temple",
            "chests/village/village_toolsmith",
            "chests/village/village_weaponsmith",
            // Trial Chambers (added in 1.21)
            "chests/trial_chambers/corridor",
            "chests/trial_chambers/entrance",
            "chests/trial_chambers/intersection",
            "chests/trial_chambers/intersection_barrel",
            "chests/trial_chambers/supply",
            "chests/trial_chambers/reward",
            "chests/trial_chambers/reward_rare",
            "chests/trial_chambers/vault",
            "chests/trial_chambers/ominous_vault"
    );

    public static void initialize(MinecraftServer server, long seed) {
        pool.clear();
        initialized = false;
        worldSeed = seed;

        for (String path : VANILLA_CHEST_PATHS) {
            ResourceKey<LootTable> key = ResourceKey.create(
                    Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("minecraft", path));
            if (server.reloadableRegistries().getLootTable(key) != LootTable.EMPTY) {
                pool.add(key);
            }
        }

        if (pool.isEmpty()) {
            RandomizerMod.LOGGER.warn("[Randomizer] Chest loot pool is empty — chest randomization disabled");
        } else {
            initialized = true;
            RandomizerMod.LOGGER.info("[Randomizer] Chest loot pool initialized: {} tables, seed {}",
                    pool.size(), seed);
        }
    }

    /**
     * Returns a loot table key for the chest at the given position.
     * The selection is derived purely from the world seed and block position,
     * so every chest in the world gets its own independent table.
     */
    public static ResourceKey<LootTable> getMappedKey(BlockPos pos, ResourceKey<Level> dimension) {
        // Mix position and dimension into the world seed for a stable per-chest hash.
        long hash = worldSeed
                ^ ((long) pos.getX() * 3129871L)
                ^ ((long) pos.getY() * 116129781L)
                ^ ((long) pos.getZ() * 1274182917L)
                ^ ((long) dimension.toString().hashCode() * 1867861L);
        // Wang hash finalisation — spreads bits so nearby coords don't cluster.
        hash ^= (hash >>> 33);
        hash *= 0xff51afd7ed558ccdL;
        hash ^= (hash >>> 33);
        return pool.get((int) Math.floorMod(hash, pool.size()));
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private ChestLootRandomizer() {}
}
