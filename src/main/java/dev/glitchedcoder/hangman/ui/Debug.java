package dev.glitchedcoder.hangman.ui;

public enum Debug {

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

    @Override
    public String toString() {
        return name;
    }
}