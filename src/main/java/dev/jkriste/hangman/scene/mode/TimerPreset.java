package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.scene.menu.ScrollableComponentOption;

import javax.annotation.Nonnull;

public enum TimerPreset implements ScrollableComponentOption {

    OFF("Off", 0),
    SEC30("30s", 30),
    SEC60("1m", 60),
    SEC90("1m30s", 90),
    SEC120("2m", 120),
    SEC150("2m30s", 150),
    SEC180("3m", 180),
    SEC210("3m30s", 210),
    SEC240("4m", 240),
    SEC270("4m30s", 270),
    SEC300("5m", 300);

    private final String name;
    private final int timeInSec;

    public static final TimerPreset[] values = values();

    TimerPreset(String name, int timeInSec) {
        this.name = name;
        this.timeInSec = timeInSec;
    }

    public int getTimeInSec() {
        return this.timeInSec;
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }
}
