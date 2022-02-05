package dev.glitchedcoder.hangman.sound;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Validator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public enum Sound {

    UPGRADE_PURCHASE("upgrade_purchase"),
    THEME_SONG("theme_song");

    private final String loc;

    Sound(String loc) {
        this.loc = "/sound/" + loc + ".wav";
    }

    public AudioInputStream asStream() {
        URL resource = Hangman.class.getResource(this.loc);
        Validator.requireNotNull(resource, "Sound '{}' does not exist.", name());
        File file = new File(resource.getFile());
        try {
            return AudioSystem.getAudioInputStream(file);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }
}