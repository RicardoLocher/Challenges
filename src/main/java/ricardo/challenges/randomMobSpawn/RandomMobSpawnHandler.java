package ricardo.challenges.randomMobSpawn;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import ricardo.challenges.Challenges;

import java.util.HashMap;
import java.util.Map;

public class RandomMobSpawnHandler {
    private static final Map<EntityType<?>, EntityType<?>> randomEntityMap = new HashMap<>();
    private static boolean spawnRandomMobs = false;

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world instanceof ServerWorld serverWorld && spawnRandomMobs) {
                EntityType<?> randomEntityType = getRandomLivingEntityType();
                if (randomEntityType != null) {
                    spawnEntity(serverWorld, randomEntityType, pos);
                    Challenges.LOGGER.info("Spawned random mob: " + randomEntityType.getName());
                } else {
                    Challenges.LOGGER.warn("No living entities found to spawn.");
                }
                return true;
            }
            return true; // Allow normal block breaking if spawnRandomMobs is false
        });
    }

    public static void spawnRandomMobs() {
        randomEntityMap.clear();
        Challenges.LOGGER.info("Populating randomEntityMap with living entities...");

        for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
            // Check if the entity is a subclass of LivingEntity
            if (entityType.isSummonable()
                && (entityType.getSpawnGroup() == SpawnGroup.CREATURE ||
                    entityType.getSpawnGroup() == SpawnGroup.MONSTER ||
                    entityType.getSpawnGroup() == SpawnGroup.WATER_CREATURE||
                    entityType.getSpawnGroup() == SpawnGroup.UNDERGROUND_WATER_CREATURE)) {
                randomEntityMap.put(entityType, entityType);
                Challenges.LOGGER.info("Added entity to map: " + entityType.getName());
            } else {
                Challenges.LOGGER.debug("Skipped non-living entity: " + entityType.getName());
            }
        }

        Challenges.LOGGER.info("RandomEntityMap size: " + randomEntityMap.size());
        spawnRandomMobs = !randomEntityMap.isEmpty();
    }

    public static void removeRandomMobs() {
        spawnRandomMobs = false;
        Challenges.LOGGER.info("Random mob spawning disabled.");
    }

    private static EntityType<?> getRandomLivingEntityType() {
        if (randomEntityMap.isEmpty()) return null;
        Random random = Random.create();
        return randomEntityMap.values()
                .stream()
                .skip(random.nextInt(randomEntityMap.size()))
                .findFirst()
                .orElse(null);
    }

    private static void spawnEntity(ServerWorld world, EntityType<?> entityType, BlockPos pos) {
        Entity entity = entityType.create(world, SpawnReason.EVENT);
        if (entity != null) {
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center entity in block
            world.spawnEntity(entity);
        } else {
            Challenges.LOGGER.warn("Failed to create entity of type: " + entityType.getName());
        }
    }
}
