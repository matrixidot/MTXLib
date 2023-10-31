package me.neo.mtxlib.api.twist;

import jdk.jfr.Registered;
import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.registering.MTXRegistrable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Twist<T> implements MTXRegistrable<T> {
    private final ArrayList<UUID> boundPlayers = new ArrayList<>();

    private final String name;

    private final String description;

    private final int id;

    public Twist(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public boolean isBound(Player player) {
        return boundPlayers.contains(player.getUniqueId());
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

    public List<Player> getBoundPlayers() {
        return boundPlayers.stream().map(Bukkit::getPlayer).toList();
    }

    public boolean bindPlayer(Player player, boolean silent) {
        if (player == null) {
            MTXLib.log.error("Player is null when trying to bind to " + name + ".");
            return false;
        }
        if (!silent) {
            player.sendMessage(ChatColor.GREEN + "Got new twist: " + name + ".");
            player.sendMessage(ChatColor.AQUA + description);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 4f);
        }
        boundPlayers.add(player.getUniqueId());
        MTXLib.log.info("Bound " + player.getName() + " to " + name + ".");
        return true;
    }
    public boolean bindPlayer(UUID player, boolean silent) {
        return bindPlayer(Bukkit.getPlayer(player), silent);
    }

    public boolean unbindPlayer(Player player, boolean unregistered) {
        if (player == null) {
            MTXLib.log.error("Player is null when trying to unbind from " + name + ".");
            return false;
        }
        if (unregistered) {
            player.sendMessage(ChatColor.RED + "Lost twist: " + name + "." + " This was because " + name + " is going to be unregistered.");
            MTXLib.log.info("Unbound " + player.getName() + " from " + name + " because " + name + " is going to be unregistered.");
        } else {
            player.sendMessage(ChatColor.RED + "Lost twist: " + name + ".");
            MTXLib.log.info("Unbound " + player.getName() + " from " + name + ".");
        }
        boundPlayers.remove(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
        return true;
    }
    public boolean unbindPlayer(UUID player, boolean unregistered) {
        return unbindPlayer(Bukkit.getPlayer(player), unregistered);
    }

    public void onRegister() {
        MTXLib.log.info("Registered twist: " + name + ".");
    }

    public void onUnregister() {
        MTXLib.log.info("Unregistered twist: " + name + ".");
    }
}
