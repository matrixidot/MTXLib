package me.neo.mtxlib.api.core.registering;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.core.interfaces.IRegistrable;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MRegistry<T> {
    private final static HashMap<String, MRegistry<?>> registries = new HashMap<>();
    private final String name;
    private final List<IRegistrable<T>> entries = new ArrayList<>();

    private boolean isSealed = false;
    public static <B> MRegistry<B> create(String name) {
        return new MRegistry<>(name);
    }

    private MRegistry(String name) {
        this.name = name;
    }

    public <I extends T> IRegistrable<I> registerItem(String name, IRegistrable<I> reg, final Plugin plugin) {
        if (isSealed)
            throw new UnsupportedOperationException("Cannot add item to registry after it is registered.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(reg);

        if (!entries.contains(reg))
            entries.add((IRegistrable<T>) reg);
        else
            throw new IllegalArgumentException("Duplicate attempting to be registered to " + name);

        if (reg instanceof Listener l)
            Bukkit.getPluginManager().registerEvents(l, plugin);
        MTXLib.log.success("Successfully registered: [" + name + "] into registry: [" + name + "]");
        return reg;
    }

    /**
     * Registers the registry to seal it and allow for it to be accessed
     * Will throw {@link UnsupportedOperationException} if the registry has a conflicting name or is sealed
     * @param registry The {@link MRegistry} to register
     */
    public static void register(MRegistry<?> registry) {
        if (registry.isSealed())
            throw new UnsupportedOperationException("Attempted to register sealed registry");
        if (registries.containsKey(registry.getName()))
            throw new UnsupportedOperationException("Attempted to register a registry with conflicting name: " + registry.getName());

        registry.seal();
        MTXLib.log.success("Sealed and registered registry: [" + registry.getName() + "]");
    }

    public String getName() {
        return this.name;
    }

    public List<IRegistrable<T>> getEntries() {
        return entries;
    }

    public List<String> getNames() {
        return entries.stream().map(IRegistrable::getName).toList();
    }

    public IRegistrable<T> get(String name) {
        for (IRegistrable<T> r : entries)
            if (r.getName().equals(name))
                return r;
        return null;
    }

    public <I extends T> IRegistrable<T> get(Class<I> clazz) {
        for (IRegistrable<T> r : entries) {
            if (clazz == r.getClass())
                return r;
        }
        return null;
    }
    public boolean isSealed() {
        return isSealed;
    }

    protected void seal() {
        isSealed = true;
    }
}
