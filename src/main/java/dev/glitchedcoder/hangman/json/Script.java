package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Script {

    @SerializedName("crimes_sfl")
    private List<String> crimesSfl;
    @SerializedName("crimes_nsfl")
    private List<String> crimesNsfl;
    private Map<String, List<String>> lines;

    private static Script instance;
    private static final Random RANDOM = new Random();

    /**
     * Gets the {@link Script} instance.
     * <br />
     * If the current {@link Script} instance is
     * {@code null}, it will be {@link #loadScript() loaded}.
     *
     * @return The {@link Script} instance.
     * @throws IllegalArgumentException Thrown if the script instance is null.
     */
    public static Script getScript() {
        if (instance == null)
            loadScript();
        return Validator.requireNotNull(instance);
    }

    /**
     * Loads the {@link Script} from the resources.
     * <br />
     * If the current {@link Script} instance isn't
     * {@code null}, this method will do nothing.
     *
     * @throws IllegalArgumentException Thrown if the script couldn't be loaded.
     */
    public static void loadScript() {
        if (instance != null)
            return;
        Gson gson = Constants.GSON;
        InputStream in = Hangman.class.getResourceAsStream(Constants.SCRIPT);
        Validator.requireNotNull(in, "Could not load script!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        instance = gson.fromJson(reader, Script.class);
    }

    /**
     * Retrieves a random crime from the list of crimes.
     * <br />
     * This is used in the {@link ScriptSection#INTRODUCTION} script section.
     * <br />
     * Hopefully you find them as funny as I did.
     *
     * @return A random crime from the crime list.
     */
    public String randomCrime() {
        Config config = Config.getConfig();
        boolean nsfl = config.getNSFL().isOn();
        int rand = RANDOM.nextInt(nsfl ? crimesNsfl.size() : crimesSfl.size());
        return nsfl ? crimesNsfl.get(rand) : crimesSfl.get(rand);
    }

    /**
     * Gets the {@link List<String> script} for the given {@link ScriptSection}.
     * <br />
     * Returns a copy of the {@link List<String> script} instead of the original.
     *
     * @param section The section to retrieve.
     * @return The script for the given {@link ScriptSection}.
     */
    public List<String> getSection(@Nonnull ScriptSection section) {
        Validator.requireNotNull(section, "Given script section is null!");
        if (!section.hasNsflVersion())
            return new ArrayList<>(lines.get(section.getSflId()));
        boolean nsfl = Config.getConfig().getNSFL().isOn();
        return new ArrayList<>(lines.get(nsfl ? section.getNsflId() : section.getSflId()));
    }
}