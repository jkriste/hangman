package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents a {@link ScrollableMenuComponent} of the {@link Integer} type.
 * <br />
 * Given a {@code min} and {@code max}, the {@link ScrollableIntMenuComponent}
 * will generate an {@link Integer} array and pass it along to {@link ScrollableMenuComponent}.
 */
public final class ScrollableIntMenuComponent extends ScrollableMenuComponent<ScrollableIntMenuComponent.Int> {

    @ParametersAreNonnullByDefault
    public ScrollableIntMenuComponent(Scene view, int min, int max, double scale) {
        super(view, toArray(min, max), scale);
    }

    private static Int[] toArray(int min, int max) {
        Validator.checkArgument(max > min, "Given min '{}' >= to given max '{}'", min, max);
        Int[] array = new Int[max - min + 1];
        for (int i = min; i <= max; i++)
            array[i - min] = new Int(i);
        return array;
    }

    // This could really be better as a record (if I ever upgrade to Java 14+).
    public static class Int implements ScrollableComponentOption {

        private final int i;

        public Int(int i) {
            this.i = i;
        }

        public int getInt() {
            return i;
        }

        @Nonnull
        @Override
        public String getName() {
            return String.valueOf(this.i);
        }
    }
}