package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.TextInput;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.ApiRequest;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class ApiKeyEntry extends Scene {

    private final Set<Key> keys;
    private final TextInput textInput;
    private final FixedTexture header;
    private final FixedTexture footer;
    private final IconOverlay iconOverlay;

    public ApiKeyEntry() {
        this.keys = KeySelector.create()
                .group(Key.WRITABLE_KEYS)
                .with(Key.ENTER)
                .with(Key.BACKSPACE)
                .build();
        BufferedImage headerText = new TexturePreprocessor("Please input your API key")
                .color(Color.LIGHT_GRAY)
                .scale(3D)
                .removeBackground()
                .build();
        BufferedImage footerText = new TexturePreprocessor("Failed to auth your API key")
                .color(Color.RED)
                .removeBackground()
                .scale(2D)
                .build();
        this.header = new FixedTexture(this, headerText);
        this.footer = new FixedTexture(this, footerText);
        this.textInput = new TextInput(this, 5, 5, true);
        this.iconOverlay = new IconOverlay(this, Color.WHITE, 2.5);
    }

    @Override
    protected void onLoad() {
        iconOverlay.setIcon(Icon.A_TO_Z, 2, 0);
        iconOverlay.setIcon(Icon.ZERO_TO_NINE, 2, 1);
        iconOverlay.setIcon(Icon.ENTER, 1, 0);
        iconOverlay.setIcon(Icon.BACKSPACE, 1, 1);
        this.header.setLocation(Location.topCenter(header.getBounds()));
        this.textInput.setLocation(Location.center(textInput.getBounds()));
        this.footer.setLocation(Location.bottomCenter(footer.getBounds()));
        this.iconOverlay.setLocation(Location.bottomLeft(iconOverlay.getBounds()));
        addRenderables(header, footer, textInput, iconOverlay);
        spawnAll(header, footer, textInput, iconOverlay);
        footer.setVisible(false);
    }

    @Override
    protected void onUnload() {
        disposeAll(header, footer, textInput);
    }

    @Override
    protected void onKeyPress(Key key) {
        if (key == Key.ENTER) {
            footer.setVisible(false);
            if (textInput.isInvalid()) {
                return;
            }
            textInput.lockAll(true);
            config.setApiKey(textInput.getInput());
            if (ApiRequest.checkApiKey())
                setScene(new MainMenu());
            else {
                footer.setVisible(true);
                textInput.lockAll(false);
            }
        } else
            textInput.handleKeyInput(key);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }
}