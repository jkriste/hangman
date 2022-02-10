package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.sound.Sound;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class Splash extends Scene {

    private final Set<Key> keys;
    private final BufferedImage image;
    private final BufferedImage image2;
    private final Location center;
    private final Location topCenter;

    public Splash() {
        this.keys = KeySelector.create()
                .with(Key.F)
                .build();
        this.image = new TexturePreprocessor("HELLO, GAMERS!")
                .color(Color.CYAN)
                .removeBackground()
                .scale(6D)
                .build();
        this.image2 = new TexturePreprocessor(Texture.EXECUTIONER)
                .color(Color.RED)
                .scale(6D)
                .build();
        this.center = Location.center(new Rectangle(image.getWidth(), image.getHeight()));
        this.topCenter = Location.topCenter(new Rectangle(image2.getWidth(), image2.getHeight()));
    }

    @Override
    protected void onLoad() {
        setBackground(Color.WHITE);
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
    protected void tick(byte count) {
        super.tick(count);
        System.out.println(count);
    }

    @Override
    protected void draw(Graphics2D graphics) {
        super.draw(graphics);
        graphics.drawImage(this.image, center.getX(), center.getY(), null);
        graphics.drawImage(this.image2, topCenter.getX(), topCenter.getY(), null);
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.F) {
            System.out.println("Playing sound.");
            playSound(Sound.THEME_SONG);
        }
    }
}