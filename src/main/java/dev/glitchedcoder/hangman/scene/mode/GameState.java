package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.ui.Icon;

import static dev.glitchedcoder.hangman.ui.Icon.*;

public enum GameState {

    PICKING_OPTION(new Icon[][] {{}, {ENTER, UP_ARROW}, {LEFT_ARROW, DOWN_ARROW, RIGHT_ARROW}}),
    GUESSING_LETTER(new Icon[][] {{}, {}, {A_TO_Z}}),
    GUESSING_WORD(new Icon[][] {{}, {}, {A_TO_Z, ENTER, BACKSPACE}}),
    PICKING_FROM_INVENTORY(new Icon[][] {{}, {ENTER, UP_ARROW}, {LEFT_ARROW, DOWN_ARROW, RIGHT_ARROW}}),
    READING_TEXT(new Icon[][] {{}, {}, {ENTER}});

    private final Icon[][] overlay;

    GameState(Icon[][] overlay) {
        this.overlay = overlay;
    }

    public Icon[][] getOverlay() {
        return overlay;
    }
}