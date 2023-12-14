package me.neo.mtxlib.api.core.interfaces;

import org.bukkit.event.Listener;

/**
 * Represents an attachable object that can listen to events
 * @param <T> The type of attachable
 */
public interface IEAttachable<T> extends IAttachable<T>, Listener {
}
