package dev.glitchedcoder.hangman.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.glitchedcoder.hangman.Hangman;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public final class Constants {

    public static final File JAR;
    public static final Gson GSON;
    public static final String SCRIPT;
    public static final String API_URL;
    public static final File CONFIG_FILE;
    public static final File APPDATA_DIR;
    public static final byte MIN_WORD_LENGTH;
    public static final byte MAX_WORD_LENGTH;
    public static final List<String> UNIX_ARGS;
    public static final String VERSION = "1.2.0";
    public static final List<String> WINDOWS_ARGS;
    public static final String TITLE = "Hanged Men";

    static {
        File osAppData;
        String os = System.getProperty("os.name").toUpperCase(Locale.ROOT);
        if (os.contains("WIN"))
            osAppData = new File(System.getenv("AppData"));
        else if (os.contains("MAC")) {
            String dir = System.getProperty("user.home");
            dir += "/Library/Application Support";
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
        File jar = null;
        try {
            jar = new File(Hangman.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JAR = Validator.requireNotNull(jar);
        MIN_WORD_LENGTH = 4;
        MAX_WORD_LENGTH = 14;
        UNIX_ARGS = List.of(
                "/bin/bash", "-c", "/bin/bash -c \"sleep 2 > /dev/null && java -jar " + JAR.getAbsolutePath() + "\""
        );
        WINDOWS_ARGS = List.of(
                "cmd", "/c", "ping", "localhost", "-n", "2", ">", "nul",
                "&&", "java", "-jar", JAR.getAbsolutePath()
        );
        SCRIPT = "/lang/script.json";
    }

    private Constants() {
    }
}