package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Entity implements Renderable {

    private final UUID id;
    private final Scene scene;
    private final EntityType type;
    private final AtomicReference<Location> loc;

    private volatile boolean dead;
    private volatile boolean loaded;
    private volatile boolean visible;
    private volatile boolean shouldRemove;

    @ParametersAreNonnullByDefault
    protected Entity(Scene view, EntityType type) {
        this(view, type, new Location());
    }

    @ParametersAreNonnullByDefault
    protected Entity(Scene scene, EntityType type, Location loc) {
        this.scene = scene;
        this.id = UUID.randomUUID();
        this.type = type;
        this.loc = new AtomicReference<>(loc);
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
        return this.visible && !this.dead;
    }

    @Override
    public boolean shouldRemove() {
        return this.shouldRemove || this.dead;
    }

    @Override
    public void dispose() {
        this.shouldRemove = true;
    }

    @Override
    public final void remove() {
        Validator.checkArgument(!dead && loaded, "Tried to remove dead & unloaded entity.");
        this.dead = true;
        this.loaded = false;
        onUnload();
    }

    @Override
    public final void spawn() {
        Validator.checkArgument(!dead, "Tried to spawn dead entity {}: {}", type, id);
        onLoad();
        this.loaded = true;
        this.visible = true;
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
     * Gets whether the {@link Entity} is dead.
     * <br />
     * Although this doesn't refer to an "alive"
     * {@link Entity}, it does account for whether
     * the {@link Entity} has been {@link #remove() removed}.
     *
     * @return True if the {@link Entity} is dead, false otherwise.
     */
    public final boolean isDead() {
        return dead;
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
     * the top left of the {@link dev.glitchedcoder.hangman.window.Window}
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
     * the top left of the {@link dev.glitchedcoder.hangman.window.Window}
     * and goes all the way down to the bottom right, represented
     * as {@code (WIDTH, HEIGHT)} of the window (minus insets).
     *
     * @param location The new location.
     */
    public final void setLocation(@Nonnull Location location) {
        this.loc.set(location);
    }
}