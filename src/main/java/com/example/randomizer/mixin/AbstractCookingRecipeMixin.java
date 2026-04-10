package com.example.randomizer.mixin;

import com.example.randomizer.randomization.RecipeOutputRandomizer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts the output of all furnace-type recipes (smelting, blasting,
 * smoking, campfire cooking) via their shared base class.
 */
@Mixin(AbstractCookingRecipe.class)
public class AbstractCookingRecipeMixin {

    @Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
    private void remapSmeltingOutput(SingleRecipeInput input, HolderLookup.Provider registries,
                                     CallbackInfoReturnable<ItemStack> cir) {
        if (!RecipeOutputRandomizer.isInitialized()) return;
        ItemStack original = cir.getReturnValue();
        if (original.isEmpty()) return;
        Item mapped = RecipeOutputRandomizer.getMappedSmeltingItem(original.getItem());
        if (mapped == original.getItem()) return;
        cir.setReturnValue(new ItemStack(mapped, original.getCount()));
    }
}
