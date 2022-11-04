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

    UA('A', Texture.UA),
    UB('B', Texture.UB),
    UC('C', Texture.UC),
    UD('D', Texture.UD),
    UE('E', Texture.UE),
    UF('F', Texture.UF),
    UG('G', Texture.UG),
    UH('H', Texture.UH),
    UI('I', Texture.UI),
    UJ('J', Texture.UJ),
    UK('K', Texture.UK),
    UL('L', Texture.UL),
    UM('M', Texture.UM),
    UN('N', Texture.UN),
    UO('O', Texture.UO),
    UP('P', Texture.UP),
    UQ('Q', Texture.UQ),
    UR('R', Texture.UR),
    US('S', Texture.US),
    UT('T', Texture.UT),
    UU('U', Texture.UU),
    UV('V', Texture.UV),
    UW('W', Texture.UW),
    UX('X', Texture.UX),
    UY('Y', Texture.UY),
    UZ('Z', Texture.UZ),
    LA('a', Texture.LA),
    LB('b', Texture.LB),
    LC('c', Texture.LC),
    LD('d', Texture.LD),
    LE('e', Texture.LE),
    LF('f', Texture.LF),
    LG('g', Texture.LG),
    LH('h', Texture.LH),
    LI('i', Texture.LI),
    LJ('j', Texture.LJ),
    LK('k', Texture.LK),
    LL('l', Texture.LL),
    LM('m', Texture.LM),
    LN('n', Texture.LN),
    LO('o', Texture.LO),
    LP('p', Texture.LP),
    LQ('q', Texture.LQ),
    LR('r', Texture.LR),
    LS('s', Texture.LS),
    LT('t', Texture.LT),
    LU('u', Texture.LU),
    LV('v', Texture.LV),
    LW('w', Texture.LW),
    LX('x', Texture.LX),
    LY('y', Texture.LY),
    LZ('z', Texture.LZ),
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