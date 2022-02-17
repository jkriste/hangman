package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.window.Scene;

public class ScrollableMenuComponent<T> extends MenuComponent {

    private byte index;

    private final T[] options;

    public ScrollableMenuComponent(Scene view, T[] options, double scale) {
        super(view, "<" + options[0].toString() + ">", scale);
        this.index = 0;
        this.options = options;
    }

    public void scrollLeft() {
        if (index - 1 >= 0)
            --index;
        else
            index = (byte) (options.length - 1);
        setText("<" + options[index].toString() + ">");
    }

    public void scrollRight() {
        if (index + 1 < options.length)
            ++index;
        else
            index = 0;
        setText("<" + options[index].toString() + ">");
    }

    public T getSelected() {
        return this.options[index];
    }
}