package pl.memexurer.krecenie.encoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Dithering
{
    private static final byte[] mapColors;

    static {
        try(InputStream stream = Dithering.class.getClassLoader().getResourceAsStream("mapColors/mapColors_V8.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {
            mapColors = (byte[]) objectInputStream.readObject();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static byte[] getMapColors() {
        return mapColors;
    }

    public static BufferedImage floydSteinbergDithering(final BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final C3[][] array = new C3[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                array[i][j] = new C3(bufferedImage.getRGB(j, i));
            }
        }
        final byte[] mapColors = getMapColors();
        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int l = 0; l < bufferedImage.getWidth(); ++l) {
                if ((bufferedImage.getRGB(l, k) >> 24 & 0xFF) < 122) {
                    bufferedImage.setRGB(l, k, 0);
                }
                else {
                    final C3 c3 = array[k][l];
                    final C3 c4 = new C3(MapPalette.getColor(mapColors[c3.toColor().getRGB() & 0xFFFFFF]).getRGB());
                    bufferedImage.setRGB(l, k, c4.toColor().getRGB());
                    final C3 sub = c3.sub(c4);
                    if (l + 1 < width) {
                        array[k][l + 1] = array[k][l + 1].add(sub.mul(0.4375));
                    }
                    if (l - 1 >= 0 && k + 1 < height) {
                        array[k + 1][l - 1] = array[k + 1][l - 1].add(sub.mul(0.1875));
                    }
                    if (k + 1 < height) {
                        array[k + 1][l] = array[k + 1][l].add(sub.mul(0.3125));
                    }
                    if (l + 1 < width && k + 1 < height) {
                        array[k + 1][l + 1] = array[k + 1][l + 1].add(sub.mul(0.0625));
                    }
                }
            }
        }
        return bufferedImage;
    }

    public static class C3
    {
        int r;
        int g;
        int b;

        public C3(final int n) {
            super();
            final Color color = new Color(n);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        public C3(final int r, final int g, final int b) {
            super();
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public C3 add(final C3 c3) {
            return new C3(this.r + c3.r, this.g + c3.g, this.b + c3.b);
        }

        public C3 sub(final C3 c3) {
            return new C3(this.r - c3.r, this.g - c3.g, this.b - c3.b);
        }

        public C3 mul(final double n) {
            return new C3((int)(n * this.r), (int)(n * this.g), (int)(n * this.b));
        }

        public int diff(final C3 c3) {
            return Math.abs(this.r - c3.r) + Math.abs(this.g - c3.g) + Math.abs(this.b - c3.b);
        }

        public int toRGB() {
            return this.toColor().getRGB();
        }

        public Color toColor() {
            return new Color(this.clamp(this.r), this.clamp(this.g), this.clamp(this.b));
        }

        public int clamp(final int n) {
            return Math.max(0, Math.min(255, n));
        }
    }
}