package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.entity.IconOverlay;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.scene.IconLayout;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;
import dev.jkriste.hangman.window.key.Key;
import dev.jkriste.hangman.window.key.KeySelector;
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
        this.index = -1;
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
     * <br />
     * The {@link MenuComponent}s should be ordered in the array
     * in the order of appearance, i.e., {@code getComponents[0]}
     * will be the first to appear, {@code getComponents[1]} will
     * be the second to appear, and so on.
     *
     * @return The list of components that belong to the menu.
     */
    protected abstract MenuComponent[] getComponents();

    /**
     * Gets the default {@link IconLayout} for the {@link Menu}.
     * <br />
     * If {@code null}, the {@link Menu} class will pick either:
     * <ul>
     *     <li>{@link IconLayout#MENU}</li>
     *     <li>{@link IconLayout#MENU_WITH_PARENT}</li>
     *     <li>{@link IconLayout#MENU_WITH_SCROLLABLE_COMPONENTS}</li>
     * </ul>
     * ...depending on the {@link Menu} configuration.
     * <br />
     * By default, this method returns {@code null}.
     *
     * @return The default {@link IconLayout} or {@code null} for no preference.
     */
    @Nullable
    protected IconLayout getLayout() {
        return null;
    }

    /**
     * Gets the spacing for {@link MenuComponent}s.
     * <br />
     * This acts as the artificial spacing between
     * {@link MenuComponent}s.
     * <br />
     * Only used when calling {@link #autoCenter()}.
     * <br />
     * By default, this method returns {@code 0}.
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
     * <br />
     * By default, this method returns {@code 0}.
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
    protected void onInit() {
        addRenderable(overlay);
        MenuComponent[] components = getComponents();
        boolean scrollableComponents = false;
        for (byte b = 0; b < components.length; b++) {
            MenuComponent component = components[b];
            if (this.index != -1)
                component.setFocused(false);
            if (component.isFocusable() && index == -1) {
                component.setFocused(true);
                this.index = b;
            }
            if (component instanceof ScrollableMenuComponent)
                scrollableComponents = true;
        }
        Validator.checkArgument(this.index != -1, "Empty or non-focusable components in menu.");
        addRenderables(components);
        spawnAll(components);
        // set a default icon overlay (based on the type of menu) if getLayout() is null
        if (scrollableComponents) {
            keys.add(Key.ARROW_RIGHT);
            keys.add(Key.ARROW_LEFT);
            overlay.setIcons(IconLayout.MENU_WITH_SCROLLABLE_COMPONENTS);
        } else
            overlay.setIcons(getParent() != null ? IconLayout.MENU_WITH_PARENT : IconLayout.MENU);
        if (getLayout() != null)
            overlay.setIcons(getLayout());
        overlay.setLocation(Location::bottomLeft);
        overlay.spawn();
    }

    @Override
    protected void onDispose() {
        disposeAll(getComponents());
        overlay.dispose();
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
                if (focused instanceof ScrollableMenuComponent)
                    ((ScrollableMenuComponent<?>) focused).scrollLeft();
                break;
            }
            case ARROW_RIGHT: {
                MenuComponent focused = currentlyFocused();
                if (focused instanceof ScrollableMenuComponent)
                    ((ScrollableMenuComponent<?>) focused).scrollRight();
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
                    setScene(parent, true);
                break;
            }
            default:
                // do nothing
        }
    }

    /**
     * Gets the {@link MenuComponent}s of the {@link Menu}
     * and {@link Location#center(Rectangle) centers} them
     * based on the following {@link Menu} preferences:
     * <ul>
     *    <li>{@link #getComponentSpacing()}</li>
     *    <li>{@link #getComponentYOffset()}</li>
     * </ul>
     * This method should be called <b>AFTER</b> {@code super.onInit()}.
     */
    protected final void autoCenter() {
        MenuComponent[] components = getComponents();
        byte yOffset = getComponentSpacing();
        int totalY = 0;
        for (MenuComponent component : components) {
            totalY += component.getBounds().height;
        }
        Rectangle totalSize = new Rectangle(0, totalY + (yOffset * components.length));
        Location centered = Location.center(totalSize);
        int currentY = centered.getY();
        for (MenuComponent component : components) {
            Rectangle bounds = component.getBounds();
            Location loc = Location.center(bounds);
            loc.setY(currentY + yOffset + getComponentYOffset());
            component.setLocation(loc);
            currentY += bounds.height + yOffset;
        }
    }

    /**
     * Gets the next {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     * <br />
     * Theoretically, this method should return a non-{@code null}
     * value since {@link #onInit()} checks for at least <i>one</i>
     * {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     *
     * @return The next {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     */
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

    /**
     * Gets the last {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     * <br />
     * Theoretically, this method should return a non-{@code null}
     * value since {@link #onInit()} checks for at least <i>one</i>
     * {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     *
     * @return The last {@link MenuComponent#isFocusable() focusable} {@link MenuComponent}.
     */
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

    /**
     * Gets the currently {@link MenuComponent#isFocused() focused} {@link MenuComponent}.
     *
     * @return The currently {@link MenuComponent#isFocused() focused} {@link MenuComponent}.
     */
    private MenuComponent currentlyFocused() {
        return getComponents()[index];
    }
}