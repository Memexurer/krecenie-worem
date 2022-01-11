package pl.memexurer.krecenie.animation;

import pl.memexurer.krecenie.animation.AnimatedFrameProvider;

public interface RepeatableFrameProvider extends AnimatedFrameProvider {
    @Override
    default byte[][] provideFrame() {
        if (getFrameNumber() >= getFrameCount()) {
            if(reset())
                return null;
        }

        return provideFrame(nextFrameNumber());
    }

    byte[][] provideFrame(int number);

    int getFrameNumber();

    int nextFrameNumber();

    int getFrameCount();

    boolean reset();
}
