package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.util.Constants;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@link Window} of the program.
 * <br />
 * Handles the main game thread as well as listening for
 * {@link #focusGained(FocusEvent)} and {@link #focusLost(FocusEvent)} events.
 */
public final class Window extends JFrame implements Runnable, FocusListener {

    private volatile boolean running;

    private final View view;

    private static final byte FRAMES_PER_SECOND = 30;

    public Window(View view) {
        this.view = view;
        this.running = false;
        Resolution resolution = Config.getConfig().getResolution();
        Dimension d = new Dimension(resolution.getWidth(), resolution.getHeight());
        setSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
        setResizable(false);
        setTitle(Constants.TITLE + " v" + Constants.VERSION);
        setIconImage(Texture.EXECUTIONER.asImage());
        Insets insets = getInsets();
        // get the usable width & height for the view
        final Dimension usable = new Dimension(d.width, d.height - insets.top - insets.bottom);
        view.adjustDimensions(usable);
        Scene.adjustDimensions(usable);
        Location.adjustDimensions(usable);
        add(view);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Hangman.exit();
            }
        });
        // center the frame on the screen
        setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        this.running = true;
        final byte MAX_UPDATES = 5;
        final double TIME_BETWEEN_UPDATES = 1000000000D / FRAMES_PER_SECOND;
        double lastUpdate = System.nanoTime();
        double lastRender;
        setVisible(true);
        requestFocus();
        while (running) {
            double now = System.nanoTime();
            byte updateCount = 0;
            while (now - lastUpdate > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES) {
                view.tick(updateCount);
                lastUpdate += TIME_BETWEEN_UPDATES;
                updateCount++;
            }
            if (now - lastUpdate > TIME_BETWEEN_UPDATES)
                lastUpdate = now - TIME_BETWEEN_UPDATES;
            view.draw();
            lastRender = now;
            while (now - lastRender < TIME_BETWEEN_UPDATES && now - lastUpdate < TIME_BETWEEN_UPDATES) {
                Thread.yield();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                now = System.nanoTime();
            }
        }
        view.close();
    }

    @Override
    public void focusGained(FocusEvent e) {
        view.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        view.focusLost(e);
    }

    /**
     * Kills the {@link Window}'s game thread.
     * <br />
     * When the game thread stops, the {@link View}
     * will {@link Scene#onUnload() unload the scene}.
     */
    public void stop() {
        this.running = false;
    }
}