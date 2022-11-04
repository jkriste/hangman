package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.entity.Entity;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.Renderable;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Acts as the level/scene being displayed in the {@link View}.
 */
@EqualsAndHashCode
public abstract class Scene implements KeyEventDispatcher {

    private volatile boolean disposed;
    private volatile boolean initialized;
    private final AtomicReference<Color> bg;
    private final List<Renderable> renderables;
    protected final ScheduledExecutorService executor = Hangman.getExecutor();

    protected static Dimension dimension;
    protected static final Config config = Config.getConfig();

    protected Scene() {
        this.bg = new AtomicReference<>(Color.BLACK);
        this.renderables = new CopyOnWriteArrayList<>();
    }

    /**
     * Called by {@link Window the window}, this method
     * helps the {@link Scene} to know which dimensions
     * to render the background and any other size-dependent
     * {@link Renderable} objects.
     * <br />
     * The given width and height should account for insets.
     *
     * @param d The dimension of the {@link Window} minus insets.
     */
    static void adjustDimensions(Dimension d) {
        dimension = d;
    }

    /**
     * Called when the {@link Scene} is being initialized.
     * <br />
     * This method is strictly for <b>first-time</b> initialization.
     * <br />
     * For {@link Scene}s that have already been initialized but
     * were {@link #onUnload() unloaded}, {@link #onLoad()} will
     * be called instead of this method.
     *
     * @see #hasInitialized()
     */
    protected abstract void onInit();

    /**
     * Called when the {@link Scene} is being disposed.
     * <br />
     * This method is strictly for <b>deletion</b>.
     * <br />
     * Once a {@link Scene} is disposed, it cannot be
     * {@link #onLoad() reloaded}.
     *
     * @see #isDisposed()
     */
    protected abstract void onDispose();

    /**
     * Gets the {@link Set<Key>}s that the {@link Scene} is listening for.
     * <br />
     * Use the {@link dev.glitchedcoder.hangman.window.key.KeySelector}
     * class to construct a {@link Set<Key>}s object.
     *
     * @return The {@link Set<Key>}s that the {@link Scene} is listening for.
     */
    protected abstract Set<Key> getKeyListeners();

    /**
     * Called after {@link #onInit() initialization}
     * or when the {@link Scene} is being reloaded.
     * <br />
     * This method should not be creating any {@link Renderable}
     * objects that are vital to the {@link Scene}.
     */
    protected void onLoad() {
    }

    /**
     * Called before {@link #onDispose() disposal}
     * or when the {@link Scene} is being unloaded.
     * <br />
     * This method should not dispose any {@link Renderable}
     * objects that are vital to the {@link Scene}.
     */
    protected void onUnload() {
    }

    /**
     * Called when a {@link Key} that is in
     * {@link #getKeyListeners() the key listeners}
     * is being pressed. If there are no key listeners,
     * this method will never be called, even when valid
     * {@link Key}s are being pressed.
     *
     * @param key The key that was pressed.
     */
    protected void onKeyPress(Key key) {
    }

    /**
     * Called when the {@link Window} has gained focus.
     *
     * @param event The focus event.
     */
    protected void focusGained(FocusEvent event) {
    }

    /**
     * Called when the {@link Window} has lost focus.
     * <br />
     * If in game, the {@link Scene} can handle this event
     * by opening a pause menu or other menu.
     *
     * @param event The focus event.
     */
    protected void focusLost(FocusEvent event) {
    }

    /**
     * Called when the {@link Window} is ticking the {@link View}.
     * <br />
     * While this method can be overidden, the implementation should
     * similarly call {@code super(count);} due to the handling of
     * {@link Renderable renderable objects}.
     *
     * @param count The tick count, usually {@code 0 <= count <= 30}.
     */
    protected void tick(byte count) {
        for (Renderable r : this.renderables) {
            if (r.shouldRemove()) {
                r.remove();
                renderables.remove(r);
                continue;
            }
            if (!r.shouldDraw())
                continue;
            r.tick(count);
        }
    }

    /**
     * Called when the {@link Window} needs to render the current frame.
     * <br />
     * While this method can be overidden, the implementation should
     * similarly call {@code super(graphics);} due to the handling of
     * {@link Renderable renderable objects}.
     *
     * @param graphics The graphics object.
     */
    protected void draw(Graphics2D graphics) {
        for (Renderable r : this.renderables) {
            if (!r.shouldDraw())
                continue;
            r.draw(graphics);
            if (r instanceof Entity) {
                Entity entity = (Entity) r;
                graphics.setColor(Color.GREEN);
                Location location = entity.getLocation();
                Rectangle bounds = entity.getBounds();
                graphics.drawRect(location.getX(), location.getY(), bounds.width, bounds.height);
            }
        }
    }

    /**
     * A shortcut for setting the current {@link Scene}.
     * <br />
     * Gets the {@link View} of the {@link Window} and
     * passes the given {@link Scene} onto it.
     * <br />
     * Will automatically {@link Scene#onDispose() dispose}
     * the last {@link Scene}.
     *
     * @param scene The scene to set.
     * @see #setScene(Scene, boolean) 
     */
    protected final void setScene(@Nonnull Scene scene) {
        this.setScene(scene, true);
    }

    /**
     * A shortcut for setting the current {@link Scene}.
     * <br />
     * Gets the {@link View} of the {@link Window} and
     * passes the given {@link Scene} onto it.
     * <br />
     * If {@code disposeLast} is {@code true}, all
     * {@link #renderables} will be cleared.
     *
     * @param scene The scene to set.
     * @param disposeLast True to dispose this scene, false otherwise.
     */
    protected final void setScene(@Nonnull Scene scene, boolean disposeLast) {
        Validator.requireNotNull(scene, "Given scene is null!");
        View view = Hangman.getWindow().getView();
        view.setScene(scene, disposeLast);
        if (disposeLast)
            this.renderables.clear();
    }

    /**
     * Adds the given {@link Renderable} object to the {@link Scene}.
     * <br />
     * Used to sort all {@link #renderables} by {@link RenderPriority}.
     *
     * @param renderable The renderable object to add.
     */
    protected final void addRenderable(@Nonnull Renderable renderable) {
        Validator.requireNotNull(renderable, "Given renderable is null!");
        this.renderables.add(renderable);
        Collections.sort(this.renderables);
    }

    /**
     * Adds the given {@link Renderable} objects to the {@link Scene}.
     * <br />
     * Used to sort all {@link #renderables} by {@link RenderPriority}.
     *
     * @param renderables The renderable objects to add.
     */
    protected final void addRenderables(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        if (renderables.length == 0)
            return;
        Collections.addAll(this.renderables, renderables);
        Collections.sort(this.renderables);
    }

    /**
     * A QoL method that {@link Renderable#spawn() spawns} all
     * of the given {@link Renderable} objects.
     *
     * @param renderables The renderable objects to spawn.
     */
    protected final void spawnAll(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        for (Renderable renderable : renderables)
            renderable.spawn();
    }

    /**
     * A QoL method that {@link Renderable#dispose() disposes}
     * all of the given {@link Renderable} objects.
     *
     * @param renderables The renderable objects to dispose of.
     */
    protected final void disposeAll(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        for (Renderable renderable : renderables)
            renderable.dispose();
    }

    /**
     * Checks whether the {@link Scene} has been {@link #onInit() initialized}.
     * <br />
     * A {@link Scene} will only be initialized once, when it is initially being loaded by the {@link View}.
     * After being {@link #onInit() initialized}, only {@link #onLoad()} and {@link #onUnload()} will be
     * called until the eventual {@link #onDispose() disposal} of the {@link Scene}.
     *
     * @return True if the {@link Scene} has been {@link #onInit() initialized}, false otherwise.
     * @see #onInit()
     */
    public final boolean hasInitialized() {
        return initialized;
    }

    /**
     * Checks whether the {@link Scene} has been {@link #onDispose() disposed}.
     * <br />
     * If {@code true}, the {@link Scene} cannot be {@link View#setScene(Scene, boolean) reset}.
     *
     * @return True if the {@link Scene} has been {@link #onDispose() disposed}, false otherwise.
     * @see #onDispose()
     */
    public final boolean isDisposed() {
        return disposed;
    }

    /**
     * Gets the {@link Color} for the background of the {@link Scene}.
     * <br />
     * Used by {@link View} to render the background pre-{@link #draw(Graphics2D)}.
     *
     * @return The {@link Color} for the background of the {@link Scene}.
     */
    public final Color getBackground() {
        return bg.get();
    }

    /**
     * Sets the background {@link Color} for the {@link Scene}.
     *
     * @param color The color to make the background.
     */
    public final void setBackground(@Nonnull Color color) {
        Validator.requireNotNull(color, "The given color for the background is null!");
        this.bg.set(color);
    }

    /**
     * Gets the {@link List} of {@link Renderable}s.
     * <br />
     * {@link Renderable} objects are sorted by
     * their {@link RenderPriority}.
     * <br />
     * Returns an immutable {@link List}.
     *
     * @return The {@link List} of {@link Renderable}s.
     */
    public final List<Renderable> getRenderables() {
        return Collections.unmodifiableList(renderables);
    }

    /**
     * Checks whether the {@link Window} or {@link View} is focused.
     * <br />
     * This method acts for both the {@link Window} and the {@link View}
     * as both of them are essentially the same component.
     * <br />
     * A shortcut for checking if the {@link View} is focused.
     *
     * @return True if the {@link Window} or {@link View} is focused, false otherwise.
     */
    public final boolean isFocused() {
        return Hangman.getWindow().getView().isFocused();
    }

    /**
     * Used internally to set the {@link Scene} as initialized.
     * <br />
     * Called after {@link #onInit()}.
     */
    final void markInitialized() {
        this.initialized = true;
        Hangman.debug("Scene '{}' marked as initialized.", this.getClass().getSimpleName());
    }

    /**
     * Used internally to set the {@link Scene} as disposed.
     * <br />
     * Called before {@link #onDispose()}.
     */
    final void markDisposed() {
        this.disposed = true;
        Hangman.debug("Scene '{}' marked as disposed.", this.getClass().getSimpleName());
    }

    @Override
    public final boolean dispatchKeyEvent(KeyEvent event) {
        if (getKeyListeners().isEmpty())
            return false;
        Key key = Key.of(event.getKeyCode());
        if (getKeyListeners().contains(key)) {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                onKeyPress(key);
                return true;
            }
        }
        return false;
    }
}