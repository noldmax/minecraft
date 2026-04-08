package com.example.randomizer;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomizerMod implements ModInitializer {
	public static final String MOD_ID = "randomizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Randomizer mod initialized!");
	}
}
