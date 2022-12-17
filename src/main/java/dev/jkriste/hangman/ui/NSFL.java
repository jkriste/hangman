package dev.jkriste.hangman.ui;

import dev.jkriste.hangman.scene.menu.ScrollableComponentOption;

import javax.annotation.Nonnull;

public enum NSFL implements ScrollableComponentOption {

    ON("NSFL On"),
    OFF("NSFL Off");

    public static final NSFL[] values = values();

    private final String name;

    NSFL(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return this == ON;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }
}