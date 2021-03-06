package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class LightFixture extends Entity {

    private boolean on;

    private final Rectangle bounds;
    private final byte flickerChance;
    private final BufferedImage lightOn;
    private final BufferedImage lightOff;

    private static final Random RANDOM = new Random();

    public LightFixture(Scene scene, byte flickerChance, double scale) {
        super(scene, EntityType.LIGHT_FIXTURE);
        this.lightOn = new TexturePreprocessor(Texture.LIGHT_FIXTURE_ON)
                .scale(scale)
                .build();
        this.lightOff = new TexturePreprocessor(Texture.LIGHT_FIXTURE_OFF)
                .scale(scale)
                .build();
        this.flickerChance = flickerChance;
        this.bounds = new Rectangle(lightOn.getWidth(), lightOn.getHeight());
    }

    @Override
    protected void onLoad() {
        this.on = true;
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
        this.on = RANDOM.nextInt(flickerChance) != 0;
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        graphics.drawImage(on ? lightOn : lightOff, getLocation().getX(), getLocation().getY(), null);
    }
}