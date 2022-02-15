package dev.glitchedcoder.hangman.entity;

import lombok.EqualsAndHashCode;

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

    public byte getPriority() {
        return priority;
    }

    @Override
    public int compareTo(RenderPriority o) {
        return this.priority - o.priority;
    }
}