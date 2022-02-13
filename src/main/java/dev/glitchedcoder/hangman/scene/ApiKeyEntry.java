package dev.glitchedcoder.hangman.scene;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.TextInput;
import dev.glitchedcoder.hangman.scene.menu.MainMenu;
import dev.glitchedcoder.hangman.sound.Sound;
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
public class ApiKeyEntry extends Scene {

    private final Set<Key> keys;
    private final TextInput textInput;
    private final FixedTexture header;
    private final FixedTexture footer;
    private final FixedTexture subHeader;

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
        BufferedImage subHeaderText = new TexturePreprocessor("Type with your keyboard + press enter")
                .color(Color.GRAY)
                .scale(2D)
                .removeBackground()
                .build();
        this.header = new FixedTexture(this, headerText);
        this.footer = new FixedTexture(this, footerText);
        this.textInput = new TextInput(this, 5, 5, true);
        this.subHeader = new FixedTexture(this, subHeaderText);
    }

    @Override
    protected void onLoad() {
        this.header.setLocation(Location.topCenter(header.getBounds()));
        this.textInput.setLocation(Location.center(textInput.getBounds()));
        this.footer.setLocation(Location.bottomCenter(footer.getBounds()));
        Location subLoc = Location.topCenter(subHeader.getBounds());
        subLoc.add(0, header.getBounds().height);
        this.subHeader.setLocation(subLoc);
        addRenderables(header, footer, textInput, subHeader);
        spawnAll(header, footer, textInput, subHeader);
        footer.setVisible(false);
    }

    @Override
    protected void onUnload() {
        disposeAll(header, footer, textInput);
    }

    @Override
    protected void onKeyPress(Key key) {
        executor.schedule(() -> playSound(Sound.KEY_PRESS), 0, TimeUnit.SECONDS);
        if (key == Key.ENTER) {
            footer.setVisible(false);
            if (!textInput.isValid()) {
                playSound(Sound.INVALID_SELECTION);
                return;
            }
            config.setApiKey(textInput.getInput());
            if (ApiRequest.checkApiKey())
                setScene(new MainMenu());
            else
                footer.setVisible(true);
        } else
            textInput.handleKeyInput(key);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }
}