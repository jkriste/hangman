package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.TextBox;
import dev.glitchedcoder.hangman.entity.TextInput;
import dev.glitchedcoder.hangman.scene.menu.ScrollableMenuComponent;
import dev.glitchedcoder.hangman.ui.Portrait;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.ApiRequest;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class FreeMode extends Scene {

    private GameState state;

    private final String word;
    private final Set<Key> keys;
    private final TextBox textBox;
    private final LightFixture light;
    private final FixedTexture table;
    private final FixedTexture hands;
    private final TextInput textInput;
    private final IconOverlay overlay;
    private final ScrollableMenuComponent<Action> action;

    public FreeMode(@Nullable String word) {
        this.keys = KeySelector.create()
                .group(Key.ALPHABETICAL_KEYS)
                .group(Key.ARROW_KEYS)
                .with(Key.ENTER)
                .build();
        if (word == null) {
            int length = ApiRequest.randomWordLength();
            this.word = Validator.requireNotNull(ApiRequest.requestWord(length)).getWord();
        } else
            this.word = word;
        this.textInput = new TextInput(this, this.word.length(), 5, this.word.length() < 10);
        this.light = new LightFixture(this, (byte) 10, 4.1);
        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE).scale(4).build();
        BufferedImage hands = new TexturePreprocessor(Texture.HAND_TEXTURE).scale(4).build();
        this.table = new FixedTexture(this, table);
        this.hands = new FixedTexture(this, hands);
        this.overlay = new IconOverlay(this, Color.WHITE, 2.5);
        this.action = new ScrollableMenuComponent<>(this, Action.values, 2.5);
        this.textBox = new TextBox(this, Portrait.EXECUTIONER, Color.WHITE);
        this.state = GameState.PICKING_OPTION;
    }

    @Override
    protected void onLoad() {
        addRenderables(textInput, overlay, table, hands, light);

    }

    @Override
    protected void onUnload() {
        disposeAll(light, table, hands, textInput, overlay);
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        switch (state) {

        }
    }

    private void setState(@Nonnull GameState state) {
        this.state = state;
        switch (state) {
            case READING_TEXT:
                action.setVisible(false);
        }
        overlay.setIcons(state.getOverlay());
    }
}