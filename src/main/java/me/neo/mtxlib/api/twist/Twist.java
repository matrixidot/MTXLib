package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.api.core.IEventBindable;

@SuppressWarnings("unused")
public abstract class Twist<T> implements IEventBindable<T> {
    private final String name;
    private final String description;

    private final int id;

    public Twist(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getId() {
        return id;
    }
}
