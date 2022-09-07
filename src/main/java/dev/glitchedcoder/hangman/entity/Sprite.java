package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.ui.AnimatedTexture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite extends Entity {

    private int index;

    private final byte speed;
    private final Rectangle bounds;
    private final BufferedImage[] textures;

    public Sprite(Scene scene, BufferedImage[] images, byte speed, double scalar) {
        super(scene, EntityType.SPRITE);
        this.textures = new BufferedImage[images.length];
        for (int i = 0; i < images.length; i++) {
            this.textures[i] = new TexturePreprocessor(images[i])
                    .scale(scalar)
                    .build();
        }
        this.speed = speed;
        this.bounds = new Rectangle(textures[0].getWidth(), textures[0].getHeight());
    }

    public Sprite(Scene scene, AnimatedTexture texture, byte speed, double scalar) {
        super(scene, EntityType.SPRITE);
        BufferedImage[] array = texture.asImageArray();
        this.textures = new BufferedImage[array.length];
        for (int i = 0; i < array.length; i++) {
            this.textures[i] = new TexturePreprocessor(array[i])
                    .scale(scalar)
                    .build();
        }
        this.speed = speed;
        this.bounds = new Rectangle(textures[0].getWidth(), textures[0].getHeight());
    }

    @Override
    protected void onLoad() {
        // do nothing
    }

    @Override
    protected void onUnload() {
        for (int i = 0; i < textures.length; i++)
            this.textures[i] = null;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        if (count % speed == 0) {
            if (index < textures.length - 1)
                ++index;
            else
                index = 0;
        }
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        graphics.drawImage(textures[index], getLocation().getX(), getLocation().getY(), null);
    }
}