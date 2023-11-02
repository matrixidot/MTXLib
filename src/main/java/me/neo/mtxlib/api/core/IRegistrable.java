package me.neo.mtxlib.api.core;

import org.bukkit.event.Listener;

/**
 * Represents an object that can be registered to the MTXRegistry
 * This also allows for event handling
 * @param <T> The Implementing class
 */
@SuppressWarnings("unused")
public interface IRegistrable<T> extends Listener {
    String getName();

    int getId();

    void onRegister();

    void onUnregister();
}
