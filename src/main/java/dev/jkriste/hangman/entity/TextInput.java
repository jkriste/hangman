package dev.jkriste.hangman.entity;

import dev.jkriste.hangman.ui.CharMap;
import dev.jkriste.hangman.util.Constants;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;
import dev.jkriste.hangman.window.key.Key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class TextInput extends Entity {

    private final byte length;
    private final Rectangle bounds;
    private final List<CharacterInput> list;

    private static final double DEFAULT_SCALE = 5;

    public TextInput(Scene scene, int length, boolean spaces) {
        this(scene, length, DEFAULT_SCALE, spaces);
    }

    public TextInput(Scene scene, int length, double scalar, boolean spaces) {
        super(scene, EntityType.TEXT_INPUT);
        this.length = (byte) Validator.constrain(length, Constants.MIN_WORD_LENGTH, Constants.MAX_WORD_LENGTH);
        this.list = new ArrayList<>();
        for (byte i = 0; i < this.length; i++)
            list.add(new CharacterInput(scene, scalar, spaces));
        Rectangle charBounds = list.get(0).getBounds();
        this.bounds = new Rectangle(charBounds.width * this.length, charBounds.height);
    }

    @Override
    protected void onLoad() {
        Rectangle charBounds = list.get(0).getBounds();
        for (byte i = 0; i < this.length; i++) {
            Location loc = new Location(getLocation().getX() + (i * charBounds.width), getLocation().getY());
            this.list.get(i).setLocation(loc);
        }
    }

    @Override
    protected void onUnload() {
        for (CharacterInput input : list)
            input.onUnload();
        list.clear();
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
        for (CharacterInput input : this.list)
            input.draw(graphics);
    }

    /**
     * Gets the word stored in the {@link TextInput}.
     * <br />
     * If {@link #isInvalid() invalid}, there will be parts
     * of the returned string that will have a space.
     *
     * @return The word stored in the {@link TextInput}.
     */
    public String getInput() {
        StringBuilder builder = new StringBuilder();
        for (CharacterInput input : this.list)
            builder.append(input.getCharacter());
        return builder.toString();
    }

    /**
     * Sets the character for the {@link CharacterInput} at the given index.
     *
     * @param index The index of the character input.
     * @param c     The character to put in the input.
     * @throws IllegalArgumentException Thrown if the index is out of bounds.
     */
    public void setCharacter(int index, char c) {
        Validator.checkArgument(index >= 0 && index < list.size(), "Invalid index {}", index);
        this.list.get(index).setCharacter(c);
    }

    /**
     * Handles {@link Key} input.
     * <br />
     * The given {@link Key} should be limited to
     * {@link Key#BACKSPACE} or {@link Key#WRITABLE_KEYS}.
     * <br />
     * Used to either fill or empty {@link CharacterInput}s.
     *
     * @param key The key to handle.
     * @throws IllegalArgumentException Thrown if the given key is null.
     * @throws IllegalArgumentException Thrown if not BACKSPACE or a WRITABLE_KEY.
     */
    public void handleKeyInput(@Nonnull Key key) {
        Validator.requireNotNull(key, "Given key is null!");
        // check if writable key OR backspace
        Validator.checkArgument(!key.isActionKey() || key == Key.BACKSPACE, "Given key is invalid: {}", key.name());
        if (key == Key.BACKSPACE) {
            CharacterInput input = lastTaken();
            if (input == null)
                return;
            input.setCharacter(CharacterInput.EMPTY_CHAR);
        } else {
            CharacterInput input = nextAvailable();
            if (input == null)
                return;
            CharMap map = Validator.requireNotNull(key.getMap());
            input.setCharacter(map.getCharacter());
        }
    }

    /**
     * Checks if the {@link TextInput} is invalid.
     * <br />
     * Returns true when any single {@link CharacterInput}
     * {@link CharacterInput#isEmpty() is empty}.
     *
     * @return True if invalid, false otherwise.
     */
    public boolean isInvalid() {
        for (CharacterInput input : this.list) {
            if (input.isEmpty())
                return true;
        }
        return false;
    }

    /**
     * Locks the {@link CharacterInput} at the given index.
     * <br />
     * Locked {@link CharacterInput}s should not be modifiable
     * through keys such as {@link Key#BACKSPACE} or {@link Key#WRITABLE_KEYS}.
     *
     * @param index The index to lock at.
     */
    public void lockCharacter(int index) {
        index = Validator.constrain(index, 0, length - 1);
        this.list.get(index).setLocked(true);
    }

    /**
     * Sets all {@link CharacterInput}s to locked or unlocked.
     * <br />
     * Locked {@link CharacterInput}s should not be modifiable
     * through keys such as {@link Key#BACKSPACE} or {@link Key#WRITABLE_KEYS}.
     *
     * @param locked True to lock all, false otherwise.
     */
    public void lockAll(boolean locked) {
        for (CharacterInput input : this.list)
            input.setLocked(locked);
    }

    /**
     * Checks if all of the {@link CharacterInput}s are locked.
     * <br />
     * Locked {@link CharacterInput}s should not be modifiable
     * through keys such as {@link Key#BACKSPACE} or {@link Key#WRITABLE_KEYS}.
     *
     * @return True if all {@link CharacterInput}s are locked, false otherwise.
     */
    public boolean allLocked() {
        for (CharacterInput input : this.list) {
            if (input.isUnlocked())
                return false;
        }
        return true;
    }

    /**
     * Gets the first available {@link CharacterInput}.
     * <br />
     * Used for {@link Key#WRITABLE_KEYS}.
     * <br />
     * Can return {@code null} if none are available.
     *
     * @return The first available {@link CharacterInput} or {@code null}.
     */
    @Nullable
    private CharacterInput nextAvailable() {
        for (CharacterInput input : list) {
            if (input.isEmpty() && input.isUnlocked())
                return input;
        }
        return null;
    }

    /**
     * Gets the last taken {@link CharacterInput}.
     * <br />
     * Used for {@link Key#BACKSPACE}.
     * <br />
     * Can return {@code null} if empty.
     *
     * @return The last taken {@link CharacterInput} or {@code null}.
     */
    @Nullable
    private CharacterInput lastTaken() {
        for (byte i = (byte) (length - 1); i >= 0; i--) {
            CharacterInput input = list.get(i);
            if (!input.isEmpty() && input.isUnlocked())
                return input;
        }
        return null;
    }
}