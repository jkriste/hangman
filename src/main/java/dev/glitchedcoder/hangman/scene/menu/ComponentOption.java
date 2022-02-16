package dev.glitchedcoder.hangman.scene.menu;

/**
 * Acts as a single {@link ScrollableMenuComponent}
 * option that can be displayed.
 * <br />
 * Should be implemented by enum types such as
 * {@link dev.glitchedcoder.hangman.window.Resolution}
 * for the best outcome.
 */
public interface ComponentOption {

    String getName();

}