package pl.memexurer.krecenie.animation;

import pl.memexurer.krecenie.animated.AnimatedImage;

public class GifFrameProvider implements EndableFrameProvider, RepeatableFrameProvider {
    private final AnimatedImage image;
    private int currentFrame;

    public GifFrameProvider(AnimatedImage image) {
        this.image = image;
    }

    @Override
    public byte[][] provideFrame() {
        if (getFrameNumber() >= getFrameCount()) {
             return null;
        }

        return provideFrame(nextFrameNumber());
    }

    @Override
    public int getSize() {
        return image.size();
    }

    @Override
    public byte[][] provideFrame(int number) {
        return image.frames()[number];
    }

    @Override
    public int getFrameCount() {
        return image.frames().length;
    }

    @Override
    public int getFrameNumber() {
        return currentFrame;
    }

    @Override
    public int nextFrameNumber() {
        return currentFrame++;
    }

    @Override
    public boolean hasEnded() {
        return currentFrame >= getFrameCount();
    }

    @Override
    public boolean reset() {
        currentFrame = 0;
        return false;
    }
}
