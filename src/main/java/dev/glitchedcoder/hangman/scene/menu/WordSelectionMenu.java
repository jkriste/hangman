package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.TextInput;
import dev.glitchedcoder.hangman.scene.mode.FreeMode;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class WordSelectionMenu extends Menu {

    private final TextInput input;
    private final FixedTexture header;
    private final MultiplayerMenu parent;
    private final MenuComponent[] components;

    private static final byte COMPONENT_SIZE = 2;

    public WordSelectionMenu(MultiplayerMenu parent, byte length) {
        this.parent = parent;
        this.input = new TextInput(this, length, 4, false);
        this.keys.addAll(Key.ALPHABETICAL_KEYS);
        this.keys.add(Key.BACKSPACE);
        this.components = new MenuComponent[COMPONENT_SIZE];
        MenuComponent submit = new MenuComponent(this, "Submit", 3);
        MenuComponent back = new MenuComponent(this, "Back", 3);
        submit.onSelect(() -> {
            if (input.isInvalid())
                return;
            FreeMode mode = new FreeMode(input.getInput());
            setScene(mode);
        });
        back.onSelect(() -> setScene(parent));
        BufferedImage header = new TexturePreprocessor("User 1 input word")
                .color(Color.WHITE)
                .scale(3)
                .removeBackground()
                .build();
        this.header = new FixedTexture(this, header);
        components[0] = submit;
        components[1] = back;
    }

    @Nullable
    @Override
    protected Menu getParent() {
        return parent;
    }

    @Override
    protected MenuComponent[] getComponents() {
        return this.components;
    }

    @Override
    protected byte getComponentSpacing() {
        return 10;
    }

    @Override
    protected int getComponentYOffset() {
        return (int) (205 * config.getResolution().getScalar());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        autoCenter();
        overlay.setIcon(Icon.A_TO_Z, 2, 2);
        overlay.setIcon(Icon.BACKSPACE, 1, 2);
        addRenderables(input, header);
        header.setLocation(Location.topCenter(header.getBounds()));
        input.setLocation(Location.center(input.getBounds()));
        spawnAll(input, header);
    }

    @Override
    protected void onKeyPress(Key key) {
        super.onKeyPress(key);
        if (key == Key.BACKSPACE || !key.isActionKey())
            input.handleKeyInput(key);
    }
}