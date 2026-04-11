package com.example.randomizer.mixin;

import com.example.randomizer.randomization.RecipeOutputRandomizer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
    private void remapCraftingOutput(CraftingInput input, HolderLookup.Provider registries,
                                     CallbackInfoReturnable<ItemStack> cir) {
        if (!RecipeOutputRandomizer.isInitialized()) return;
        ItemStack original = cir.getReturnValue();
        if (original.isEmpty()) return;
        Item mapped = RecipeOutputRandomizer.getMappedCraftingItem(original.getItem());
        if (mapped == original.getItem()) return;
        cir.setReturnValue(new ItemStack(mapped, 1));
    }
}
