package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.entity.Timer;
import dev.jkriste.hangman.json.ScriptSection;
import dev.jkriste.hangman.scene.EndScreen;
import dev.jkriste.hangman.scene.menu.PauseMenu;
import dev.jkriste.hangman.scene.menu.ScrollableMenuComponent;
import dev.jkriste.hangman.ui.CharMap;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.key.Key;
import dev.jkriste.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class SMode extends BaseGame {

    private FixedTexture guessesText;
    private FixedTexture guessedLettersText;

    private final Timer timer;
    private final Phase phase;
    private final Set<Key> keys;
    private final ScrollableMenuComponent<Action> actionSelector;

    protected SMode(@Nonnull Phase phase) {
        super(requestWord(phase.getWordLength()));
        this.phase = phase;
        this.keys = KeySelector.create()
                .group(Key.ALPHABETICAL_KEYS)
                .group(Key.ARROW_KEYS)
                .with(Key.ENTER)
                .with(Key.ESCAPE)
                .with(Key.BACKSPACE)
                .build();
        this.actionSelector = new ScrollableMenuComponent<>(this, Action.values, 2.5);
        this.timer = phase.hasNext() ? null : new Timer(this, Color.RED, TimerPreset.SEC90);
    }

    @Override
    protected void onStateChange(@Nonnull GameState newState) {
        switch (newState) {
            case READING_TEXT:
                actionSelector.setVisible(false);
                textBox.setVisible(true);
                break;
            case PICKING_OPTION:
                if (timer != null)
                    timer.start();
                if (input.allLocked()) {
                    setState(GameState.GAME_WON);
                    return;
                }
                if (guesses <= 0) {
                    setState(GameState.GAME_OVER);
                    return;
                }
                textBox.setVisible(false);
                actionSelector.setVisible(true);
                actionSelector.setFocused(true);
                updateGuessedLetters(this.guessedLettersText);
                break;
            case GAME_WON:
                if (timer != null)
                    timer.toggle();
                if (phase.hasNext()) {
                    Phase next = Validator.requireNotNull(phase.next());
                    fadeOut.onFinish(() -> setScene(new SMode(next)));
                    fadeOut.spawn();
                    return;
                }
                actionSelector.setVisible(false);
                textBox.addLines(script.getSection(ScriptSection.GAME_WON));
                textBox.setVisible(true);
                fadeOut.onFinish(() -> setScene(new EndScreen(true)));
                textBox.onFinish(fadeOut::spawn);
                break;
            case GAME_OVER:
                if (timer != null)
                    timer.toggle();
                actionSelector.setVisible(false);
                textBox.addLines(script.getSection(ScriptSection.GAME_LOST, WORD_KEYWORD, word));
                fadeOut.onFinish(() -> setScene(new EndScreen(false)));
                textBox.onFinish(fadeOut::spawn);
                textBox.setVisible(true);
                break;
            case GUESSING_WORD:
            case GUESSING_LETTER:
                actionSelector.setVisible(false);
                break;
            default:
        }
    }

    @Override
    protected void onInit() {
        super.onInit();
        this.textBox.addLines(phase.getScript());
        this.textBox.onFinish(() -> setState(GameState.PICKING_OPTION));
        this.actionSelector.setLocation(Location::center);
        this.actionSelector.getLocation().setY(input.getLocation().getY() + textBox.getBounds().height / 2);
        this.actionSelector.onSelect(action -> setState(action.getState()));
        if (timer != null) {
            this.timer.setLocation(Location::topRight);
            this.timer.onFinish(() -> setState(GameState.GAME_OVER));
            this.timer.setInterpolation(Color.GREEN, Color.RED);
            addRenderable(timer);
            timer.spawn();
        }
        updateGuesses(this.guessesText, Color.GREEN);
        updateGuessedLetters(this.guessedLettersText);
        addRenderables(actionSelector, guessesText, guessedLettersText);
        spawnAll(actionSelector, guessesText, guessedLettersText);
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        disposeAll(guessesText, guessedLettersText, actionSelector);
        if (timer != null)
            timer.dispose();
    }

    @Override
    protected void onFadeInFinish() {
        super.onFadeInFinish();
        textBox.spawn();
    }

    @Override
    protected void onKeyPress(Key key) {
        if (noKeyInput())
            return;
        if (key == Key.ESCAPE) {
            setScene(new PauseMenu(this), false);
            return;
        }
        switch (getState()) {
            case GUESSING_LETTER:
                if (key.isActionKey())
                    return;
                CharMap map = Validator.requireNotNull(key.getMap());
                if (!guessLetter(map.getCharacter()))
                    this.guesses -= WRONG_LETTER_PENALTY;
                Color c1 = TexturePreprocessor.interpolateLinear(Color.GREEN, Color.RED, (float) guesses / phase.getMaxGuesses());
                updateGuesses(this.guessesText, c1);
                setState(GameState.PICKING_OPTION);
                break;
            case GUESSING_WORD:
                if (key == Key.ENTER) {
                    if (input.isInvalid())
                        return;
                    if (guessWord(input.getInput())) {
                        setState(GameState.GAME_WON);
                        return;
                    }
                    this.guesses -= WRONG_WORD_PENALTY;
                    Color c2 = TexturePreprocessor.interpolateLinear(Color.GREEN, Color.RED, (float) guesses / phase.getMaxGuesses());
                    updateGuesses(this.guessesText, c2);
                    setState(GameState.PICKING_OPTION);
                    return;
                }
                if (key == Key.BACKSPACE || !key.isActionKey())
                    input.handleKeyInput(key);
                break;
            case READING_TEXT:
            case GAME_OVER:
            case GAME_WON:
                if (!textBox.hasNextLine()) {
                    setState(GameState.PICKING_OPTION);
                    return;
                }
                if (key == Key.ENTER)
                    textBox.nextLine();
                break;
            case PICKING_OPTION:
                if (key == Key.ENTER)
                    actionSelector.select();
                else if (key == Key.ARROW_LEFT)
                    actionSelector.scrollLeft();
                else if (key == Key.ARROW_RIGHT)
                    actionSelector.scrollRight();
                break;
            default:
        }
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }
}
