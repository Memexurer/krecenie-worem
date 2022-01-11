package pl.memexurer.krecenie.function;

public interface DoubleTransformation extends Transformation {
    @Override
    default Integer applyInt(Integer frame, Integer max) {
        return Math.min((int) Math.round(apply((double) frame / max) * frame), max - 1);
    }

    double apply(double percentage);
}
