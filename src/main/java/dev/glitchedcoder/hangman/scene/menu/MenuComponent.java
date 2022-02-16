package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.Entity;
import dev.glitchedcoder.hangman.entity.EntityType;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MenuComponent extends Entity {

    private String text;
    private Rectangle bounds;
    private Runnable onSelect;
    private BufferedImage image;
    private volatile boolean focused;
    private volatile boolean focusable;

    private final double scalar;

    private static final Color FOCUSED;
    private static final Color UNFOCUSED;
    private static final Color UNFOCUSABLE;

    static {
        FOCUSED = Color.WHITE;
        UNFOCUSED = Color.LIGHT_GRAY;
        UNFOCUSABLE = Color.DARK_GRAY;
    }

    protected MenuComponent(Scene view, String text, double scale) {
        super(view, EntityType.MENU_COMPONENT);
        this.scalar = scale;
        this.text = text;
        this.focusable = true;
    }

    @Override
    protected void onLoad() {
        update();
    }

    @Override
    protected void onUnload() {
        this.image = null;
        this.bounds = null;
        this.text = null;
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
        graphics.drawImage(this.image, getLocation().getX(), getLocation().getY(), null);
    }

    @Nonnull
    @Override
    public RenderPriority getRenderPriority() {
        return RenderPriority.HIGH;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public boolean isFocusable() {
        return this.focusable;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        update();
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
        update();
    }

    public void onSelect(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onSelect = runnable;
    }

    public void setText(String text) {
        this.text = text;
        update();
    }

    public void select() {
        if (onSelect != null)
            onSelect.run();
    }

    protected void update() {
        this.image = new TexturePreprocessor(this.text)
                .color(focusable ? (focused ? FOCUSED : UNFOCUSED) : UNFOCUSABLE)
                .scale(scalar)
                .removeBackground()
                .build();
        this.bounds = new Rectangle(image.getWidth(), image.getHeight());
        // account for any changes in text
        int newX = Location.center(this.bounds).getX();
        getLocation().setX(newX);
    }
}