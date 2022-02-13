package dev.glitchedcoder.hangman.window.key;

import dev.glitchedcoder.hangman.ui.CharMap;

import javax.annotation.Nullable;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper for {@link java.awt.event.KeyEvent}'s key constants.
 * This {@link Key} enum will only contain supported keys.
 */
public enum Key {

    A(KeyEvent.VK_A, CharMap.A),
    B(KeyEvent.VK_B, CharMap.B),
    C(KeyEvent.VK_C, CharMap.C),
    D(KeyEvent.VK_D, CharMap.D),
    E(KeyEvent.VK_E, CharMap.E),
    F(KeyEvent.VK_F, CharMap.F),
    G(KeyEvent.VK_G, CharMap.G),
    H(KeyEvent.VK_H, CharMap.H),
    I(KeyEvent.VK_I, CharMap.I),
    J(KeyEvent.VK_J, CharMap.J),
    K(KeyEvent.VK_K, CharMap.K),
    L(KeyEvent.VK_L, CharMap.L),
    M(KeyEvent.VK_M, CharMap.M),
    N(KeyEvent.VK_N, CharMap.N),
    O(KeyEvent.VK_O, CharMap.O),
    P(KeyEvent.VK_P, CharMap.P),
    Q(KeyEvent.VK_Q, CharMap.Q),
    R(KeyEvent.VK_R, CharMap.R),
    S(KeyEvent.VK_S, CharMap.S),
    T(KeyEvent.VK_T, CharMap.T),
    U(KeyEvent.VK_U, CharMap.U),
    V(KeyEvent.VK_V, CharMap.V),
    W(KeyEvent.VK_W, CharMap.W),
    X(KeyEvent.VK_X, CharMap.X),
    Y(KeyEvent.VK_Y, CharMap.Y),
    Z(KeyEvent.VK_Z, CharMap.Z),
    N0(KeyEvent.VK_0, CharMap.N0),
    N1(KeyEvent.VK_1, CharMap.N1),
    N2(KeyEvent.VK_2, CharMap.N2),
    N3(KeyEvent.VK_3, CharMap.N3),
    N4(KeyEvent.VK_4, CharMap.N4),
    N5(KeyEvent.VK_5, CharMap.N5),
    N6(KeyEvent.VK_6, CharMap.N6),
    N7(KeyEvent.VK_7, CharMap.N7),
    N8(KeyEvent.VK_8, CharMap.N8),
    N9(KeyEvent.VK_9, CharMap.N9),
    ESCAPE(KeyEvent.VK_ESCAPE, null),
    BACKSPACE(KeyEvent.VK_BACK_SPACE, null),
    ARROW_UP(KeyEvent.VK_UP, null),
    ARROW_DOWN(KeyEvent.VK_DOWN, null),
    ARROW_LEFT(KeyEvent.VK_LEFT, null),
    ARROW_RIGHT(KeyEvent.VK_RIGHT, null),
    ENTER(KeyEvent.VK_ENTER, null);

    private static final Key[] values = values();
    private static final Map<Integer, Key> LOOKUP;

    static {
        Map<Integer, Key> map = new HashMap<>();
        for (Key k : values)
            map.put(k.id, k);
        LOOKUP = Collections.unmodifiableMap(map);
    }

    // Key groups that can be used with KeySelector

    public static final Set<Key> ARROW_KEYS;
    public static final Set<Key> ACTION_KEYS;
    public static final Set<Key> WRITABLE_KEYS;

    static {
        Set<Key> keys = new HashSet<>();
        Collections.addAll(keys, ARROW_UP, ARROW_LEFT, ARROW_RIGHT, ARROW_DOWN);
        ARROW_KEYS = Collections.unmodifiableSet(keys);
        keys.clear();
        for (Key k : values) {
            if (k.isActionKey())
                keys.add(k);
        }
        ACTION_KEYS = Collections.unmodifiableSet(keys);
        keys.clear();
        for (Key k : values) {
            if (!k.isActionKey())
                keys.add(k);
        }
        WRITABLE_KEYS = Collections.unmodifiableSet(keys);
    }

    private final int id;
    private final CharMap map;

    Key(int id, @Nullable CharMap map) {
        this.id = id;
        this.map = map;
    }

    /**
     * Gets the {@link Key} id.
     *
     * @return The id of the {@link Key}.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the {@link CharMap} the {@link Key} is linked to.
     * <br />
     * Can be {@code null} if the {@link Key} is an
     * {@link Key#isActionKey() action key}.
     *
     * @return The {@link CharMap} the {@link Key} is linked to.
     */
    @Nullable
    public CharMap getMap() {
        return map;
    }

    /**
     * Checks if the {@link Key} is an action key.
     * <br />
     * An action key refers to whether or not a valid
     * character is associated with this key. i.e.,
     * is it alphanumeric?
     *
     * @return True if the {@link Key} is an action key, false otherwise.
     */
    public boolean isActionKey() {
        return this.map == null;
    }

    /**
     * Checks if the {@link Key} is numeric.
     * <br />
     * This only accounts for {@link Key}s {@link #N0} to {@link #N9}.
     *
     * @return True if the {@link Key} is numeric, false otherwise.
     */
    public boolean isNumeric() {
        if (this.map == null)
            return false;
        return Character.isDigit(this.map.getCharacter());
    }

    @Nullable
    public static Key of(int key) {
        if (!LOOKUP.containsKey(key))
            return null;
        return LOOKUP.get(key);
    }
}