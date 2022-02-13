package dev.glitchedcoder.hangman.json;

/**
 * Represents a class instance of the
 * JSON object returned from the API.
 * Check out {@link dev.glitchedcoder.hangman.util.ApiRequest}
 * to see how this is done.
 */
public final class Word {

    private String word;

    /**
     * Gets the word given by the API.
     *
     * @return The word given by the API.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the length of the word.
     * <br />
     * This length is constrained between
     * {@link dev.glitchedcoder.hangman.util.Constants#MIN_WORD_LENGTH}
     * and {@link dev.glitchedcoder.hangman.util.Constants#MAX_WORD_LENGTH}.
     *
     * @return The length of the word.
     */
    public int getLength() {
        return word.length();
    }
}