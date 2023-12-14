package me.neo.mtxlib.api.core.interfaces;

import org.bukkit.event.Listener;

/**
 * Represents a registrable object that can listen to events
 * @param <T> the type of registrable
 */
public interface IERegistrable<T> extends IRegistrable<T>, Listener {
}
