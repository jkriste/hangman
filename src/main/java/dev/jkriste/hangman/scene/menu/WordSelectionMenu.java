package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.entity.FadeOut;
import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.entity.TextInput;
import dev.jkriste.hangman.json.Strings;
import dev.jkriste.hangman.scene.IconLayout;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class WordSelectionMenu extends Menu {

    private FadeOut fadeOut;
    private FixedTexture header;

    private final TextInput input;
    private final MultiplayerMenu parent;
    private final MenuComponent[] components;

    private static final byte COMPONENT_SIZE = 2;

    public WordSelectionMenu(MultiplayerMenu parent, int length) {
        this.parent = parent;
        this.input = new TextInput(this, length, 4, false);
        this.keys.addAll(Key.ALPHABETICAL_KEYS);
        this.keys.add(Key.BACKSPACE);
        this.components = new MenuComponent[COMPONENT_SIZE];
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

    @Nullable
    @Override
    protected IconLayout getLayout() {
        return IconLayout.WORD_SELECTION_MENU;
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
    protected void onInit() {
        BufferedImage header = new TexturePreprocessor(Strings.WORD_PROMPT)
                .color(Color.WHITE)
                .scale(3)
                .removeBackground()
                .build();
        this.header = new FixedTexture(this, header);
        this.fadeOut = new FadeOut(this, Color.BLACK, (byte) 8);
        MenuComponent submit = new MenuComponent(this, Strings.SUBMIT, 3);
        MenuComponent back = new MenuComponent(this, Strings.MENU_BACK, 3);
        submit.onSelect(() -> {
            if (input.isInvalid())
                return;
            fadeOut.spawn();
        });
//        fadeOut.onFinish(() -> setScene(new FreeMode(input.getInput())));
        back.onSelect(() -> setScene(parent));
        components[0] = submit;
        components[1] = back;
        super.onInit();
        autoCenter();
        addRenderables(input, this.header);
        this.header.setLocation(Location::topCenter);
        input.setLocation(Location::center);
        spawnAll(input, this.header);
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        disposeAll(input, header, fadeOut);
    }

    @Override
    protected void onKeyPress(Key key) {
        if (fadeOut.shouldDraw())
            return;
        super.onKeyPress(key);
        if (key == Key.BACKSPACE || !key.isActionKey())
            input.handleKeyInput(key);
    }
}