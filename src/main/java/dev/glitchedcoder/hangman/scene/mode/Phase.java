package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.json.ScriptSection;
import java.util.Collections;

import javax.annotation.Nullable;
import java.util.List;

public enum Phase {

    PHASE_ONE(true, 4),
    PHASE_TWO(true, 9),
    PHASE_THREE(false, 14);

    private final int wordLength;
    private final boolean hasNext;

    Phase(boolean hasNext, int wordLength) {
        this.hasNext = hasNext;
        this.wordLength = wordLength;
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

    public int getWordLength() {
        return wordLength;
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