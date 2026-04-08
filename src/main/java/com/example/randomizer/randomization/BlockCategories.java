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
            // Logs
            Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG,
            Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG,
            Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG, Blocks.PALE_OAK_LOG,
            // Leaves
            Blocks.OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.SPRUCE_LEAVES,
            Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES, Blocks.CHERRY_LEAVES, Blocks.PALE_OAK_LEAVES,
            Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES,
            // Nether
            Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL,
            Blocks.BASALT, Blocks.BLACKSTONE, Blocks.MAGMA_BLOCK,
            Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM,
            Blocks.CRIMSON_STEM, Blocks.WARPED_STEM,
            Blocks.GLOWSTONE,
            // End
            Blocks.END_STONE, Blocks.OBSIDIAN,
            // Other natural
            Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
            Blocks.MOSS_BLOCK, Blocks.DRIPSTONE_BLOCK,
            Blocks.SCULK, Blocks.AMETHYST_BLOCK,
            Blocks.MUDDY_MANGROVE_ROOTS, Blocks.MANGROVE_ROOTS
    );

    /** Blocks primarily obtained through crafting. */
    public static final List<Block> CRAFTED = List.of(
            // Planks
            Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS,
            Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS,
            Blocks.MANGROVE_PLANKS, Blocks.CHERRY_PLANKS,
            Blocks.PALE_OAK_PLANKS, Blocks.BAMBOO_PLANKS,
            // Stone processing
            Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,
            Blocks.POLISHED_GRANITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_ANDESITE,
            Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS,
            Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS,
            Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS,
            Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES,
            Blocks.POLISHED_TUFF, Blocks.TUFF_BRICKS, Blocks.CHISELED_TUFF,
            // Slabs
            Blocks.OAK_SLAB, Blocks.BIRCH_SLAB, Blocks.SPRUCE_SLAB,
            Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB,
            Blocks.STONE_SLAB, Blocks.COBBLESTONE_SLAB,
            Blocks.STONE_BRICK_SLAB, Blocks.SANDSTONE_SLAB,
            Blocks.BRICK_SLAB, Blocks.PURPUR_SLAB, Blocks.QUARTZ_SLAB,
            // Stairs
            Blocks.OAK_STAIRS, Blocks.BIRCH_STAIRS, Blocks.SPRUCE_STAIRS,
            Blocks.COBBLESTONE_STAIRS, Blocks.STONE_BRICK_STAIRS,
            Blocks.BRICK_STAIRS, Blocks.SANDSTONE_STAIRS, Blocks.PURPUR_STAIRS,
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
            // Utility blocks
            Blocks.CRAFTING_TABLE, Blocks.FURNACE, Blocks.CHEST,
            Blocks.BOOKSHELF, Blocks.NOTE_BLOCK, Blocks.TNT,
            Blocks.IRON_BARS, Blocks.GLASS_PANE,
            // Resource blocks
            Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK, Blocks.LAPIS_BLOCK, Blocks.COAL_BLOCK,
            Blocks.COPPER_BLOCK, Blocks.REDSTONE_BLOCK,
            // Misc crafted
            Blocks.SLIME_BLOCK, Blocks.HAY_BLOCK, Blocks.BONE_BLOCK,
            Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR, Blocks.CHISELED_QUARTZ_BLOCK,
            Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR,
            Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
            Blocks.SEA_LANTERN
    );

    /** Blocks primarily obtained through smelting. */
    public static final List<Block> SMELTED = List.of(
            Blocks.SMOOTH_STONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE,
            Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_BASALT,
            Blocks.GLASS,
            Blocks.TERRACOTTA,
            Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA,
            Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA,
            Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
            Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
            Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA,
            Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA,
            Blocks.SPONGE,
            Blocks.CRACKED_STONE_BRICKS,
            Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_TILES,
            Blocks.CRACKED_NETHER_BRICKS
    );

    private BlockCategories() {}

    public static List<List<Block>> getCategories() {
        return List.of(NATURAL, CRAFTED, SMELTED);
    }
}
