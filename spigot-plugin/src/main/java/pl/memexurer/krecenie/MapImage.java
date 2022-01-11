package pl.memexurer.krecenie;

import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import java.util.Collections;

public class MapImage {
    private final int[] frames;

    public MapImage(int[] frames) {
        this.frames = frames;
    }

    public static MapImage create(int size, World world) {
        int[] frames = new int[size];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = generateMapID(world);
        }
        return new MapImage(frames);
    }

    private static int generateMapID(World world) {
        final MapView map = Bukkit.createMap((world != null) ? world : Bukkit.getWorlds().get(0));
        map.setCenterX(Integer.MAX_VALUE);
        map.setCenterZ(Integer.MAX_VALUE);
        map.getRenderers().forEach(map::removeRenderer);
        return map.getId();
    }

    private static void writeMapUpdate(Player player, byte[] contents, int mapId) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutMap(mapId, (byte) 4, Collections.emptyList(), contents, 0, 0, 128, 128));
    }

    public int[] getFrames() {
        return frames;
    }

    public void updateMap(Player player, byte[][] contents) {
        if(contents == null || contents.length == 0)
            return;

        if (contents.length != frames.length)
            throw new IllegalArgumentException("Invalid content size. (should be " + frames.length + ", not " + contents.length + ")");

        for (int i = 0; i < contents.length; i++) {
            writeMapUpdate(player, contents[i], frames[i]);
        }
    }
}
