package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public abstract class Menu extends Scene {

    protected Set<Key> keys;

    protected Menu() {
        this.keys = KeySelector.create()
                .with(Key.ARROW_UP)
                .with(Key.ARROW_DOWN)
                .with(Key.ENTER)
                .with(Key.ESCAPE)
                .build();
    }

    @Nullable
    protected abstract Menu getParent();

    protected abstract List<MenuComponent> getComponents();

    protected abstract void select(@Nonnull MenuComponent component);

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onLoad() {
        List<MenuComponent> components = getComponents();
        Validator.checkArgument(!components.isEmpty(), "Empty components.");
        for (MenuComponent component : components) {

        }
    }

    @Override
    protected void onKeyPress(Key key) {
        switch (key) {

        }
    }
}
