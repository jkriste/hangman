package dev.jkriste.hangman.ui;

import dev.jkriste.hangman.scene.menu.ScrollableComponentOption;

import javax.annotation.Nonnull;

public enum Debug implements ScrollableComponentOption {

    DEBUG_ON("Debug On"),
    DEBUG_OFF("Debug Off");

    public static final Debug[] values = values();

    private final String name;

    Debug(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return this == DEBUG_ON;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }
}