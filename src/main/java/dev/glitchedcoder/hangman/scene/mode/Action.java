package dev.glitchedcoder.hangman.scene.mode;

public enum Action {

    GUESS_LETTER("Guess Letter"),
    GUESS_WORD("Guess Word"),
    @Deprecated(since = "1.0.1")
    PICK_FROM_INVENTORY("Use Item");

    public static final Action[] values = values();

    private final String name;

    Action(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}