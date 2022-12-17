package dev.jkriste.hangman.scene.mode;

import dev.jkriste.hangman.json.Config;
import dev.jkriste.hangman.json.Script;
import dev.jkriste.hangman.json.ScriptSection;

import java.util.Collections;

import javax.annotation.Nullable;
import java.util.List;

public enum Phase {

    PHASE_ONE(true, 4, 14),
    PHASE_TWO(true, 9, 11),
    PHASE_THREE(false, 14, 8);

    private final byte maxGuesses;
    private final byte wordLength;
    private final boolean hasNext;

    Phase(boolean hasNext, int wordLength, int maxGuesses) {
        this.hasNext = hasNext;
        this.wordLength = (byte) wordLength;
        this.maxGuesses = (byte) maxGuesses;
    }

    public List<String> getScript() {
        switch (this) {
            case PHASE_ONE:
                boolean playedBefore = Config.getConfig().hasPlayedBefore();
                return Script.getScript().getSection(
                        playedBefore ? ScriptSection.PHASE_ONE_NO_TUTORIAL : ScriptSection.PHASE_ONE_TUTORIAL
                );
            case PHASE_TWO:
                return Script.getScript().getSection(ScriptSection.PHASE_TWO);
            case PHASE_THREE:
                return Script.getScript().getSection(ScriptSection.PHASE_THREE);
            default:
                return Collections.emptyList();
        }
    }

    public boolean hasNext() {
        return hasNext;
    }

    public byte getWordLength() {
        return wordLength;
    }

    public byte getMaxGuesses() {
        return maxGuesses;
    }

    @Nullable
    public Phase next() {
        switch (this) {
            case PHASE_ONE:
                return PHASE_TWO;
            case PHASE_TWO:
                return PHASE_THREE;
            default:
                return null;
        }
    }
}