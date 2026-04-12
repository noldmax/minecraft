package com.example.randomizer.mixin;

import com.example.randomizer.randomization.MobDropRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Intercepts mob drop generation so that each mob drops items from a randomly
 * mapped mob's loot table instead of its own.
 *
 * randomizer$mappedType is set to the target entity type just before
 * dropFromLootTable runs and cleared when it exits.  getLootTable() checks
 * this field and, if non-null, returns the mapped type's loot table.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private EntityType<?> randomizer$mappedType = null;

    @Inject(method = "dropFromLootTable", at = @At("HEAD"))
    private void setMappedType(ServerLevel level, DamageSource source,
                                boolean causedByLastHurt, CallbackInfo ci) {
        if (!MobDropRandomizer.isInitialized()) return;
        LivingEntity self = (LivingEntity)(Object)this;
        EntityType<?> mapped = MobDropRandomizer.getMappedEntityType(self.getType());
        randomizer$mappedType = (mapped != self.getType()) ? mapped : null;
    }

    @Inject(method = "dropFromLootTable", at = @At("RETURN"))
    private void clearMappedType(ServerLevel level, DamageSource source,
                                  boolean causedByLastHurt, CallbackInfo ci) {
        randomizer$mappedType = null;
    }

    @Inject(method = "getLootTable", at = @At("HEAD"), cancellable = true)
    private void remapLootTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> cir) {
        if (randomizer$mappedType == null) return;
        cir.setReturnValue(randomizer$mappedType.getDefaultLootTable());
    }
}
