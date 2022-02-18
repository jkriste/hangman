package dev.glitchedcoder.hangman.ui;

import java.awt.image.BufferedImage;

import static dev.glitchedcoder.hangman.ui.Texture.*;

public enum AnimatedTexture {

    HANGING_MAN(new Texture[] {
            HANGING_MAN_1, HANGING_MAN_2, HANGING_MAN_3, HANGING_MAN_4,
            HANGING_MAN_5, HANGING_MAN_6, HANGING_MAN_7, HANGING_MAN_8
    });

    private final Texture[] textures;

    AnimatedTexture(Texture[] textures) {
        this.textures = textures;
    }

    public BufferedImage[] asImageArray() {
        BufferedImage[] array = new BufferedImage[textures.length];
        for (int i = 0; i < textures.length; i++)
            array[i] = textures[i].asImage();
        return array;
    }
}