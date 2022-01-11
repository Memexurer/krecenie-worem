package pl.memexurer.krecenie.animated;

import org.bukkit.entity.Player;
import pl.memexurer.krecenie.MapImage;
import pl.memexurer.krecenie.WallMapDisplay;
import pl.memexurer.krecenie.animation.AnimatedFrameProvider;

public class AnimatedImageDisplayTask<T extends AnimatedFrameProvider> implements Runnable {
    private final Player player;

    private final MapImage mapImage;
    private final T provider;

    public AnimatedImageDisplayTask(WallMapDisplay display, Player player, T provider) {
        this.provider = provider;
        this.player = player;

        this.mapImage = MapImage.create(provider.getSize(), player.getWorld());
        display.applyImage(mapImage);
    }

    @Override
    public void run() {
        byte[][] frame = provider.provideFrame();
        if (frame != null)
            mapImage.updateMap(player, frame);
    }
}
