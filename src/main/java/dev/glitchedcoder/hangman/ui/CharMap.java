package dev.glitchedcoder.hangman.ui;

import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to map characters to {@link Texture}s.
 * <br />
 * Used to map {@link String}s to {@link BufferedImage}s.
 * These images are then pre-processed in the
 * {@link dev.glitchedcoder.hangman.window.Scene} and then
 * are stitched together to make one large {@link BufferedImage}.
 */
public enum CharMap {

    A('a', Texture.A),
    B('b', Texture.B),
    C('c', Texture.C),
    D('d', Texture.D),
    E('e', Texture.E),
    F('f', Texture.F),
    G('g', Texture.G),
    H('h', Texture.H),
    I('i', Texture.I),
    J('j', Texture.J),
    K('k', Texture.K),
    L('l', Texture.L),
    M('m', Texture.M),
    N('n', Texture.N),
    O('o', Texture.O),
    P('p', Texture.P),
    Q('q', Texture.Q),
    R('r', Texture.R),
    S('s', Texture.S),
    T('t', Texture.T),
    U('u', Texture.U),
    V('v', Texture.V),
    W('w', Texture.W),
    X('x', Texture.X),
    Y('y', Texture.Y),
    Z('z', Texture.Z),
    N0('0', Texture.N0),
    N1('1', Texture.N1),
    N2('2', Texture.N2),
    N3('3', Texture.N3),
    N4('4', Texture.N4),
    N5('5', Texture.N5),
    N6('6', Texture.N6),
    N7('7', Texture.N7),
    N8('8', Texture.N8),
    N9('9', Texture.N9),
    SPACE(' ', Texture.SPACE),
    PERIOD('.', Texture.PERIOD),
    COMMA(',', Texture.COMMA),
    EXCLAMATION_POINT('!', Texture.EXCLAMATION_POINT),
    QUESTION_MARK('?', Texture.QUESTION_MARK),
    PLUS('+', Texture.AND),
    AMPERSAND('&', Texture.AND),
    LEFT_CAROT('<', Texture.SOLID_LEFT_ARROW),
    RIGHT_CAROT('>', Texture.SOLID_RIGHT_ARROW),
    UNDERSCORE('_', Texture.UNDERSCORE),
    APOSTROPHE('\'', Texture.APOSTROPHE),
    COLON(':', Texture.COLON);

    /**
     * Used as a character-to-{@link Texture} lookup.
     * <br />
     * Since the implementation of this is a {@link HashMap},
     * the time complexity of a single character to {@link Texture}
     * lookup is O(1) and is as efficient as it can get.
     */
    private static final Map<Character, Texture> LOOKUP;

    static {
        Map<Character, Texture> map = new HashMap<>();
        for (CharMap m : values()) {
            if (Character.isAlphabetic(m.c)) {
                map.put(Character.toLowerCase(m.c), m.t);
                map.put(Character.toUpperCase(m.c), m.t);
            } else
                map.put(m.c, m.t);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    private final char c;
    private final Texture t;

    CharMap(char c, @Nonnull Texture texture) {
        this.c = c;
        this.t = texture;
    }

    /**
     * Gets the character (or key) that represents the {@link Texture}.
     *
     * @return The character that represents the {@link Texture}.
     */
    public char getCharacter() {
        return c;
    }

    /**
     * Gets the {@link Texture} that the character (or key) represents.
     *
     * @return The {@link Texture} that the character represents.
     */
    @Nonnull
    public Texture getTexture() {
        return t;
    }

    /**
     * Translates the given character to a {@link BufferedImage}.
     * <br />
     * If the given character is not in the lookup, i.e., an
     * invalid {@link Texture}, an {@link IllegalArgumentException}
     * will be thrown.
     *
     * @param c The character to translate.
     * @return The given character as a {@link BufferedImage}.
     * @throws IllegalArgumentException Thrown if the given char does not exist as a texture.
     */
    @Nonnull
    public static BufferedImage translate(char c) {
        Validator.checkArgument(LOOKUP.containsKey(c), "Invalid character given on translation: '{}'!", c);
        return Validator.requireNotNull(LOOKUP.get(c).asImage());
    }

    /**
     * Translates the given string to a same-sized array of {@link BufferedImage}s.
     * <br />
     * If a given character is not in the lookup, i.e., an invalid {@link Texture},
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param s The string to translate.
     * @return The given string as a same-sized array of {@link BufferedImage}s.
     */
    @Nonnull
    public static BufferedImage[] translate(String s) {
        BufferedImage[] images = new BufferedImage[s.length()];
        char[] array = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            char c = array[i];
            Validator.checkArgument(LOOKUP.containsKey(c), "Invalid character given on translation: '{}'!", c);
            images[i] = LOOKUP.get(c).asImage();
        }
        return images;
    }
}