package dev.glitchedcoder.hangman.window.key;

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
     * Groups an array of {@link Key}s together.
     *
     * @param keys The keys to add to the set.
     * @return The current {@link KeySelector} instance.
     */
    public KeySelector group(Key[] keys) {
        Collections.addAll(this.selectedKeys, keys);
        return this;
    }

    /**
     * Adds the given {@link Key} to the {@link Set<Key>}.
     *
     * @param key The key to add.
     * @return The current {@link KeySelector} instance.
     */
    public KeySelector with(Key key) {
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