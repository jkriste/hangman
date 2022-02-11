package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.util.Constants;

import javax.annotation.Nonnull;
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

    private static final short MS_IN_S = 1000;
    private static final byte FRAMES_PER_SECOND = 10;

    public Window(View view) {
        this.view = view;
        this.running = false;
        add(view);
        adjustResolution(Config.getConfig().getResolution());
        setResizable(false);
        setTitle(Constants.TITLE + " v" + Constants.VERSION);
        setIconImage(Texture.EXECUTIONER.asImage());
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
        final double TIME_BETWEEN_UPDATES = 1000000000D / FRAMES_PER_SECOND;
        double lastUpdate = System.nanoTime();
        double lastRender;
        long timer = System.currentTimeMillis();
        byte updateCount = 0;
        setVisible(true);
        requestFocus();
        while (running) {
            double now = System.nanoTime();
            while (now - lastUpdate > TIME_BETWEEN_UPDATES && updateCount < FRAMES_PER_SECOND) {
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
            if ((System.currentTimeMillis() - timer) > MS_IN_S) {
                timer = System.currentTimeMillis();
                updateCount = 0;
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

    /**
     * Adjusts the {@link Resolution} of the {@link Window}.
     * <br />
     * Finds the {@link Dimension}s of the usable window
     * and subsequently passes these {@link Dimension}s
     * off to {@link View}, {@link Scene}, and {@link Location}.
     *
     * @param resolution The new resolution.
     */
    private void adjustResolution(@Nonnull Resolution resolution) {
        Dimension d = new Dimension(resolution.getWidth(), resolution.getHeight());
        setSize(d);
        setPreferredSize(d);
        pack();
        Insets insets = getInsets();
        // get the usable width & height for the view
        int newWidth = d.width - insets.left - insets.right;
        int newHeight = d.height - insets.top - insets.bottom;
        Dimension usable = new Dimension(newWidth, newHeight);
        System.out.println("newHeight: " + newHeight + ", newWidth: " + newWidth);
        view.adjustDimensions(usable);
        Scene.adjustDimensions(usable);
        Location.adjustDimensions(usable);
    }

    /**
     * Gets the {@link View}.
     * <br />
     * Used by the {@link Scene} as a shortcut
     * for changing the current {@link Scene}
     * displayed in the {@link View}.
     *
     * @return The {@link View}.
     */
    View getView() {
        return this.view;
    }
}