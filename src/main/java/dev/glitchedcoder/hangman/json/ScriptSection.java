package dev.glitchedcoder.hangman.json;

import javax.annotation.Nullable;

public enum ScriptSection {

    INTRODUCTION("introduction_sfl", "introduction_nsfl"),
    INTRODUCTION_TUTORIAL("introduction_tutorial"),
    INTRODUCTION_NO_TUTORIAL("introduction_no_tutorial"),
    PHASE_ONE_TUTORIAL("phase_one_tutorial"),
    PHASE_ONE_NO_TUTORIAL("phase_one_no_tutorial"),
    PHASE_TWO("phase_two"),
    PHASE_THREE("phase_three"),
    GAME_LOST("game_lost_sfl", "game_lost_nsfl"),
    GAME_WON("game_won_sfl", "game_won_nsfl");

    private final String sflId;
    private final String nsflId;

    ScriptSection(String sflId) {
        this(sflId, null);
    }

    ScriptSection(String sflId, String nsflId) {
        this.sflId = sflId;
        this.nsflId = nsflId;
    }

    public boolean hasNsflVersion() {
        return nsflId != null;
    }

    public String getSflId() {
        return sflId;
    }

    @Nullable
    public String getNsflId() {
        return nsflId;
    }
}