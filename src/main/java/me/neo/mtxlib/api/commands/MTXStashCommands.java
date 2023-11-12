package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.neo.mtxlib.api.twist.ItemStash;
import org.bukkit.ChatColor;

public class MTXStashCommands {
    protected MTXStashCommands() {
    }

    protected CommandAPICommand stash() {
        return new CommandAPICommand("stash")
                .withSubcommand(claim());
    }

    private CommandAPICommand claim() {
        return new CommandAPICommand("claim")
                .executesPlayer((sender, args) -> {
                    if (ItemStash.get(sender.getUniqueId()) == null) {
                        ItemStash.createStash(sender.getUniqueId());
                    }
                    ItemStash.get(sender.getUniqueId()).tryClaim();

                });
    }
}
