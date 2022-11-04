package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Script {

    @SerializedName("crimes.sfl")
    private List<String> crimesSfl;
    @SerializedName("crimes.nsfl")
    private List<String> crimesNsfl;
    @SerializedName("strings")
    private Map<String, String> strings;
    @SerializedName("lines")
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
        Validator.requireNotNull(in, "Could not load {}!", Constants.SCRIPT);
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
     * Retrieves a {@link Strings} object from the {@link Script}.
     *
     * @param string The string to retrieve.
     * @return A {@link Strings} object from the {@link Script}.
     */
    public String getString(@Nonnull Strings string) {
        Validator.requireNotNull(string, "Given Strings is null!");
        Validator.checkArgument(strings.containsKey(string.getId()), "Failed to retrieve string w/ id '{}'", string.getId());
        return strings.get(string.getId());
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

    /**
     * Gets the {@link List<String> script} for the given {@link ScriptSection}
     * and replaces the given {@code keyword} with the given {@code replacement}. i.e.,
     * <br />
     * {@code getSection(ScriptSection.INTRODUCTION_TUTORIAL, "Executioner", "Bob");}
     * <br />
     * will produce the following string for {@code list.get(0)}:
     * <br />
     * {@code "They call me the Bob."}
     * <br />
     * This method will only replace the <b>first</b> instance of the given keyword.
     *
     * @param section The section to retrieve.
     * @param keyword The keyword to replace.
     * @param replacement The replacement for the keyword.
     * @return The script for the given {@link ScriptSection} with the given replacement.
     */
    @ParametersAreNonnullByDefault
    public List<String> getSection(ScriptSection section, String keyword, String replacement) {
        List<String> script = getSection(section);
        int index = -1;
        for (int i = 0; i < script.size(); i++) {
            if (script.get(i).contains(keyword)) {
                index = i;
                break;
            }
        }
        Validator.checkArgument(index != -1, "Script section '{}' does not contain keyword '{}'", section, keyword);
        script.set(index, script.get(index).replaceFirst(keyword, replacement));
        return script;
    }
}