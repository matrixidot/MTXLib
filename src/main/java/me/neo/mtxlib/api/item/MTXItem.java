package me.neo.mtxlib.api.item;

import org.bukkit.event.Listener;

public abstract class MTXItem implements Listener {
    private final String name;
    private final String description;
    private final int id;

    public MTXItem(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

}
