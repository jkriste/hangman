package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.FadeIn;
import dev.glitchedcoder.hangman.entity.FadeOut;
import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.ApiRequest;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
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

    private static final byte SKIP = 11;

    public Splash() {
        this.fadeIn = new FadeIn(this, Color.WHITE, SKIP);
        this.fadeOut = new FadeOut(this, Color.BLACK, SKIP);
        BufferedImage text = new TexturePreprocessor("Made by Justin")
                .color(Color.DARK_GRAY)
                .removeBackground()
                .scale(4)
                .build();
        this.text = new FixedTexture(this, text);
        this.portrait = new FixedTexture(this, Texture.SELF_PORTRAIT);
        this.text.setLocation(Location.bottomCenter(this.text.getBounds()));
        this.portrait.setLocation(Location.center(this.portrait.getBounds()));
    }

    @Override
    protected void onLoad() {
        setBackground(Color.WHITE);
        addRenderables(fadeIn, fadeOut, text, portrait);
        spawnAll(fadeIn, text, portrait);
        fadeIn.onFinish(() -> {
            fadeIn.dispose();
            executor.schedule(fadeOut::spawn, 1, TimeUnit.SECONDS);
        });
        fadeOut.onFinish(() -> {
            if (ApiRequest.checkApiKey())
                setScene(new MainMenu());
            else
                setScene(new ApiKeyEntry());
        });
    }

    @Override
    protected void onUnload() {
        disposeAll(fadeOut, fadeIn, text, portrait);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return KeySelector.none();
    }
}