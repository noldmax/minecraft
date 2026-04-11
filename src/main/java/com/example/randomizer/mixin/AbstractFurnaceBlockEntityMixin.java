package com.example.randomizer.mixin;

import com.example.randomizer.randomization.RecipeOutputRandomizer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Intercepts the smelting output inside AbstractFurnaceBlockEntity.burn() by
 * redirecting the call to AbstractCookingRecipe.assemble().
 * This covers all furnace variants: furnace, blast furnace, smoker, campfire.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Redirect(
        method = "burn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;assemble(Lnet/minecraft/world/item/crafting/SingleRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack remapSmeltingOutput(AbstractCookingRecipe recipe,
                                                  SingleRecipeInput input,
                                                  HolderLookup.Provider registries) {
        ItemStack original = recipe.assemble(input, registries);
        if (!RecipeOutputRandomizer.isInitialized() || original.isEmpty()) return original;
        Item mapped = RecipeOutputRandomizer.getMappedSmeltingItem(original.getItem());
        if (mapped == original.getItem()) return original;
        int count = RecipeOutputRandomizer.getMappedSmeltingCount(mapped);
        return new ItemStack(mapped, count);
    }
}
