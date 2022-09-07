//package dev.glitchedcoder.hangman.scene.mode;
//
//import dev.glitchedcoder.hangman.entity.FadeIn;
//import dev.glitchedcoder.hangman.entity.FadeOut;
//import dev.glitchedcoder.hangman.entity.FixedTexture;
//import dev.glitchedcoder.hangman.entity.IconOverlay;
//import dev.glitchedcoder.hangman.entity.LightFixture;
//import dev.glitchedcoder.hangman.entity.Location;
//import dev.glitchedcoder.hangman.entity.RenderPriority;
//import dev.glitchedcoder.hangman.entity.TextBox;
//import dev.glitchedcoder.hangman.entity.TextInput;
//import dev.glitchedcoder.hangman.entity.Timer;
//import dev.glitchedcoder.hangman.json.Script;
//import dev.glitchedcoder.hangman.json.ScriptSection;
//import dev.glitchedcoder.hangman.scene.EndScreen;
//import dev.glitchedcoder.hangman.scene.menu.PauseMenu;
//import dev.glitchedcoder.hangman.scene.menu.ScrollableMenuComponent;
//import dev.glitchedcoder.hangman.ui.CharMap;
//import dev.glitchedcoder.hangman.ui.Portrait;
//import dev.glitchedcoder.hangman.ui.Texture;
//import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
//import dev.glitchedcoder.hangman.util.ApiRequest;
//import dev.glitchedcoder.hangman.util.Validator;
//import dev.glitchedcoder.hangman.window.Scene;
//import dev.glitchedcoder.hangman.window.key.Key;
//import dev.glitchedcoder.hangman.window.key.KeySelector;
//import lombok.EqualsAndHashCode;
//
//import javax.annotation.Nonnull;
//import java.awt.Color;
//import java.awt.event.FocusEvent;
//import java.awt.image.BufferedImage;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@EqualsAndHashCode(callSuper = true)
//public class StoryMode extends Scene {
//
//    private byte guesses;
//    private GameState state;
//    private FixedTexture letters;
//
//    private final Phase phase;
//    private final String word;
//    private final Timer timer;
//    private final Set<Key> keys;
//    private final FadeIn fadeIn;
//    private final FadeOut fadeOut;
//    private final TextBox textBox;
//    private final LightFixture light;
//    private final FixedTexture table;
//    private final FixedTexture hands;
//    private final TextInput textInput;
//    private final IconOverlay overlay;
//    private final FixedTexture guessText;
//    private final Map<Character, Boolean> guessed;
//    private final ScrollableMenuComponent<Action> action;
//
//    private static final String KEYWORD = "%word%";
//
//    public StoryMode(@Nonnull Phase phase) {
//        this.phase = phase;
//        this.keys = KeySelector.create()
//                .group(Key.ALPHABETICAL_KEYS)
//                .group(Key.ARROW_KEYS)
//                .with(Key.ENTER)
//                .with(Key.ESCAPE)
//                .with(Key.BACKSPACE)
//                .build();
//        this.word = Validator.requireNotNull(ApiRequest.requestWord(phase.getWordLength())).getWord();
//        this.textInput = new TextInput(this, this.word.length(), 5, false);
//        this.light = new LightFixture(this, (byte) 10, 4.1);
//        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
//                .scale(4)
//                .build();
//        BufferedImage hands = new TexturePreprocessor(config.getNSFL().isOn() ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
//                .scale(4)
//                .build();
//        BufferedImage guessText = new TexturePreprocessor(String.valueOf(guesses))
//                .color(Color.RED)
//                .scale(3)
//                .removeBackground()
//                .build();
//        this.table = new FixedTexture(this, table);
//        this.hands = new FixedTexture(this, hands);
//        this.guessText = new FixedTexture(this, guessText);
//        this.overlay = new IconOverlay(this, Color.WHITE);
//        this.action = new ScrollableMenuComponent<>(this, new Action[] { Action.GUESS_LETTER, Action.GUESS_WORD }, 2.5);
//        this.textBox = new TextBox(this, Portrait.EXECUTIONER, Color.WHITE);
//        this.guesses = (byte) ((26 - word.length()) * (2D / 3D));
//        this.guessed = new HashMap<>();
//        this.fadeIn = new FadeIn(this, Color.BLACK, (byte) 11);
//        this.fadeOut = new FadeOut(this, Color.BLACK, (byte) 11);
//        this.textBox.addLines(phase.getScript());
//        config.setPlayedBefore(true);
//        this.textBox.onFinish(() -> setState(GameState.PICKING_OPTION));
//        this.timer = phase.next() == null ? new Timer(this, Color.RED, 2, 90) : null;
//    }
//
//    @Override
//    protected void onInit() {
//        this.fadeIn.onFinish(() -> {
//            textBox.spawn();
//            fadeIn.dispose();
//        });
//        this.textInput.setLocation(Location.center(textInput.getBounds()));
//        this.action.setLocation(Location.center(action.getBounds()));
//        this.action.getLocation().setY(textInput.getLocation().getY() + textBox.getBounds().height / 2);
//        this.light.setLocation(Location.topCenter(light.getBounds()));
//        this.hands.setLocation(Location.bottomCenter(hands.getBounds()));
//        this.table.setLocation(Location.bottomCenter(table.getBounds()));
//        this.textBox.setLocation(Location.bottomCenter(textBox.getBounds()));
//        this.guessText.setLocation(Location.bottomRight(guessText.getBounds()));
//        this.overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
//        if (timer != null) {
//            this.timer.setLocation(Location.topRight(timer.getBounds()));
//            this.timer.onFinish(() -> setState(GameState.GAME_OVER));
//            addRenderable(timer);
//            timer.spawn();
//        }
//        this.light.setRenderPriority(new RenderPriority(124));
//        this.hands.setRenderPriority(new RenderPriority(125));
//        this.textBox.setRenderPriority(RenderPriority.MAX);
//        this.overlay.setIcons(GameState.READING_TEXT.getOverlay());
//        addRenderables(textInput, overlay, table, hands, light, guessText, action, textBox, fadeIn, fadeOut);
//        spawnAll(textInput, overlay, table, hands, light, guessText, action);
//        if (fadeIn.isAlive())
//            fadeIn.spawn();
//        this.action.onSelect(() -> setState(action.getSelected().getState()));
//        updateGuessedLetters();
//        updateGuesses();
//        setState(textBox.hasNextLine() ? GameState.READING_TEXT : GameState.PICKING_OPTION);
//    }
//
//    @Override
//    protected void onDispose() {
//        // do nothing
//    }
//
//    @Override
//    protected Set<Key> getKeyListeners() {
//        return this.keys;
//    }
//
//    @Override
//    protected void onKeyPress(Key key) {
//        if (fadeIn.shouldDraw() || fadeOut.shouldDraw())
//            return;
//        if (key == Key.ESCAPE) {
//            setScene(new PauseMenu(this));
//            return;
//        }
//        switch (state) {
//            case GUESSING_LETTER: {
//                if (key.isActionKey())
//                    return;
//                CharMap map = Validator.requireNotNull(key.getMap());
//                if (guessed.containsKey(map.getCharacter()))
//                    return;
//                if (!guessLetter(map.getCharacter()))
//                    --guesses;
//                updateGuesses();
//                setState(GameState.PICKING_OPTION);
//                break;
//            }
//            case GUESSING_WORD: {
//                if (key == Key.ENTER) {
//                    if (textInput.isInvalid())
//                        return;
//                    if (!guessWord(textInput.getInput()) || !textInput.allLocked())
//                        guesses -= 2;
//                    updateGuesses();
//                    setState(GameState.PICKING_OPTION);
//                    return;
//                }
//                textInput.handleKeyInput(key);
//                break;
//            }
//            case READING_TEXT:
//            case GAME_OVER:
//            case GAME_WON:
//                if (!textBox.hasNextLine()) {
//                    setState(GameState.PICKING_OPTION);
//                    return;
//                }
//                if (key == Key.ENTER)
//                    textBox.nextLine();
//                break;
//            case PICKING_OPTION: {
//                if (key == Key.ENTER)
//                    action.select();
//                if (key == Key.ARROW_LEFT)
//                    action.scrollLeft();
//                if (key == Key.ARROW_RIGHT)
//                    action.scrollRight();
//                break;
//            }
//            default:
//        }
//    }
//
//    @Override
//    protected void focusLost(FocusEvent event) {
//        setScene(new PauseMenu(this));
//    }
//
//    private void setState(@Nonnull GameState state) {
//        this.state = state;
//        switch (state) {
//            case READING_TEXT: {
//                action.setVisible(false);
//                textBox.setVisible(true);
//                break;
//            }
//            case PICKING_OPTION: {
//                if (timer != null)
//                    timer.start();
//                if (guesses == 0) {
//                    setState(GameState.GAME_OVER);
//                    return;
//                }
//                if (textInput.allLocked()) {
//                    setState(GameState.GAME_WON);
//                    return;
//                }
//                textBox.setVisible(false);
//                action.setVisible(true);
//                action.setFocused(true);
//                updateGuessedLetters();
//                break;
//            }
//            case GAME_OVER: {
//                if (timer != null)
//                    timer.toggle();
//                action.setVisible(false);
//                textBox.setVisible(true);
//                List<String> s = script.getSection(ScriptSection.GAME_LOST, KEYWORD, word);
//                textBox.addLines(s);
//                fadeOut.onFinish(() -> setScene(new EndScreen(false)));
//                textBox.onFinish(fadeOut::spawn);
//                break;
//            }
//            case GUESSING_LETTER:
//            case GUESSING_WORD:
//                action.setVisible(false);
//                break;
//            case GAME_WON: {
//                if (timer != null)
//                    timer.toggle();
//                if (phase.hasNext()) {
//                    Phase next = Validator.requireNotNull(phase.next());
//                    fadeOut.onFinish(() -> setScene(new StoryMode(next)));
//                    fadeOut.spawn();
//                    return;
//                }
//                action.setVisible(false);
//                textBox.setVisible(true);
//                List<String> script = Script.getScript().getSection(ScriptSection.GAME_WON);
//                textBox.addLines(script);
//                fadeOut.onFinish(() -> setScene(new EndScreen(true)));
//                textBox.onFinish(fadeOut::spawn);
//                break;
//            }
//            default:
//        }
//        overlay.clear();
//        overlay.setIcons(state.getOverlay());
//    }
//
//    private void updateGuesses() {
//        if (guesses < 0)
//            guesses = 0;
//        BufferedImage guessText = new TexturePreprocessor(String.valueOf(guesses))
//                .color(Color.RED)
//                .scale(3)
//                .removeBackground()
//                .build();
//        this.guessText.setImage(guessText);
//        this.guessText.setLocation(Location.bottomRight(this.guessText.getBounds()));
//    }
//
//    private boolean guessLetter(char c) {
//        if (word.indexOf(c) == -1) {
//            guessed.put(c, false);
//            return false;
//        }
//        Set<Integer> indices = new HashSet<>();
//        char[] array = word.toCharArray();
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] == c)
//                indices.add(i);
//        }
//        for (int i : indices) {
//            textInput.setCharacter(i, c);
//            textInput.lockCharacter(i);
//        }
//        guessed.put(c, true);
//        return true;
//    }
//
//    private boolean guessWord(String word) {
//        char[] guessedWord = word.toCharArray();
//        char[] actualWord = this.word.toCharArray();
//        Set<Integer> indices = new HashSet<>();
//        for (int i = 0; i < actualWord.length; i++) {
//            if (guessedWord[i] == actualWord[i])
//                indices.add(i);
//            else
//                textInput.setCharacter(i, ' ');
//        }
//        for (int i : indices) {
//            textInput.setCharacter(i, actualWord[i]);
//            textInput.lockCharacter(i);
//        }
//        return !indices.isEmpty();
//    }
//
//    private void updateGuessedLetters() {
//        StringBuilder builder = new StringBuilder();
//        for (Map.Entry<Character, Boolean> entry : guessed.entrySet()) {
//            // recommended by sonarlint instead of !entry.getValue()
//            if (Boolean.FALSE.equals(entry.getValue()))
//                builder.append(entry.getKey()).append(" ");
//        }
//        if (builder.length() == 0)
//            return;
//        BufferedImage image = new TexturePreprocessor(builder.toString())
//                .color(Color.RED)
//                .removeBackground()
//                .scale(2)
//                .build();
//        if (letters == null) {
//            letters = new FixedTexture(this, image);
//            letters.setRenderPriority(new RenderPriority(126));
//            addRenderable(letters);
//            letters.spawn();
//        } else
//            this.letters.setImage(image);
//        this.letters.setLocation(Location.topCenter(letters.getBounds()));
//    }
//}