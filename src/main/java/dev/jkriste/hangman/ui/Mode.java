package dev.jkriste.hangman.ui;

import dev.jkriste.hangman.scene.menu.ScrollableComponentOption;

import javax.annotation.Nonnull;

public enum Mode implements ScrollableComponentOption {

    ONLINE("Online Mode"),
    OFFLINE("Offline Mode");

    public static final Mode[] values = values();

    private final String name;

    Mode(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return this == ONLINE;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }
}