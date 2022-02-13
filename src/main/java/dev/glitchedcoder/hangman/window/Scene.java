package dev.glitchedcoder.hangman.window;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.Renderable;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.sound.Sound;
import dev.glitchedcoder.hangman.sound.Volume;
import dev.glitchedcoder.hangman.util.Validator;
import dev.glitchedcoder.hangman.window.key.Key;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Acts as the level/scene being displayed in the {@link View}.
 */
@EqualsAndHashCode(of = "id")
public abstract class Scene implements KeyEventDispatcher {

    private final UUID id;
    private final AtomicReference<Color> bg;
    private final List<Renderable> renderables;
    protected final ScheduledExecutorService executor = Hangman.getExecutor();

    protected static Dimension dimension;
    protected static final Config config = Config.getConfig();

    protected Scene() {
        this.id = UUID.randomUUID();
        this.bg = new AtomicReference<>(Color.BLACK);
        this.renderables = new CopyOnWriteArrayList<>();
    }

    /**
     * Called when the {@link Scene} is being loaded.
     */
    protected abstract void onLoad();

    /**
     * Called when the {@link Scene} is being unloaded.
     */
    protected abstract void onUnload();

    /**
     * Gets the {@link Set<Key>}s that the {@link Scene} is listening for.
     * <br />
     * Use the {@link dev.glitchedcoder.hangman.window.key.KeySelector}
     * class to construct a {@link Set<Key>}s object.
     *
     * @return The {@link Set<Key>}s that the {@link Scene} is listening for.
     */
    protected abstract Set<Key> getKeyListeners();

    /**
     * Called when a {@link Key} that is in
     * {@link #getKeyListeners() the key listeners}
     * is being pressed. If there are no key listeners,
     * this method will never be called, even when valid
     * {@link Key}s are being pressed.
     *
     * @param key The key that was pressed.
     */
    protected void onKeyPress(Key key) {
    }

    /**
     * Called when the {@link Window} has gained focus.
     *
     * @param event The focus event.
     */
    protected void focusGained(FocusEvent event) {
    }

    /**
     * Called when the {@link Window} has lost focus.
     * <br />
     * If in game, the {@link Scene} can handle this event
     * by opening a pause menu or other menu.
     *
     * @param event The focus event.
     */
    protected void focusLost(FocusEvent event) {
    }

    /**
     * Called when the {@link Window} is ticking the {@link View}.
     * <br />
     * While this method can be overidden, the implementation should
     * similarly call {@code super(count);} due to the handling of
     * {@link Renderable renderable objects}.
     *
     * @param count The tick count, usually {@code 0 <= count <= 30}.
     */
    protected void tick(byte count) {
        for (Renderable r : this.renderables) {
            if (!r.shouldDraw())
                continue;
            if (r.shouldRemove()) {
                r.remove();
                renderables.remove(r);
                continue;
            }
            r.tick(count);
        }
    }

    /**
     * Called when the {@link Window} needs to render the current frame.
     * <br />
     * While this method can be overidden, the implementation should
     * similarly call {@code super(graphics);} due to the handling of
     * {@link Renderable renderable objects}.
     *
     * @param graphics The graphics object.
     */
    protected void draw(Graphics2D graphics) {
        for (Renderable r : this.renderables) {
            if (r.shouldDraw())
                r.draw(graphics);
        }
    }

    /**
     * Plays the given {@link Sound}.
     * <br />
     * The sound's {@link FloatControl.Type#MASTER_GAIN} will be
     * adjusted based on the {@link Volume} stored in the {@link Config}.
     * <br />
     * Depending on the OS and JRE, a {@link Sound} may not play
     * at all or the {@link Volume} (even when adjusted in-game)
     * will not be changed.
     *
     * @param sound The sound to play.
     * @throws IllegalArgumentException Thrown if the given sound is null.
     */
    protected final void playSound(@Nonnull Sound sound) {
        Volume volume = config.getVolume();
        if (volume == Volume.MUTE)
            return;
        Validator.requireNotNull(sound, "Given sound is null!");
        try (Clip clip = AudioSystem.getClip()) {
            if (clip.isRunning())
                clip.stop();
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume.getGain());
            }
            clip.open(sound.asStream());
            clip.start();
            while(clip.getMicrosecondLength() != clip.getMicrosecondPosition())
            {
                // todo: LineListener
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A shortcut for setting the current {@link Scene}.
     * <br />
     * Gets the {@link View} of the {@link Window} and
     * passes the given {@link Scene} onto it.
     *
     * @param scene The scene to set.
     */
    protected final void setScene(@Nonnull Scene scene) {
        Validator.requireNotNull(scene, "Given scene is null!");
        View view = Hangman.getWindow().getView();
        view.setScene(scene);
    }

    /**
     * Adds the given {@link Renderable} object to the {@link Scene}.
     * <br />
     * Used to sort all {@link #renderables} by {@link RenderPriority}.
     *
     * @param renderable The renderable object to add.
     */
    protected final void addRenderable(@Nonnull Renderable renderable) {
        Validator.requireNotNull(renderable, "Given renderable is null!");
        this.renderables.add(renderable);
        Collections.sort(this.renderables);
    }

    /**
     * Adds the given {@link Renderable} objects to the {@link Scene}.
     * <br />
     * Used to sort all {@link #renderables} by {@link RenderPriority}.
     *
     * @param renderables The renderable objects to add.
     */
    protected final void addRenderables(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        if (renderables.length == 0)
            return;
        Collections.addAll(this.renderables, renderables);
        Collections.sort(this.renderables);
    }

    /**
     * A QoL method that {@link Renderable#spawn() spawns} all
     * of the given {@link Renderable} objects.
     *
     * @param renderables The renderable objects to spawn.
     */
    protected final void spawnAll(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        for (Renderable renderable : renderables)
            renderable.spawn();
    }

    /**
     * A QoL method that {@link Renderable#dispose() disposes}
     * all of the given {@link Renderable} objects.
     *
     * @param renderables The renderable objects to dispose of.
     */
    protected final void disposeAll(@Nonnull Renderable... renderables) {
        Validator.requireNotNull(renderables, "Given renderables array is null!");
        for (Renderable renderable : renderables)
            renderable.dispose();
    }

    /**
     * Gets the {@link UUID} of the {@link Scene}.
     *
     * @return The {@link UUID} of the {@link Scene}.
     */
    public final UUID getId() {
        return id;
    }

    /**
     * Gets the {@link Color} for the background of the {@link Scene}.
     * <br />
     * Used by {@link View} to render the background pre-{@link #draw(Graphics2D)}.
     *
     * @return The {@link Color} for the background of the {@link Scene}.
     */
    public final Color getBackground() {
        return bg.get();
    }

    /**
     * Sets the background {@link Color} for the {@link Scene}.
     *
     * @param color The color to make the background.
     */
    public final void setBackground(@Nonnull Color color) {
        Validator.requireNotNull(color, "The given color for the background is null!");
        this.bg.set(color);
    }

    /**
     * Gets the {@link List} of {@link Renderable}s.
     * <br />
     * {@link Renderable} objects are sorted by
     * their {@link RenderPriority}.
     * <br />
     * Returns an immutable {@link List}.
     *
     * @return The {@link List} of {@link Renderable}s.
     */
    public final List<Renderable> getRenderables() {
        return Collections.unmodifiableList(renderables);
    }

    @Override
    public final boolean dispatchKeyEvent(KeyEvent event) {
        if (getKeyListeners().isEmpty())
            return false;
        Key key = Key.of(event.getKeyCode());
        if (getKeyListeners().contains(key)) {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                onKeyPress(key);
                return true;
            }
        }
        return false;
    }

    /**
     * Called by {@link Window the window}, this method
     * helps the {@link Scene} to know which dimensions
     * to render the background and any other size-dependent
     * {@link Renderable} objects.
     * <br />
     * The given width and height should account for insets.
     *
     * @param d The dimension of the {@link Window} minus insets.
     */
    static void adjustDimensions(Dimension d) {
        dimension = d;
    }
}