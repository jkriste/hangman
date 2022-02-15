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
        Config config = Config.getConfig();
        SPACE_BETWEEN_PORTRAIT = (byte) (10 * config.getResolution().getScalar());
        SPACE_BETWEEN_BOX = (byte) (30 * config.getResolution().getScalar());
        BOX_SCALE = 7;
        TEXT_SCALE = 2;
        PORTRAIT_SCALE = 3.5;
        LINE_HEIGHT = (short) (32 * config.getResolution().getScalar());
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
        update();
    }

    @Override
    protected void onUnload() {
        // todo
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

    public void nextLine() {
        lines.remove(0);
        if (lines.isEmpty()) {
            if (onFinish == null)
                return;
            onFinish.run();
        } else
            update();
    }

    public void onFinish(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onFinish = runnable;
    }

    public void addLines(@Nonnull List<String> lines) {
        Validator.requireNotNull(lines, "Given lines are null!");
        for (String s : lines) {
            Validator.checkArgument(s.length() <= MAX_CHARACTERS,
                    "Line '{}' is longer than max chars ({})", s, MAX_CHARACTERS);
            this.lines.add(s);
        }
        update();
    }

    public void setTextColor(@Nonnull Color color) {
        Validator.requireNotNull(color, "Given text color is null!");
        this.textColor = color;
        update();
    }

    public void setPortrait(@Nonnull Portrait portrait) {
        Validator.requireNotNull(portrait, "Given portrait is null!");
        this.portraitImage = new TexturePreprocessor(portrait.getTexture())
                .scale(PORTRAIT_SCALE)
                .build();
    }

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
                builder.append(word).append(" ");
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