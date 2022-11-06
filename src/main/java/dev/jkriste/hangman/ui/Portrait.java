package dev.jkriste.hangman.ui;

import javax.annotation.Nonnull;

public enum Portrait {

    UNKNOWN(Texture.QUESTIONMARK_PORTRAIT),
    EXECUTIONER(Texture.EXECUTIONER);

    private final Texture texture;

    Portrait(@Nonnull Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}