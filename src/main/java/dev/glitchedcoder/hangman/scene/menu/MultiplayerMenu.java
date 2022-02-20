package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.Constants;
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
    private static final byte COMPONENT_SIZE = 3;

    public MultiplayerMenu(MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
        ScrollableIntMenuComponent length = new ScrollableIntMenuComponent(
                this, Constants.MIN_WORD_LENGTH, Constants.MAX_WORD_LENGTH, SCALAR
        );
        MenuComponent next = new MenuComponent(this, "Next", SCALAR);
        MenuComponent back = new MenuComponent(this, "Back", SCALAR);
        components[0] = length;
        components[1] = next;
        components[2] = back;
        next.onSelect(() -> setScene(new WordSelectionMenu(this, length.getSelected().byteValue())));
        back.onSelect(() -> setScene(parent));
        BufferedImage headerText = new TexturePreprocessor("User 1 select word length")
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
    protected void onLoad() {
        super.onLoad();
        autoCenter();
        addRenderable(header);
        header.setLocation(Location.topCenter(header.getBounds()));
        header.spawn();
    }
}