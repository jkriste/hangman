package dev.glitchedcoder.hangman.window.key;

import javax.annotation.Nullable;
import java.awt.event.KeyEvent;

/**
 * A wrapper for {@link java.awt.event.KeyEvent}'s key constants.
 * This {@link Key} enum will only contain supported keys.
 */
public enum Key {

    A(KeyEvent.VK_A),
    B(KeyEvent.VK_B),
    C(KeyEvent.VK_C),
    D(KeyEvent.VK_D),
    E(KeyEvent.VK_E),
    F(KeyEvent.VK_F),
    G(KeyEvent.VK_G),
    H(KeyEvent.VK_H),
    I(KeyEvent.VK_I),
    J(KeyEvent.VK_J),
    K(KeyEvent.VK_K),
    L(KeyEvent.VK_L),
    M(KeyEvent.VK_M),
    N(KeyEvent.VK_N),
    O(KeyEvent.VK_O),
    P(KeyEvent.VK_P),
    Q(KeyEvent.VK_Q),
    R(KeyEvent.VK_R),
    S(KeyEvent.VK_S),
    T(KeyEvent.VK_T),
    U(KeyEvent.VK_U),
    V(KeyEvent.VK_V),
    W(KeyEvent.VK_W),
    X(KeyEvent.VK_X),
    Y(KeyEvent.VK_Y),
    Z(KeyEvent.VK_Z),
    ESCAPE(KeyEvent.VK_ESCAPE),
    BACKSPACE(KeyEvent.VK_BACK_SPACE),
    ARROW_UP(KeyEvent.VK_UP),
    ARROW_DOWN(KeyEvent.VK_DOWN),
    ARROW_LEFT(KeyEvent.VK_LEFT),
    ARROW_RIGHT(KeyEvent.VK_RIGHT),
    ENTER(KeyEvent.VK_ENTER);

    // Key groups that can be used with KeySelector

    public static final Key[] ALL_KEYS = values();
    public static final Key[] ARROW_KEYS = { ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT, ARROW_UP };
    public static final Key[] ALPHABET = { A, B, C, D, E, F, G, H, I,
                                           J, K, L, M, N, O, P, Q, R,
                                           S, T, U, V, W, X, Y, Z };

    private final int id;

    Key(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public static Key of(int key) {
        for (Key k : ALL_KEYS) {
            if (k.id == key)
                return k;
        }
        return null;
    }

//    public static boolean isAlphabetic(Key key) {
//
//    }
}