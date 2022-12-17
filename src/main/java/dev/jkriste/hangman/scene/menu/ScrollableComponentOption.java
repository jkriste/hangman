package dev.jkriste.hangman.scene.menu;

import javax.annotation.Nonnull;

/**
 * Represents a singular option for a {@link ScrollableMenuComponent}.
 */
public interface ScrollableComponentOption {

    /**
     * Gets the name of the {@link ScrollableMenuComponent}.
     * <br />
     * Used to identify the option, will be displayed.
     *
     * @return The name of the {@link ScrollableMenuComponent}.
     */
    @Nonnull
    String getName();

}
