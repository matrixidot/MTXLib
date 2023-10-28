package me.neo.mtxlib.api.item;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.customevents.RegisterItemEvent;
import me.neo.mtxlib.api.customevents.UnregisterItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class MTXItemManager {
    public static ArrayList<MTXItem> items = new ArrayList<>();

    public static ArrayList<String> itemNames = new ArrayList<>();

    @Nullable
    public static <T extends MTXItem> MTXItem get(Class<T> clazz) {
        for (MTXItem item : items) {
            if (clazz == item.getClass())
                return item;
        }
        return null;
    }

    @Nullable
    public static MTXItem get(String name) {
        for (MTXItem item : items) {
            if (item.getName().toLowerCase().equals(name))
                return item;
        }
        return null;
    }

    @Nullable
    public static MTXItem get(int id) {
        for (MTXItem item : items) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    public static boolean tryRegister(@NotNull MTXItem item, @NotNull JavaPlugin plugin) {
        if (items.contains(item)) {
            MTXLib.log.warn("While trying to register item: " + item.getName() + " it is already registered");
            return false;
        }
        RegisterItemEvent event = new RegisterItemEvent(item);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Registering event for item: " + item.getName() + " was cancelled.");
            return false;
        }
        items.add(item);
        itemNames.add(item.getName().toLowerCase());
        item.onRegister();
        Bukkit.getPluginManager().registerEvents(item, plugin);
        MTXLib.log.success("Item: " + item.getName() + " registered.");
        return true;
    }

    public static boolean tryUnregister(@NotNull MTXItem item) {
        if (!items.contains(item)) {
            MTXLib.log.warn("While trying to unregister item: " + item.getName() + " it is not registered.");
            return false;
        }

        UnregisterItemEvent event = new UnregisterItemEvent(item);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MTXLib.log.info("Unregistering event for item: " + item.getName() + " was cancelled, it will not be registered.");
            return true;
        }
        items.remove(item);
        itemNames.remove(item.getName().toLowerCase());
        item.onUnregister();
        MTXLib.log.success("Item: " + item.getName() + " unregistered");
        return true;
    }


}
