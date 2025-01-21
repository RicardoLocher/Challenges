package ricardo.challenges.randomizer;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomizeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("randomizeDrops")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    RandomDropsHandler.randomizeDrops();
                    return 1;
                })
        );

        dispatcher.register(literal("resetDrops")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    RandomDropsHandler.resetDrops();
                    return 1;
                })
        );
    }
}
