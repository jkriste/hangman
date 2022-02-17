package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class EndScreen extends Scene {

    private final Set<Key> keys;
    private final IconOverlay overlay;
    private final FixedTexture gameText;

    public EndScreen(boolean won) {
        BufferedImage text = new TexturePreprocessor(won ? "GAME WON" : "GAME OVER")
                .color(won ? Color.GREEN : Color.RED)
                .scale(5)
                .removeBackground()
                .build();
        this.gameText = new FixedTexture(this, text);
        this.overlay = new IconOverlay(this, Color.WHITE, 2.5);
        this.overlay.setIcon(Icon.ENTER, 2, 0);
        this.keys = KeySelector.create().with(Key.ENTER).build();
    }

    @Override
    protected void onLoad() {
        addRenderables(overlay, gameText);
        overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
        gameText.setLocation(Location.bottomCenter(gameText.getBounds()));
        spawnAll(overlay, gameText);
    }

    @Override
    protected void onUnload() {
        disposeAll(overlay, gameText);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.ENTER)
            setScene(new MainMenu());
    }
}