package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.Entity;
import dev.glitchedcoder.hangman.entity.FadeIn;
import dev.glitchedcoder.hangman.entity.FadeOut;
import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.sound.Sound;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
public class Splash extends Scene {

    private final FadeIn fadeIn;
    private final FadeOut fadeOut;
    private final FixedTexture text;
    private final FixedTexture portrait;

    private static final byte SKIP = 12;

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
        fadeIn.spawn();
        text.spawn();
        portrait.spawn();
        fadeIn.onFinish(() -> {
            playSound(Sound.SPLASH_SOUND);
            fadeIn.dispose();
            executor.schedule(fadeOut::spawn, 1, TimeUnit.SECONDS);
        });
        fadeOut.onFinish(() -> {
            // todo: check if api key is valid
            if (config.getApiKey().isEmpty())
                setScene(new ApiKeyEntry());
            else
                setScene(new MainMenu());
        });
    }

    @Override
    protected void onUnload() {
        Stream.of(
                fadeIn,
                fadeOut,
                text,
                portrait
        ).forEach(Entity::dispose);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return KeySelector.none();
    }
}