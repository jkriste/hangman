package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.scene.IconLayout;

public enum GameState {

    READING_TEXT(IconLayout.READING_TEXT),
    PICKING_OPTION(IconLayout.PICKING_OPTION),
    GAME_OVER(IconLayout.GAME_FINISH),
    GAME_WON(IconLayout.GAME_FINISH),
    @Deprecated(since = "1.2.0")
    PICKING_FROM_INVENTORY(IconLayout.PICKING_FROM_INVENTORY),
    GUESSING_LETTER(IconLayout.GUESSING_LETTER),
    GUESSING_WORD(IconLayout.GUESSING_WORD),
    TRANSITION(IconLayout.TRANSITION);

    private final IconLayout layout;

    GameState(IconLayout layout) {
        this.layout = layout;
    }

    public IconLayout getLayout() {
        return layout;
    }
}