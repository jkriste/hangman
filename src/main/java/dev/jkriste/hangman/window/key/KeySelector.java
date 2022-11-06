package dev.jkriste.hangman.window.key;

import dev.jkriste.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An easier way to create a {@link Set<Key>}, basically a {@link Set<Key>} factory.
 * <br />
 * There is no rhyme nor reason why it is constructed the way it is, I just like it this way.
 */
public final class KeySelector {

    private final Set<Key> selectedKeys;

    private KeySelector() {
        this.selectedKeys = new HashSet<>();
    }

    private KeySelector(Key key) {
        this.selectedKeys = new HashSet<>();
        this.selectedKeys.add(key);
    }

    private KeySelector(Key[] keys) {
        this.selectedKeys = new HashSet<>();
        Collections.addAll(this.selectedKeys, keys);
    }

    private KeySelector(Collection<Key> keys) {
        this.selectedKeys = new HashSet<>(keys);
    }

    /**
     * Creates a new {@link KeySelector} instance.
     *
     * @return A new {@link KeySelector} instance.
     */
    public static KeySelector create() {
        return new KeySelector();
    }

    /**
     * Returns an empty {@link Set<Key>}.
     * <br />
     * Better than typing out {@code Collections.emptySet();}
     * each time, in my opinion...
     *
     * @return An empty {@link Set<Key>}.
     */
    public static Set<Key> none() {
        return Collections.emptySet();
    }

    /**
     * Returns a singleton {@link Set<Key>} with the given key.
     *
     * @param key The key to put in the set.
     * @return A singleton {@link Set<Key>} with the given key.
     * @throws IllegalArgumentException Thrown if key is null.
     */
    public static Set<Key> singleton(@Nonnull Key key) {
        Validator.requireNotNull(key, "Given key is null!");
        return Collections.singleton(key);
    }

    /**
     * Adds the given array of {@link Key}s to the builder.
     *
     * @param keys The keys to add to the set.
     * @return The current {@link KeySelector} instance.
     */
    public KeySelector group(@Nonnull Key[] keys) {
        Collections.addAll(this.selectedKeys, keys);
        return this;
    }

    /**
     * Adds the given {@link Collection} of {@link Key}s to the builder.
     *
     * @param keys The keys to add to the set.
     * @return The current {@link KeySelector} instance.
     */
    public KeySelector group(@Nonnull Collection<Key> keys) {
        this.selectedKeys.addAll(keys);
        return this;
    }

    /**
     * Adds the given {@link Key} to the {@link Set<Key>}.
     *
     * @param key The key to add.
     * @return The current {@link KeySelector} instance.
     */
    public KeySelector with(@Nonnull Key key) {
        this.selectedKeys.add(key);
        return this;
    }

    /**
     * Returns the built {@link Set<Key>}.
     *
     * @return The built {@link Set<Key>}.
     */
    public Set<Key> build() {
        return selectedKeys;
    }
}