package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.PlayerBindTwistEvent;
import me.neo.mtxlib.api.customevents.PlayerUnbindTwistEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TwistManager {
    private TwistManager() {}

    public static boolean isBound(Player player, Twist<?> twist) {
        return twist.isBound(player);
    }

    public static boolean tryBind(@NotNull Player player, @NotNull Twist<?> twist, boolean silent) {
        if (twist.isBound(player)) {
            MTXLib.log.info(player.getName() + " is already bound to " + twist.getName() + ".");
            return false;
        }

        PlayerBindTwistEvent event = new PlayerBindTwistEvent(player, twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Binding event for " + player.getName() + " to " + twist.getName() + " was cancelled.");
            return false;
        }

        if (twist instanceof ItemTwist it) {
            return it.bindPlayer(player, silent);
        }
        return twist.bindPlayer(player, silent);
    }

    public static boolean tryUnbind(@NotNull Player player, @NotNull Twist<?> twist, boolean unregistered) {
        if (!twist.isBound(player)) {
            MTXLib.log.info(player.getName() + " is not bound to " + twist.getName() + ".");
            return false;
        }
        if (unregistered) {
            if (twist instanceof ItemTwist it) {
                return it.unbindPlayer(player, unregistered);
            }
            return twist.unbindPlayer(player, unregistered);
        }

        PlayerUnbindTwistEvent event = new PlayerUnbindTwistEvent(player, twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unbinding event for " + player.getName() + " from " + twist.getName() + " was cancelled.");
            return false;
        }

        if (twist instanceof ItemTwist it) {
            return it.unbindPlayer(player, unregistered);
        }
        return twist.unbindPlayer(player, unregistered);
    }


}
