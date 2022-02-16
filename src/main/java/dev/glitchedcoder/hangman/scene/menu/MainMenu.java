package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.scene.ApiKeyEntry;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public class MainMenu extends Menu {

    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 4;

    public MainMenu() {
        this.components = new MenuComponent[COMPONENT_SIZE];
        MenuComponent singlePlayer = new MenuComponent(this, "SINGLEPLAYER", SCALAR);
        MenuComponent multiPlayer = new MenuComponent(this, "MULTIPLAYER", SCALAR);
        MenuComponent preferences = new MenuComponent(this, "PREFERENCES", SCALAR);
        MenuComponent exit = new MenuComponent(this, "EXIT", SCALAR);
        singlePlayer.onSelect(() -> setScene(new SingleplayerMenu(this)));
        // todo
        multiPlayer.onSelect(() -> setScene(new ApiKeyEntry()));
        preferences.onSelect(() -> setScene(new PreferencesMenu(this)));
        exit.onSelect(Hangman::exit);
        this.components[0] = singlePlayer;
        this.components[1] = multiPlayer;
        this.components[2] = preferences;
        this.components[3] = exit;
    }

    @Nullable
    @Override
    protected Menu getParent() {
        return null;
    }

    @Override
    protected MenuComponent[] getComponents() {
        return this.components;
    }

    @Override
    protected byte getYOffset() {
        return 10;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        autoCenter();
    }
}