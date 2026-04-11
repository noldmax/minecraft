package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

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
        craftingMap.clear();
        smeltingMap.clear();
        initialized = false;

        RecipeManager rm = server.getRecipeManager();
        var registries = server.registryAccess();
        Random random = new Random(seed);

        // Crafting: shaped + shapeless (RecipeType.CRAFTING covers both)
        Set<Item> craftingItems = new LinkedHashSet<>();
        rm.recipeMap().byType(RecipeType.CRAFTING).stream()
                .map(holder -> holder.value().getResultItem(registries).getItem())
                .filter(item -> item != Items.AIR)
                .forEach(craftingItems::add);
        buildMap(craftingMap, new ArrayList<>(craftingItems), random);

        // Smelting: all furnace variants share one pool
        Set<Item> smeltingItems = new LinkedHashSet<>();
        rm.recipeMap().byType(RecipeType.SMELTING).stream()
                .map(holder -> holder.value().getResultItem(registries).getItem())
                .filter(item -> item != Items.AIR)
                .forEach(smeltingItems::add);
        rm.recipeMap().byType(RecipeType.BLASTING).stream()
                .map(holder -> holder.value().getResultItem(registries).getItem())
                .filter(item -> item != Items.AIR)
                .forEach(smeltingItems::add);
        rm.recipeMap().byType(RecipeType.SMOKING).stream()
                .map(holder -> holder.value().getResultItem(registries).getItem())
                .filter(item -> item != Items.AIR)
                .forEach(smeltingItems::add);
        rm.recipeMap().byType(RecipeType.CAMPFIRE_COOKING).stream()
                .map(holder -> holder.value().getResultItem(registries).getItem())
                .filter(item -> item != Items.AIR)
                .forEach(smeltingItems::add);
        buildMap(smeltingMap, new ArrayList<>(smeltingItems), random);

        initialized = true;
        RandomizerMod.LOGGER.info("[Randomizer] Recipe mapping initialized: {} crafting, {} smelting entries",
                craftingMap.size(), smeltingMap.size());
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
