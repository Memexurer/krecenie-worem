package pl.memexurer.krecenie.animated;

import java.io.*;

public final class AnimatedImageParser {
    private AnimatedImageParser() {
    }

    public static AnimatedImage readAnimatedImage(InputStream stream) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(stream)) {
            int width = inputStream.readByte(), height = inputStream.readByte(), frameCount = inputStream.readInt(), size = width * height;

            byte[][][] animatedImage = new byte[frameCount][size][16384];
            for(int i = 0; i < frameCount; i++) {
                for(int j = 0; j < size; j++) {
                    inputStream.read(animatedImage[i][j]);
                }
            }

            return new AnimatedImage(width, height, animatedImage);
        }
    }

    public static AnimatedImage readAnimatedImage(File file) throws IOException {
        try(FileInputStream inputStream = new FileInputStream(file)) {
            return readAnimatedImage(inputStream);
        }
    }
}
