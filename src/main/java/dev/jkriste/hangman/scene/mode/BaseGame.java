package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.entity.CharacterInput;
import dev.jkriste.hangman.entity.FadeIn;
import dev.jkriste.hangman.entity.FadeOut;
import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.IconOverlay;
import dev.jkriste.hangman.entity.LightFixture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.entity.RenderPriority;
import dev.jkriste.hangman.entity.TextBox;
import dev.jkriste.hangman.entity.TextInput;
import dev.jkriste.hangman.json.Script;
import dev.jkriste.hangman.json.ScriptSection;
import dev.jkriste.hangman.json.Words;
import dev.jkriste.hangman.scene.IconLayout;
import dev.jkriste.hangman.scene.menu.PauseMenu;
import dev.jkriste.hangman.ui.Portrait;
import dev.jkriste.hangman.ui.Texture;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.ApiRequest;
import dev.jkriste.hangman.util.Constants;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;
import dev.jkriste.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base class for all instances of a game.
 */
@EqualsAndHashCode(callSuper = true)
public abstract class BaseGame extends Scene {

    protected byte guesses;
    protected FadeIn fadeIn;
    protected FadeOut fadeOut;
    protected TextBox textBox;
    protected TextInput input;
    protected FixedTexture table;
    protected FixedTexture hands;
    protected IconOverlay overlay;
    protected LightFixture fixture;

    protected final String word;
    private final AtomicReference<GameState> gameState;
    protected final Map<Character, Boolean> guessedLetters;

    private static final byte SKIP = 8;
    protected static final byte WRONG_WORD_PENALTY = 2;
    protected static final byte WRONG_LETTER_PENALTY = 1;
    protected static final String WORD_KEYWORD = "%word%";
    protected static final String CRIME_KEYWORD = "%crime%";
    protected static final String LETTER_KEYWORD = "%letter%";
    protected static final Script script = Script.getScript();

    protected BaseGame(@Nullable String word) {
        if (word == null && isPlayable())
            word = requestWord(-1);
        this.word = word;
        this.guesses = word == null ? -1 : (byte) ((26 - word.length()) * (2D / 3D));
        this.guessedLetters = isPlayable() ? new HashMap<>() : null;
        this.gameState = new AtomicReference<>(GameState.TRANSITION);
    }

    /**
     * Requests a word from either the {@link ApiRequest API} or pulls one locally.
     * <br />
     * If the given {@code length} is {@code -1}, a random word length between
     * {@link Constants#MIN_WORD_LENGTH} and {@link Constants#MAX_WORD_LENGTH}
     * will be generated and a word with that length will be picked at random.
     *
     * @param length The length of the word, or -1 if random length.
     * @return A word from either the {@link ApiRequest API} or one pulled locally.
     */
    protected static String requestWord(int length) {
        if (length == -1)
            length = ApiRequest.randomWordLength();
        else
            length = Validator.constrain(length, Constants.MIN_WORD_LENGTH, Constants.MAX_WORD_LENGTH);
        return config.getMode().isOnline() ? ApiRequest.requestWord(length) : Words.randomWord(length);
    }

    @Override
    protected void onInit() {
        this.fadeIn = new FadeIn(this, Color.BLACK, SKIP);
        this.fadeOut = new FadeOut(this, Color.BLACK, SKIP);
        if (enableOverlay()) {
            this.fixture = new LightFixture(this, (byte) 10); // 10% chance to flicker per tick
            BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
                    .scale(4)
                    .build();
            this.table = new FixedTexture(this, table);
            BufferedImage hands = new TexturePreprocessor(config.getNSFL().isOn() ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                    .scale(4)
                    .build();
            this.hands = new FixedTexture(this, hands);
            this.fixture.setLocation(Location::topCenter);
            this.hands.setLocation(Location::bottomCenter);
            this.table.setLocation(Location::bottomCenter);
            this.fixture.setRenderPriority(new RenderPriority(124));
            this.hands.setRenderPriority(new RenderPriority(125));
            addRenderables(fixture, this.table, this.hands);
            spawnAll(fixture, this.table, this.hands);
        }
        if (isPlayable()) {
            this.input = new TextInput(this, word.length(), inputSpaces());
            this.input.setLocation(Location::center);
            addRenderable(input);
            input.spawn();
        }
        this.textBox = new TextBox(this, getPortrait(), Color.WHITE);
        this.textBox.setLocation(Location::bottomCenter);
        this.textBox.setRenderPriority(new RenderPriority(126));
        this.overlay = new IconOverlay(this, Color.WHITE);
        this.overlay.setIcons(IconLayout.TRANSITION);
        this.overlay.setLocation(Location::bottomLeft);
        addRenderables(fadeIn, fadeOut, textBox, overlay);
        spawnAll(fadeIn, overlay);
        fadeIn.onFinish(this::onFadeInFinish);
    }

    @Override
    protected void onDispose() {
        disposeAll(overlay, textBox);
        if (enableOverlay())
            disposeAll(fixture, hands, table);
        if (isPlayable())
            input.dispose();
        if (!fadeIn.shouldRemove())
            fadeIn.dispose();
    }

    @Override
    protected final void focusLost(FocusEvent event) {
        setScene(new PauseMenu(this), false);
    }

    /**
     * Gets the current {@link GameState}.
     *
     * @return The current {@link GameState}.
     */
    protected final GameState getState() {
        return gameState.get();
    }

    /**
     * Called when the {@link GameState} is being updated.
     * <br />
     * Do <b>NOT</b> use this method to internally update the
     * {@link GameState}. Instead, use {@link #setState(GameState)}.
     * <br />
     * Called by {@link #setState(GameState)}.
     *
     * @param newState The new game state.
     */
    protected abstract void onStateChange(@Nonnull GameState newState);

    /**
     * Called when the {@link FadeIn} is finished rendering.
     * <br />
     * This method can be overridden, but the overriding method
     * should be calling {@code super.onFadeInFinish()} to properly
     * set the starting {@link GameState} and dispose of the {@link FadeIn}.
     */
    protected void onFadeInFinish() {
        setState(getStartingState());
        fadeIn.dispose();
    }

    /**
     * Checks whether the {@link BaseGame} is playable.
     * <br />
     * <i>Playable</i>, referring to if there are actual
     * elements of the game that will be interactible other
     * than the standard {@link TextBox} script.
     * <br />
     * By default, this method returns {@code true}, but can be overridden.
     * <br />
     * If {@code false}, the following variables will be changed:
     * <ul>
     *     <li>{@link #word} {@code = null}</li>
     *     <li>{@link #guessedLetters} {@code = null}</li>
     *     <li>{@link #guesses} {@code = -1}</li>
     *     <li>{@link #input} {@code = null}</li>
     * </ul>
     *
     * @return True if the {@link BaseGame} is playable, false otherwise.
     */
    protected boolean isPlayable() {
        return true;
    }

    /**
     * Checks whether to enable the {@link BaseGame} overlay.
     * <br />
     * This includes:
     * <ul>
     *     <li>{@link LightFixture}</li>
     *     <li>{@link FixedTexture} ({@link Texture#TABLE_TEXTURE})</li>
     *     <li>{@link FixedTexture} ({@link Texture#HANDS_BOUND}/{@link Texture#HANDS_UNBOUND})</li>
     * </ul>
     * By default, this method returns {@code true}, but can be overridden.
     *
     * @return True if the {@link BaseGame} overlay should be enabled, false otherwise.
     */
    protected boolean enableOverlay() {
        return true;
    }

    /**
     * Gets the default {@link Portrait} for the {@link TextBox}.
     * <br />
     * By default, this method returns {@link Portrait#EXECUTIONER}.
     *
     * @return The default {@link Portrait} for the {@link TextBox}.
     */
    protected Portrait getPortrait() {
        return Portrait.EXECUTIONER;
    }

    /**
     * Checks whether the {@link TextInput} should have spaces.
     * <br />
     * By default, this method returns {@code false}.
     *
     * @return True if the {@link TextInput} should have spaces, false otherwise.
     * @see TextInput#TextInput(Scene, int, boolean)
     */
    protected boolean inputSpaces() {
        return false;
    }

    /**
     * Gets the starting {@link GameState}.
     * <br />
     * Used in {@link #onFadeInFinish()}.
     * <br />
     * By default, this method returns {@link GameState#READING_TEXT}.
     *
     * @return The starting {@link GameState}.
     */
    protected GameState getStartingState() {
        return GameState.READING_TEXT;
    }

    /**
     * Sets the current {@link GameState} of the {@link BaseGame}.
     * <br />
     * Use this method instead of {@link #onStateChange(GameState)}
     * when internally updating the {@link GameState}.
     * <br />
     * Updates the {@link IconOverlay} and passes on the
     * {@link GameState} to {@link #onStateChange(GameState)}.
     *
     * @param state The new game state.
     */
    protected final void setState(@Nonnull GameState state) {
        Validator.requireNotNull(state, "Given game state is null!");
        Hangman.debug("GameState changed from '{}' to '{}'", this.gameState.get().name(), state.name());
        if (overlay != null)
            overlay.setIcons(state.getLayout());
        this.gameState.set(state);
        onStateChange(state);
    }

    /**
     * Checks whether no {@link Key} input should be allowed.
     * <br />
     * Checks if either:
     * <ol>
     *     <li>The {@link Scene} is not {@link #isFocused() focused}.</li>
     *     <li>The {@link FadeIn} object is {@link FadeIn#isRemoved() alive}.</li>
     *     <li>The {@link FadeOut} object is {@link FadeOut#isRemoved() alive}.</li>
     *     <li>The {@link GameState} is {@link GameState#TRANSITION}.</li>
     * </ol>
     *
     * @return True to ignore {@link Key} input, false otherwise.
     */
    protected final boolean noKeyInput() {
        if (!isFocused())
            return true;
        if (fadeIn.shouldDraw() || fadeOut.shouldDraw())
            return true;
        return getState() == GameState.TRANSITION;
    }

    /**
     * Guesses the given {@code char}.
     * <br />
     * If the {@link #guessedLetters} already contain the character,
     * the {@link ScriptSection#ALREADY_GUESSED} script will be loaded.
     * The {@link GameState} will be set to {@link GameState#READING_TEXT}
     * and on finish, the {@link GameState} will be set back to {@link GameState#PICKING_OPTION}.
     * <br />
     * If the given character is not present in the {@link #word}, the
     * character will be added to the {@link #guessedLetters} and will return {@code false}.
     * <br />
     * Any instances of the given character that have not already been guessed will
     * be inserted into the {@link #input text input} and locked and the given
     * character will be added to the {@link #guessedLetters}.
     *
     * @param c The character to guess.
     * @return True if the {@link #word} contained a non-guessed character, false otherwise.
     * @throws IllegalArgumentException Thrown if {@link #isPlayable()} returns {@code false}.
     */
    protected final boolean guessLetter(char c) {
        Validator.checkArgument(isPlayable(), "Call to guessLetter(char) when #isPlayable() returned false.");
        if (guessedLetters.containsKey(c)) {
            List<String> lines = script.getSection(ScriptSection.ALREADY_GUESSED, LETTER_KEYWORD, String.valueOf(c));
            textBox.addLines(lines);
            textBox.onFinish(() -> setState(GameState.PICKING_OPTION));
            setState(GameState.READING_TEXT);
            return false;
        }
        if (word.indexOf(c) == -1) {
            guessedLetters.put(c, false);
            return false;
        }
        Set<Integer> indices = new HashSet<>();
        char[] array = word.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == c)
                indices.add(i);
        }
        for (int i : indices) {
            input.setCharacter(i, c);
            input.lockCharacter(i);
        }
        guessedLetters.put(c, true);
        return true;
    }

    /**
     * Guesses the given word.
     * <br />
     * This method does not check against {@link #guessedLetters}.
     * <br />
     * Instead, if a letter is present at the given index, i.e., if
     * the word guessed was {@code "life"} and the actual word was
     * {@code "live"} then all empty character slots will be filled
     * and {@link TextInput#lockCharacter(int) locked}, while any
     * incorrect letters will be removed.
     *
     * @param s The guessed word.
     * @return True if the guessed word filled any slots, false otherwise.
     * @throws IllegalArgumentException Thrown if {@link #isPlayable()} returns {@code false}.
     */
    protected final boolean guessWord(@Nonnull String s) {
        Validator.requireNotNull(s, "Given word is null!");
        Validator.requireEqual(s.length(), word.length());
        Validator.checkArgument(isPlayable(), "Call to guessWord(String) when #isPlayable() returned false.");
        char[] guessedWord = s.toCharArray();
        char[] actualWord = word.toCharArray();
        Set<Integer> indices = new HashSet<>();
        for (int i = 0; i < actualWord.length; i++) {
            if (guessedWord[i] == actualWord[i])
                indices.add(i);
            else
                input.setCharacter(i, CharacterInput.EMPTY_CHAR);
        }
        for (int i : indices) {
            input.setCharacter(i, actualWord[i]);
            input.lockCharacter(i);
        }
        return !indices.isEmpty();
    }

    /**
     * Updates the guesses {@link FixedTexture}.
     * <br />
     * {@link Location} of the {@link FixedTexture} will
     * be reset to the {@link Location#bottomRight(Rectangle)}.
     *
     * @param texture The fixed texture to update.
     * @param color   The color of the text.
     */
    @ParametersAreNonnullByDefault
    protected final void updateGuesses(@Nullable FixedTexture texture, Color color) {
        Validator.requireNotNull(color, "Given color is null!");
        this.guesses = (byte) Math.max(guesses, 0);
        BufferedImage guessText = new TexturePreprocessor(String.valueOf(guesses))
                .color(color)
                .scale(3)
                .removeBackground()
                .build();
        if (texture != null)
            texture.setImage(guessText);
        else
            texture = new FixedTexture(this, guessText);
        texture.setLocation(Location::bottomRight);
    }

    /**
     * Updates the guessed letters {@link FixedTexture}.
     * <br />
     * {@link Location} of the {@link FixedTexture} will
     * be reset to the {@link Location#topCenter(Rectangle)}.
     * <br />
     * Since {@link #guessedLetters} is iterated as an {@link Map.Entry#entrySet()},
     * the order of the {@link #guessedLetters} will not be retained.
     *
     * @param texture The fixed texture to update.
     */
    protected final void updateGuessedLetters(@Nullable FixedTexture texture) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Character, Boolean> entry : guessedLetters.entrySet()) {
            // recommended by sonarlint instead of !entry.getValue()
            if (Boolean.FALSE.equals(entry.getValue()))
                builder.append(entry.getKey()).append(' ');
        }
        String letters = builder.length() == 0 ? " " : builder.toString().trim();
        BufferedImage image = new TexturePreprocessor(letters)
                .color(Color.RED)
                .removeBackground()
                .scale(2)
                .build();
        if (texture != null)
            texture.setImage(image);
        else
            texture = new FixedTexture(this, image);
        texture.setLocation(Location::topCenter);
    }
}