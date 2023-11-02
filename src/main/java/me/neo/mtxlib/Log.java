package me.neo.mtxlib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public class Log {
    private final boolean verbose;
    private final String prefix;
    public Log(String prefix, boolean verbose) {
        this.prefix = prefix;
        this.verbose = verbose;
    }

    public void message(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }
    public void info(String message) {
        if (verbose)
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + prefix +  message);
    }

    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + prefix + message);
    }

    public void debug(String message) {
        if (verbose)
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + prefix + message);
    }

    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix +  message);
    }

    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
    }

    public void fatal(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "A fatal error has occurred");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + prefix + message);
    }

}
