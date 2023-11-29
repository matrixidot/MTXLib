package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

public class MTXCommand {
    private final JavaPlugin plugin;
    private final MTXItemCommands itemCommands;
    private final MTXBindCommands bindCommands;
    private final MTXStashCommands stashCommands;

    public MTXCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.itemCommands = new MTXItemCommands();
        this.bindCommands = new MTXBindCommands();
        this.stashCommands = new MTXStashCommands();
        mtx();
    }

    public void mtx() {
        new CommandAPICommand("mtx")
                .withSubcommand(bindCommands.bind())
                .withSubcommand(bindCommands.unbind())
                .withSubcommand(itemCommands.item())
                .withSubcommand(stashCommands.stash())
                //.withSubcommand(registryCommands.get())
                .register();
    }
}
