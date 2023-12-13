package me.neo.mtxlib.api.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MRegistry<T> {
    private static final HashMap<String, MRegistry<?>> registries = new HashMap<>();
    private final String name;
    private final List<IRegistrable<T>> entries = new ArrayList<>();

    private boolean isRegistered = false;
    public static <B> MRegistry<B> create(String name) {
        return new MRegistry<>(name);
    }

    private MRegistry(String name) {
        this.name = name;
    }

    public <I extends T> IRegistrable<I> registerItem(final String name, final IRegistrable<I> reg) {
        if (isFinalized())
            throw new UnsupportedOperationException("Cannot add item to registry after it is registered.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(reg);

        if (!entries.contains(reg))
            entries.add((IRegistrable<T>) reg);
        else
            throw new IllegalArgumentException("Duplicate attempting to be registered to " + name);
        return reg;
    }

    /**
     * Registers the registry to finialize it and allow for it to be accessed
     * @param registry The {@link MRegistry} to register
     * @return true if it was successful false otherwise
     */
    public static boolean register(MRegistry<?> registry) {
        if (registry.isFinalized())
            return false;
        registries.put(registry.getName(), registry);
        registry.finialize();
        return true;
    }

    public String getName() {
        return this.name;
    }

    public List<IRegistrable<T>> getEntries() {
        return entries;
    }

    public boolean isFinalized() {
        return isRegistered;
    }

    protected void finialize() {
        isRegistered = true;
    }
}
