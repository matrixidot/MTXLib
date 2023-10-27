package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.PlayerBindTwistEvent;
import me.neo.mtxlib.api.customevents.PlayerUnbindTwistEvent;
import me.neo.mtxlib.api.customevents.RegisterTwistEvent;
import me.neo.mtxlib.api.customevents.UnregisterTwistEvent;
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
    public static ArrayList<Twist> twists = new ArrayList<>();
    public static ArrayList<String> twistNames = new ArrayList<>();

    public static boolean isBound(Player player, Twist twist) {
        return twist.isBound(player);
    }

    @Nullable
    public static <T extends Twist> Twist get(Class<T> clazz) {
        for (Twist twist : twists) {
            if (clazz == twist.getClass())
                return twist;
        }
        return null;
    }

    @Nullable
    public static Twist get(String name) {
        for (Twist twist : twists) {
            if (twist.getName().toLowerCase().equals(name))
                return twist;
        }
        return null;
    }

    @Nullable
    public static Twist get(int id) {
        for (Twist twist : twists) {
            if (twist.getId() == id)
                return twist;
        }
        return null;
    }

    public static List<Twist> getAllBoundTwists(@NotNull Player player) {
        List<Twist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist.isBound(player))
                bound.add(twist);
        }
        return bound;
    }

    public static List<ItemTwist> getSoulboundItemTwists(@NotNull Player player) {
        List<ItemTwist> bound = new ArrayList<>();
        for (Twist twist : twists) {
            if (twist instanceof ItemTwist it && it.isBound(player) && it.isSoulbound())
                bound.add(it);
        }
        return bound;
    }

    public static boolean tryRegister(@NotNull Twist twist, @NotNull JavaPlugin plugin) {
        if (twists.contains(twist)) {
            MTXLib.log.warn("While trying to register twist: " + twist.getName() + " it is already registered.");
            return false;
        }
        RegisterTwistEvent event = new RegisterTwistEvent(twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Registering event for twist: " + twist.getName() + " was cancelled, it will not be registered.");
            return false;
        }
        twists.add(twist);
        twistNames.add(twist.getName().toLowerCase());
        twist.onRegister();
        Bukkit.getPluginManager().registerEvents(twist, plugin);
        MTXLib.log.success("Twist: " + twist.getName() + " registered.");
        return true;
    }

    public static boolean tryUnregister(@NotNull Twist twist) {
        if (!twists.contains(twist)) {
            MTXLib.log.warn("While trying to unregister twist: " + twist.getName() + " it is not registered.");
            return false;
        }

        UnregisterTwistEvent event = new UnregisterTwistEvent(twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unregistering event for twist: " + twist.getName() + " was cancelled, it will not be unregistered.");
            return false;
        }
        MTXLib.log.info("Unbinding bound players...");
        for (Player player : twist.getBoundPlayers()) {
            TwistManager.tryUnbind(player, twist, true);
        }
        twists.remove(twist);
        twistNames.remove(twist.getName().toLowerCase());
        twist.onUnregister();
        MTXLib.log.success("Twist: " + twist.getName() + " unregistered");
        return true;
    }


    public static boolean tryBind(@NotNull Player player, @NotNull Twist twist, boolean silent) {
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

    public static boolean tryUnbind(@NotNull Player player, @NotNull Twist twist, boolean unregistered) {
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
