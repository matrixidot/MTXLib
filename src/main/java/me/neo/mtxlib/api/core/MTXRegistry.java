package me.neo.mtxlib.api.core;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.RegisterIRegstrableEvent;
import me.neo.mtxlib.api.customevents.UnregisterIRegistrableEvent;
import me.neo.mtxlib.api.item.MTXItem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;


/**
 * Used to store and retrieve any objects you want to be registered
 * Ex: Twists, MTXItems...
 * You can define your own class to register using the {@link IRegistrable} Interface.
 */
@SuppressWarnings("unused")
public class MTXRegistry {
    private MTXRegistry() {}

    public static ArrayList<IRegistrable<?>> registered = new ArrayList<>();
    public static ArrayList<String> registeredNames = new ArrayList<>();
    public static ArrayList<String> bindableNames = new ArrayList<>();
    public static ArrayList<String> itemNames = new ArrayList<>();


    @Nullable
    public static <T extends IRegistrable<?>> IRegistrable<?> get(Class<T> clazz) {
        for (IRegistrable<?> r : registered) {
            if (clazz == r.getClass())
                return r;
        }
        return null;
    }

    @Nullable
    public static IRegistrable<?> get(String name) {
        for (IRegistrable<?> r : registered) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    @Nullable
    public static IRegistrable<?> get(int id) {
        for (IRegistrable<?> r : registered) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    public static boolean tryRegister(IRegistrable<?> r, JavaPlugin plugin) {
        if (registered.contains(r)) {
            MTXLib.log.warn("While trying to register: " + r.getName() + " it is already registered.");
            return false;
        }

        RegisterIRegstrableEvent event = new RegisterIRegstrableEvent(r);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Registering event for: " + r.getName() + " was cancelled, it will not be registered.");
            return false;
        }

        registered.add(r);
        registeredNames.add(r.getName());
        if (r instanceof IBindable<?>)
            bindableNames.add(r.getName());
        if (r instanceof MTXItem)
            itemNames.add(r.getName());
        r.onRegister();
        Bukkit.getPluginManager().registerEvents(r, plugin);
        MTXLib.log.success(r.getName() + " registered.");
        return true;
    }

    public static boolean tryUnregister(IRegistrable<?> r) {
        if (!registered.contains(r)) {
            MTXLib.log.warn("While trying to unregister: " + r.getName() + " it was not registered.");
            return false;
        }

        UnregisterIRegistrableEvent event = new UnregisterIRegistrableEvent(r);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unregistering event for: " + r.getName() + " was cancelled, it will not be unregistered.");
            return false;
        }

        registered.remove(r);
        registeredNames.remove(r.getName());
        if (r instanceof IBindable<?>)
            bindableNames.remove(r.getName());
        if (r instanceof MTXItem)
            itemNames.remove(r.getName());
        r.onUnregister();
        MTXLib.log.success(r.getName() + "unregistered.");
        return true;
    }
}
