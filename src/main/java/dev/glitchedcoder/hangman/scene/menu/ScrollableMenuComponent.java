package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;

/**
 * Used to represent a selectable list of options.
 * <br />
 * This class uses {@link Object#toString()} to represent
 * the option in a plaintext format. Make sure to override
 * the {@link Object#toString()} method in your selected
 * class type ({@code <T>}) to display the option correctly.
 *
 * @param <T> The type of selectable options.
 */
public class ScrollableMenuComponent<T> extends MenuComponent {

    private int index;

    private final T[] options;

    public ScrollableMenuComponent(Scene view, T[] options, double scale) {
        super(view, '<' + options[0].toString() + '>', scale);
        Validator.checkArgument(options.length > 1, "Scrollable component options length < 2 (length: {})", options.length);
        this.index = 0;
        this.options = options;
    }

    /**
     * Decreases the current index of the {@link ScrollableMenuComponent}.
     * <br />
     * Acts as a scroll to the left.
     * <br />
     * If the current index is at {@code 0}, the index will be
     * set to the {@code size of the options - 1}.
     */
    public void scrollLeft() {
        if (index - 1 >= 0)
            --index;
        else
            index = (byte) (options.length - 1);
        setText('<' + options[index].toString() + '>');
    }

    /**
     * Increases the current index of the {@link ScrollableMenuComponent}.
     * <br />
     * Acts as a scroll to the right.
     * <br />
     * If the current index is at the {@code size of the options - 1},
     * the index will be set to {@code 0}.
     */
    public void scrollRight() {
        if (index + 1 < options.length)
            ++index;
        else
            index = 0;
        setText('<' + options[index].toString() + '>');
    }

    /**
     * Gets the selected type, {@link T}.
     * <br />
     * Accessed by {@code options[index]}.
     *
     * @return The selected type, {@link T}.
     */
    public T getSelected() {
        return this.options[index];
    }

    /**
     * Sets the current selected option of the {@link MenuComponent}.
     *
     * @param type The type to set selected.
     * @throws IllegalArgumentException Thrown if type is null or doesn't exist in the given options.
     */
    public void setIndex(@Nonnull T type) {
        Validator.requireNotNull(type, "Given type is null!");
        int index = -1;
        for (int i = 0; i < options.length; i++) {
            T t = options[i];
            if (t == type || t.equals(type)) {
                index = i;
                break;
            }
        }
        Validator.checkArgument(index != -1, "Given type '{}' does not exist in the given options.", type.toString());
        this.index = index;
        setText('<' + options[index].toString() + '>');
    }
}