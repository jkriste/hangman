package dev.glitchedcoder.hangman.sound;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum Sound {

    SPLASH_SOUND("splash_sound"),
    INVALID_SELECTION("invalid_selection"),
    MENU_SELECT("menu_select"),
    MENU_MOVE("menu_move"),
    KEY_PRESS("key_press");

    private final String loc;

    Sound(String loc) {
        this.loc = "/sound/" + loc + ".wav";
    }

    @Nullable
    public AudioInputStream asStream() {
        InputStream in = Hangman.class.getResourceAsStream(this.loc);
        Validator.requireNotNull(in, "InputStream for sound '{}' returned null.", name());
        BufferedInputStream bufferedIn = new BufferedInputStream(in);
        try {
            return AudioSystem.getAudioInputStream(bufferedIn);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }
}