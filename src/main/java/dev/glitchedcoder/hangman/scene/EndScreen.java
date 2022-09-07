package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.FadeOut;
import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.Sprite;
import dev.glitchedcoder.hangman.json.Strings;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.ui.AnimatedTexture;
import dev.glitchedcoder.hangman.ui.NSFL;
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

    private final boolean won;
    private final Set<Key> keys;
    private final FadeOut fadeOut;
    private final Sprite hangingMan;
    private final LightFixture light;
    private final IconOverlay overlay;
    private final FixedTexture gameText;

    public EndScreen(boolean won) {
        this.won = won;
        BufferedImage text = new TexturePreprocessor(won ? Strings.WIN : Strings.LOSS)
                .color(won ? Color.GREEN : Color.RED)
                .scale(5)
                .removeBackground()
                .build();
        this.gameText = new FixedTexture(this, text);
        this.overlay = new IconOverlay(this, Color.WHITE);
        this.overlay.setIcons(IconLayout.GAME_FINISH);
        this.keys = KeySelector.singleton(Key.ENTER);
        this.hangingMan = new Sprite(this, AnimatedTexture.HANGING_MAN, (byte) 2, 4);
        this.light = new LightFixture(this, (byte) 10);
        this.fadeOut = new FadeOut(this, Color.BLACK, (byte) 11);
    }

    @Override
    protected void onInit() {
        this.fadeOut.onFinish(() -> setScene(new MainMenu()));
        hangingMan.setRenderPriority(RenderPriority.MAX);
        light.setRenderPriority(RenderPriority.HIGH);
        addRenderables(overlay, gameText, light, hangingMan, fadeOut);
        light.setLocation(Location::topCenter);
        hangingMan.setLocation(Location::topCenter);
        overlay.setLocation(Location::bottomLeft);
        gameText.setLocation(Location::center);
        spawnAll(overlay, gameText, light, hangingMan);
        if (won || config.getNSFL() == NSFL.OFF)
            hangingMan.setVisible(false);
    }

    @Override
    protected void onDispose() {
        disposeAll(overlay, gameText);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.ENTER)
            fadeOut.spawn();
    }
}