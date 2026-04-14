package com.example.randomizer;

import com.example.randomizer.randomization.BlockDropRandomizer;
import com.example.randomizer.randomization.ChestLootRandomizer;
import com.example.randomizer.randomization.MobDropRandomizer;
import com.example.randomizer.randomization.RecipeOutputRandomizer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomizerMod implements ModInitializer {
    public static final String MOD_ID = "randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Initialize block drop randomization when a server starts,
        // seeded by the overworld seed so the same world always gets the same shuffle.
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            if (overworld != null) {
                long seed = overworld.getSeed();
                BlockDropRandomizer.initialize(seed);
                RecipeOutputRandomizer.initialize(server, seed);
                MobDropRandomizer.initialize(seed);
                ChestLootRandomizer.initialize(server, seed);
            }
        });

        LOGGER.info("[Randomizer] Mod initialized.");
    }
}
