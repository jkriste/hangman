package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.sound.Sound;
import dev.glitchedcoder.hangman.sound.Volume;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class Splash extends Scene {

    private final Set<Key> keys;

    public Splash() {
        this.keys = KeySelector.create()
                .with(Key.F)
                .build();
    }

    @Override
    protected void onLoad() {
        System.out.println("Test: onLoad()");
    }

    @Override
    protected void onUnload() {
        System.out.println("Test: onUnload()");
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.F) {
            System.out.println("Playing sound.");
            playSound(Sound.THEME_SONG, Volume.NORMAL);
        }
    }
}