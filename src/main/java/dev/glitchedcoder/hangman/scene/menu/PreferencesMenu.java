package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.scene.ApiKeyEntry;
import dev.glitchedcoder.hangman.window.Resolution;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public class PreferencesMenu extends Menu {

    private final MainMenu parent;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 4;

    public PreferencesMenu(@Nonnull MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
        ScrollableMenuComponent resolutions = new ScrollableMenuComponent(this, Resolution.values, SCALAR);
        MenuComponent changeKey = new MenuComponent(this, "Change API Key", SCALAR);
        MenuComponent applyComponent = new MenuComponent(this, "APPLY & RESTART", SCALAR);
        MenuComponent backComponent = new MenuComponent(this, "BACK", SCALAR);
        changeKey.onSelect(() -> setScene(new ApiKeyEntry()));
        applyComponent.onSelect(() -> {
            Resolution resolution = (Resolution) resolutions.getSelected();
            config.setResolution(resolution);
            Hangman.restart();
        });
        backComponent.onSelect(() -> setScene(parent));
        components[0] = resolutions;
        components[1] = changeKey;
        components[2] = applyComponent;
        components[3] = backComponent;
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
    protected void onLoad() {
        super.onLoad();
        autoCenter();
    }

    @Override
    protected byte getYOffset() {
        return 10;
    }
}