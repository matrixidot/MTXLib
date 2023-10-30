package me.neo.mtxlib.api.registering;

import me.neo.mtxlib.MTXLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Used to store and retrieve any objects you want to be registered
 * Ex: Twists, MTXItems...
 * You can define your own class to register using the {@link MTXRegisterable} Interface.
 */
public class MTXRegistry {
    private MTXRegistry() {}

    public static ArrayList<MTXRegisterable<?>> registered = new ArrayList<>();
    public static ArrayList<String> registeredNames = new ArrayList<>();

    @Nullable
    public static <T extends MTXRegisterable<?>> MTXRegisterable<?> get(Class<T> clazz) {
        for (MTXRegisterable<?> r : registered) {
            if (clazz == r.getClass())
                return r;
        }
        return null;
    }

    @Nullable
    public static MTXRegisterable<?> get(String name) {
        for (MTXRegisterable<?> r : registered) {
            if (r.getName().toLowerCase().equals(name))
                return r;
        }
        return null;
    }

    @Nullable
    public static MTXRegisterable<?> get(int id) {
        for (MTXRegisterable<?> r : registered) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    public static boolean tryRegister(MTXRegisterable<?> r, JavaPlugin plugin) {
        if (registered.contains(r)) {
            MTXLib.log.warn("While trying to register: " + r.getName() + " it is already registered.");
            return false;
        }

        RegisterMTXRegisterableEvent event = new RegisterMTXRegisterableEvent(twist);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Registering event for: " + r.getName() + " was cancelled, it will not be registered.");
            return false;
        }

        registered.add(r);
        registeredNames.add(r.getName());
        r.onRegister();
        Bukkit.getPluginManager().registerEvents(r, plugin);
        MTXLib.log.success(r.getName() + " registered.");
        return true;
    }
}
