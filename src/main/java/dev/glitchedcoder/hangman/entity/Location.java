package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Window;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.awt.Dimension;
import java.awt.Rectangle;

@EqualsAndHashCode(of = {"x", "y"})
public final class Location {

    private short x;
    private short y;

    private static Dimension dimension;
    private static final Config CONFIG = Config.getConfig();

    public Location() {
        this(0, 0);
    }

    public Location(int x, int y) {
        this.x = (short) x;
        this.y = (short) y;
    }

    private Location(Location l) {
        this.x = l.x;
        this.y = l.y;
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (0, 0)}.
     * <br />
     * Corresponds to the top left of the window.
     *
     * @return A new {@link Location} instance corresponding to the top left of the window.
     */
    public static Location topLeft() {
        return new Location(0, 0);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code ((WIDTH / 2) - (box.width / 2), 0)}.
     * <br />
     * Corresponds to the top center of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the top center of the window.
     */
    public static Location topCenter(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) ((dimension.width / 2) - (box.width / 2));
        return new Location(width, 0);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (WIDTH - box.width, 0)}.
     * <br />
     * Corresponds to the top right of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the top right of the window.
     */
    public static Location topRight(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) (dimension.width - box.width);
        return new Location(width, 0);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code ((WIDTH / 2) - (box.width / 2), (HEIGHT / 2) - (box.height / 2))}
     * <br />
     * Corresponds to the center of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the center of the window.
     */
    public static Location center(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) ((dimension.width / 2) - (box.width / 2));
        short height = (short) ((dimension.height / 2) - (box.height / 2));
        return new Location(width, height);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (0, (HEIGHT / 2) - (box.height / 2)}.
     * <br />
     * Corresponds to the center left of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the center left of the window.
     */
    public static Location centerLeft(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short height = (short) ((dimension.height / 2) - (box.height / 2));
        return new Location(0, height);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (WIDTH - box.width, (HEIGHT / 2) - (box.height / 2))}.
     * <br />
     * Corresponds to the center right of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the center right of the window.
     */
    public static Location centerRight(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) (dimension.width - box.width);
        short height = (short) ((dimension.height / 2) - (box.height / 2));
        return new Location(width, height);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (0, HEIGHT - box.height)}.
     * <br />
     * Corresponds to the bottom left of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the bottom left of the window.
     */
    public static Location bottomLeft(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short height = (short) (dimension.height - box.height);
        return new Location(0, height);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code ((WIDTH / 2) - (box.width / 2), HEIGHT - box.height)}.
     * <br />
     * Corresponds to the bottom center of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the bottom center of the window.
     */
    public static Location bottomCenter(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) ((dimension.width / 2) - (box.width / 2));
        short height = (short) (dimension.height - box.height);
        return new Location(width, height);
    }

    /**
     * Creates a new {@link Location} instance.
     * <br />
     * The coordinate calculations can be summed by:
     * <br />
     * {@code (WIDTH - box.width, HEIGHT - box.height)}.
     * <br />
     * Corresponds to the bottom right of the window.
     *
     * @param box The bounding box of the object.
     * @return A new {@link Location} instance corresponding to the bottom right of the window.
     */
    public static Location bottomRight(Rectangle box) {
        Validator.requireNotNull(dimension, "Location dimensions null!");
        short width = (short) (dimension.width - box.width);
        short height = (short) (dimension.height - box.height);
        return new Location(width, height);
    }

    /**
     * Called by {@link Window the window}, this method
     * helps the {@link Location} to know which dimensions
     * the {@link Window} is in order to set the bounds
     * for any of the static methods (such as {@link #center(Rectangle)}).
     * <br />
     * The given dimensions should account for insets.
     *
     * @param d The dimension of the {@link Window} minus insets.
     */
    public static void adjustDimensions(@Nonnull Dimension d) {
        Validator.requireNotNull(d, "Given dimension is null!");
        dimension = d;
        System.out.println("w: " + d.width + ", h: " + d.height);
    }

    /**
     * Gets the x coordinate.
     *
     * @return The x coordinate.
     */
    public short getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return The y coordinate.
     */
    public short getY() {
        return y;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x The x coordinate.
     */
    public void setX(int x) {
        this.x = (short) x;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y The y coordinate.
     */
    public void setY(int y) {
        this.y = (short) y;
    }

    /**
     * Copies the current {@link Location}
     * instance and returns a new one.
     *
     * @return A new but equal {@link Location} instance.
     */
    public Location copy() {
        return new Location(this);
    }

    /**
     * Adds the given x and y amounts to the {@link Location}.
     *
     * @param x The x amount to add.
     * @param y The y amount to add.
     */
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Subtracts the given x and y amounts from the {@link Location}.
     * <br />
     * Fair note that {@code x > 0 && y > 0} in order for it to subtract.
     *
     * @param x The x amount to sub.
     * @param y The y amount to sub.
     */
    public void sub(int x, int y) {
        this.x -= x;
        this.y -= y;
    }

    /**
     * Multiplies the current location by the given scalar.
     * <br />
     * Usually used to scale objects based on resolution.
     *
     * @param scalar The amount to scale the location by.
     */
    public void mul(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Gets the distance between the current {@link Location}
     * instance and the given {@link Location}.
     *
     * @param loc The location to compare.
     * @return The distance between the two locations.
     */
    public double dist(@Nonnull Location loc) {
        return Math.sqrt(distSqr(loc));
    }

    /**
     * Gets the width between the current {@link Location}
     * instance and the given {@link Location}.
     *
     * @param loc The location to compare.
     * @return The width between the two locations.
     */
    public int distX(@Nonnull Location loc) {
        return Math.abs(this.x - loc.x);
    }

    /**
     * Gets the height between the current {@link Location}
     * instance and the given {@link Location}.
     *
     * @param loc The location to compare.
     * @return The height between the two locations.
     */
    public int distY(@Nonnull Location loc) {
        return Math.abs(this.y - loc.y);
    }

    /**
     * Gets the distance squared between the current
     * {@link Location} instance and the given {@link Location}.
     *
     * @param loc The location to compare.
     * @return The distance squared between the two locations.
     */
    public double distSqr(@Nonnull Location loc) {
        int x = this.x - loc.x;
        int y = this.y - loc.y;
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    /**
     * Gets the midpoint between the current instance
     * and the given {@link Location} instance.
     *
     * @param loc The location to compare.
     * @return The midpoint between the two {@link Location}s.
     */
    public Location getMidpoint(@Nonnull Location loc) {
        Validator.requireNotNull(loc, "Given loc is null!");
        int x = Math.round((this.x + loc.x) / 2F);
        int y = Math.round((this.y + loc.y) / 2F);
        return new Location(x, y);
    }

    @Override
    public String toString() {
        return "x: " + this.x + ", y: " + this.y;
    }
}