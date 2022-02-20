package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.json.ScriptSection;

import javax.annotation.Nullable;
import java.util.List;

public enum Phase {

    PHASE_ONE(ScriptSection.PHASE_ONE, true, 14),
    PHASE_TWO(ScriptSection.PHASE_TWO, true, 10),
    PHASE_THREE(ScriptSection.PHASE_THREE, false, 4);

    private final int wordLength;
    private final boolean hasNext;
    private final ScriptSection section;

    Phase(ScriptSection section, boolean hasNext, int wordLength) {
        this.section = section;
        this.hasNext = hasNext;
        this.wordLength = wordLength;
    }

    public List<String> getScript() {
        return Script.getScript().getSection(section);
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