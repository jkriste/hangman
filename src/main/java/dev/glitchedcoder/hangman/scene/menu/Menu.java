package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public abstract class Menu extends Scene {

    private byte index;
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
     * Gets an array of {@link MenuComponent}s
     * that belong to the {@link Menu}.
     * <br />
     * The {@link MenuComponent}s in the returned array
     * should have at least one {@link MenuComponent} that's
     * {@link MenuComponent#isFocusable() focusable} and should
     * not be empty.
     *
     * @return The list of components that belong to the menu.
     */
    protected abstract MenuComponent[] getComponents();

    /**
     * Gets the spacing for {@link MenuComponent}s.
     * <br />
     * This acts as the artificial spacing between
     * {@link MenuComponent}s.
     * <br />
     * Only used when calling {@link #autoCenter()}.
     *
     * @return The spacing for {@link MenuComponent}s.
     */
    protected byte getComponentSpacing() {
        return 0;
    }

    /**
     * Gets the Y-offset for {@link MenuComponent}s.
     * <br />
     * Used to shift all {@link MenuComponent} upward or downward.
     * <br />
     * Only used when calling {@link #autoCenter()}.
     *
     * @return The Y-offset for {@link MenuComponent}s.
     */
    protected int getComponentYOffset() {
        return 0;
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onLoad() {
        addRenderable(overlay);
        MenuComponent[] components = getComponents();
        boolean scrollableComponents = false;
        index = -1;
        for (byte b = 0; b < components.length; b++) {
            MenuComponent component = components[b];
            if (index != -1)
                component.setFocused(false);
            if (component.isFocusable() && index == -1) {
                component.setFocused(true);
                index = b;
            }
            if (component instanceof ScrollableMenuComponent)
                scrollableComponents = true;
            addRenderable(component);
            component.spawn();
        }
        Validator.checkArgument(index != -1, "Empty or non-focusable components in menu.");
        if (scrollableComponents) {
            keys.add(Key.ARROW_RIGHT);
            keys.add(Key.ARROW_LEFT);
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
        overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
        overlay.spawn();
    }

    @Override
    protected final void onUnload() {
        // do nothing
    }

    @Override
    protected void onKeyPress(Key key) {
        switch (key) {
            case ARROW_DOWN: {
                MenuComponent focused = currentlyFocused();
                MenuComponent next = nextFocusable();
                focused.setFocused(false);
                Validator.requireNotNull(next).setFocused(true);
                break;
            }
            case ARROW_UP: {
                MenuComponent focused = currentlyFocused();
                MenuComponent last = lastFocusable();
                focused.setFocused(false);
                Validator.requireNotNull(last).setFocused(true);
                break;
            }
            case ARROW_LEFT: {
                MenuComponent focused = currentlyFocused();
                if (focused instanceof ScrollableMenuComponent) {
                    ScrollableMenuComponent<?> component = (ScrollableMenuComponent<?>) focused;
                    component.scrollLeft();
                }
                break;
            }
            case ARROW_RIGHT: {
                MenuComponent focused = currentlyFocused();
                if (focused instanceof ScrollableMenuComponent) {
                    ScrollableMenuComponent<?> component = (ScrollableMenuComponent<?>) focused;
                    component.scrollRight();
                }
                break;
            }
            case ENTER: {
                MenuComponent focused = currentlyFocused();
                focused.select();
                break;
            }
            case ESCAPE: {
                Menu parent = getParent();
                if (parent != null)
                    setScene(parent);
                break;
            }
            default:
                // do nothing
        }
    }

    protected final void autoCenter() {
        MenuComponent[] components = getComponents();
        byte yOffset = getComponentSpacing();
        Rectangle totalSize = new Rectangle(0, (components[0].getBounds().height + yOffset) * components.length);
        Location centered = Location.center(totalSize);
        for (byte b = 0; b < components.length; b++) {
            Rectangle bounds = components[b].getBounds();
            Location loc = Location.center(bounds);
            loc.setY(centered.getY() + (b * (bounds.height + yOffset)) + getComponentYOffset());
            components[b].setLocation(loc);
        }
    }

    @Nullable
    private MenuComponent nextFocusable() {
        MenuComponent[] components = getComponents();
        for (byte b = (byte) (index + 1); b < components.length; b++) {
            if (components[b].isFocusable()) {
                this.index = b;
                return components[b];
            }
        }
        for (byte b = 0; b <= index; b++) {
            if (components[b].isFocusable()) {
                this.index = b;
                return components[b];
            }
        }
        return null;
    }

    @Nullable
    private MenuComponent lastFocusable() {
        MenuComponent[] components = getComponents();
        for (byte b = (byte) (index - 1); b >= 0; b--) {
            if (components[b].isFocusable()) {
                this.index = b;
                return components[b];
            }
        }
        for (byte b = (byte) (components.length - 1); b >= index; b--) {
            if (components[b].isFocusable()) {
                this.index = b;
                return components[b];
            }
        }
        return null;
    }

    private MenuComponent currentlyFocused() {
        return getComponents()[index];
    }
}