package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.ui.CharMap;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;

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

    public String getInput() {
        StringBuilder builder = new StringBuilder();
        for (CharacterInput input : this.list)
            builder.append(input.getCharacter());
        return builder.toString();
    }

    public void handleKeyInput(@Nonnull Key key) {
        Validator.requireNotNull(key, "Given key is null!");
        // check if writable key OR backspace
        Validator.checkArgument(!key.isActionKey() || key == Key.BACKSPACE, "Given key is invalid: {}", key.name());
        if (key == Key.BACKSPACE) {
            CharacterInput input = lastTaken();
            if (input == null)
                return;
            input.setCharacter(' ');
        } else {
            CharacterInput input = nextAvailable();
            if (input == null)
                return;
            CharMap map = Validator.requireNotNull(key.getMap());
            input.setCharacter(map.getCharacter());
        }
    }

    public boolean isValid() {
        for (CharacterInput input : this.list) {
            if (input.isEmpty())
                return false;
        }
        return true;
    }

    public void lockCharacter(int index) {
        index = Validator.constrain(index, 0, length - 1);
        this.list.get(index).setLocked(true);
    }

    @Nullable
    private CharacterInput nextAvailable() {
        for (CharacterInput input : list) {
            if (input.isEmpty() && input.isUnlocked())
                return input;
        }
        return null;
    }

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