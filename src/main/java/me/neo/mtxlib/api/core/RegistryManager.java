package me.neo.mtxlib.api.core;

import java.util.HashMap;

public class RegistryManager {
    private RegistryManager() {}

    public static HashMap<String, Registry<?>> registries = new HashMap<>();

    /**
     * Tries to get or create a new registry with the given type and name.
     * Will cause an exception if the registry found with the name does not match the type specified.
     * @param name The name of the registry
     * @param <T> The type of IBindable<?> to store
     * @return The registry found or created.
     *
     */
    public static <T extends IBindable<?>> Registry<T> getRegistry(String name) {
        try {

        }
        if (registries.containsKey(name)) {
            return (Registry<T>) registries.get(name);
        }
        Registry<T> registry = new Registry<>(name);
        registries.put(name, registry);
    }

}
