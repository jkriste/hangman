package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcoder.hangman.sound.Volume;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Resolution;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Represents the class instance of locally
 * stored persistent data. Check the README
 * for locations as well as what is stored.
 */
public final class Config {

    private String apiKey;
    private Volume volume;
    private Resolution resolution;

    private static Config instance;

    public Config() {
        this(false);
    }

    public Config(boolean defaults) {
        if (!defaults)
            return;
        this.apiKey = "";
        this.volume = Volume.NORMAL;
        this.resolution = Resolution.P576;
    }

    /**
     * Gets the class instance of {@link Config}.
     * <br />
     * If one does not exist, it will be created
     * by calling {@link #loadConfig()}.
     *
     * @return The class instance of {@link Config}.
     */
    public static Config getConfig() {
        if (instance == null)
            loadConfig();
        return instance;
    }

    /**
     * Loads the {@link Constants#CONFIG_FILE config file}.
     * <br />
     * If the config file's parent directory does not exist,
     * it will be created. Subsequentially, if the config file
     * does not exist either, it will also be created with a
     * new instance of Config with the default values:
     * <br />
     * {@code
     *      { "apiKey": "", "resolution": "P576", "volume": "NORMAL" }
     * }
     * <br />
     * If the config file exists, it will be loaded as the class instance.
     *
     * @throws IllegalArgumentException Thrown if the config file or
     *                                  its parent directory could not
     *                                  be created.
     */
    public static void loadConfig() {
        if (instance != null)
            return;
        Gson gson = Constants.GSON;
        File config = Constants.CONFIG_FILE;
        try {
            File parent = config.getParentFile();
            if (!parent.exists())
                Validator.checkArgument(parent.mkdir(), "Failed to create parent dir of config file!");
            if (config.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(config));
                instance = gson.fromJson(reader, Config.class);
                reader.close();
            } else {
                Validator.checkArgument(config.createNewFile(), "Failed to create config file!");
                instance = new Config(true);
                JsonWriter writer = new JsonWriter(new FileWriter(config));
                gson.toJson(instance, Config.class, writer);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current config instance to the {@link Constants#CONFIG_FILE}.
     * <br />
     * Uses GSON to write the data to the file.
     *
     * @throws IllegalArgumentException Thrown if the file could not be created.
     */
    public static void saveConfig() {
        if (instance == null)
            return;
        Gson gson = Constants.GSON;
        File file = Constants.CONFIG_FILE;
        try {
            if (!file.exists())
                Validator.checkArgument(file.createNewFile(), "Failed to create config file!");
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            gson.toJson(instance, Config.class, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the stored API key.
     * <br />
     * The API key returned may not
     * be the same as the API key that
     * exists in the config file due to
     * {@link #getConfig() the class instance}
     * not being saved until exit.
     *
     * @return The stored API key.
     */
    @Nonnull
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Gets the stored {@link Resolution}.
     * <br />
     * The {@link Resolution} returned may not
     * be the same as the {@link Resolution}
     * that exists in the config file due to
     * {@link #getConfig() the class instance}
     * not being saved until exit.
     *
     * @return The stored {@link Resolution}.
     */
    @Nonnull
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Gets the stored {@link Volume}.
     * <br />
     * The {@link Volume} returned may not
     * be the same as the {@link Volume}
     * that exists in the config file due to
     * {@link #getConfig() the class instance}
     * not being saved until exit.
     *
     * @return The stored {@link Volume}.
     */
    public Volume getVolume() {
        return volume;
    }

    /**
     * Sets the API key.
     * <br />
     * This is used to communicate with the API
     * that will return a random word (or of a
     * desired length), and the API key is
     * required in order to return a valid response.
     *
     * @param apiKey The API key.
     * @throws IllegalArgumentException Thrown if the given key is null.
     * @throws IllegalArgumentException Thrown if the given key is not of length 5.
     */
    public void setApiKey(@Nonnull String apiKey) {
        Validator.requireNotNull(apiKey, "Given API key is null!");
        Validator.checkArgument(apiKey.length() == 5, "Given API key '{}' has invalid length.", apiKey);
        this.apiKey = apiKey.toUpperCase(Locale.ROOT);
    }

    /**
     * Sets the {@link Resolution}.
     * <br />
     * This method does not actually update
     * the resolution of the Window, rather
     * just updates the config file.
     *
     * @param resolution The resolution to set.
     * @throws IllegalArgumentException Thrown if the given resolution is null.
     */
    public void setResolution(@Nonnull Resolution resolution) {
        Validator.requireNotNull(resolution, "Given resolution is null!");
        this.resolution = resolution;
    }

    /**
     * Sets the {@link Volume}.
     * <br />
     * This method updates the volume that
     * the next sound will use.
     *
     * @param volume The volume to set.
     */
    public void setVolume(Volume volume) {
        this.volume = volume;
    }
}