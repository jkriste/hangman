package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.entity.Entity;
import dev.glitchedcoder.hangman.entity.EntityType;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.Scene;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MenuComponent extends Entity {

    private String text;
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
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onUnload() {

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void tick(byte count) {

    }

    @Override
    public void draw(@Nonnull Graphics2D graphics) {

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

    }

    public void setFocusable(boolean focusable) {

    }

    public void onSelect(@Nonnull Runnable runnable) {
        Validator.requireNotNull(runnable, "Given runnable is null!");
        this.onSelect = runnable;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void update() {

    }
}