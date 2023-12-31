package me.neo.mtxlib;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.neo.mtxlib.api.commands.MTXCommand;
import me.neo.mtxlib.api.core.MTXRegistry;
import me.neo.mtxlib.util.Log;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class MTXLib {
    /* LIB FIELDS */
    private static JavaPlugin parentPlugin;
    public static Log log;

    /**
     * Call this method in your plugin's onLoad method.
     * Sets fields and sets up CommandAPI
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} that is using the library.
     * @param <T> The {@link org.bukkit.plugin.java.JavaPlugin}.
     */
    public static <T extends JavaPlugin> void onLoad(T plugin) {
        log = new Log("[MTXLib]: ", true);
        parentPlugin = plugin;
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).verboseOutput(true));
    }

    /**
     * Call this method after registering everything
     * @param plugin The parent plugin
     * @param logDebug whether to log debug messages
     * @param <T> The javaplugin parent.
     */
    public static <T extends JavaPlugin> void onEnable(T plugin, boolean logDebug) {
        log = new Log("[MTXLib]: ", logDebug);
        new MTXCommand(plugin);
        for (String s : MTXRegistry.registeredNames)
            MTXLib.log.success("Registered: " + s);
    }

    public static void onDisable() {
        CommandAPI.onDisable();
    }

    /* Util Methods */

    /* Getters */
    public static JavaPlugin getPlugin() {
        return parentPlugin;
    }
}
