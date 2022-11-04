package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.scene.IconLayout;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Represents an overlay of {@link Icon}s
 * that the user can use to know which
 * {@link dev.glitchedcoder.hangman.window.key.Key}s
 * are registered by the {@link Scene}.
 * <br />
 * Since there are a total of 9 {@link Icon}s,
 * the {@link Icon} matrix can support all 9.
 * <br />
 * The matrix design allows configuration
 * and customization by the {@link Scene}.
 */
public class IconOverlay extends Entity {

    private final Color color;
    private final double scale;
    private final Rectangle bounds;
    private final Icon[][] iconMatrix;
    private final BufferedImage[] images;

    private static final byte MATRIX_SIZE = 3;
    private static final double DEFAULT_SCALE = 2.5;

    public IconOverlay(Scene scene, Color color) {
        this(scene, color, DEFAULT_SCALE);
    }

    public IconOverlay(Scene scene, Color color, double scale) {
        super(scene, EntityType.ICON_OVERLAY);
        this.color = color;
        this.scale = scale;
        this.images = new BufferedImage[MATRIX_SIZE];
        this.iconMatrix = new Icon[MATRIX_SIZE][MATRIX_SIZE];
        // creating an empty icon and scaling it to get the correct entity size
        BufferedImage iconSize = new TexturePreprocessor(Texture.BLANK_ICON)
                .scale(scale)
                .build();
        this.bounds = new Rectangle(iconSize.getWidth() * MATRIX_SIZE, iconSize.getHeight() * MATRIX_SIZE);
        setRenderPriority(RenderPriority.MIN);
    }

    @Override
    protected void onLoad() {
        // do nothing
    }

    @Override
    protected void onUnload() {
        for (byte i = 0; i < MATRIX_SIZE; i++)
            images[i] = null;
        for (byte i = 0; i < MATRIX_SIZE; i++) {
            for (byte j = 0; j < MATRIX_SIZE; j++)
                iconMatrix[i][j] = null;
        }
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        // do nothing
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        int height = this.images[0].getHeight();
        for (byte i = 0; i < MATRIX_SIZE; i++)
            graphics.drawImage(this.images[i], getLocation().getX(), getLocation().getY() + (i * height), null);
    }

    /**
     * Checks if any {@link Icon}s exist in the {@link IconOverlay}.
     *
     * @return True if {@link Icon}s exist, false otherwise.
     */
    public boolean hasIcons() {
        for (byte i = 0; i < MATRIX_SIZE; i++) {
            for (byte j = 0; j < MATRIX_SIZE; j++) {
                if (iconMatrix[i][j] != null)
                    return true;
            }
        }
        return false;
    }

    /**
     * Sets the given {@link Icon} at the given x and y indices.
     * <br />
     * The given {@code x} and {@code y} are constrained between
     * {@code (O,} {@link #MATRIX_SIZE}{@code ]}.
     *
     * @param icon The icon to set.
     * @param x The row index.
     * @param y The col index.
     */
    public void setIcon(@Nonnull Icon icon, int x, int y) {
        x = Validator.constrain(x, 0, MATRIX_SIZE - 1);
        y = Validator.constrain(y, 0, MATRIX_SIZE - 1);
        this.iconMatrix[x][y] = icon;
        update();
    }

    /**
     * Sets the {@link IconOverlay} to the given {@link IconLayout}.
     * <br />
     * Should be a max size of {@link #MATRIX_SIZE}.
     *
     * @param layout The layout to set.
     */
    public void setIcons(@Nonnull IconLayout layout) {
        this.setIcons(layout.getIconMatrix());
    }

    /**
     * Sets the {@link IconOverlay} {@link Icon} matrix with the given.
     * <br />
     * Should be a max size of {@link #MATRIX_SIZE}.
     *
     * @param iconMatrix The matrix to set.
     */
    public void setIcons(@Nonnull Icon[][] iconMatrix) {
        if (iconMatrix.length == 0) {
            clear();
            return;
        }
        for (byte i = 0; i < iconMatrix.length; i++) {
            for (byte j = 0; j < iconMatrix[i].length; j++) {
                this.iconMatrix[i][j] = iconMatrix[i][j];
            }
        }
        update();
    }

    /**
     * Clears all {@link Icon}s from the {@link IconOverlay}.
     */
    public void clear() {
        for (byte i = 0; i < MATRIX_SIZE; i++) {
            for (byte j = 0; j < MATRIX_SIZE; j++) {
                this.iconMatrix[i][j] = null;
            }
        }
        update();
    }

    /**
     * Removes the first instance of the given {@link Icon}, if any.
     *
     * @param icon The icon to remove.
     */
    public void removeIcon(@Nonnull Icon icon) {
        for (byte i = 0; i < MATRIX_SIZE; i++) {
            for (byte j = 0; j < MATRIX_SIZE; j++) {
                if (iconMatrix[i][j] != icon)
                    continue;
                iconMatrix[i][j] = null;
                update();
                return;
            }
        }
    }

    /**
     * Updates the {@link IconOverlay}.
     * <br />
     * Builds a 2d-matrix imitation of the contained {@link Icon}s.
     */
    private void update() {
        for (byte b = 0; b < MATRIX_SIZE; b++) {
            BufferedImage image = null;
            for (byte c = 0; c < MATRIX_SIZE; c++) {
                Icon i = this.iconMatrix[b][c];
                BufferedImage icon = new TexturePreprocessor(i != null ? i.getTexture() : Texture.BLANK_ICON)
                        .color(color)
                        .scale(scale)
                        .build();
                if (image == null)
                    image = icon;
                else
                    image = TexturePreprocessor.stitch(image, icon);
            }
            this.images[b] = image;
        }
    }
}
