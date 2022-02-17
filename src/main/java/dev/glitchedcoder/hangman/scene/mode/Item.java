package dev.glitchedcoder.hangman.scene.mode;

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