package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.FadeOut;
import dev.glitchedcoder.hangman.json.Strings;
import dev.glitchedcoder.hangman.scene.mode.SIntro;
import dev.glitchedcoder.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;

@EqualsAndHashCode(callSuper = true)
public class SingleplayerMenu extends Menu {

    private FadeOut fadeOut;

    private final MainMenu parent;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 3;

    public SingleplayerMenu(MainMenu parent) {
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
    protected byte getComponentSpacing() {
        return 10;
    }

    @Override
    protected void onInit() {
        MenuComponent storyMode = new MenuComponent(this, Strings.STORY, SCALAR);
        MenuComponent freeMode = new MenuComponent(this, Strings.FREE, SCALAR);
        MenuComponent back = new MenuComponent(this, Strings.MENU_BACK, SCALAR);
        this.fadeOut = new FadeOut(this, Color.BLACK, (byte) 8);
        addRenderable(fadeOut);
        storyMode.onSelect(() -> {
            fadeOut.onFinish(() -> setScene(new SIntro()));
            fadeOut.spawn();
        });
        freeMode.onSelect(() -> {
//            fadeOut.onFinish(() -> setScene(new FreeMode(null)));
            fadeOut.spawn();
        });
        back.onSelect(() -> setScene(parent, true));
        components[0] = storyMode;
        components[1] = freeMode;
        components[2] = back;
        super.onInit();
        autoCenter();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        fadeOut.dispose();
    }

    @Override
    protected void onKeyPress(Key key) {
        if (fadeOut.shouldDraw())
            return;
        super.onKeyPress(key);
    }
}