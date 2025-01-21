package ricardo.challenges.randomizer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import ricardo.challenges.Challenges;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RandomDropsHandler {
    private static final Map<Block, Item> blockDropMap = new HashMap<>();
    private static boolean useRandomDrops = false; // Flag to toggle random drops

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world instanceof ServerWorld serverWorld && useRandomDrops) {
                Block block = state.getBlock();

                // Prevent the block's default drops
                state.onStacksDropped(serverWorld, pos, ItemStack.EMPTY, true);

                // Drop the random item instead
                Item randomDrop = blockDropMap.getOrDefault(block, block.asItem());
                Block.dropStack(serverWorld, pos, new ItemStack(randomDrop));

                // Remove the block manually
                world.setBlockState(pos, Blocks.AIR.getDefaultState());

                // Log the dropped item
                Challenges.LOGGER.info("Dropped " + randomDrop);

                return false; // Cancels further block processing
            }
            return true; // Proceed with normal block breaking if useRandomDrops is false
        });

    }

    public static void randomizeDrops() {
        blockDropMap.clear();
        Random random = Random.create();
        for (Block block : Registries.BLOCK) {
            Optional<RegistryEntry.Reference<Item>> optionalRandomItem = Registries.ITEM.getRandom(random);
            Item randomItem = optionalRandomItem.map(RegistryEntry.Reference::value).orElse(block.asItem());
            blockDropMap.put(block, randomItem);
        }
        useRandomDrops = true;
    }

    public static void resetDrops() {
        blockDropMap.clear();
        useRandomDrops = false; // Disable random drops
    }
}
