package com.example.randomizer.randomization;

import com.example.randomizer.RandomizerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.*;

/**
 * Generates and stores per-world recipe output mappings.
 * Crafting outputs (shaped + shapeless) shuffle among themselves.
 * All furnace-type outputs (smelting, blasting, smoking, campfire) shuffle together.
 * Mapping is deterministic: same seed = same shuffle.
 *
 * Count preservation: when item A maps to item B, the yield is B's original
 * recipe count (e.g. planks originally give 4, so whatever maps to planks gives 4).
 */
public final class RecipeOutputRandomizer {

    private static final Map<Item, Item> craftingMap = new HashMap<>();
    private static final Map<Item, Item> smeltingMap = new HashMap<>();
    /** Each item's original recipe output count, used to set yield for mapped results. */
    private static final Map<Item, Integer> craftingCounts = new HashMap<>();
    private static final Map<Item, Integer> smeltingCounts = new HashMap<>();
    private static boolean initialized = false;

    public static void initialize(MinecraftServer server, long seed) {
        RandomizerMod.LOGGER.info("[Randomizer] Initializing recipe maps with seed {}", seed);
        craftingMap.clear();
        smeltingMap.clear();
        craftingCounts.clear();
        smeltingCounts.clear();
        initialized = false;

        RecipeManager rm = server.getRecipeManager();
        Random random = new Random(seed);

        // ── Crafting ──────────────────────────────────────────────────────────
        Set<Item> craftingOutputs = new LinkedHashSet<>();
        for (RecipeHolder<?> holder : rm.getAllOfType(RecipeType.CRAFTING)) {
            ItemAndCount pair = getResultItemAndCount(holder.value());
            if (pair == null || pair.item() == Items.AIR) continue;
            if (craftingOutputs.add(pair.item())) {          // first occurrence wins
                craftingCounts.put(pair.item(), pair.count());
            }
        }
        buildMap(craftingMap, new ArrayList<>(craftingOutputs), random);
        RandomizerMod.LOGGER.info("[Randomizer] Crafting map: {} unique outputs", craftingMap.size());

        // ── Smelting (all furnace variants share one shuffle pool) ─────────────
        Set<Item> smeltingOutputs = new LinkedHashSet<>();
        collectOutputs(rm, smeltingOutputs, smeltingCounts, RecipeType.SMELTING);
        collectOutputs(rm, smeltingOutputs, smeltingCounts, RecipeType.BLASTING);
        collectOutputs(rm, smeltingOutputs, smeltingCounts, RecipeType.SMOKING);
        collectOutputs(rm, smeltingOutputs, smeltingCounts, RecipeType.CAMPFIRE_COOKING);
        buildMap(smeltingMap, new ArrayList<>(smeltingOutputs), random);
        RandomizerMod.LOGGER.info("[Randomizer] Smelting map: {} unique outputs", smeltingMap.size());

        initialized = true;
    }

    private static <I extends RecipeInput, T extends Recipe<I>> void collectOutputs(
            RecipeManager rm, Set<Item> out, Map<Item, Integer> counts, RecipeType<T> type) {
        for (RecipeHolder<T> holder : rm.getAllOfType(type)) {
            ItemAndCount pair = getResultItemAndCount(holder.value());
            if (pair == null || pair.item() == Items.AIR) continue;
            if (out.add(pair.item())) {
                counts.put(pair.item(), pair.count());
            }
        }
    }

    private static ItemAndCount getResultItemAndCount(Recipe<?> recipe) {
        List<? extends RecipeDisplay> displays = recipe.display();
        if (displays.isEmpty()) return null;
        SlotDisplay result = displays.get(0).result();
        if (result instanceof SlotDisplay.ItemStackSlotDisplay d) {
            return new ItemAndCount(d.stack().getItem(), d.stack().getCount());
        }
        if (result instanceof SlotDisplay.ItemSlotDisplay d) {
            return new ItemAndCount(d.item().value(), 1);
        }
        return null;
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

    /** Returns the original recipe count for the given mapped item (default 1). */
    public static int getMappedCraftingCount(Item mapped) {
        return craftingCounts.getOrDefault(mapped, 1);
    }

    public static Item getMappedSmeltingItem(Item original) {
        return smeltingMap.getOrDefault(original, original);
    }

    public static int getMappedSmeltingCount(Item mapped) {
        return smeltingCounts.getOrDefault(mapped, 1);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private record ItemAndCount(Item item, int count) {}

    private RecipeOutputRandomizer() {}
}
