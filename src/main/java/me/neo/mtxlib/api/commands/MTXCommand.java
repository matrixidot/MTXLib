package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

public class MTXCommand {
    private final JavaPlugin plugin;
    private final MTXItemCommands itemCommands;
    private final MTXBindCommands attachCommands;
    private final MTXStashCommands stashCommands;

    public MTXCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.itemCommands = new MTXItemCommands();
        this.attachCommands = new MTXBindCommands();
        this.stashCommands = new MTXStashCommands();
        mtx();
    }

    public void mtx() {
        new CommandAPICommand("mtx")
                .withSubcommand(attachCommands.attach())
                .withSubcommand(attachCommands.detach())
                .withSubcommand(itemCommands.item())
                .withSubcommand(stashCommands.stash())
                //.withSubcommand(registryCommands.get())
                .register();
    }
}
