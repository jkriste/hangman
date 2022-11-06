package dev.jkriste.hangman.entity;

import lombok.EqualsAndHashCode;

/**
 * Acts as a way to sort {@link Renderable} objects.
 * <br />
 * {@link #MIN} will be rendered first (and appear last), while
 * {@link #MAX} will be rendered last (and appear first).
 * <br />
 * While it's good to use the {@link RenderPriority} constants,
 * a custom {@link RenderPriority} can be made using the constructor.
 * <br />
 * In theory, there can be 255 total separate render layers.
 */
@EqualsAndHashCode(of = "priority")
public final class RenderPriority implements Comparable<RenderPriority> {

    public static final RenderPriority MIN;
    public static final RenderPriority LOW;
    public static final RenderPriority NORMAL;
    public static final RenderPriority HIGH;
    public static final RenderPriority MAX;

    static {
        MIN = new RenderPriority(-128);
        LOW = new RenderPriority(-64);
        NORMAL = new RenderPriority(0);
        HIGH = new RenderPriority(64);
        MAX = new RenderPriority(127);
    }

    private final byte priority;

    public RenderPriority(int priority) {
        this.priority = (byte) priority;
    }

    /**
     * Gets the number representation of the {@link RenderPriority}.
     * <br />
     * This can range from {@code -128} to {@code 127},
     * with {@code -128} being the first rendered and {@code 127}
     * being the last to be rendered.
     *
     * @return The number representation of the {@link RenderPriority}.
     */
    public byte getPriority() {
        return priority;
    }

    @Override
    public int compareTo(RenderPriority o) {
        return this.priority - o.priority;
    }
}