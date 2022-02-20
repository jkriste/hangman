package dev.glitchedcoder.hangman.ui;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static java.awt.Color.TRANSLUCENT;

public final class TexturePreprocessor {

    private String text;
    private Color color;
    private double scalar;
    private boolean removeBg;
    private BufferedImage image;

    private static final Config CONFIG;
    private static final Color[] COLOR_SWATCH;
    private static final Color BACKGROUND = Color.BLACK;

    static {
        COLOR_SWATCH = new Color[] {
                new Color(64, 64, 64),
                new Color(128, 128, 128),
                new Color(192, 192, 192),
                new Color(255, 255, 255)
        };
        CONFIG = Config.getConfig();
    }

    public TexturePreprocessor(@Nonnull Texture texture) {
        this.scalar = 1D;
        this.image = texture.asImage();
    }

    public TexturePreprocessor(@Nonnull String text) {
        Validator.requireNotNull(text, "Given text is null!");
        Validator.checkArgument(!text.isEmpty(), "Given text is empty.");
        this.text = text;
        this.scalar = 1D;
    }

    public TexturePreprocessor(@Nonnull BufferedImage image) {
        this.scalar = 1D;
        this.image = image;
    }

    /**
     * Sets the text of the {@link TexturePreprocessor}.
     * <br />
     * If using the {@link TexturePreprocessor(Texture)} or
     * {@link TexturePreprocessor(BufferedImage)} constructor,
     * this method should not be used, as it will construct
     * the {@link #build() built image} as the text.
     *
     * @param text The text to set.
     * @return The current {@link TexturePreprocessor} instance.
     */
    public TexturePreprocessor setText(@Nonnull String text) {
        Validator.requireNotNull(text, "Given text is null!");
        this.text = text;
        return this;
    }

    /**
     * Sets the scalar to scale the {@link BufferedImage} to.
     *
     * @param scalar The scale.
     * @return The current {@link TexturePreprocessor} instance.
     */
    public TexturePreprocessor scale(double scalar) {
        this.scalar *= scalar;
        return this;
    }

    /**
     * Sets the {@link Color} to adjust the {@link Texture} to.
     *
     * @param color The color to adjust to.
     * @return The current {@link TexturePreprocessor} instance.
     */
    public TexturePreprocessor color(@Nonnull Color color) {
        Validator.requireNotNull(color, "Given color is null!");
        this.color = color;
        return this;
    }

    /**
     * Marks the flag to remove the {@link #BACKGROUND} color from the image.
     *
     * @return The current {@link TexturePreprocessor} instance.
     */
    public TexturePreprocessor removeBackground() {
        this.removeBg = true;
        return this;
    }

    /**
     * Builds the new {@link BufferedImage} with the desired properties.
     * <br/>
     * Builds the {@link BufferedImage} in the following order:
     * <ol>
     *     <li>If the text is not {@code null}, it builds it as an image.</li>
     *     <li>The scale is adjusted by the resolution scalar.</li>
     *     <li>All {@link #BACKGROUND}-colored pixels are made translucent.</li>
     *     <li>The {@link #adjustColor(Color, Color) color is adjusted}.</li>
     *     <li>The image is scaled up.</li>
     * </ol>
     *
     * @return A new {@link BufferedImage} with the desired properties.
     */
    @Nonnull
    public BufferedImage build() {
        // create the image from text (if text != null)
        if (text != null) {
            BufferedImage[] images = CharMap.translate(this.text);
            this.image = images[0];
            for (int i = 1; i < images.length; i++)
                this.image = stitch(this.image, images[i]);
        }
        // multiply the scalar to the current resolution's scalar
        this.scalar *= CONFIG.getResolution().getScalar();
        // remove the background if requested
        if (removeBg) {
            int rgb = BACKGROUND.getRGB();
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (image.getRGB(i, j) != rgb)
                        continue;
                    image.setRGB(i, j, TRANSLUCENT);
                }
            }
        }
        // adjust the color if necessary
        if (color != null) {
            for (Color color : COLOR_SWATCH) {
                Color adjusted = adjustColor(color, this.color);
                int rgb = color.getRGB();
                int rgbAdjusted = adjusted.getRGB();
                for (int i = 0; i < image.getWidth(); i++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        if (this.image.getRGB(i, j) != rgb)
                            continue;
                        this.image.setRGB(i, j, rgbAdjusted);
                    }
                }
            }
        }
        // scale up the image
        if (this.scalar > 1D) {
            int newWidth = (int) (this.image.getWidth() * this.scalar);
            int newHeight = (int) (this.image.getHeight() * this.scalar);
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(this.image, 0, 0, newWidth, newHeight, null);
            graphics.dispose();
            this.image = newImage;
        }
        return this.image;
    }

    /**
     * Adjusts the two given {@link Color}s.
     * <br />
     * It should be noted that {@code c1} is being adjusted
     * to look more like {@code c2}.
     * <br />
     * Uses <a href="https://en.wikipedia.org/wiki/Relative_luminance>Relative Luminance"</a>
     * to adjust the {@link Color}.
     *
     * @param c1 The color to be adjusted.
     * @param c2 The desired color to adjust c1 to.
     * @return A new {@link Color}, with {@code c1} being adjusted to {@code c2}.
     */
    private Color adjustColor(Color c1, Color c2) {
        double luminance = getRelativeLuminance(c1);
        return new Color(
                (int) (c2.getRed() * luminance / 255),
                (int) (c2.getGreen() * luminance / 255),
                (int) (c2.getBlue() * luminance / 255)
        );
    }

    /**
     * Gets the relative luminance for the given {@link Color}.
     * <br />
     * Read more <a href="https://en.wikipedia.org/wiki/Relative_luminance">here</a>.
     *
     * @param color The color.
     * @return The relative luminance for the given {@link Color}.
     */
    private double getRelativeLuminance(Color color) {
        return (0.2126 * color.getRed())
                + (0.7152 * color.getGreen())
                + (0.0722 * color.getBlue());
    }

    /**
     * Stitches the two given {@link BufferedImage}s together.
     * <br />
     * Returns a new {@link BufferedImage} instance.
     *
     * @param i1 Image one.
     * @param i2 Image two.
     * @return A new {@link BufferedImage} representing i1+i2.
     */
    public static BufferedImage stitch(BufferedImage i1, BufferedImage i2) {
        int newWidth = i1.getWidth() + i2.getWidth();
        int newHeight = Math.max(i1.getHeight(), i2.getHeight());
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.drawImage(i1, 0, 0, null);
        graphics2D.drawImage(i2, i1.getWidth(), 0, null);
        graphics2D.dispose();
        return image;
    }
}