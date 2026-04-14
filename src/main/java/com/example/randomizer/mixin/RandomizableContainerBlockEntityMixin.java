package com.example.randomizer.mixin;

import com.example.randomizer.randomization.ChestLootRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts chest loot generation so that each chest type yields loot from a
 * randomly mapped chest loot table instead of its own.
 *
 * At the HEAD of unpackLootTable (called when a container is first opened),
 * we replace this.lootTable with the mapped key before the loot is rolled.
 * The original method then clears the field to null after generation, so the
 * mapping is applied exactly once per chest, which is the correct behaviour.
 *
 * require = 0 keeps the game bootable if the method name is wrong in this
 * MC version; the probe in ChestLootRandomizer will log the real name.
 */
@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

    @Mutable
    @Shadow
    protected ResourceKey<LootTable> lootTable;

    @Inject(method = "unpackLootTable", at = @At("HEAD"), require = 0)
    private void remapChestLoot(Player player, CallbackInfo ci) {
        if (!ChestLootRandomizer.isInitialized() || this.lootTable == null) return;
        this.lootTable = ChestLootRandomizer.getMappedKey(this.lootTable);
    }
}
