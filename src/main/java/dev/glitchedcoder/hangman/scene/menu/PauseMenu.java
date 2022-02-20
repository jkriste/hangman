package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.window.Scene;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public class PauseMenu extends Menu {

    private final Scene scene;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 3;

    public PauseMenu(@Nonnull Scene scene) {
        this.scene = scene;
        this.components = new MenuComponent[COMPONENT_SIZE];
        MenuComponent resume = new MenuComponent(this, "Resume", SCALAR);
        MenuComponent mainMenu = new MenuComponent(this, "Main Menu", SCALAR);
        MenuComponent exit = new MenuComponent(this, "Exit", SCALAR);
        resume.onSelect(() -> setScene(scene));
        mainMenu.onSelect(() -> setScene(new MainMenu()));
        exit.onSelect(Hangman::exit);
        components[0] = resume;
        components[1] = mainMenu;
        components[2] = exit;
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
    protected void onLoad() {
        super.onLoad();
        autoCenter();
    }

    @Override
    protected byte getComponentSpacing() {
        return 10;
    }
}