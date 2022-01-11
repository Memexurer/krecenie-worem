package pl.memexurer.krecenie.animated;

public record AnimatedImage(int width, int height, byte[][][] frames) {
    public int size() {
        return width * height;
    }
}
