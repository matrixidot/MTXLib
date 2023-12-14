package me.neo.mtxlib.api.core.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an object that a player can be attached to.
 * Extends {@link IRegistrable} since an attachable object needs to be registered first.
 * @param <T> The implementing class
 */
@SuppressWarnings("unused")
public interface IAttachable<T> extends IRegistrable<T> {
    List<UUID> boundPlayers = new ArrayList<>();

    default void bind(Player player) {
        boundPlayers.add(player.getUniqueId());
    }

    default void unbind(Player player) {
        boundPlayers.remove(player.getUniqueId());
    }

    default boolean isBound(Player player) {
        return boundPlayers.contains(player.getUniqueId());
    }

    default List<Player> getBoundPlayers() {
        return boundPlayers.stream().map(Bukkit::getPlayer).toList();
    }

    String[] bindMessage();

    void onBind(Player player);

    void onUnbind(Player player);
}
