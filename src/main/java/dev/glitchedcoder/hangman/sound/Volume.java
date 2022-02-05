package dev.glitchedcoder.hangman.sound;

import java.io.Serializable;

public enum Volume implements Serializable {

    MUTE("OFF", -1000F),
    MIN("MIN", -6.0206F),
    LOW("LOW", -3.0103F),
    NORMAL("NORMAL", 0F),
    HIGH("HIGH", 3.0103F),
    MAX("MAX", 6.0206F);

    private final float gain;
    private final String name;

    Volume(String name, float gain) {
        this.name = name;
        this.gain = gain;
    }

    public String getName() {
        return name;
    }

    public float getGain() {
        return gain;
    }
}