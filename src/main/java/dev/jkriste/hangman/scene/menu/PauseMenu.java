package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.json.Strings;
import dev.jkriste.hangman.window.Scene;
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
    protected void onInit() {
        MenuComponent resume = new MenuComponent(this, Strings.RESUME, SCALAR);
        MenuComponent mainMenu = new MenuComponent(this, Strings.MAIN_MENU, SCALAR);
        MenuComponent exit = new MenuComponent(this, Strings.MENU_EXIT, SCALAR);
        resume.onSelect(() -> setScene(scene));
        mainMenu.onSelect(() -> setScene(new MainMenu()));
        exit.onSelect(Hangman::exit);
        components[0] = resume;
        components[1] = mainMenu;
        components[2] = exit;
        super.onInit();
        autoCenter();
    }

    @Override
    protected byte getComponentSpacing() {
        return 10;
    }
}