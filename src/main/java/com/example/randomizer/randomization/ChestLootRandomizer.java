package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

/**
 * Generates and stores the per-world chest loot-table mapping.
 * All chest loot tables that exist in the current world are shuffled together
 * so every chest type yields loot from a different (but consistent) chest.
 * Same seed = same shuffle.
 *
 * We build the candidate list from the known vanilla chest table paths and
 * validate each one via reloadableRegistries().getLootTable() — any key that
 * returns LootTable.EMPTY simply isn't present in this version and is skipped.
 * This avoids invalid keys in the bijection and handles version differences
 * without needing to enumerate a registry.
 */
public final class ChestLootRandomizer {

    private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mapping = new HashMap<>();

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
        mapping.clear();

        // Build the pool: include only paths whose loot table actually exists.
        // getLootTable() returns LootTable.EMPTY for missing keys, so we can
        // use that to filter without needing registry enumeration.
        List<ResourceKey<LootTable>> pool = new ArrayList<>();
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
