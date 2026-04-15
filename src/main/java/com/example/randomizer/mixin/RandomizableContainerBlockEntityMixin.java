package com.example.randomizer.mixin;

import com.example.randomizer.randomization.ChestLootRandomizer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts chest loot generation so that each individual chest yields loot
 * from a loot table chosen by its block position + world seed, rather than
 * its own type's table. Two chests of the same type in different locations
 * will get different loot tables.
 */
@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

    @Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true, require = 0)
    private void remapChestLoot(CallbackInfoReturnable<ResourceKey<LootTable>> cir) {
        if (!ChestLootRandomizer.isInitialized()) return;
        // The original key may be null if the chest has already been opened.
        if (cir.getReturnValue() == null) return;

        BlockEntity self = (BlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) return;

        BlockPos pos = self.getBlockPos();
        ResourceKey<Level> dimension = level.dimension();

        cir.setReturnValue(ChestLootRandomizer.getMappedKey(pos, dimension));
    }
}
