package com.example.randomizer.randomization;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

/**
 * Defines which blocks belong to each randomization category.
 * Blocks only shuffle with others in the same category.
 * Blocks not listed here keep their original drops.
 */
public final class BlockCategories {

    /** Blocks that generate naturally in the world (overworld, nether, end). */
    public static final List<Block> NATURAL = List.of(
            // Stone variants
            Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE,
            Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE,
            // Dirt & soil
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT,
            Blocks.PODZOL, Blocks.MYCELIUM, Blocks.ROOTED_DIRT,
            Blocks.MUD, Blocks.CLAY, Blocks.DIRT_PATH,
            // Sediment
            Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND,
            Blocks.SANDSTONE, Blocks.RED_SANDSTONE,
            // Ores
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
            // Logs & stems
            Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG,
            Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG,
            Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG, Blocks.PALE_OAK_LOG,
            Blocks.CRIMSON_STEM, Blocks.WARPED_STEM,
            // Leaves
            Blocks.OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.SPRUCE_LEAVES,
            Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES, Blocks.CHERRY_LEAVES, Blocks.PALE_OAK_LEAVES,
            Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES,
            // Nether
            Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL,
            Blocks.BASALT, Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE, Blocks.MAGMA_BLOCK,
            Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM,
            Blocks.WARPED_WART_BLOCK, Blocks.SHROOMLIGHT, Blocks.GLOWSTONE,
            // End
            Blocks.END_STONE, Blocks.OBSIDIAN,
            Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR,
            // Ice & snow
            Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
            // Lush caves / other natural
            Blocks.MOSS_BLOCK, Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE,
            Blocks.SCULK, Blocks.SCULK_VEIN, Blocks.SCULK_CATALYST, Blocks.SCULK_SHRIEKER,
            Blocks.AMETHYST_BLOCK, Blocks.BUDDING_AMETHYST,
            Blocks.MUDDY_MANGROVE_ROOTS, Blocks.MANGROVE_ROOTS,
            Blocks.BIG_DRIPLEAF, Blocks.HANGING_ROOTS,
            Blocks.PALE_MOSS_BLOCK, Blocks.PALE_HANGING_MOSS
    );

    /** Blocks primarily obtained through crafting. */
    public static final List<Block> CRAFTED = List.of(
            // Planks
            Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS,
            Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS,
            Blocks.MANGROVE_PLANKS, Blocks.CHERRY_PLANKS,
            Blocks.PALE_OAK_PLANKS, Blocks.BAMBOO_PLANKS,
            Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS,
            // Stone processing
            Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.COBBLED_DEEPSLATE,
            Blocks.POLISHED_GRANITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_ANDESITE,
            Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS,
            Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS,
            Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE,
            Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, Blocks.CHISELED_DEEPSLATE,
            Blocks.POLISHED_TUFF, Blocks.TUFF_BRICKS, Blocks.CHISELED_TUFF, Blocks.CHISELED_TUFF_BRICKS,
            Blocks.BRICKS, Blocks.MUD_BRICKS,
            Blocks.END_STONE_BRICKS, Blocks.PURPUR_SLAB, Blocks.PURPUR_STAIRS,
            // Slabs
            Blocks.OAK_SLAB, Blocks.BIRCH_SLAB, Blocks.SPRUCE_SLAB,
            Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB,
            Blocks.STONE_SLAB, Blocks.COBBLESTONE_SLAB,
            Blocks.STONE_BRICK_SLAB, Blocks.SANDSTONE_SLAB,
            Blocks.BRICK_SLAB, Blocks.QUARTZ_SLAB,
            // Stairs
            Blocks.OAK_STAIRS, Blocks.BIRCH_STAIRS, Blocks.SPRUCE_STAIRS,
            Blocks.COBBLESTONE_STAIRS, Blocks.STONE_BRICK_STAIRS,
            Blocks.BRICK_STAIRS, Blocks.SANDSTONE_STAIRS,
            // Fences & walls
            Blocks.OAK_FENCE, Blocks.BIRCH_FENCE, Blocks.SPRUCE_FENCE,
            Blocks.JUNGLE_FENCE, Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.COBBLESTONE_WALL, Blocks.STONE_BRICK_WALL, Blocks.BRICK_WALL,
            // Wool
            Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL,
            Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL,
            Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL,
            Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL,
            Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL,
            // Stained glass
            Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS,
            Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS,
            Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS,
            Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS,
            // Utility & storage
            Blocks.CRAFTING_TABLE, Blocks.CRAFTER,
            Blocks.FURNACE, Blocks.BLAST_FURNACE, Blocks.SMOKER,
            Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL,
            Blocks.BOOKSHELF, Blocks.CHISELED_BOOKSHELF,
            Blocks.NOTE_BLOCK, Blocks.TNT, Blocks.JUKEBOX,
            Blocks.IRON_BARS, Blocks.GLASS_PANE,
            Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE,
            Blocks.LANTERN, Blocks.SOUL_LANTERN, Blocks.CHAIN,
            Blocks.LIGHTNING_ROD, Blocks.BELL,
            // Shulker boxes
            Blocks.SHULKER_BOX,
            Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX,
            // Redstone
            Blocks.DISPENSER, Blocks.DROPPER, Blocks.HOPPER,
            Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.OBSERVER,
            Blocks.REDSTONE_LAMP, Blocks.TARGET,
            // Anvils
            Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL,
            // Special crafted
            Blocks.ENCHANTING_TABLE, Blocks.BREWING_STAND,
            Blocks.BEACON, Blocks.CONDUIT,
            Blocks.LODESTONE, Blocks.RESPAWN_ANCHOR,
            // Resource blocks
            Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK, Blocks.LAPIS_BLOCK, Blocks.COAL_BLOCK,
            Blocks.COPPER_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.NETHERITE_BLOCK,
            Blocks.AMETHYST_CLUSTER,
            // Copper variants (1.21)
            Blocks.CHISELED_COPPER,
            Blocks.EXPOSED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER,
            Blocks.COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE, Blocks.OXIDIZED_COPPER_GRATE,
            Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB,
            // Misc crafted
            Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK,
            Blocks.HAY_BLOCK, Blocks.BONE_BLOCK, Blocks.DRIED_KELP_BLOCK,
            Blocks.NETHER_WART_BLOCK,
            Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR, Blocks.CHISELED_QUARTZ_BLOCK,
            Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
            Blocks.SEA_LANTERN, Blocks.SPONGE
    );

    /** Blocks primarily obtained through smelting. */
    public static final List<Block> SMELTED = List.of(
            // Smooth stone variants
            Blocks.SMOOTH_STONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE,
            Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_BASALT,
            // Glass
            Blocks.GLASS,
            // Terracotta (smelting clay blocks)
            Blocks.TERRACOTTA,
            Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA,
            Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA,
            Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
            Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
            Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA,
            Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA,
            // Glazed terracotta (smelting stained terracotta)
            Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA,
            Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA,
            Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
            Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA,
            Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA,
            // Cracked variants (smelting their non-cracked form)
            Blocks.CRACKED_STONE_BRICKS,
            Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_TILES,
            Blocks.CRACKED_NETHER_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
    );

    private BlockCategories() {}

    public static List<List<Block>> getCategories() {
        return List.of(NATURAL, CRAFTED, SMELTED);
    }
}
