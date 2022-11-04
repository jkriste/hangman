package dev.glitchedcoder.hangman.entity;

import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.ui.Portrait;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TextBox extends Entity {

    private Color textColor;
    private Runnable onFinish;
    private Location portraitLoc;
    private BufferedImage portraitImage;

    private final Rectangle bounds;
    private final List<String> lines;
    private final BufferedImage boxImage;
    private final BufferedImage[] lineImages;

    private static final byte MAX_LINES;
    private static final byte BOX_SCALE;
    private static final byte TEXT_SCALE;
    private static final short LINE_HEIGHT;
    private static final byte MAX_CHARACTERS;
    private static final double PORTRAIT_SCALE;
    private static final byte SPACE_BETWEEN_BOX;
    private static final byte SPACE_BETWEEN_PORTRAIT;
    private static final byte MAX_CHARACTERS_PER_LINE;

    static {
        MAX_LINES = 3;
        MAX_CHARACTERS_PER_LINE = 26;
        MAX_CHARACTERS = (byte) (MAX_LINES * MAX_CHARACTERS_PER_LINE);
        double scalar = Config.getConfig().getResolution().getScalar();
        SPACE_BETWEEN_PORTRAIT = (byte) (10 * scalar);
        SPACE_BETWEEN_BOX = (byte) (30 * scalar);
        BOX_SCALE = 7;
        TEXT_SCALE = 2;
        PORTRAIT_SCALE = 3.5D;
        LINE_HEIGHT = (short) (36 * scalar);
    }

    @ParametersAreNonnullByDefault
    public TextBox(Scene scene, Portrait portrait, Color textColor) {
        super(scene, EntityType.TEXT_BOX);
        this.textColor = textColor;
        this.lines = new ArrayList<>();
        this.lineImages = new BufferedImage[MAX_LINES];
        this.boxImage = new TexturePreprocessor(Texture.TEXT_BOX)
                .scale(BOX_SCALE)
                .build();
        this.portraitImage = new TexturePreprocessor(portrait.getTexture())
                .scale(PORTRAIT_SCALE)
                .build();
        this.bounds = new Rectangle(boxImage.getWidth(), boxImage.getHeight());
        setPortrait(portrait);
    }

    @Override
    protected void onLoad() {
        Location loc = getLocation();
        int portY = (loc.getY() + (getBounds().height / 2) - (portraitImage.getHeight() / 2));
        int portX = (loc.getX() + SPACE_BETWEEN_BOX);
        this.portraitLoc = new Location(portX, portY);
        if (!lines.isEmpty())
            update();
    }

    @Override
    protected void onUnload() {
        // do nothing
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void tick(byte count) {
        // do nothing
    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {
        Location loc = getLocation();
        graphics.drawImage(boxImage, loc.getX(), loc.getY(), null);
        graphics.drawImage(portraitImage, portraitLoc.getX(), portraitLoc.getY(), null);
        for (byte b = 0; b < MAX_LINES; b++) {
            BufferedImage line = lineImages[b];
            if (line == null)
                continue;
            graphics.drawImage(line, (portraitLoc.getX() + portraitImage.getWidth() + SPACE_BETWEEN_PORTRAIT),
                    (portraitLoc.getY() + (b * LINE_HEIGHT)), null);
        }
    }

    /**
     * Goes on to the next line of the {@link TextBox}.
     * <br />
     * If there {@link #hasNextLine() is no next line},
     * the {@link Runnable} passed onto {@link #onFinish(Runnable)}
     * will be ran.
     */
    public void nextLine() {
        lines.remove(0);
        if (lines.isEmpty()) {
            if (onFinish == null)
                return;
            onFinish.run();
        } else
            update();
    }

    /**
     * Checks if the {@link TextBox} has a next line.
     *
     * @return True if there is a next line, false otherwise.
     */
    public boolean hasNextLine() {
        return !lines.isEmpty();
    }

    /**
     * Sets what code to run on finish of the {@link TextBox}'s lines.
     *
     * @param runnable The code to run on finish.
     * @throws IllegalArgumentException Thrown if the given runnable is null.
     */
    public void onFinish(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onFinish = runnable;
    }

    /**
     * Adds the given {@link List<String> lines} to the {@link TextBox}.
     * <br />
     * Can be used inside of {@link #onFinish(Runnable)}.
     * <br />
     * Any singular line should be less than {@link #MAX_CHARACTERS}.
     *
     * @param lines The lines to add.
     * @throws IllegalArgumentException Thrown if the given lines are null.
     * @throws IllegalArgumentException Thrown if any line is over max characters.
     */
    public void addLines(@Nonnull List<String> lines) {
        Validator.requireNotNull(lines, "Given lines are null!");
        for (String s : lines) {
            Validator.checkArgument(s.length() <= MAX_CHARACTERS,
                    "Line '{}' is longer than max chars ({})", s, MAX_CHARACTERS);
            this.lines.add(s);
        }
        update();
    }

    /**
     * Sets the {@link Color} of the text.
     *
     * @param color The color to set the text.
     * @throws IllegalArgumentException Thrown if the given color is null.
     */
    public void setTextColor(@Nonnull Color color) {
        Validator.requireNotNull(color, "Given text color is null!");
        this.textColor = color;
        update();
    }

    /**
     * Sets the {@link Portrait} of the {@link TextBox}.
     * <br />
     * All {@link Portrait}s should use the same width and height,
     * otherwise it'll look funky/out of proper placement.
     *
     * @param portrait The portrait to set.
     * @throws IllegalArgumentException Thrown if the given portrait is null.
     */
    public void setPortrait(@Nonnull Portrait portrait) {
        Validator.requireNotNull(portrait, "Given portrait is null!");
        this.portraitImage = new TexturePreprocessor(portrait.getTexture())
                .scale(PORTRAIT_SCALE)
                .build();
    }

    /**
     * Updates the {@link TextBox}.
     * <br />
     * Separates the current line into words that
     * do not exceed the {@link #MAX_CHARACTERS_PER_LINE}.
     */
    private void update() {
        String line = this.lines.get(0);
        Validator.requireNotNull(line, "Line at index 0 returned null.");
        String[] words = line.split(" ");
        byte index = 0;
        for (byte b = 0; b < MAX_LINES; b++) {
            StringBuilder builder = new StringBuilder();
            for (; index < words.length; index++) {
                String word = words[index];
                if (builder.length() + word.length() > MAX_CHARACTERS_PER_LINE)
                    break;
                builder.append(word).append(' ');
            }
            if (builder.length() == 0) {
                this.lineImages[b] = null;
                continue;
            }
            this.lineImages[b] = new TexturePreprocessor(builder.toString())
                    .color(textColor)
                    .scale(TEXT_SCALE)
                    .removeBackground()
                    .build();
        }
    }
}