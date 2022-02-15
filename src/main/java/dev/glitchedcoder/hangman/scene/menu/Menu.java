package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public abstract class Menu extends Scene {

    protected Set<Key> keys;

    protected final IconOverlay overlay;

    protected Menu() {
        this.keys = KeySelector.create()
                .with(Key.ARROW_UP)
                .with(Key.ARROW_DOWN)
                .with(Key.ENTER)
                .with(Key.ESCAPE)
                .build();
        this.overlay = new IconOverlay(this, Color.WHITE, 2.5);
    }

    /**
     * Gets the parent {@link Menu}.
     * <br />
     * This is used for quick switching of {@link Scene}s.
     * <br />
     * For {@link Menu}s such as {@link MainMenu},
     * this method will return {@code null}.
     *
     * @return The parent of the {@link Menu}.
     */
    @Nullable
    protected abstract Menu getParent();

    /**
     * Gets a {@link List} of {@link MenuComponent}s
     * that belong to the {@link Menu}.
     * <br />
     * The {@link MenuComponent}s in the returned {@link List}
     * should have at least one {@link MenuComponent} that's
     * {@link MenuComponent#isFocusable() focusable} and should
     * not be {@link List#isEmpty() empty}.
     *
     * @return The list of components that belong to the menu.
     */
    protected abstract List<MenuComponent> getComponents();

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onLoad() {
        addRenderable(overlay);
        List<MenuComponent> components = getComponents();
        boolean scrollableComponents = false;
        MenuComponent firstFocusable = null;
        for (MenuComponent component : components) {
            if (firstFocusable == null && component.isFocusable())
                firstFocusable = component;
            if (component instanceof ScrollableMenuComponent)
                scrollableComponents = true;
            addRenderable(component);
            component.spawn();
        }
        Validator.requireNotNull(firstFocusable, "Empty or non-focusable components in menu.");
        firstFocusable.setFocused(true);
        if (scrollableComponents) {
            overlay.setIcon(Icon.LEFT_ARROW, 2, 0);
            overlay.setIcon(Icon.DOWN_ARROW, 2, 1);
            overlay.setIcon(Icon.RIGHT_ARROW, 2, 2);
            overlay.setIcon(Icon.UP_ARROW, 1, 1);
            overlay.setIcon(Icon.ESCAPE, 1, 0);
            overlay.setIcon(Icon.ENTER, 1, 2);
        } else {
            overlay.setIcon(Icon.UP_ARROW, 2, 0);
            overlay.setIcon(Icon.DOWN_ARROW, 2, 1);
            if (getParent() != null) {
                overlay.setIcon(Icon.ESCAPE, 1, 0);
                overlay.setIcon(Icon.ENTER, 1, 1);
            } else
                overlay.setIcon(Icon.ENTER, 2, 2);
        }
        overlay.spawn();
    }

    @Override
    protected void onUnload() {
        overlay.dispose();
        for (MenuComponent component : getComponents())
            component.dispose();
    }

    @Override
    protected void onKeyPress(Key key) {
        switch (key) {

        }
    }

    private MenuComponent nextFocusable() {
        return null;
    }

    private MenuComponent lastFocusable() {
        return null;
    }

    private MenuComponent currentlyFocused() {
        return null;
    }
}