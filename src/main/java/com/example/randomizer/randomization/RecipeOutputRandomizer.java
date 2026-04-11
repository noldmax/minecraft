package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;

import java.lang.reflect.Method;
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
            // Probe RecipeManager API
            RecipeManager rm = server.getRecipeManager();
            RandomizerMod.LOGGER.info("[Randomizer] RecipeManager class: {}", rm.getClass().getName());
            RandomizerMod.LOGGER.info("[Randomizer] --- RecipeManager public methods ---");
            for (Method m : rm.getClass().getMethods()) {
                RandomizerMod.LOGGER.info("[Randomizer] RM method: {}", m.toGenericString());
            }

            // Probe ShapedRecipe API
            RandomizerMod.LOGGER.info("[Randomizer] --- ShapedRecipe declared methods ---");
            for (Method m : ShapedRecipe.class.getDeclaredMethods()) {
                RandomizerMod.LOGGER.info("[Randomizer] ShapedRecipe method: {}", m.toGenericString());
            }

            // Probe AbstractCookingRecipe API
            RandomizerMod.LOGGER.info("[Randomizer] --- AbstractCookingRecipe declared methods ---");
            for (Method m : AbstractCookingRecipe.class.getDeclaredMethods()) {
                RandomizerMod.LOGGER.info("[Randomizer] CookingRecipe method: {}", m.toGenericString());
            }
        } catch (Throwable t) {
            RandomizerMod.LOGGER.error("[Randomizer] Exception during probe", t);
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
