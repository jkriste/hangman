package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CharacterInput extends Entity {

    private char character;
    private boolean locked;
    private BufferedImage texture;
    private BufferedImage underscore;

    private final double scale;
    private final boolean space;
    private final Rectangle bounds;

    private static final Color LOCKED_COLOR = Color.GREEN;
    private static final Color UNLOCKED_COLOR = Color.WHITE;

    CharacterInput(Scene scene, double scalar, boolean space) {
        super(scene, EntityType.CHARACTER_INPUT);
        this.scale = scalar;
        this.space = space;
        this.character = ' ';
        this.underscore = new TexturePreprocessor(space ? "_ " : "_")
                .color(UNLOCKED_COLOR)
                .scale(scalar)
                .removeBackground()
                .build();
        this.bounds = new Rectangle(underscore.getWidth(), underscore.getHeight());
    }

    @Override
    protected void onLoad() {
        // do nothing
    }

    @Override
    protected void onUnload() {
        this.texture = null;
        this.underscore = null;
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
        graphics.drawImage(underscore, getLocation().getX(), getLocation().getY(), null);
        if (!isEmpty())
            graphics.drawImage(texture, getLocation().getX(), getLocation().getY() - 15, null);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        update();
    }

    public void setCharacter(char c) {
        this.character = c;
        update();
    }

    public boolean isUnlocked() {
        return !locked;
    }

    public char getCharacter() {
        return character;
    }

    public boolean isEmpty() {
        return this.character == ' ';
    }

    private void update() {
        this.underscore = new TexturePreprocessor(space ? "_ " : "_")
                .color(locked ? LOCKED_COLOR : UNLOCKED_COLOR)
                .scale(scale)
                .removeBackground()
                .build();
        if (!isEmpty()) {
            this.texture = new TexturePreprocessor(String.valueOf(character))
                    .color(Color.WHITE)
                    .scale(scale)
                    .removeBackground()
                    .build();
        }
    }
}