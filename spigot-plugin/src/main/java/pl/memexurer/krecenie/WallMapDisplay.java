package pl.memexurer.krecenie;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class WallMapDisplay {
    private final List<ItemFrame> itemFrames;
    private final Vector firstFrame;
    private final Direction direction;
    private MapImage image;

    public WallMapDisplay(List<ItemFrame> entityFrames, Vector firstFrame, Direction direction) {
        this.itemFrames = entityFrames;
        this.firstFrame = firstFrame;
        this.direction = direction;
    }

    private static void applyMapId(ItemFrame frame, int mapId) {
        ItemStack itemStack = new ItemStack(Material.MAP);
        itemStack.setDurability((short) mapId);
        frame.setItem(itemStack);
    }

    public void applyImage(MapImage image) {
        this.image = image;
        updateFrames();
    }

    private void updateFrames() {
        for (ItemFrame frame : itemFrames) {
            applyMapId(frame, this.image.getFrames()[getWeight(frame)]);
        }
    }

    private int getWeight(ItemFrame frame) {
        Location location = frame.getLocation();

        Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()).subtract(firstFrame);

        int n = (int) vector.dot(direction.getXDirection());
        int n2 = (int) vector.dot(new Vector(0, -1, 0));

        /*
        Preconditions.checkArgument(n2 >= 0, "Y coord was smaller than 0: " + n2);
        Preconditions.checkArgument(n2 < 5, "Y coord was bigger than 5: " + n2);
        Preconditions.checkArgument(n >= 0, "X coord was smaller than 0: " + n);
        Preconditions.checkArgument(n < 5, "X coord was bigger than 5: " + n);
         */

        return n + (n2 * 5);
    }
}
