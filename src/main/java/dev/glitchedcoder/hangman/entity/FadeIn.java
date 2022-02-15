package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Resolution;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class FadeIn extends Entity {

    private Color color;
    private boolean finished;
    private Rectangle bounds;
    private Runnable onFinish;

    private final byte skip;

    private static final short MIN_ALPHA = 0;

    @ParametersAreNonnullByDefault
    public FadeIn(Scene scene, Color color, byte skip) {
        super(scene, EntityType.FADE_IN);
        Validator.checkArgument(skip > 0, "Given skip is <= 0.");
        this.skip = skip;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    @Override
    protected void onLoad() {
        Resolution resolution = Config.getConfig().getResolution();
        this.bounds = new Rectangle(resolution.getWidth(), resolution.getHeight());
    }

    @Override
    protected void onUnload() {
        this.bounds = null;
        this.onFinish = null;
        this.color = null;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        if (finished)
            return;
        short newAlpha = (short) Math.max(MIN_ALPHA, color.getAlpha() - skip);
        this.color = new Color(color.getRed(), color.getGreen(), color.getRed(), newAlpha);
        if (newAlpha == MIN_ALPHA) {
            this.finished = true;
            if (this.onFinish != null)
                this.onFinish.run();
        }
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        graphics.setColor(this.color);
        graphics.fill(this.bounds);
    }

    @Nonnull
    @Override
    public RenderPriority getRenderPriority() {
        return RenderPriority.MAX;
    }

    /**
     * When the alpha reaches {@link #MIN_ALPHA},
     * the given {@link Runnable} will be ran on
     * the same thread, acting as a trigger for
     * when the transition has completed.
     *
     * @param runnable What to run on finish.
     * @throws IllegalArgumentException Thrown if runnable is null.
     */
    public void onFinish(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onFinish = runnable;
    }
}