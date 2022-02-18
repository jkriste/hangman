package dev.glitchedcoder.hangman.json;

public enum ScriptSection {

    INTRODUCTION_BEGIN("introduction_begin"),
    INTRODUCTION_END("introduction_end"),
    PHASE_ONE("phase_one"),
    PHASE_TWO("phase_two"),
    PHASE_THREE("phase_three"),
    GAME_LOST("game_lost"),
    GAME_WON("game_won");

    private final String id;

    ScriptSection(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}