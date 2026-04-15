package com.example.randomizer.mixin;

import com.example.randomizer.randomization.ChestLootRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts chest loot generation so that each chest type yields loot from a
 * randomly mapped chest loot table instead of its own.
 *
 * In MC 1.21.11 there is no unpackLootTable method. Loot generation happens in
 * a superclass that calls getLootTable() to obtain the key to roll. We intercept
 * getLootTable() at RETURN and substitute the mapped key before the caller uses it.
 *
 * require = 0 keeps the game bootable if the method name changes.
 */
@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

    @Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true, require = 0)
    private void remapChestLoot(CallbackInfoReturnable<ResourceKey<LootTable>> cir) {
        ResourceKey<LootTable> original = cir.getReturnValue();
        if (original == null || !ChestLootRandomizer.isInitialized()) return;
        ResourceKey<LootTable> mapped = ChestLootRandomizer.getMappedKey(original);
        if (mapped != original) cir.setReturnValue(mapped);
    }
}
