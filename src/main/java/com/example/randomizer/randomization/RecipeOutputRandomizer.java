package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;

/**
 * Generates and stores per-world recipe output mappings.
 * Crafting outputs shuffle among crafting outputs; all furnace-type outputs
 * shuffle together. The mapping is deterministic: same seed = same shuffle.
 */
public final class RecipeOutputRandomizer {

    private static final Map<Item, Item> craftingMap = new HashMap<>();
    private static final Map<Item, Item> smeltingMap = new HashMap<>();
    private static boolean initialized = false;

    public static void initialize(MinecraftServer server, long seed) {
        RandomizerMod.LOGGER.info("[Randomizer] RecipeOutputRandomizer.initialize() called");
        craftingMap.clear();
        smeltingMap.clear();
        initialized = false;

        try {
            RecipeManager rm = server.getRecipeManager();
            RandomizerMod.LOGGER.info("[Randomizer] RecipeManager = {}", rm);
            if (rm == null) {
                RandomizerMod.LOGGER.error("[Randomizer] RecipeManager is null!");
                return;
            }
            RandomizerMod.LOGGER.info("[Randomizer] RecipeManager class: {}", rm.getClass().getName());
            for (java.lang.reflect.Method m : rm.getClass().getMethods()) {
                RandomizerMod.LOGGER.info("[Randomizer] RecipeManager method: {}", m.toGenericString());
            }
        } catch (Throwable t) {
            RandomizerMod.LOGGER.error("[Randomizer] Exception during RecipeManager probe", t);
        }
        initialized = true;
    }

    private static void buildMap(Map<Item, Item> map, List<Item> items, Random random) {
        List<Item> shuffled = new ArrayList<>(items);
        Collections.shuffle(shuffled, random);
        for (int i = 0; i < items.size(); i++) {
            map.put(items.get(i), shuffled.get(i));
        }
    }

    public static Item getMappedCraftingItem(Item original) {
        return craftingMap.getOrDefault(original, original);
    }

    public static Item getMappedSmeltingItem(Item original) {
        return smeltingMap.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private RecipeOutputRandomizer() {}
}
