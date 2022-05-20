package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.window.Scene;

/**
 * Represents a {@link ScrollableMenuComponent} of the {@link Integer} type.
 * <br />
 * Given a {@code min} and {@code max}, the {@link ScrollableIntMenuComponent}
 * will generate an {@link Integer} array and pass it along to {@link ScrollableMenuComponent}.
 */
public class ScrollableIntMenuComponent extends ScrollableMenuComponent<Integer> {

    public ScrollableIntMenuComponent(Scene view, int min, int max, double scale) {
        super(view, toArray(min, max), scale);
    }

    private static Integer[] toArray(int from, int to) {
        Integer[] array = new Integer[to - from + 1];
        for (int i = from; i <= to; i++)
            array[i - from] = i;
        return array;
    }
}