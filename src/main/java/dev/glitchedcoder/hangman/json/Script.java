package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Script {

    private List<String> crimes;
    private Map<String, List<String>> lines;

    private static Script instance;

    public static Script getScript() {
        if (instance == null)
            loadScript();
        return Validator.requireNotNull(instance);
    }

    public static void loadScript() {
        Gson gson = Constants.GSON;
        InputStream in = Hangman.class.getResourceAsStream("/script/script.json");
        Validator.requireNotNull(in, "Could not load script!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        instance = gson.fromJson(reader, Script.class);
    }

    public String randomCrime() {
        return crimes.get(new Random().nextInt(crimes.size()));
    }

    public List<String> getSection(@Nonnull ScriptSection section) {
        return lines.get(section.getId());
    }
}