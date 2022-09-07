package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.window.Window;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.util.UUID;

public interface Renderable extends Comparable<Renderable> {

    /**
     * Gets the {@link UUID} of the {@link Renderable} object.
     *
     * @return The {@link UUID} of the {@link Renderable} object.
     */
    @Nonnull
    UUID getId();

    /**
     * Used to tell the {@link Renderable} object to update.
     * <br />
     * Called by the {@link dev.glitchedcoder.hangman.window.Scene}
     * before subsequently calling {@link #draw(Graphics2D)}.
     *
     * @param count The current frame being rendered, usually
     *              {@code 0 <= count <} {@link Window#FRAMES_PER_SECOND}.
     */
    void tick(byte count);

    /**
     * Used to draw the {@link Renderable} object.
     * <br />
     * Called by the {@link dev.glitchedcoder.hangman.window.Scene}
     * after calling {@link #tick(byte)}.
     *
     * @param graphics The graphics object to draw with.
     */
    void draw(@Nonnull Graphics2D graphics);

    /**
     * Gets whether the {@link Renderable} object should be
     * drawn to the {@link dev.glitchedcoder.hangman.window.Scene}.
     *
     * @return True if the {@link Renderable} should be drawn, false otherwise.
     */
    boolean shouldDraw();

    /**
     * Gets whether the {@link Renderable} object should be
     * removed from the {@link dev.glitchedcoder.hangman.window.Scene}.
     * <br />
     * If {@code true}, the {@link dev.glitchedcoder.hangman.window.Scene}
     * will then remove the {@link Renderable} object from the scene.
     *
     * @return True if the {@link Renderable} should be removed, false otherwise.
     */
    boolean shouldRemove();

    /**
     * Passively marks the {@link Renderable} object as disposable.
     * <br />
     * Calling this method should mark {@link #shouldDraw()}
     * as {@code false} and {@link #shouldRemove()} as {@code true}.
     * <br />
     * This method should be used over {@link #remove()} by any
     * implementation of {@link dev.glitchedcoder.hangman.window.Scene}
     * due to {@link dev.glitchedcoder.hangman.window.Scene} removing
     * the {@link Renderable} object on tick.
     */
    void dispose();

    /**
     * Proactively removes the {@link Renderable} object.
     * <br />
     * Calling this method should mark {@link #shouldDraw()} as {@code false}.
     * <br />
     * This method should not be used by any implementation of
     * {@link dev.glitchedcoder.hangman.window.Scene} and should
     * instead use {@link #dispose()} to passively remove the {@link Renderable} object.
     */
    void remove();

    /**
     * Called by a {@link dev.glitchedcoder.hangman.window.Scene}
     * implementation when the {@link Renderable} object should be loaded.
     * <br />
     * Calling this method should mark {@link #shouldDraw()} as {@code true}.
     */
    void spawn();

    /**
     * Gets the {@link RenderPriority} of the {@link Renderable} object.
     * <br />
     * By default, this method will return {@link RenderPriority#NORMAL}.
     * <br />
     * This method is used by the {@link dev.glitchedcoder.hangman.window.Scene}
     * to sort all {@link Renderable} objects based on {@link RenderPriority}.
     *
     * @return The {@link RenderPriority} of the {@link Renderable} object.
     */
    @Nonnull
    default RenderPriority getRenderPriority() {
        return RenderPriority.NORMAL;
    }

    default int compareTo(Renderable o) {
        return getRenderPriority().compareTo(o.getRenderPriority());
    }
}