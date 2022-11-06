package dev.jkriste.hangman.json;

import javax.annotation.Nullable;

public enum ScriptSection {

    INTRODUCTION("story.intro.sfl", "story.intro.nsfl"),
    INTRODUCTION_TUTORIAL("story.intro.tutorial"),
    INTRODUCTION_NO_TUTORIAL("story.intro.skiptutorial"),
    PHASE_ONE_TUTORIAL("story.p1.tutorial"),
    PHASE_ONE_NO_TUTORIAL("story.p1.skiptutorial"),
    PHASE_TWO("story.p2"),
    PHASE_THREE("story.p3"),
    GAME_LOST("story.lose.sfl", "story.lose.nsfl"),
    GAME_WON("story.win.sfl", "story.win.nsfl"),
    ALREADY_GUESSED("story.generic.alreadyGuessed");

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