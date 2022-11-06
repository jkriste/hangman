package dev.jkriste.hangman.ui;

public enum NSFL {

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

    @Override
    public String toString() {
        return name;
    }
}