package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.neo.mtxlib.api.core.MTXRegistry;
import me.neo.mtxlib.api.item.MTXItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MTXItemCommands {

    protected MTXItemCommands() {}

    protected CommandAPICommand item() {
        return new CommandAPICommand("item")
                .withSubcommand(give());
    }

    protected CommandAPICommand give() {
        return new CommandAPICommand("give")
                .withArguments(new PlayerArgument("player"), new GreedyStringArgument("item name").replaceSuggestions(ArgumentSuggestions.strings(MTXRegistry.itemNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    Player player = (Player) args.get(0);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: Player not found.");
                        return;
                    }
                    String itemName = (String) args.get(1);
                    MTXItem<?> item = (MTXItem<?>) MTXRegistry.get(itemName);
                    if (item == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + itemName + " is not a valid item name");
                    } else {
                        player.getInventory().addItem(item.getItem());
                        sender.sendMessage(ChatColor.GREEN + "Successfully gave " + player.getName() + " " + itemName + ".");
                    }
                });
    }
}
