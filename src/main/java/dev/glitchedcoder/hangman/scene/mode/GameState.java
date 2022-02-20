package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.ui.Icon;

import static dev.glitchedcoder.hangman.ui.Icon.*;

public enum GameState {

    PICKING_OPTION(new Icon[][] { {}, { ENTER, ESCAPE }, { LEFT_ARROW, RIGHT_ARROW } }),
    GUESSING_LETTER(new Icon[][] { {}, {}, { A_TO_Z, ESCAPE } }),
    GUESSING_WORD(new Icon[][] { {}, { null, ESCAPE }, { A_TO_Z, ENTER, BACKSPACE } }),
    PICKING_FROM_INVENTORY(new Icon[][] { {}, { ENTER, ESCAPE }, { LEFT_ARROW, RIGHT_ARROW } }),
    READING_TEXT(new Icon[][] { {}, {}, { ENTER, ESCAPE } }),
    GAME_OVER(new Icon[][] { {}, {}, { ENTER, ESCAPE } }),
    GAME_WON(new Icon[][] { {}, {}, { ENTER, ESCAPE } });

    private final Icon[][] overlay;

    GameState(Icon[][] overlay) {
        this.overlay = overlay;
    }

    public Icon[][] getOverlay() {
        return overlay;
    }
}