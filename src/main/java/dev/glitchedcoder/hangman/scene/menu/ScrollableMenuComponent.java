package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.window.Scene;

public class ScrollableMenuComponent extends MenuComponent {

    private byte index;

    private final ComponentOption[] options;

    public ScrollableMenuComponent(Scene view, ComponentOption[] options, double scale) {
        super(view, "<" + options[0].getName() + ">", scale);
        this.index = 0;
        this.options = options;
    }

    public void scrollLeft() {
        if (index - 1 >= 0)
            --index;
        else
            index = (byte) (options.length - 1);
        setText("<" + options[index].getName() + ">");
    }

    public void scrollRight() {
        if (index + 1 < options.length)
            ++index;
        else
            index = 0;
        setText("<" + options[index].getName() + ">");
    }

    public ComponentOption getSelected() {
        return this.options[index];
    }
}