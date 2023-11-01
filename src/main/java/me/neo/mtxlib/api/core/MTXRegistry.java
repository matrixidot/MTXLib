package me.neo.mtxlib.api.core;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.RegisterMTXRegistrableEvent;
import me.neo.mtxlib.api.customevents.UnregisterMTXRegistrableEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Used to store and retrieve any objects you want to be registered
 * Ex: Twists, MTXItems...
 * You can define your own class to register using the {@link MTXRegistrable} Interface.
 */
public class MTXRegistry {
    private MTXRegistry() {}

    public static ArrayList<MTXRegistrable<?>> registered = new ArrayList<>();
    public static ArrayList<String> registeredNames = new ArrayList<>();

    @Nullable
    public static <T extends MTXRegistrable<?>> MTXRegistrable<?> get(Class<T> clazz) {
        for (MTXRegistrable<?> r : registered) {
            if (clazz == r.getClass())
                return r;
        }
        return null;
    }

    @Nullable
    public static MTXRegistrable<?> get(String name) {
        for (MTXRegistrable<?> r : registered) {
            if (r.getName().toLowerCase().equals(name))
                return r;
        }
        return null;
    }

    @Nullable
    public static MTXRegistrable<?> get(int id) {
        for (MTXRegistrable<?> r : registered) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    public static boolean tryRegister(MTXRegistrable<?> r, JavaPlugin plugin) {
        if (registered.contains(r)) {
            MTXLib.log.warn("While trying to register: " + r.getName() + " it is already registered.");
            return false;
        }

        RegisterMTXRegistrableEvent event = new RegisterMTXRegistrableEvent(r);
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

    public static boolean tryUnregister(MTXRegistrable<?> r) {
        if (!registered.contains(r)) {
            MTXLib.log.warn("While trying to unregister: " + r.getName() + " it was not registered.");
            return false;
        }

        UnregisterMTXRegistrableEvent event = new UnregisterMTXRegistrableEvent(r);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unregistering event for: " + r.getName() + " was cancelled, it will not be unregistered.");
            return false;
        }

        registered.remove(r);
        registeredNames.remove(r.getName());
        r.onUnregister();
        MTXLib.log.success(r.getName() + "unregistered.");
        return true;
    }
}
