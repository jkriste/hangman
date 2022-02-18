package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.scene.ApiKeyEntry;
import dev.glitchedcoder.hangman.ui.NSFL;
import dev.glitchedcoder.hangman.window.Resolution;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public class PreferencesMenu extends Menu {

    private final MainMenu parent;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 5;

    public PreferencesMenu(@Nonnull MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
        ScrollableMenuComponent<Resolution> resolutions = new ScrollableMenuComponent<>(this, Resolution.values, SCALAR);
        ScrollableMenuComponent<NSFL> nsfl = new ScrollableMenuComponent<>(this, NSFL.values, SCALAR);
        MenuComponent changeKey = new MenuComponent(this, "Change API Key", SCALAR);
        MenuComponent applyComponent = new MenuComponent(this, "APPLY & RESTART", SCALAR);
        MenuComponent backComponent = new MenuComponent(this, "BACK", SCALAR);
        changeKey.onSelect(() -> setScene(new ApiKeyEntry()));
        applyComponent.onSelect(() -> {
            NSFL notsafe = nsfl.getSelected();
            config.setNsfl(notsafe);
            Resolution resolution = resolutions.getSelected();
            config.setResolution(resolution);
            Hangman.restart();
        });
        backComponent.onSelect(() -> setScene(parent));
        components[0] = resolutions;
        components[1] = nsfl;
        components[2] = changeKey;
        components[3] = applyComponent;
        components[4] = backComponent;
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