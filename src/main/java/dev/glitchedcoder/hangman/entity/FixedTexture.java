package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class FixedTexture extends Entity {

    private final Rectangle bounds;
    private final BufferedImage image;

    @ParametersAreNonnullByDefault
    public FixedTexture(Scene scene, Texture texture) {
        super(scene, EntityType.FIXED_TEXTURE);
        this.image = texture.asImage();
        Validator.requireNotNull(this.image, "Failed to load texture '{}'!", texture.name());
        this.bounds = new Rectangle(image.getWidth(), image.getHeight());
    }

    @ParametersAreNonnullByDefault
    public FixedTexture(Scene scene, BufferedImage image) {
        super(scene, EntityType.FIXED_TEXTURE);
        Validator.requireNotNull(image, "Given image is null!");
        this.image = image;
        this.bounds = new Rectangle(image.getWidth(), image.getHeight());
    }

    @Override
    protected void onLoad() {
        // do nothing
    }

    @Override
    protected void onUnload() {
        // do nothing
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
        graphics.drawImage(this.image, getLocation().getX(), getLocation().getY(), null);
    }
}