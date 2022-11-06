package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.window.key.Key;
import dev.jkriste.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class SMode extends BaseGame {

    private final Phase phase;
    private final Set<Key> keys;

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
    }

    @Override
    protected void onStateChange(@Nonnull GameState newState) {

    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
    }

    @Override
    protected void onKeyPress(Key key) {
        if (noKeyInput())
            return;

    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }
}
