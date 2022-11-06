package dev.jkriste.hangman.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.util.ApiRequest;
import dev.jkriste.hangman.util.Constants;
import dev.jkriste.hangman.util.Validator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a class instance of the
 * JSON object returned from the API.
 * Check out {@link ApiRequest}
 * to see how this is done.
 */
public final class Words {

    @SerializedName("words")
    private List<String> words;
    private transient Map<Integer, List<String>> mappedWords;

    private static Words instance;
    private static final Random RANDOM = Hangman.getRandom();

    public static void loadWords() {
        if (instance != null)
            return;
        Gson gson = Constants.GSON;
        InputStream in = Hangman.class.getResourceAsStream(Constants.WORDS);
        Validator.requireNotNull(in, "Could not load {}!", Constants.WORDS);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        instance = gson.fromJson(reader, Words.class);
        instance.mapWords();
    }

    public static String randomWord(int length) {
        Validator.requireNotNull(instance, "Cannot fetch random word: instance == null.");
        if (length == -1)
            length = ApiRequest.randomWordLength();
        List<String> words = instance.mappedWords.get(length);
        return words.get(RANDOM.nextInt(words.size()));
    }

    private void mapWords() {
        Validator.requireNull(this.mappedWords, "Cannot create mapped words object when non-null.");
        this.mappedWords = new HashMap<>();
        for (String s : words) {
            int length = s.length();
            List<String> words = this.mappedWords.getOrDefault(length, new ArrayList<>());
            words.add(s);
            this.mappedWords.put(length, words);
        }
    }

    private Map<Integer, List<String>> getMappedWords() {
        return mappedWords;
    }
}