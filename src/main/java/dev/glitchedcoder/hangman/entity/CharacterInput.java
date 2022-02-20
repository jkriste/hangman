package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.json.Config;
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

    private static final char EMPTY_CHAR;
    private static final byte SPACE_BETWEEN;
    private static final Color LOCKED_COLOR;
    private static final Color UNLOCKED_COLOR;

    static {
        EMPTY_CHAR = ' ';
        LOCKED_COLOR = Color.GREEN;
        UNLOCKED_COLOR = Color.WHITE;
        Config config = Config.getConfig();
        SPACE_BETWEEN = (byte) (16 * config.getResolution().getScalar());
    }

    CharacterInput(Scene scene, double scalar, boolean space) {
        super(scene, EntityType.CHARACTER_INPUT);
        this.scale = scalar;
        this.space = space;
        this.character = EMPTY_CHAR;
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
            graphics.drawImage(texture, getLocation().getX(), getLocation().getY() - SPACE_BETWEEN, null);
    }

    /**
     * Locks this {@link CharacterInput}.
     * <br />
     * When locked, the character stored in
     * this instance cannot be changed until
     * unlocked.
     *
     * @param locked True to lock, false otherwise.
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        update();
    }

    /**
     * Sets the character of the {@link CharacterInput}.
     *
     * @param c The char to set.
     */
    public void setCharacter(char c) {
        this.character = c;
        update();
    }

    /**
     * Checks if the {@link CharacterInput} is unlocked.
     * <br />
     * When unlocked, the {@link CharacterInput}'s
     * character can be set by the {@link TextInput}.
     *
     * @return True if unlocked, false otherwise.
     */
    public boolean isUnlocked() {
        return !locked;
    }

    /**
     * Gets the character stored in this
     * instance of {@link CharacterInput}.
     * <br />
     * If {@link #isEmpty() empty}, it will
     * return a {@code ' '} (space) character.
     *
     * @return The character stored in the {@link CharacterInput}.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Checks if the {@link CharacterInput} is empty.
     *
     * @return True if the {@link CharacterInput} is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.character == EMPTY_CHAR;
    }

    /**
     * Updates the {@link CharacterInput}.
     * <br />
     * Constructs both the underscore and the character (if present).
     */
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