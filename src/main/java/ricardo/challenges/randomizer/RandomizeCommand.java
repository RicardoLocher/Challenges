package ricardo.challenges.randomizer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomizeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("randomizeDrops")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(RandomizeCommand::execute));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        RandomDropsHandler.randomizeDrops();
        return 1;
    }
}
