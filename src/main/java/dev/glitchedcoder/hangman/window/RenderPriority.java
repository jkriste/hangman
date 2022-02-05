package dev.glitchedcoder.hangman.window;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "priority")
public final class RenderPriority implements Comparable<RenderPriority> {

    public static final RenderPriority MIN;
    public static final RenderPriority LOW;
    public static final RenderPriority NORMAL;
    public static final RenderPriority HIGH;
    public static final RenderPriority MAX;

    static {
        MIN = new RenderPriority((byte) -128);
        LOW = new RenderPriority((byte) -64);
        NORMAL = new RenderPriority((byte) 0);
        HIGH = new RenderPriority((byte) 64);
        MAX = new RenderPriority((byte) 127);
    }

    private final byte priority;

    public RenderPriority(byte priority) {
        this.priority = priority;
    }

    public byte getPriority() {
        return priority;
    }

    @Override
    public int compareTo(RenderPriority o) {
        return this.priority - o.priority;
    }
}