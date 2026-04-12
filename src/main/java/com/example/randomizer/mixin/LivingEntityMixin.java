package com.example.randomizer.mixin;

import com.example.randomizer.randomization.MobDropRandomizer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Intercepts mob drop generation so that each mob drops items from a randomly
 * mapped mob's loot table instead of its own.
 *
 * In MC 1.21.11, dropFromLootTable() obtains the loot table via the entity
 * type, but NOT via EntityType.getDefaultLootTable().  We redirect the
 * getType() call inside dropFromLootTable to return the mapped EntityType;
 * whatever the method then does with that type (getDefaultLootTable,
 * lootTable(), registry key lookup, …) will automatically use the mapped mob.
 *
 * require = 0 lets the game boot even if the call-site changes in a future
 * version so we can diagnose via the probe log in MobDropRandomizer.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(
        method = "dropFromLootTable",
        require = 0,
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;getType()Lnet/minecraft/world/entity/EntityType;")
    )
    private EntityType<?> remapEntityTypeForDrop(LivingEntity self) {
        if (!MobDropRandomizer.isInitialized()) return self.getType();
        return MobDropRandomizer.getMappedEntityType(self.getType());
    }
}
