package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.scene.menu.ScrollableComponentOption;

import javax.annotation.Nonnull;

/**
 * @deprecated Deprecated due to not being used in the game.
 */
@Deprecated(since = "1.0.1")
public enum Item implements ScrollableComponentOption {

    HINT("Hint"),
    ADD_GUESS("Add 1 Guess"),
    BACK("Back");

    private final String name;

    Item(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }
}