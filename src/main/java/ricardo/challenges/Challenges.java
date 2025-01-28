package ricardo.challenges;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ricardo.challenges.randomMobSpawn.RandomMobSpawnCommand;
import ricardo.challenges.randomMobSpawn.RandomMobSpawnHandler;
import ricardo.challenges.randomizer.RandomDropsHandler;
import ricardo.challenges.randomizer.RandomizeCommand;

public class Challenges implements ModInitializer {
	public static final String MOD_ID = "challenges";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RandomDropsHandler.initialize();
		RandomMobSpawnHandler.initialize();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			RandomizeCommand.register(dispatcher);
			RandomMobSpawnCommand.register(dispatcher);
		});
	}
}