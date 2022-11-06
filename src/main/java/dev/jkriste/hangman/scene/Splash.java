package dev.jkriste.hangman.scene;

import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.entity.FadeIn;
import dev.jkriste.hangman.entity.FadeOut;
import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.json.Words;
import dev.jkriste.hangman.scene.menu.MainMenu;
import dev.jkriste.hangman.ui.Mode;
import dev.jkriste.hangman.ui.Texture;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.ApiRequest;
import dev.jkriste.hangman.window.Scene;
import dev.jkriste.hangman.window.key.Key;
import dev.jkriste.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode(callSuper = true)
public class Splash extends Scene {

    private final FadeIn fadeIn;
    private final FadeOut fadeOut;
    private final FixedTexture text;
    private final FixedTexture portrait;

    private static final byte SKIP = 8;
    private static final byte FADE_OUT_DELAY = 2;

    public Splash() {
        this.fadeIn = new FadeIn(this, Color.BLACK, SKIP);
        this.fadeOut = new FadeOut(this, Color.BLACK, SKIP);
        BufferedImage website = new TexturePreprocessor(Texture.JKRISTE_DEV)
                .scale(0.75D)
                .build();
        this.text = new FixedTexture(this, website);
        BufferedImage portrait = new TexturePreprocessor(Texture.SELF_PORTRAIT)
                .scale(0.65D)
                .build();
        this.portrait = new FixedTexture(this, portrait);
        this.text.setLocation(Location::topCenter);
        this.portrait.setLocation(Location::bottomCenter);
    }

    @Override
    protected void onInit() {
        setBackground(Color.BLACK);
        addRenderables(fadeIn, fadeOut, text, portrait);
        spawnAll(fadeIn, text, portrait);
        fadeIn.onFinish(() -> {
            fadeIn.dispose();
            executor.schedule(fadeOut::spawn, FADE_OUT_DELAY, TimeUnit.SECONDS);
        });
        if (config.getMode().isOnline() && !ApiRequest.checkApi()) {
            Hangman.debug("Failed to contact the API, going offline...");
            config.setMode(Mode.OFFLINE);
            Words.loadWords();
        }
        fadeOut.onFinish(() -> {
            if (config.getMode() == Mode.OFFLINE || ApiRequest.checkApiKey())
                setScene(new MainMenu());
            else
                setScene(new ApiKeyEntry());
        });
    }

    @Override
    protected void onDispose() {
        disposeAll(fadeOut, fadeIn, text, portrait);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return KeySelector.none();
    }
}