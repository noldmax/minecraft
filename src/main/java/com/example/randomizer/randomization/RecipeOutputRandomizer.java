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
 */
public final class RecipeOutputRandomizer {

    private static final Map<Item, Item> craftingMap = new HashMap<>();
    private static final Map<Item, Item> smeltingMap = new HashMap<>();
    private static boolean initialized = false;

    @SuppressWarnings("unchecked")
    public static void initialize(MinecraftServer server, long seed) {
        RandomizerMod.LOGGER.info("[Randomizer] Initializing recipe maps with seed {}", seed);
        craftingMap.clear();
        smeltingMap.clear();
        initialized = false;

        RecipeManager rm = server.getRecipeManager();
        Random random = new Random(seed);

        // ── Crafting ──────────────────────────────────────────────────────────
        Set<Item> craftingOutputs = new LinkedHashSet<>();
        for (RecipeHolder<?> holder : rm.getAllOfType(RecipeType.CRAFTING)) {
            Item item = getResultItem(holder.value());
            if (item != null && item != Items.AIR) craftingOutputs.add(item);
        }
        buildMap(craftingMap, new ArrayList<>(craftingOutputs), random);
        RandomizerMod.LOGGER.info("[Randomizer] Crafting map: {} unique outputs", craftingMap.size());

        // ── Smelting (all furnace variants share one shuffle pool) ─────────────
        Set<Item> smeltingOutputs = new LinkedHashSet<>();
        for (RecipeType<?> type : new RecipeType<?>[]{ RecipeType.SMELTING, RecipeType.BLASTING,
                RecipeType.SMOKING, RecipeType.CAMPFIRE_COOKING }) {
            for (RecipeHolder<?> holder : rm.getAllOfType(type)) {
                Item item = getResultItem(holder.value());
                if (item != null && item != Items.AIR) smeltingOutputs.add(item);
            }
        }
        buildMap(smeltingMap, new ArrayList<>(smeltingOutputs), random);
        RandomizerMod.LOGGER.info("[Randomizer] Smelting map: {} unique outputs", smeltingMap.size());

        initialized = true;
    }

    /** Extract the primary result Item from any recipe via its display() data. */
    private static Item getResultItem(Recipe<?> recipe) {
        List<? extends RecipeDisplay> displays = recipe.display();
        if (displays.isEmpty()) return null;
        SlotDisplay result = displays.get(0).result();
        return slotDisplayItem(result);
    }

    private static Item slotDisplayItem(SlotDisplay display) {
        if (display instanceof SlotDisplay.ItemStackSlotDisplay d) {
            return d.stack().getItem();
        }
        if (display instanceof SlotDisplay.ItemSlotDisplay d) {
            // item() returns Holder<Item> in 1.21.x
            return d.item().value();
        }
        if (display instanceof SlotDisplay.CompositeSlotDisplay d) {
            for (SlotDisplay inner : d.contents()) {
                Item item = slotDisplayItem(inner);
                if (item != null && item != Items.AIR) return item;
            }
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

    public static Item getMappedSmeltingItem(Item original) {
        return smeltingMap.getOrDefault(original, original);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private RecipeOutputRandomizer() {}
}
