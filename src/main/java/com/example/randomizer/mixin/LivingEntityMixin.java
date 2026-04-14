package com.example.randomizer.mixin;

import com.example.randomizer.randomization.MobDropRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Intercepts mob drop generation so that each mob drops items from a randomly
 * mapped mob's loot table instead of its own.
 *
 * In MC 1.21.11 the 3-arg dropFromLootTable(ServerLevel, DamageSource, boolean)
 * resolves the loot table key and delegates to the 4-arg overload
 * dropFromLootTable(ServerLevel, DamageSource, boolean, ResourceKey<LootTable>).
 * We intercept the ResourceKey parameter of the 4-arg overload at HEAD and
 * swap it for the mapped mob's loot table key.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(
        method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private ResourceKey<LootTable> remapLootTableKey(ResourceKey<LootTable> tableKey) {
        if (!MobDropRandomizer.isInitialized()) return tableKey;
        LivingEntity self = (LivingEntity)(Object)this;
        EntityType<?> original = self.getType();
        EntityType<?> mapped = MobDropRandomizer.getMappedEntityType(original);
        if (mapped == original) return tableKey;
        return mapped.getDefaultLootTable().orElse(tableKey);
    }
}
