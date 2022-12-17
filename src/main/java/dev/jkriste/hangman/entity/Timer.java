package dev.jkriste.hangman.entity;

import dev.jkriste.hangman.scene.mode.TimerPreset;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.Validator;
import dev.jkriste.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Acts as a {@link Timer} with the ability to:
 * <ul>
 *     <li>{@link #start() Start}</li>
 *     <li>{@link #toggle() Toggle}</li>
 *     <li>{@link #onFinish(Runnable) Run code when finished}</li>
 * </ul>
 * The {@link Timer} will automatically be paused
 * until {@link #toggle()} or {@link #start()} are called.
 */
public class Timer extends Entity {

    private Color color1;
    private Color color2;
    private Runnable onFinish;
    private BufferedImage texture;
    private volatile boolean paused;
    private volatile boolean finished;
    private volatile boolean interpolate;

    private final int startTime;
    private final Rectangle bounds;
    private final AtomicInteger time;
    private final TexturePreprocessor preprocessor;

    private static final byte SEC_IN_MIN = 60;
    private static final double DEFAULT_SCALE = 2;

    @ParametersAreNonnullByDefault
    public Timer(Scene scene, Color color, TimerPreset preset) {
        this(scene, color, DEFAULT_SCALE, preset);
    }

    @ParametersAreNonnullByDefault
    public Timer(Scene scene, Color color, double scale, TimerPreset preset) {
        super(scene, EntityType.TIMER);
        this.startTime = preset.getTimeInSec();
        this.time = new AtomicInteger(preset.getTimeInSec());
        this.preprocessor = new TexturePreprocessor(formatTime(preset.getTimeInSec()))
                .scale(scale)
                .color(color);
        this.texture = preprocessor.build();
        this.bounds = new Rectangle(texture.getWidth(), texture.getHeight());
        this.paused = true;
    }

    @Override
    protected void onLoad() {
        // do nothing
    }

    @Override
    protected void onUnload() {
        this.onFinish = null;
        this.texture = null;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        if (paused || finished)
            return;
        if (count == 0) {
            int newTime = time.getAndDecrement();
            if (newTime >= 0)
                update(newTime);
            if (newTime == 0) {
                this.paused = true;
                this.finished = true;
                if (onFinish != null)
                    onFinish.run();
            }
        }
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        graphics.drawImage(this.texture, getLocation().getX(), getLocation().getY(), null);
    }

    /**
     * Starts the {@link Timer}.
     * <br />
     * If the {@link Timer} is {@link #isFinished() finished},
     * this method will do nothing.
     */
    public void start() {
        if (finished)
            return;
        this.paused = false;
    }

    /**
     * Toggles the {@link Timer}.
     * <br />
     * If the {@link Timer} is paused,
     * this method will unpause the {@link Timer}.
     * <br />
     * Likewise, if the {@link Timer} is not paused,
     * this method will pause the {@link Timer}.
     * <br />
     * If the {@link Timer} is {@link #isFinished() finished},
     * this method will do nothing.
     */
    public void toggle() {
        if (finished)
            return;
        boolean temp = this.paused;
        this.paused = !temp;
    }

    /**
     * Sets the {@link Runnable} to execute one the {@link Timer} is finished.
     * <br />
     * If the {@link Timer} is {@link #isFinished() finished}, this method will do nothing.
     *
     * @param runnable What to run on finish.
     */
    public void onFinish(@Nonnull Runnable runnable) {
        if (finished)
            return;
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onFinish = runnable;
    }

    /**
     * Checks if the {@link Timer} has finished.
     * <br />
     * If {@code true}, the {@link Timer} cannot be reused.
     *
     * @return True if the {@link Timer} is finished, false otherwise.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Checks if the {@link Timer} is paused.
     * <br />
     * If {@code true}, the {@link Timer} will not count down.
     *
     * @return True if the {@link Timer} is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the {@link Color} interpolation for the {@link Timer}.
     * <br />
     * As the timer counts down, the {@link Color} will transition
     * from the given {@link Color} {@code c1} towards the other given
     * {@link Color} {@code c2}.
     *
     * @param c1 The starting color.
     * @param c2 The ending color.
     * @see TexturePreprocessor#interpolateLinear(Color, Color, float)
     */
    @ParametersAreNonnullByDefault
    public void setInterpolation(Color c1, Color c2) {
        this.interpolate = true;
        this.color1 = c1;
        this.color2 = c2;
    }

    /**
     * Formats the given {@code timeLeft} into the following format:
     * <br />
     * {@code MIN:SEC}
     * <br />
     * If the given {@code timeLeft} is negative, it will give negative results.
     *
     * @param timeLeft The time left.
     * @return The time left formatted as {@code MIN:SEC}.
     */
    private String formatTime(int timeLeft) {
        StringBuilder builder = new StringBuilder();
        int minute = timeLeft / SEC_IN_MIN;
        int second = timeLeft % SEC_IN_MIN;
        builder.append(minute)
                .append(':')
                .append(second < 10 ? '0' + second : second);
        return builder.toString();
    }

    /**
     * Updates the {@link Timer}'s texture.
     * <br />
     * If a {@link #setInterpolation(Color, Color) interpolation} has been set,
     * the {@link Color} will be calculated and set.
     *
     * @param timeLeft The time left.
     * @see TexturePreprocessor#interpolateLinear(Color, Color, float)
     */
    private void update(int timeLeft) {
        String formattedTime = formatTime(timeLeft);
        if (!this.interpolate)
            this.texture = this.preprocessor
                    .setText(formattedTime)
                    .build();
        else {
            Color color = TexturePreprocessor.interpolateLinear(color1, color2, (float) timeLeft / startTime);
            this.texture = this.preprocessor
                    .color(color)
                    .setText(formattedTime)
                    .build();
        }
    }
}