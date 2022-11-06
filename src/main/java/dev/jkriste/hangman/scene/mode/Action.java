package dev.jkriste.hangman.scene.mode;

public enum Action {

    GUESS_LETTER("Guess Letter", GameState.GUESSING_LETTER),
    GUESS_WORD("Guess Word", GameState.GUESSING_WORD),
    @Deprecated(since = "1.0.1")
    PICK_FROM_INVENTORY("Use Item", GameState.PICKING_FROM_INVENTORY);

    public static final Action[] values = values();

    private final String name;
    private final GameState state;

    Action(String name, GameState state) {
        this.name = name;
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public String toString() {
        return this.name;
    }
}