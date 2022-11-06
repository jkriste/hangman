package dev.jkriste.hangman.ui;

import java.awt.image.BufferedImage;

public enum AnimatedTexture {

    HANGING_MAN(new Texture[] {
            Texture.HANGING_MAN_1, Texture.HANGING_MAN_2, Texture.HANGING_MAN_3, Texture.HANGING_MAN_4,
            Texture.HANGING_MAN_5, Texture.HANGING_MAN_6, Texture.HANGING_MAN_7, Texture.HANGING_MAN_8
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