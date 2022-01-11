package pl.memexurer.krecenie.animation;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.LinkedList;

public class DelegatedFrameProvider implements AnimatedFrameProvider {
    private final LinkedList<EndableFrameProvider> frameProviderQueue;
    private Runnable end;
    private int size;

    public DelegatedFrameProvider(LinkedList<EndableFrameProvider> frameProviderList, Runnable end) {
        this.frameProviderQueue = frameProviderList;
        this.end = end;

        for (EndableFrameProvider frameProvider : frameProviderList)
            if (this.size != 0 && frameProvider.getSize() != this.size)
                throw new IllegalArgumentException("Invalid size at " + frameProvider + " - should be " + this.size);
            else
                this.size = frameProvider.getSize();
    }

    public DelegatedFrameProvider(Runnable end, EndableFrameProvider... providers) {
        this(new LinkedList<>(Arrays.asList(providers)), end);
    }

    @Override
    public byte[][] provideFrame() {
        if (frameProviderQueue.isEmpty()) {
            if (end != null) {
                end.run();
                end = null;
            }
            return null;
        }

        EndableFrameProvider currentFrame = frameProviderQueue.peekFirst();
        if (currentFrame.hasEnded()) {
            frameProviderQueue.removeFirst();
            return provideFrame();
        }

        return currentFrame.provideFrame();
    }

    @Override
    public int getSize() {
        return size;
    }
}
