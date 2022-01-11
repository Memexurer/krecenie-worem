package pl.memexurer.krecenie.animation.function;

import pl.memexurer.krecenie.animation.RepeatableFrameProvider;
import pl.memexurer.krecenie.function.Transformation;

public class FunctionFrameProviderWrapper extends FunctionFrameProvider {
    private final RepeatableFrameProvider frameProvider;

    public FunctionFrameProviderWrapper(Transformation transformation, RepeatableFrameProvider frameProvider) {
        super(transformation, frameProvider.getFrameCount());
        this.frameProvider = frameProvider;
    }

    @Override
    public int getSize() {
        return frameProvider.getSize();
    }

    @Override
    public byte[][] provideFrame(int number) {
        return frameProvider.provideFrame(number);
    }
}
