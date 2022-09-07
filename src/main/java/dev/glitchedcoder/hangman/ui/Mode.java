package dev.glitchedcoder.hangman.ui;

public enum Mode {

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

    @Override
    public String toString() {
        return name;
    }
}