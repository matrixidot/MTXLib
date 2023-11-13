package me.neo.mtxlib.api.core;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.PlayerBindEvent;
import me.neo.mtxlib.api.customevents.PlayerUnbindEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * This is the class that is responsible for handling the classes and objects that players can be attached to.
 */
@SuppressWarnings("unused")
public class MTXBinder {
    private MTXBinder() {}

    public static boolean tryBind(Player player, IBindable<?> b, boolean silent) {
        if (player == null) {
            MTXLib.log.warn("Player is null when trying to bind.");
            return false;
        }
        if (b == null) {
            MTXLib.log.warn("Bindable is null when trying to bind.");
            return false;
        }
        if (b.isBound(player)) {
            MTXLib.log.info(player.getName() + " is already bound to: " + b.getName());
            return false;
        }

        PlayerBindEvent event = new PlayerBindEvent(player, b);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Binding event for: " + player.getName() + " to: " + b.getName() + " was cancelled, they will not be bound.");
            return false;
        }


        if (!silent) {
            for (String s : b.bindMessage())
                player.sendMessage(s);
        }
        b.bind(player);
        b.onBind(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 4f);
        return true;
    }

    public static boolean tryUnbind(Player player, IBindable<?> b, boolean unregistered) {
        if (player == null) {
            MTXLib.log.warn("Player is null when unbinding.");
            return false;
        }
        if (b == null) {
            MTXLib.log.warn("Bindable is null when binding.");
            return false;
        }

        if (unregistered) {
            MTXLib.log.info(player.getName() + " unbound from: " + b.getName() + " because it was unregistered");
            player.sendMessage(ChatColor.RED + "You were unbound from: " + b.getName() + " because it was unregistered.");
            b.onUnbind(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
            return true;
        }

        if (!b.isBound(player)) {
            MTXLib.log.info(player.getName() + " is not bound to: " + b.getName() + " and cannot be unbound.");
            return false;
        }

        PlayerUnbindEvent event = new PlayerUnbindEvent(player, b);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unbind event for: " + player.getName() + " from: " + b.getName() + " was cancelled, they will not be unbound.");
            return false;
        }
        b.unbind(player);
        b.onUnbind(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
        return true;
    }
}
