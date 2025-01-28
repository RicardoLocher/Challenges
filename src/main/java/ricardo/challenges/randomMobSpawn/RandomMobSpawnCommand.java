package ricardo.challenges.randomMobSpawn;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class RandomMobSpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("activateSpawnRandomMobs")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    RandomMobSpawnHandler.spawnRandomMobs();
                    return 1;
                })
        );

        dispatcher.register(literal("deactivateSpawnRandomMobs")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    RandomMobSpawnHandler.removeRandomMobs();
                    return 1;
                })
        );
    }
}
