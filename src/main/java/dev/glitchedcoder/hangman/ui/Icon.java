package dev.glitchedcoder.hangman.ui;

import javax.annotation.Nonnull;

public enum Icon {

    ENTER(Texture.ENTER_ICON),
    ESCAPE(Texture.ESCAPE_ICON),
    BACKSPACE(Texture.BACKSPACE_ICON),
    A_TO_Z(Texture.A_TO_Z_ICON),
    ZERO_TO_NINE(Texture.ZERO_TO_NINE_ICON),
    LEFT_ARROW(Texture.LEFT_ARROW_ICON),
    DOWN_ARROW(Texture.DOWN_ARROW_ICON),
    RIGHT_ARROW(Texture.RIGHT_ARROW_ICON),
    UP_ARROW(Texture.UP_ARROW_ICON);

    private final Texture texture;

    Icon(@Nonnull Texture texture) {
        this.texture = texture;
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }
}