package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        File script = Constants.SCRIPT_FILE;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(script));
            instance = gson.fromJson(reader, Script.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String randomCrime() {
        return crimes.get(new Random().nextInt(crimes.size()));
    }

    public List<String> getSection(@Nonnull ScriptSection section) {
        return lines.get(section.getId());
    }
}