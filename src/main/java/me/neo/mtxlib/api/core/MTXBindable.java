package me.neo.mtxlib.api.core;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface MTXBindable<T> {
    ArrayList<UUID> boundPlayers = new ArrayList<>();

    default boolean isBound(Player player) {
        return boundPlayers.contains(player.getUniqueId());
    }
    
}
