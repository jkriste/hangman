package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.scene.menu.ComponentOption;

/**
 * Represents most, if not all, 16:9 aspect ratio resolutions.
 */
public enum Resolution implements ComponentOption {

    P576("1024x576", 1024, 576, 1D),
    P648("1152x648", 1152, 648, 1.125),
    P720("1280x720", 1280, 720, 1.25),
    P900("1600x900", 1600, 900, 1.5625),
    P1080("1920x1080", 1920, 1080, 1.875),
    P1440("2560x1440", 2560, 1440, 2.5),
    P2160("3840x2160", 3840, 2160, 3.75);

    private final String name;
    private final short width;
    private final short height;
    private final double scalar;

    public static final Resolution[] values = values();

    Resolution(String name, int width, int height, double scalar) {
        this.name = name;
        this.width = (short) width;
        this.height = (short) height;
        this.scalar = scalar;
    }

    @Override
    public String getName() {
        return name;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public double getScalar() {
        return scalar;
    }
}