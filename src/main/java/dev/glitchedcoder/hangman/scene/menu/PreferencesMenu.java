package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.json.Strings;
import dev.glitchedcoder.hangman.scene.ApiKeyEntry;
import dev.glitchedcoder.hangman.ui.Debug;
import dev.glitchedcoder.hangman.ui.Mode;
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
    private static final byte COMPONENT_SIZE = 7;

    public PreferencesMenu(@Nonnull MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
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
    protected void onInit() {
        ScrollableMenuComponent<Resolution> resolutions = new ScrollableMenuComponent<>(this, Resolution.values, SCALAR);
        ScrollableMenuComponent<NSFL> nsfl = new ScrollableMenuComponent<>(this, NSFL.values, SCALAR);
        ScrollableMenuComponent<Mode> mode = new ScrollableMenuComponent<>(this, Mode.values, SCALAR);
        ScrollableMenuComponent<Debug> debug = new ScrollableMenuComponent<>(this, Debug.values, SCALAR);
        resolutions.setIndex(config.getResolution());
        nsfl.setIndex(config.getNSFL());
        mode.setIndex(config.getMode());
        debug.setIndex(config.getDebug());
        MenuComponent changeKey = new MenuComponent(this, Strings.CHANGE_KEY, SCALAR);
        MenuComponent applyComponent = new MenuComponent(this, Strings.APPLY, SCALAR);
        MenuComponent backComponent = new MenuComponent(this, Strings.MENU_BACK, SCALAR);
        if (!config.getMode().isOnline())
            changeKey.setFocusable(false);
        changeKey.onSelect(() -> setScene(new ApiKeyEntry()));
        applyComponent.onSelect(() -> {
            config.setNsfl(nsfl.getSelected());
            config.setResolution(resolutions.getSelected());
            config.setMode(mode.getSelected());
            config.setDebug(debug.getSelected());
            Hangman.restart();
        });
        backComponent.onSelect(() -> setScene(parent));
        components[0] = resolutions;
        components[1] = nsfl;
        components[2] = mode;
        components[3] = debug;
        components[4] = changeKey;
        components[5] = applyComponent;
        components[6] = backComponent;
        super.onInit();
        autoCenter();
    }

    @Override
    protected byte getComponentSpacing() {
        return 10;
    }
}