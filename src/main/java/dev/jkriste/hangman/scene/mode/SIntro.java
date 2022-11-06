package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.json.ScriptSection;
import dev.jkriste.hangman.scene.menu.PauseMenu;
import dev.jkriste.hangman.ui.Portrait;
import dev.jkriste.hangman.window.key.Key;
import dev.jkriste.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class SIntro extends BaseGame {

    private final Set<Key> keys;

    public SIntro() {
        super(null);
        this.keys = KeySelector.create()
                .with(Key.ENTER)
                .with(Key.ESCAPE)
                .build();
    }

    @Override
    protected void onStateChange(@Nonnull GameState newState) {
        if (newState == GameState.READING_TEXT)
            textBox.spawn();
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return keys;
    }

    @Override
    protected void onInit() {
        super.onInit();
        fadeOut.onFinish(() -> setScene(new SMode(Phase.PHASE_ONE)));
        List<String> intro = script.getSection(ScriptSection.INTRODUCTION, CRIME_KEYWORD, script.randomCrime());
        textBox.addLines(intro);
        textBox.onFinish(this::onIntroFinish);
    }

    @Override
    protected boolean isPlayable() {
        return false;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (noKeyInput() || getState() != GameState.READING_TEXT)
            return;
        if (key == Key.ESCAPE)
            setScene(new PauseMenu(this), false);
        else if (key == Key.ENTER)
            textBox.nextLine();
    }

    @Override
    protected Portrait getPortrait() {
        return Portrait.UNKNOWN;
    }

    @Override
    protected GameState getStartingState() {
        return GameState.READING_TEXT;
    }

    private void onIntroFinish() {
        textBox.setPortrait(Portrait.EXECUTIONER);
        boolean playedBefore = config.hasPlayedBefore();
        List<String> lines = script.getSection(playedBefore ? ScriptSection.INTRODUCTION_NO_TUTORIAL : ScriptSection.INTRODUCTION_TUTORIAL);
        textBox.addLines(lines);
        textBox.onFinish(this::onScriptFinish);
    }

    private void onScriptFinish() {
        setState(GameState.TRANSITION);
        fadeOut.spawn();
    }
}