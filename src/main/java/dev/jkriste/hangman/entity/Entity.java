package dev.jkriste.hangman.entity;

import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;
import dev.jkriste.hangman.window.Window;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class Entity implements Renderable {

    private final UUID id;
    private final Scene scene;
    private final EntityType type;
    private final AtomicReference<Location> loc;
    private final AtomicReference<RenderPriority> priority;

    private volatile boolean loaded;
    private volatile boolean visible;
    private volatile boolean removed;
    private volatile boolean shouldRemove;

    @ParametersAreNonnullByDefault
    protected Entity(Scene scene, EntityType type) {
        this(scene, type, new Location());
    }

    @ParametersAreNonnullByDefault
    protected Entity(Scene scene, EntityType type, Location loc) {
        this.scene = scene;
        this.id = UUID.randomUUID();
        this.type = type;
        this.loc = new AtomicReference<>(loc);
        this.priority = new AtomicReference<>(RenderPriority.NORMAL);
    }

    /**
     * Called when the {@link Entity} is to be loaded.
     * <br />
     * Called before {@link #spawn()} has returned.
     */
    protected abstract void onLoad();

    /**
     * Called when the {@link Entity} is to be unloaded.
     * <br />
     * Called before {@link #remove()} has returned.
     */
    protected abstract void onUnload();

    /**
     * Gets the bounding box of the {@link Entity}.
     *
     * @return The bounding box of the {@link Entity}.
     */
    public abstract Rectangle getBounds();

    @Override
    @Nonnull
    public final UUID getId() {
        return id;
    }

    @Override
    public boolean shouldDraw() {
        return this.visible && !this.removed && this.loaded;
    }

    @Override
    public boolean shouldRemove() {
        if (!this.loaded)
            return false;
        return this.shouldRemove && !this.removed;
    }

    @Override
    public void dispose() {
        this.shouldRemove = true;
        Hangman.debug("Entity '{}' marked for disposal.", this.type.name());
    }

    @Override
    public final void remove() {
        Validator.checkArgument(!removed && loaded, "Tried to remove already-removed & unloaded entity.");
        this.removed = true;
        this.loaded = false;
        onUnload();
    }

    @Override
    public final void spawn() {
        Validator.checkArgument(!loaded && !removed, "Tried to spawn already-loaded or removed entity {}: {}", type, id);
        onLoad();
        this.loaded = true;
        this.visible = true;
    }

    /**
     * Gets the {@link RenderPriority} of the {@link Entity}.
     *
     * @return The {@link RenderPriority} of the {@link Entity}.
     */
    @Nonnull
    @Override
    public final RenderPriority getRenderPriority() {
        return priority.get();
    }

    /**
     * Gets the type of the {@link Entity}.
     * <br />
     * This is merely used to differentiate
     * different types of {@link Entity}s and
     * also allows better {@link Entity} interaction.
     *
     * @return The type of the {@link Entity}.
     */
    @Nonnull
    public final EntityType getType() {
        return type;
    }

    /**
     * Gets whether the {@link Entity} is removed.
     * <br />
     * If {@code true}, the {@link Entity} cannot be spawned or removed again.
     * <br />
     * Once {@link #remove() removed}, the {@link Scene} should
     * also remove any instances it may still have of the {@link Entity}.
     *
     * @return True if the {@link Entity} is removed, false otherwise.
     */
    public final boolean isRemoved() {
        return removed;
    }

    /**
     * Gets whether the {@link Entity} has been {@link #onLoad() loaded}.
     *
     * @return True if the {@link Entity} has been loaded, false otherwise.
     */
    public final boolean isLoaded() {
        return loaded;
    }

    /**
     * Gets whether the {@link Entity} is visible.
     * <br />
     * If {@code false}, the {@link Entity} will not be
     * rendered. {@link #draw(Graphics2D)} will not be
     * called by the {@link Scene}.
     *
     * @return True if the {@link Entity} is visible, false otherwise.
     */
    public final boolean isVisible() {
        return visible;
    }

    /**
     * Gets the {@link Scene} that the {@link Entity} belongs to.
     *
     * @return The {@link Scene} that the {@link Entity} belongs to.
     */
    @Nonnull
    public final Scene getScene() {
        return scene;
    }

    /**
     * Gets the current {@link Location} of the {@link Entity}.
     * <br />
     * This {@link Location} starts at {@code (0, 0)} which is
     * the top left of the {@link Window}
     * and goes all the way down to the bottom right, represented
     * as {@code (WIDTH, HEIGHT)} of the window (minus insets).
     *
     * @return The current {@link Location} of the {@link Entity}.
     */
    @Nonnull
    public final Location getLocation() {
        return this.loc.get();
    }

    /**
     * Sets the visibility of the {@link Entity}.
     * <br />
     * If {@code false}, the {@link Entity} will not be
     * rendered. {@link #draw(Graphics2D)} will not be
     * called by the {@link Scene}.
     *
     * @param visible The visibility of the entity.
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the {@link Location} of the {@link Entity}.
     * <br />
     * This {@link Location} starts at {@code (0, 0)} which is
     * the top left of the {@link Window}
     * and goes all the way down to the bottom right, represented
     * as {@code (WIDTH, HEIGHT)} of the window (minus insets).
     *
     * @param location The new location.
     */
    public final void setLocation(@Nonnull Location location) {
        this.loc.set(location);
    }

    /**
     * Sets the {@link Location} of the {@link Entity}.
     * <br />
     * Uses a {@link Function} that consumes a {@link Rectangle} and outputs
     * a {@link Location} as a shortcut for static {@link Location} methods.
     *
     * @param function The function.
     */
    public final void setLocation(Function<Rectangle, Location> function) {
        this.loc.set(function.apply(getBounds()));
    }

    /**
     * Sets the {@link RenderPriority} of the {@link Entity}.
     *
     * @param priority The render priority to set.
     */
    public void setRenderPriority(@Nonnull RenderPriority priority) {
        Validator.requireNotNull(priority, "Given priority is null!");
        this.priority.set(priority);
    }
}