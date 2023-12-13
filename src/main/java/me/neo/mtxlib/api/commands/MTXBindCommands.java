package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import me.neo.mtxlib.api.core.IBindable;
import me.neo.mtxlib.api.core.MTXBinder;
import me.neo.mtxlib.api.core.MTXRegistries;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MTXBindCommands {
    private final ArrayList<Argument<?>> bindArgs = new ArrayList<>();

    protected MTXBindCommands() {
        bindArgs.add(new PlayerArgument("player")); // 0
        bindArgs.add(new GreedyStringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(MTXRegistries.bindableNames))); // 1


    }

    protected CommandAPICommand bind() {
        return new CommandAPICommand("bind")
                .withArguments(bindArgs)
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    Player player = (Player) args.get(0);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: Player not found.");
                        return;
                    }
                    String name = (String) args.get(1);
                    IBindable<?> bindable = (IBindable<?>) MTXRegistries.get(name);
                    if (bindable == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + name + " is not bindable.");
                    } else {
                        boolean success = MTXBinder.tryBind(player, bindable, false);
                        if (!success) {
                            sender.sendMessage(ChatColor.DARK_RED + "Internal error occurred when trying to bind you to: " + name);
                            sender.sendMessage(ChatColor.DARK_RED + "Please contact man_in_matrix#4484 on discord with a picture of your server's console");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "Successfully bound: " + player.getName() + " to: " + name + ".");
                        }

                    }
                });
    }

    protected CommandAPICommand unbind() {
        return new CommandAPICommand("unbind")
                .withArguments(bindArgs)
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    Player player = (Player) args.get(0);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: Player not found.");
                        return;
                    }
                    String name = (String) args.get(1);
                    IBindable<?> bindable = (IBindable<?>) MTXRegistries.get(name);
                    if (bindable == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + name + " is not bindable.");
                    } else {
                        boolean success = MTXBinder.tryUnbind(player, bindable, false);
                        if (!success) {
                            sender.sendMessage(ChatColor.DARK_RED + "Internal error occurred when trying to unbind you from: " + name);
                            sender.sendMessage(ChatColor.DARK_RED + "Please contact man_in_matrix#4484 on discord with a picture of your server's console");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "Successfully unbound: " + player.getName() + " from: " + name + ".");
                        }
                    }
                });
    }

}
