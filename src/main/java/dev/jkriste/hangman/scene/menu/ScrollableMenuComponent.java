package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

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
public class ScrollableMenuComponent<T extends ScrollableComponentOption> extends MenuComponent {

    private int index;
    private String title;
    private Consumer<T> consumer;

    private final T[] options;

    public ScrollableMenuComponent(Scene view, T[] options, double scale) {
        super(view, '<' + options[0].getName() + '>', scale);
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
        updateText();
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
        updateText();
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
        Validator.checkArgument(index != -1, "Given type '{}' does not exist in the given options.", type.getName());
        this.index = index;
        updateText();
    }

    /**
     * Performs the given {@link Consumer<T>} when selected.
     * 
     * @param consumer The action on selection.
     * @see #onSelect(Runnable) 
     */
    public void onSelect(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    /**
     *
     *
     * @return
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     *
     *
     * @param title
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
        updateText();
    }

    /**
     * Selects the {@link MenuComponent}.
     * <br />
     * If {@link #onSelect(Consumer)} was not called,
     * it will check if {@link #onSelect(Runnable)} was
     * called. If neither were called, this method will do nothing.
     * If both were called, only the {@link #onSelect(Consumer)} will
     * be ran.
     * <br />
     * Both methods will run <b>synchronously</b> to the running thread.
     */
    @Override
    public void select() {
        if (consumer != null) {
            consumer.accept(getSelected());
            return;
        }
        super.select();
    }

    private void updateText() {
        StringBuilder builder = new StringBuilder("<");
        if (this.title != null)
            builder.append(this.title).append(' ');
        builder.append(this.options[this.index].getName()).append('>');
        setText(builder.toString());
    }
}