package dev.glitchedcoder.hangman.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Resolution;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class Config {

    private String apiKey;
    private Resolution resolution;

    private static Config instance;

    public Config() {
        this(false);
    }

    public Config(boolean defaults) {
        if (!defaults)
            return;
        this.apiKey = "84C35";
        this.resolution = Resolution.P576;
    }

    public static Config getConfig() {
        if (instance == null)
            loadConfig();
        return instance;
    }

    public static void loadConfig() {
        if (instance != null)
            return;
        Gson gson = Constants.GSON;
        File config = Constants.CONFIG_FILE;
        try {
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

    public static void saveConfig() {
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

    @Nonnull
    public String getApiKey() {
        return apiKey;
    }

    @Nonnull
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Sets the API key.
     * <br />
     * This is used to
     *
     * @param apiKey
     */
    public void setApiKey(@Nonnull String apiKey) {
        Validator.requireNotNull(apiKey, "Given API key is null!");
        this.apiKey = apiKey;
    }

    public void setResolution(@Nonnull Resolution resolution) {
        Validator.requireNotNull(resolution, "Given resolution is null!");
        this.resolution = resolution;
    }
}