package me.neo.mtxlib.api.core.registering;

import me.neo.mtxlib.api.core.interfaces.IAttachable;
import me.neo.mtxlib.api.core.interfaces.IRegistrable;
import me.neo.mtxlib.api.item.MTXItem;
import me.neo.mtxlib.api.twist.Twist;
import org.bukkit.plugin.Plugin;


/**
 * Used to store and retrieve any objects you want to be registered
 * Ex: Twists, MTXItems...
 * You can define your own class to register using the {@link IRegistrable} Interface.
 */
@SuppressWarnings("unused")
public class MTXRegistries {
    private MTXRegistries() {}

    private static final MRegistry<Twist<?>> TWISTS = MRegistry.create("TWISTS");
    private static final MRegistry<MTXItem<?>> ITEMS = MRegistry.create("ITEMS");
    private static final MRegistry<IAttachable<?>> ATTACHABLES = MRegistry.create("ATTACHABLES");

    public static <T extends Twist<?>> IRegistrable<T> registerTwist(String name, Twist<T> twist, Plugin plugin) {
        registerBindable(name, twist, plugin);
        return TWISTS.registerItem(name, twist, plugin);
    }

    public static <T extends MTXItem<?>> IRegistrable<T> registerMTXItem(String name, MTXItem<T> item, Plugin plugin) {
        return ITEMS.registerItem(name, item, plugin);
    }

    public static <T extends IAttachable<?>> IRegistrable<T> registerBindable(String name, IAttachable<T> bindable, Plugin plugin) {
        return ATTACHABLES.registerItem(name, bindable, plugin);
    }

    public static MRegistry<Twist<?>> getTwists() {
        return TWISTS;
    }

    public static MRegistry<MTXItem<?>> getItems() {
        return ITEMS;
    }

    public static MRegistry<IAttachable<?>> getAttachables() {
        return ATTACHABLES;
    }

    public static void register() {
        MRegistry.register(TWISTS);
        MRegistry.register(ITEMS);
        MRegistry.register(ATTACHABLES);
    }
}
