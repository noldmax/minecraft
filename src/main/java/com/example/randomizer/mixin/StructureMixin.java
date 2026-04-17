package com.example.randomizer.mixin;

import com.example.randomizer.randomization.StructureRandomizer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Intercepts structure generation so each structure spawns as a randomly
 * mapped structure instead of its own form. Uses a ThreadLocal guard to
 * prevent the recursive call to the mapped structure's generate() from
 * triggering another remap.
 *
 * require = 0 keeps the game bootable if the method signature changes.
 */
@Mixin(Structure.class)
public abstract class StructureMixin {

    private static final ThreadLocal<Boolean> REMAPPING = ThreadLocal.withInitial(() -> false);

    @Inject(method = "generate", at = @At("HEAD"), cancellable = true, require = 0)
    private void remapStructure(
            RegistryAccess registryAccess,
            ChunkGenerator chunkGenerator,
            BiomeSource biomeSource,
            RandomState randomState,
            StructureTemplateManager templateManager,
            long seed,
            ChunkPos chunkPos,
            int references,
            ChunkAccess chunk,
            Predicate<Holder<Biome>> validBiome,
            CallbackInfoReturnable<Optional<StructureStart>> cir) {
        if (REMAPPING.get() || !StructureRandomizer.isInitialized()) return;

        Structure self = (Structure) (Object) this;
        Structure mapped = StructureRandomizer.getMappedStructure(self);
        if (mapped == self) return;

        REMAPPING.set(true);
        try {
            cir.setReturnValue(mapped.generate(registryAccess, chunkGenerator, biomeSource,
                    randomState, templateManager, seed, chunkPos, references, chunk, validBiome));
        } finally {
            REMAPPING.set(false);
        }
    }
}
