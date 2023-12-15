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
public class MTXRegisters {
    private MTXRegisters() {}

    private static final MRegistry<Twist<?>> TWISTS = MRegistry.create("TWISTS");
    private static final MRegistry<MTXItem<?>> ITEMS = MRegistry.create("ITEMS");
    private static final MRegistry<IAttachable<?>> ATTACHABLES = MRegistry.create("ATTACHABLES");

    public static <T extends Twist<?>> IRegistrable<T> registerTwist(Twist<T> twist, Plugin plugin) {
        registerBindable(twist, plugin);
        return TWISTS.registerItem( twist, plugin);
    }

    public static <T extends MTXItem<?>> IRegistrable<T> registerMTXItem(MTXItem<T> item, Plugin plugin) {
        return ITEMS.registerItem(item, plugin);
    }

    public static <T extends IAttachable<?>> IRegistrable<T> registerBindable(IAttachable<T> bindable, Plugin plugin) {
        return ATTACHABLES.registerItem(bindable, plugin);
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
