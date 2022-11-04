package dev.glitchedcoder.hangman.util;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A handy class that tests your code during runtime!
 * <br />
 *
 */
public final class Validator {

    private static volatile boolean enabled = false;

    private Validator() {
    }

    /**
     *
     *
     */
    public static void enable() {
        enabled = true;
    }

    /**
     * A useful method that replaces any `{}` with the given params.
     * e.g.
     * <br />
     * {@code Validator.format("Hello, {}! My name is {}.", "world", name);}
     *
     * @param str    The string to be formatted.
     * @param params The objects to insert into the string.
     * @return The formatted string.
     * @apiNote Credit to Walshy for writing this one lmao.
     */
    @ParametersAreNonnullByDefault
    public static String format(String str, Object... params) {
        int index = 0;
        int next;
        int off = 0;
        while ((next = str.indexOf("{}", off)) != -1) {
            if (index == params.length)
                break;
            str = str.substring(0, next) + params[index++].toString() + str.substring(next + 2);
            off = next + 1;
        }
        return str;
    }

    /**
     * Requires the two given objects are {@link Object#equals(Object) equal}.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @throws IllegalArgumentException Thrown if the given objects are not equal.
     */
    @ParametersAreNonnullByDefault
    public static void requireEqual(Object obj1, Object obj2) {
        if (!enabled)
            return;
        checkArgument(obj1.equals(obj2), "Given object '{}' is not equal to other object '{}'.",
                obj1.toString(), obj2.toString());
    }

    /**
     * Requires the two given objects are <b>NOT</b> {@link Object#equals(Object) equal}.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @throws IllegalArgumentException Thrown if the given objects are equal.
     */
    @ParametersAreNonnullByDefault
    public static void requireNotEqual(Object obj1, Object obj2) {
        if (!enabled)
            return;
        checkArgument(!obj1.equals(obj2), "Given objects '{}' are equal.", obj1.toString());
    }

    /**
     * Constrains the given {@code value}
     * between the given {@code min} and {@code max}.
     * <br />
     * {@code min <= value <= max}
     *
     * @param value The value to constrain.
     * @param min   The minimum the given value should be.
     * @param max   The maximum the given value should be.
     * @return The given {@code value} constrained bewteen
     * the given {@code min} and {@code max} values.
     * @throws IllegalArgumentException Thrown if the minimum is larger than the maximum.
     */
    public static int constrain(int value, int min, int max) {
        if (min > max)
            throw new IllegalArgumentException(format("Given min '{}' is less than given max '{}'", min, max));
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    /**
     * Requires the given object to not be null.
     *
     * @param object The object to check.
     * @param s      The message to pass onto the exception if the object is null.
     * @throws IllegalArgumentException Thrown if the given object is null.
     */
    public static void requireNotNull(Object object, String s) {
        if (!enabled)
            return;
        if (object == null)
            throw new IllegalArgumentException(s);
    }

    /**
     * Requires the given object to not be null.
     *
     * @param object The object to check.
     * @param s      The message to pass onto the exception if the object is null.
     * @param params The parameters to format.
     * @throws IllegalArgumentException Thrown if the given object is null.
     * @see #format(String, Object...)
     */
    public static void requireNotNull(Object object, String s, Object... params) {
        if (!enabled)
            return;
        if (object == null)
            throw new IllegalArgumentException(format(s, params));
    }

    /**
     * Requires all objects in the given array to be not null.
     *
     * @param objects
     */
    public static void requireNotNull(Object[] objects) {
        if (!enabled)
            return;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null)
                throw new IllegalArgumentException(format("Object at index {} is null!", i));
        }
    }

    /**
     * Requires the given object to be null.
     *
     * @param object The object to check.
     * @param s      The message to pass onto the exception if the object is not null.
     */
    public static void requireNull(Object object, String s) {
        if (!enabled)
            return;
        if (object != null)
            throw new IllegalArgumentException(s);
    }

    /**
     * Requires the given object to be null.
     *
     * @param object The object to check.
     * @param s      The message to pass onto the exception if the object is not null.
     * @param params The parameters to format.
     * @see #format(String, Object...)
     */
    public static void requireNull(Object object, String s, Object... params) {
        if (!enabled)
            return;
        if (object != null)
            throw new IllegalArgumentException(format(s, params));
    }

    /**
     * Requires the given object to not be null.
     *
     * @param object The object to check.
     * @param <T>    The type to return.
     * @return Returns the given object if it's not null.
     * @throws IllegalArgumentException Thrown if the given object is null.
     */
    @Nonnull
    public static <T> T requireNotNull(T object) {
        if (object == null)
            throw new IllegalArgumentException("Given object is null!");
        return object;
    }

    /**
     * Checks the given argument. If the argument is false,
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param arg The argument to check.
     * @param s   The message to pass onto the exception.
     * @throws IllegalArgumentException Thrown if the given arg is false.
     */
    public static void checkArgument(boolean arg, String s) {
        if (!enabled)
            return;
        if (!arg)
            throw new IllegalArgumentException(s);
    }

    /**
     * Checks the given argument. If the argument is false,
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param arg    The argument to check.
     * @param s      The message to pass onto the exception.
     * @param params The parameters to format.
     * @throws IllegalArgumentException Thrown if the given arg is false.
     * @see #format(String, Object...)
     */
    public static void checkArgument(boolean arg, String s, Object... params) {
        if (!enabled)
            return;
        if (!arg)
            throw new IllegalArgumentException(format(s, params));
    }
}