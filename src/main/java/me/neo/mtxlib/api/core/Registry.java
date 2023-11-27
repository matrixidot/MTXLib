package me.neo.mtxlib.api.core;

import java.util.ArrayList;

public class Registry<T> {
    private final String registryName;

    private final ArrayList<IRegistrable<T>> registered;

    private final ArrayList<String> registeredNames;
    
    protected Registry(String registryName) {
        this.registryName = registryName;
        registered = new ArrayList<>();
        registeredNames = new ArrayList<>();
    }
    
    public String getRegistryName() {
        return registryName;
    }
    
    public ArrayList<IRegistrable<T>> getRegistered() {
        return registered;
    }
    
    public ArrayList<String> getRegisteredNames() {
        return registeredNames;
    }
    public boolean register()
    
}
