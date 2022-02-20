package dev.glitchedcoder.hangman.scene.mode;

/**
 * @deprecated Deprecated due to not being used in the game.
 */
@Deprecated(since = "1.0.1")
public enum Item {

    HINT("Hint"),
    ADD_GUESS("Add 1 Guess");

    private final String name;

    Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}