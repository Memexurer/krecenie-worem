package pl.memexurer.krecenie.encoder;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class MediaEncoderBootstrap {
    private MediaEncoderBootstrap() {
    }

    public static void main(String[] args) throws Throwable {
        args = new String[] {"mapa.gif", "loop"};
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(args[1] + ".map"));

        GifDecoder.GifImage gif = GifDecoder.read(new FileInputStream(args[0]));
        final int frameCount = gif.getFrameCount();
        System.out.println("Frame count: " + frameCount);
        if (gif.getWidth() % 128 != 0 || gif.getHeight() % 128 != 0) {
            System.out.println("Invalid gif size: should be dividable by 128 (width: " + gif.getWidth() + ", height: " + gif.getHeight());
            return;
        }
        outputStream.writeByte(gif.getWidth() / 128);
        outputStream.writeByte(gif.getHeight() / 128);
        outputStream.writeInt(frameCount);

        for (int i = 0; i < frameCount; i++) {
            final BufferedImage image = gif.getFrame(i);

            for (byte[] data1 : flatten(image, image.getWidth() / 128, image.getHeight() / 128)) {
                outputStream.write(data1);
            }
        }
    }

    private static byte[][] flatten(final BufferedImage bufferedImage, int width, int height) {
        final byte[][] array = new byte[width * height][16384];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                final BufferedImage subimage = bufferedImage.getSubimage(i * 128, j * 128, 128, 128);
                final byte[] mapColors = Dithering.getMapColors();
                for (int k = 0; k < 128; ++k) {
                    for (int l = 0; l < 128; ++l) {
                        final int rgb = subimage.getRGB(k, l);
                        if ((rgb >> 24 & 0xFF) < 122) {
                            array[i + j * width][128 * l + k] = 0;
                        } else {
                            array[i + j * width][128 * l + k] = mapColors[rgb & 0xFFFFFF];
                        }
                    }
                }
            }
        }
        return array;
    }
}
