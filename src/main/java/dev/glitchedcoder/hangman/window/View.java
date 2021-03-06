package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.entity.Renderable;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.util.concurrent.atomic.AtomicReference;

public final class View extends Canvas implements FocusListener {

    private final AtomicReference<Scene> scene;

    private static final RenderingHints RENDERING_HINTS;
    private static final KeyboardFocusManager KEYBOARD_MANAGER;

    static {
        KEYBOARD_MANAGER = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        RENDERING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RENDERING_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    public View() {
        this.scene = new AtomicReference<>(null);
        setFocusable(true);
    }

    /**
     * Ticks the {@link View}.
     * <br />
     * Tells the {@link Scene} to update and
     * subsequently calls {@link Scene#tick(byte)}.
     *
     * @param count The current tick/frame count.
     * @throws IllegalArgumentException Thrown if the current scene is null.
     */
    public void tick(byte count) {
        Scene scene = getScene();
        Validator.requireNotNull(scene, "Current scene is null!");
        scene.tick(count);
    }

    /**
     * Draws the {@link View}.
     * <br />
     * If no {@link BufferStrategy} exists, this method
     * creates a double-{@link BufferStrategy}.
     * Gets the {@link Graphics2D} object and applies
     * {@link RenderingHints}. Draws the background of the
     * {@link Scene} and then passes it on to {@link Scene}.
     */
    public void draw() {
        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategy(2);
            return;
        }
        Scene scene = getScene();
        Validator.requireNotNull(scene, "Current scene is null!");
        Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
        graphics.setRenderingHints(RENDERING_HINTS);
        graphics.setColor(scene.getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        scene.draw(graphics);
        graphics.dispose();
        strategy.show();
    }

    /**
     * Gets the current {@link Scene} of the {@link View}.
     *
     * @return The current {@link Scene} of the {@link View}.
     */
    public synchronized Scene getScene() {
        return scene.get();
    }

    /**
     * Sets the {@link Scene} of the {@link View}.
     * <br />
     * If the {@link #getScene() current scene} is not {@code null},
     * the current scene will be {@link Scene#onUnload() unloaded}
     * and removed as a {@link java.awt.KeyEventDispatcher}.
     * <br />
     * If the given {@link Scene} is not {@code null} and isn't
     * the same as the {@link #getScene() current scene}, the given
     * {@link Scene} will be added as a {@link java.awt.KeyEventDispatcher}
     * and will be {@link Scene#onLoad() loaded}.
     *
     * @param scene The new scene to set for the view.
     */
    public synchronized void setScene(@Nonnull Scene scene) {
        Validator.requireNotNull(scene, "Given scene is null!");
        Scene current = getScene();
        if (current != null) {
            Validator.checkArgument(!current.equals(scene), "Given scene is same as current scene.");
            KEYBOARD_MANAGER.removeKeyEventDispatcher(current);
            current.onUnload();
        }
        this.scene.set(scene);
        KEYBOARD_MANAGER.addKeyEventDispatcher(scene);
        scene.onLoad();
    }

    /**
     * Called by the {@link Window} when
     * the {@link Window} has lost focus.
     * <br />
     * Passed on to the {@link Scene}.
     *
     * @param event The focus event.
     */
    @Override
    public void focusLost(FocusEvent event) {
        requestFocus();
        requestFocusInWindow();
        Scene scene = getScene();
        if (scene == null)
            return;
        scene.focusLost(event);
    }

    /**
     * Called by the {@link Window} when
     * the {@link Window} has gained focus.
     * <br />
     * Passed on to the {@link Scene}.
     *
     * @param event The focus event.
     */
    @Override
    public void focusGained(FocusEvent event) {
        requestFocus();
        requestFocusInWindow();
        Scene scene = getScene();
        if (scene == null)
            return;
        scene.focusGained(event);
    }

    /**
     * Called by the {@link Window} when the game thread
     * has either stopped or been interrupted.
     * <br />
     * Unloads the {@link #getScene() current scene}
     * if the current scene is not {@code null}.
     * <br />
     * Will set the {@link #getScene() current scene}
     * as {@code null} as a means of disposing it.
     */
    void close() {
        Scene scene = getScene();
        if (scene != null)
            scene.onUnload();
        this.scene.set(null);
    }

    /**
     * Called by {@link Window the window}, this method
     * helps the {@link Scene} to know which dimensions
     * to render the background and any other size-dependent
     * {@link Renderable} objects.
     * <br />
     * The given dimensions should account for insets.
     *
     * @param dimension The dimension of the {@link Window} minus insets.
     */
    void adjustDimensions(Dimension dimension) {
        setSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
    }
}