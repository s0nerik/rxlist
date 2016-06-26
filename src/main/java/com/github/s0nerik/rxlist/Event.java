package com.github.s0nerik.rxlist;

public class Event<E> {
    public enum Type { ITEM_ADDED, ITEM_REMOVED, ITEM_CHANGED, ITEMS_CLEARED }

    public final Type type;
    public final int index;
    public final E item;

    private Event(Type type, int index, E item) {
        this.type = type;
        this.index = index;
        this.item = item;
    }

    public static <E> Event<E> create(Type type, int index, E item) {
        return new Event<>(type, index, item);
    }
}