package me.neo.mtxlib.api.registering;

import org.bukkit.event.Listener;

/**
 * Represents a class that can be registered to the MTXRegistry
 * Any class implementing this can also handle events
 * @param <T> The Implementing class
 */
public interface MTXRegisterable<T> extends Listener {
    String getName();

    int getId();

    void onRegister();

    void onUnregister();
}
