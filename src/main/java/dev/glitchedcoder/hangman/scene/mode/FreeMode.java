package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.TextBox;
import dev.glitchedcoder.hangman.entity.TextInput;
import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.json.ScriptSection;
import dev.glitchedcoder.hangman.scene.EndScreen;
import dev.glitchedcoder.hangman.scene.menu.PauseMenu;
import dev.glitchedcoder.hangman.scene.menu.ScrollableMenuComponent;
import dev.glitchedcoder.hangman.ui.CharMap;
import dev.glitchedcoder.hangman.ui.NSFL;
import dev.glitchedcoder.hangman.ui.Portrait;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.ApiRequest;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class FreeMode extends Scene {

    private byte guesses;
    private GameState state;
    private FixedTexture letters;
    private FixedTexture guessText;

    private final String word;
    private final Set<Key> keys;
    private final TextBox textBox;
    private final LightFixture light;
    private final FixedTexture table;
    private final FixedTexture hands;
    private final TextInput textInput;
    private final IconOverlay overlay;
    private final Map<Character, Boolean> guessed;
    private final ScrollableMenuComponent<Action> action;

    public FreeMode(@Nullable String word) {
        this.keys = KeySelector.create()
                .group(Key.ALPHABETICAL_KEYS)
                .group(Key.ARROW_KEYS)
                .with(Key.ENTER)
                .with(Key.ESCAPE)
                .with(Key.BACKSPACE)
                .build();
        if (word == null) {
            int length = ApiRequest.randomWordLength();
            this.word = Validator.requireNotNull(ApiRequest.requestWord(length)).getWord();
        } else
            this.word = word;
        this.textInput = new TextInput(this, this.word.length(), 5, this.word.length() < 10);
        this.light = new LightFixture(this, (byte) 10, 4.1);
        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
                .scale(4)
                .build();
        boolean nsfl = config.getNSFL() == NSFL.ON;
        BufferedImage hands = new TexturePreprocessor(nsfl ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                .scale(4)
                .build();
        this.table = new FixedTexture(this, table);
        this.hands = new FixedTexture(this, hands);
        this.overlay = new IconOverlay(this, Color.WHITE, 2.5);
        this.action = new ScrollableMenuComponent<>(this, new Action[] { Action.GUESS_LETTER, Action.GUESS_WORD }, 2.5);
        this.textBox = new TextBox(this, Portrait.EXECUTIONER, Color.WHITE);
        this.guesses = (byte) ((26 - this.word.length()) * (2D / 3D));
        this.guessed = new HashMap<>();
        updateGuesses();
        setState(GameState.PICKING_OPTION);
    }

    @Override
    protected void onLoad() {
        this.textInput.setLocation(Location.center(textInput.getBounds()));
        this.action.setLocation(Location.center(action.getBounds()));
        this.action.getLocation().setY(textInput.getLocation().getY() + textBox.getBounds().height / 2);
        this.light.setLocation(Location.topCenter(light.getBounds()));
        this.hands.setLocation(Location.bottomCenter(hands.getBounds()));
        this.table.setLocation(Location.bottomCenter(table.getBounds()));
        this.textBox.setLocation(Location.bottomCenter(textBox.getBounds()));
        this.guessText.setLocation(Location.bottomRight(guessText.getBounds()));
        this.overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
        this.light.setRenderPriority(new RenderPriority(124));
        this.hands.setRenderPriority(new RenderPriority(125));
        this.textBox.setRenderPriority(RenderPriority.MAX);
        addRenderables(textInput, overlay, table, hands, light, guessText, action);
        spawnAll(textInput, overlay, table, hands, light, guessText, action);
        this.action.onSelect(() -> setState(
                action.getSelected() == Action.GUESS_WORD ? GameState.GUESSING_WORD : GameState.GUESSING_LETTER
        ));
    }

    @Override
    protected void onUnload() {
        // do nothing
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.ESCAPE) {
            setScene(new PauseMenu(this));
            return;
        }
        switch (state) {
            case GUESSING_LETTER: {
                if (key.isActionKey())
                    return;
                CharMap map = Validator.requireNotNull(key.getMap());
                if (guessed.containsKey(map.getCharacter()))
                    return;
                if (!guessLetter(map.getCharacter()))
                    --guesses;
                updateGuesses();
                setState(GameState.PICKING_OPTION);
                break;
            }
            case GUESSING_WORD: {
                if (key == Key.ENTER) {
                    if (textInput.isInvalid())
                        return;
                    if (!guessWord(textInput.getInput()) || !textInput.allLocked())
                        guesses -= 2;
                    updateGuesses();
                    setState(GameState.PICKING_OPTION);
                    return;
                }
                textInput.handleKeyInput(key);
                break;
            }
            case READING_TEXT:
            case GAME_OVER:
            case GAME_WON:
                if (key == Key.ENTER)
                    textBox.nextLine();
                break;
            case PICKING_OPTION: {
                if (key == Key.ENTER)
                    action.select();
                if (key == Key.ARROW_LEFT)
                    action.scrollLeft();
                if (key == Key.ARROW_RIGHT)
                    action.scrollRight();
                break;
            }
            default:
        }
    }

    private void setState(@Nonnull GameState state) {
        this.state = state;
        switch (state) {
            case READING_TEXT: {
                action.setVisible(false);
                textBox.setVisible(true);
                break;
            }
            case PICKING_OPTION: {
                if (guesses == 0) {
                    setState(GameState.GAME_OVER);
                    return;
                }
                if (textInput.allLocked()) {
                    setState(GameState.GAME_WON);
                    return;
                }
                action.setVisible(true);
                action.setFocused(true);
                updateGuessedLetters();
                break;
            }
            case GAME_OVER: {
                action.setVisible(false);
                textBox.setVisible(true);
                List<String> script = Script.getScript().getSection(ScriptSection.GAME_LOST);
                byte index = 0;
                for (byte b = 0; b < script.size(); b++) {
                    if (script.get(b).contains("%word%"))
                        index = b;
                }
                script.set(index, script.get(index).replace("%word%", word));
                textBox.addLines(script);
                textBox.onFinish(() -> setScene(new EndScreen(false)));
                addRenderable(textBox);
                textBox.spawn();
                break;
            }
            case GUESSING_LETTER:
            case GUESSING_WORD:
                action.setVisible(false);
                break;
            case GAME_WON: {
                action.setVisible(false);
                textBox.setVisible(true);
                List<String> script = Script.getScript().getSection(ScriptSection.GAME_WON);
                textBox.addLines(script);
                textBox.onFinish(() -> setScene(new EndScreen(true)));
                addRenderable(textBox);
                textBox.spawn();
                break;
            }
            default:
        }
        overlay.clear();
        overlay.setIcons(state.getOverlay());
    }

    private void updateGuesses() {
        if (guesses < 0)
            guesses = 0;
        BufferedImage guessText = new TexturePreprocessor(String.valueOf(guesses))
                .color(Color.RED)
                .scale(3)
                .removeBackground()
                .build();
        if (this.guessText == null) {
            this.guessText = new FixedTexture(this, guessText);
        } else
            this.guessText.setImage(guessText);
        this.guessText.setLocation(Location.bottomRight(this.guessText.getBounds()));
    }

    private boolean guessLetter(char c) {
        if (word.indexOf(c) == -1) {
            guessed.put(c, false);
            return false;
        }
        Set<Integer> indices = new HashSet<>();
        char[] array = word.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == c)
                indices.add(i);
        }
        for (int i : indices) {
            textInput.setCharacter(i, c);
            textInput.lockCharacter(i);
        }
        guessed.put(c, true);
        return true;
    }

    private boolean guessWord(String word) {
        char[] guessedWord = word.toCharArray();
        char[] actualWord = this.word.toCharArray();
        Set<Integer> indices = new HashSet<>();
        for (int i = 0; i < actualWord.length; i++) {
            if (guessedWord[i] == actualWord[i])
                indices.add(i);
            else
                textInput.setCharacter(i, ' ');
        }
        for (int i : indices) {
            textInput.setCharacter(i, actualWord[i]);
            textInput.lockCharacter(i);
        }
        return !indices.isEmpty();
    }

    private void updateGuessedLetters() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Character, Boolean> entry : guessed.entrySet()) {
            // recommended by sonarlint instead of !entry.getValue()
            if (Boolean.FALSE.equals(entry.getValue()))
                builder.append(entry.getKey()).append(" ");
        }
        if (builder.length() == 0)
            return;
        BufferedImage image = new TexturePreprocessor(builder.toString())
                .color(Color.RED)
                .removeBackground()
                .scale(2)
                .build();
        if (letters == null) {
            letters = new FixedTexture(this, image);
            letters.setRenderPriority(RenderPriority.MAX);
            addRenderable(letters);
            letters.spawn();
        } else
            this.letters.setImage(image);
        this.letters.setLocation(Location.topCenter(letters.getBounds()));
    }
}