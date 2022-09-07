package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

/**
 * Represents a {@link ScrollableMenuComponent} of the {@link Integer} type.
 * <br />
 * Given a {@code min} and {@code max}, the {@link ScrollableIntMenuComponent}
 * will generate an {@link Integer} array and pass it along to {@link ScrollableMenuComponent}.
 */
public final class ScrollableIntMenuComponent extends ScrollableMenuComponent<Integer> {

    public ScrollableIntMenuComponent(Scene view, int min, int max, double scale) {
        super(view, toArray(min, max), scale);
    }

    private static Integer[] toArray(int min, int max) {
        Validator.checkArgument(max > min, "Given min '{}' >= to given max '{}'", min, max);
        Integer[] array = new Integer[max - min + 1];
        for (int i = min; i <= max; i++)
            array[i - min] = i;
        return array;
    }
}