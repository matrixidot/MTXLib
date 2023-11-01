package me.neo.mtxlib;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class MTXLib {
    /* LIB FIELDS */
    private static JavaPlugin parentPlugin;
    public static Log log;

    /* TWIST API FIELDS */
    private static NamespacedKey twistInternalKey;

    /* ITEM API FIELDS */
    private static NamespacedKey mtxItemInternalKey;

    /**
     * Call this method in your plugin's onLoad method.
     * Sets fields and sets up CommandAPI
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} that is using the library.
     * @param logDebug Whether to log debug statements. Also sets CommandAPI's output to verbose
     * @param <T> The {@link org.bukkit.plugin.java.JavaPlugin}.
     */
    public static <T extends JavaPlugin> void onLoad(T plugin, boolean logDebug) {
        parentPlugin = plugin;
        twistInternalKey = new NamespacedKey(plugin, "MTXLib.TwistAPI.ItemTwist.Internal.Identifier");
        mtxItemInternalKey = new NamespacedKey(plugin, "MTXLib.ItemAPI.MTXItem.Internal.Identifier");
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).verboseOutput(true));
        log = new Log("[MTXLib]: ", logDebug);
    }

    public static <T extends JavaPlugin> void onEnable(T plugin) {
    }

    public static void onDisable() {
        CommandAPI.onDisable();
    }

    /* Util Methods */

    /* Getters */
    public static JavaPlugin getPlugin() {
        return parentPlugin;
    }
    public static String getTwistInternalString() {
        return "MTXLib.TwistAPI.ItemTwist.Internal.Identifier";
    }
    public static NamespacedKey getTwistInternalKey() {
        return twistInternalKey;
    }
    public static String getItemInternalString() {
        return "MTXLib.ItemAPI.MTXItem.Internal.Identifier";
    }
    public static NamespacedKey getMtxItemInternalKey() {
        return mtxItemInternalKey;
    }

}
