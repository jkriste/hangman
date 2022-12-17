package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.entity.Entity;
import dev.jkriste.hangman.entity.EntityType;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.entity.RenderPriority;
import dev.jkriste.hangman.json.Script;
import dev.jkriste.hangman.json.Strings;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A {@link MenuComponent} represents an interactible {@link Entity}.
 * <br />
 * Generally present in {@link Menu}s, but can also be used in {@link Scene}s.
 * <br />
 * A {@link MenuComponent} has three states:
 * <table>
 *     <tr>
 *         <th>State</th>
 *         <th>Color</th>
 *         <th>Comments</th>
 *     </tr>
 *     <tr>
 *         <td>Unfocused</td>
 *         <td>{@link Color#WHITE}</td>
 *         <td>Acts as the component being in an idle state.</td>
 *     </tr>
 *     <tr>
 *         <td>Focused</td>
 *         <td>{@code Color(202,46,12)}</td>
 *         <td>Acts as the component being hovered upon.</td>
 *     </tr>
 *     <tr>
 *         <td>Unfocusable</td>
 *         <td>{@link Color#DARK_GRAY}</td>
 *         <td>This component cannot be focused on.</td>
 *     </tr>
 * </table>
 *
 */
public class MenuComponent extends Entity {

    private String text;
    private Rectangle bounds;
    private Runnable onSelect;
    private BufferedImage image;
    private volatile boolean focused;
    private volatile boolean focusable;

    private final double scalar;

    private static final Color FOCUSED;
    private static final Color UNFOCUSED;
    private static final Color UNFOCUSABLE;

    static {
        FOCUSED = new Color(0xca2e0c);
        UNFOCUSED = Color.WHITE;
        UNFOCUSABLE = Color.DARK_GRAY;
    }

    @ParametersAreNonnullByDefault
    protected MenuComponent(Scene view, Strings s, double scale) {
        this(view, Script.getScript().getString(s), scale);
    }

    @ParametersAreNonnullByDefault
    protected MenuComponent(Scene view, String text, double scale) {
        super(view, EntityType.MENU_COMPONENT);
        this.scalar = scale;
        this.text = text;
        this.focusable = true;
        setRenderPriority(RenderPriority.HIGH);
        update();
    }

    @Override
    protected void onLoad() {
        update();
    }

    @Override
    protected void onUnload() {
        this.image = null;
        this.bounds = null;
        this.text = null;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        // do nothing
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        graphics.drawImage(this.image, getLocation().getX(), getLocation().getY(), null);
    }

    /**
     * Checks whether the {@link MenuComponent} is focused.
     * <br />
     * A focused {@link MenuComponent} represents the component
     * that is currently selected. Whether the {@link MenuComponent}
     * is focused or not will change the {@link Color} of the component.
     * <br />
     * Generally, only <b>one</b> {@link MenuComponent} can be focused at a time.
     *
     * @return True if the {@link MenuComponent} is focused, false otherwise.
     */
    public boolean isFocused() {
        return this.focused;
    }

    /**
     * Checks whether the {@link MenuComponent} is focusable.
     * <br />
     * A focusable {@link MenuComponent} allows the component
     * to be focused upon. If unfocusable, the component cannot
     * be focused upon and will be skipped in the {@link Menu}.
     *
     * @return True if the {@link MenuComponent} is focusable, false otherwise.
     */
    public boolean isFocusable() {
        return this.focusable;
    }

    /**
     * Sets whether the {@link MenuComponent} is focused.
     * <br />
     * Generally should not be calling this method when in use
     * by a {@link Menu}. However, it can be used in a {@link Scene}.
     * <br />
     * If the {@link MenuComponent} is not {@link #isFocusable() focusable},
     * this method will not do anything and the {@link MenuComponent} will be unchanged.
     * <br />
     * By default, {@link MenuComponent} will not be focused.
     *
     * @param focused True if the {@link MenuComponent} should be focused, false otherwise.
     */
    public void setFocused(boolean focused) {
        this.focused = focused;
        update();
    }

    /**
     * Sets whether the {@link MenuComponent} is focusable.
     * <br />
     * If the {@link MenuComponent} is not focusable,
     * {@link #setFocused(boolean)} will not change the
     * {@link MenuComponent} and will remain the same.
     * <br />
     * By default, {@link MenuComponent} will be focusable.
     *
     * @param focusable True if the {@link MenuComponent} should be focusable, false otherwise.
     */
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
        update();
    }

    /**
     * Sets what to run when the {@link MenuComponent} is selected.
     * <br />
     * For {@link Menu}s this often changes the {@link Scene}.
     * For {@link Scene}s this often involves changing the current {@link Scene}.
     * <br />
     * The given {@link Runnable} will <b>NOT</b> run
     * asynchronous to the main game thread.
     *
     * @param runnable What to run when selected.
     */
    public void onSelect(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onSelect = runnable;
    }

    /**
     * Sets the text to display for the {@link MenuComponent}.
     *
     * @param text The text to display.
     * @throws IllegalArgumentException Thrown if the given text is blank or empty.
     */
    public void setText(@Nonnull String text) {
        Validator.checkArgument(!text.isBlank(), "Given text '{}' is blank or empty.", text);
        this.text = text;
        update();
    }

    /**
     * Selects the {@link MenuComponent}.
     * <br />
     * If {@link #onSelect(Runnable)} was not called or the
     * stored {@link Runnable} is {@code null}, this method
     * will do nothing. Otherwise, it will run the given
     * {@link Runnable} <b>synchronous</b> to the running thread.
     */
    public void select() {
        if (onSelect != null)
            onSelect.run();
    }

    /**
     * Updates the {@link MenuComponent}.
     * <br />
     * If the {@link #setText(String) text}, {@link #getLocation() location},
     * or {@link #isFocused() state} was changed, the {@link MenuComponent}
     * will be updated accordingly and will be {@link Location#center(Rectangle) recentered}.
     */
    protected void update() {
        this.image = new TexturePreprocessor(this.text)
                .color(focusable ? (focused ? FOCUSED : UNFOCUSED) : UNFOCUSABLE)
                .scale(scalar)
                .removeBackground()
                .build();
        this.bounds = new Rectangle(image.getWidth(), image.getHeight());
        // account for any changes in text
        int newX = Location.center(this.bounds).getX();
        getLocation().setX(newX);
    }
}