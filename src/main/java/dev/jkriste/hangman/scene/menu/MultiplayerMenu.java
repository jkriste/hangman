package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.json.Strings;
import dev.jkriste.hangman.scene.mode.TimerPreset;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.Constants;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class MultiplayerMenu extends Menu {

    private final MainMenu parent;
    private final FixedTexture header;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 4;

    public MultiplayerMenu(MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
        BufferedImage headerText = new TexturePreprocessor(Strings.MULTI_PROMPT)
                .scale(3)
                .color(Color.WHITE)
                .removeBackground()
                .build();
        this.header = new FixedTexture(this, headerText);
    }

    @Nullable
    @Override
    protected Menu getParent() {
        return this.parent;
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
    protected void onInit() {
        ScrollableIntMenuComponent length = new ScrollableIntMenuComponent(
                this, Constants.MIN_WORD_LENGTH, Constants.MAX_WORD_LENGTH, SCALAR
        );
        ScrollableMenuComponent<TimerPreset> timer = new ScrollableMenuComponent<>(this, TimerPreset.values, SCALAR);
        timer.setTitle("Timer:");
        MenuComponent next = new MenuComponent(this, Strings.NEXT, SCALAR);
        MenuComponent back = new MenuComponent(this, Strings.MENU_BACK, SCALAR);
        components[0] = length;
        components[1] = timer;
        components[2] = next;
        components[3] = back;
        next.onSelect(() -> setScene(new WordSelectionMenu(this, length.getSelected().getInt(), timer.getSelected()), false));
        back.onSelect(() -> setScene(parent));
        super.onInit();
        autoCenter();
        addRenderable(header);
        header.setLocation(Location::topCenter);
        header.spawn();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        header.dispose();
    }
}