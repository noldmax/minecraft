package com.example.randomizer.mixin;

import com.example.randomizer.randomization.MobDropRandomizer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

/**
 * Intercepts mob drop generation so that each mob drops items from a randomly
 * mapped mob's loot table instead of its own.
 *
 * In MC 1.21.11, LivingEntity.dropFromLootTable() has no getLootTable() helper;
 * it calls this.getType().getDefaultLootTable() directly.  We redirect the
 * getDefaultLootTable() call within dropFromLootTable to return the mapped
 * entity type's loot table instead.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(
        method = "dropFromLootTable",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;getDefaultLootTable()Ljava/util/Optional;")
    )
    private Optional<ResourceKey<LootTable>> remapMobLootTable(EntityType<?> entityType) {
        if (!MobDropRandomizer.isInitialized()) return entityType.getDefaultLootTable();
        EntityType<?> mapped = MobDropRandomizer.getMappedEntityType(entityType);
        return mapped.getDefaultLootTable();
    }
}
