package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.scene.ApiKeyEntry;
import dev.glitchedcoder.hangman.scene.mode.FreeMode;
import dev.glitchedcoder.hangman.scene.mode.StoryIntro;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public class SingleplayerMenu extends Menu {

    private final MainMenu parent;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 3;

    public SingleplayerMenu(MainMenu parent) {
        this.parent = parent;
        this.components = new MenuComponent[COMPONENT_SIZE];
        MenuComponent storyMode = new MenuComponent(this, "Story Mode", SCALAR);
        MenuComponent freeMode = new MenuComponent(this, "Free Mode", SCALAR);
        MenuComponent back = new MenuComponent(this, "Back", SCALAR);
        storyMode.onSelect(() -> setScene(new StoryIntro()));
        freeMode.onSelect(() -> setScene(new FreeMode(null)));
        back.onSelect(() -> setScene(parent));
        components[0] = storyMode;
        components[1] = freeMode;
        components[2] = back;
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
    }
}