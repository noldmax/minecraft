package com.example.randomizer.mixin;

import com.example.randomizer.randomization.BlockDropRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Intercepts Block.getLootTable() at the point where a block's loot table key is looked up.
 * Redirects the key to the mapped block's loot table, causing the block to drop
 * whatever the mapped block would normally drop.
 *
 * NOTE: If this mixin fails to apply, check the decompiled Block source
 * (right-click Block.class -> Open in Files, or use "Go to class" in IntelliJ)
 * to verify the exact method name. In Mojang mappings it should be getLootTable().
 */
@Mixin(Block.class)
public class BlockLootTableMixin {

    @Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true)
    private void randomizeDropTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> cir) {
        if (!BlockDropRandomizer.isInitialized()) return;

        Block thisBlock = (Block) (Object) this;
        Block mapped = BlockDropRandomizer.getMappedBlock(thisBlock);

        if (mapped != thisBlock) {
            cir.setReturnValue(mapped.getLootTable());
        }
    }
}
