package pl.memexurer.krecenie.config;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Bukkit;
import pl.memexurer.krecenie.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PluginConfiguration extends OkaeriConfig {
    private final List<MapItem> mapItems = Arrays.asList(
            new MapItem(12.5, "say {PLAYER} wylosowal kurwa czolga"),
            new MapItem(12.5, "say {PLAYER} wylosowal cosmetiky"),
            new MapItem(12.5, "say {PLAYER} wylosowal pitosiwo"),
            new MapItem(12.5, "say {PLAYER} wylosowal cosmetiky"),
            new MapItem(12.5, "say {PLAYER} wylosowal pitosiwo"),
            new MapItem(12.5, "say {PLAYER} wylosowal trolfejs"),
            new MapItem(12.5, "say {PLAYER} wylosowal range"),
            new MapItem(12.5, "say {PLAYER} wylosowal pitosiwo")
    );
    public List<UUID> itemFrames = new ArrayList<>();
    public Direction direction;

    private static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }

    public MapItem getRandomMapItem() {
        return mapItems.get(ThreadLocalRandom.current().nextInt(mapItems.size() - 1));
    }

    public int getRandomItem(MapItem item, int frameCount) {
        double indexFrames = (double) frameCount / mapItems.size();

        int min = (int) (mapItems.indexOf(item) * indexFrames) + 2;
        int max = (int) (((mapItems.indexOf(item) + 1) * indexFrames)) % frameCount - 2;

        return getRandomInt(min, max);
    }
}
