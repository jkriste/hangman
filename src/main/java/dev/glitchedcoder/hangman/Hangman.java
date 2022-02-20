package dev.glitchedcoder.hangman;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.scene.Splash;
import dev.glitchedcoder.hangman.util.Constants;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.View;
import dev.glitchedcoder.hangman.window.Window;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Hangman {

    private static Window window;
    private static ScheduledExecutorService executor;

    public static void main(String[] args) {
        executor = Executors.newSingleThreadScheduledExecutor();
        Config.loadConfig();
        Script.loadScript();
        View view = new View();
        /*
         * it is important that a Window instance
         * be created before any Scene implementation
         * due to the Location class needing Window
         * dimensions in order for static methods to
         * work correctly.
         */
        window = new Window(view);
        Scene scene = new Splash();
        view.setScene(scene);
        new Thread(window).start();
    }

    /**
     * Exits the game.
     * <br />
     * Will save the current {@link Config} instance.
     */
    public static synchronized void exit() {
        Config.saveConfig();
        window.stop();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.exit(0);
    }

    /**
     * Restarts the game.
     * <br />
     * Will do all the same things {@link #exit()} does,
     * but starts a {@link ProcessBuilder} for the system
     * to execute in order to restart the game.
     */
    public static synchronized void restart() {
        ProcessBuilder builder;
        if (System.getProperty("os.name").toUpperCase(Locale.ROOT).contains("WIN"))
            builder = new ProcessBuilder(Constants.WINDOWS_ARGS);
        else
            builder = new ProcessBuilder(Constants.UNIX_ARGS);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit();
    }

    public static Window getWindow() {
        return window;
    }

    public static ScheduledExecutorService getExecutor() {
        return executor;
    }
}