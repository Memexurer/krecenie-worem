package pl.memexurer.krecenie.animation.function;

import pl.memexurer.krecenie.animation.EndableFrameProvider;
import pl.memexurer.krecenie.function.Transformation;

public abstract class FunctionFrameProvider implements EndableFrameProvider {
    protected final Transformation function;
    private final int tickLength;
    private int tick;

    protected FunctionFrameProvider(Transformation function, int tickLength) {
        this.function = function;
        this.tickLength = tickLength;
    }

    @Override
    public byte[][] provideFrame() {
        if (tick >= tickLength) {
            return null;
        }

        return provideFrame(function.applyInt(tick++, tickLength));
    }

    @Override
    public boolean hasEnded() {
        return tick >= tickLength;
    }

    public abstract byte[][] provideFrame(int number);
}
