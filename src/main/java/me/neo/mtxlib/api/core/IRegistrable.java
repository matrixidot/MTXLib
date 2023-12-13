package me.neo.mtxlib.api.core;

import org.bukkit.event.Listener;

/**
 * Represents an object that can be registered to the MTXRegistries
 * This also allows for event handling
 * @param <T> The Implementing class
 */
@SuppressWarnings("unused")
public interface IRegistrable<T> {
    /**
     * MUST BE UNIQUE BETWEEN ANY AND ALL IMPLEMENTATIONS
     * @return The name of the IRegistrable
     */
    String getName();

    /**
     * MUST BE UNIQUE BETWEEN ANY AND ALL IMPLEMENTATIONS
     * @return The id of the IRegistrable
     */
    int getId();

    void onRegister();

    void onUnregister();
}
