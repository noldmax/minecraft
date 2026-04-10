package com.example.randomizer.mixin;

import com.example.randomizer.randomization.BlockDropRandomizer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Intercepts drop generation at BlockBehaviour.BlockStateBase.getDrops(),
 * which is called whenever a block produces drops. Instead of rolling the
 * broken block's loot table, we roll the mapped block's table.
 *
 * A ThreadLocal guard prevents infinite recursion when calling getDrops()
 * on the mapped block's state inside this injection.
 */
@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public abstract class BlockDropsMixin {

    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);

    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    private void randomizeBlockDrops(LootParams.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (ACTIVE.get() || !BlockDropRandomizer.isInitialized()) return;

        Block thisBlock = ((BlockState) (Object) this).getBlock();
        Block mapped = BlockDropRandomizer.getMappedBlock(thisBlock);
        if (mapped == thisBlock) return;

        ACTIVE.set(true);
        try {
            List<ItemStack> drops = mapped.defaultBlockState().getDrops(builder);
            cir.setReturnValue(drops);
        } finally {
            ACTIVE.set(false);
        }
    }
}
