package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.ui.Icon;

import static dev.glitchedcoder.hangman.ui.Icon.*;

public enum IconLayout {

    PICKING_OPTION(new Icon[][] {
            {},
            { ENTER, ESCAPE },
            { LEFT_ARROW, RIGHT_ARROW }
    }),
    GUESSING_LETTER(new Icon[][] {
            {},
            {},
            { A_TO_Z, ESCAPE }
    }),
    GUESSING_WORD(new Icon[][] {
            {},
            { null, ESCAPE },
            { A_TO_Z, ENTER, BACKSPACE }
    }),
    @Deprecated(since = "1.2.0")
    PICKING_FROM_INVENTORY(new Icon[][] {
            {},
            { ENTER, ESCAPE },
            { LEFT_ARROW, RIGHT_ARROW }
    }),
    READING_TEXT(new Icon[][] {
            {},
            {},
            { ENTER, ESCAPE }
    }),
    GAME_FINISH(new Icon[][] {
            {},
            {},
            { ENTER }
    }),
    MENU(new Icon[][] {
            {},
            {},
            { UP_ARROW, DOWN_ARROW, ENTER }
    }),
    MENU_WITH_PARENT(new Icon[][] {
            {},
            { ENTER, ESCAPE },
            { UP_ARROW, DOWN_ARROW }
    }),
    MENU_WITH_SCROLLABLE_COMPONENTS(new Icon[][] {
            {},
            { ENTER, UP_ARROW, ESCAPE },
            { LEFT_ARROW, DOWN_ARROW, RIGHT_ARROW }
    }),
    WORD_SELECTION_MENU(new Icon[][] {
            {},
            { UP_ARROW, ESCAPE, DOWN_ARROW },
            { A_TO_Z, ENTER, BACKSPACE }
    }),
    API_KEY_ENTRY(new Icon[][] {
            {},
            { BACKSPACE, ENTER },
            { A_TO_Z, ZERO_TO_NINE }
    }),
    TRANSITION(new Icon[][] {});

    private final Icon[][] iconMatrix;

    IconLayout(Icon[][] iconMatrix) {
        this.iconMatrix = iconMatrix;
    }

    /**
     * Gets the {@link Icon} matrix.
     * <br />
     * Max size of the matrix is {@code 3x3} or 9 total.
     *
     * @return The {@link Icon} matrix.
     */
    public Icon[][] getIconMatrix() {
        return iconMatrix;
    }
}
