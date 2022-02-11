package dev.glitchedcoder.hangman.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.glitchedcoder.hangman.Hangman;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

public final class Constants {

    public static final File JAR;
    public static final Gson GSON;
    public static final String API_URL;
    public static final File CONFIG_FILE;
    public static final File APPDATA_DIR;
    public static final String VERSION = "1.0.0";
    public static final String TITLE = "Hanged Men";

    static {
        File osAppData;
        String os = System.getProperty("os.name").toUpperCase(Locale.ROOT);
        if (os.contains("WIN"))
            osAppData = new File(System.getenv("AppData"));
        else if (os.contains("MAC")) {
            String dir = System.getProperty("user.home");
            dir += "/Library/ApplicationSupport";
            osAppData = new File(dir);
        } else {
            osAppData = new File(System.getProperty("user.home"));
        }
        APPDATA_DIR = new File(osAppData, TITLE + " Data");
        CONFIG_FILE = new File(APPDATA_DIR, "config.json");
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
        API_URL = "https://clemsonhackman.com/api/word";
        File jar;
        try {
            jar = new File(Hangman.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            jar = null;
        }
        JAR = jar;
        System.out.println(CONFIG_FILE.getAbsolutePath());
        Validator.requireNotNull(JAR, "Could not get JAR location!");
        System.out.println(JAR.getAbsolutePath());
    }

    private Constants() {
    }
}