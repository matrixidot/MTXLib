package me.neo.mtxlib.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import me.neo.mtxlib.api.core.interfaces.IAttachable;
import me.neo.mtxlib.api.core.registering.MTXBinder;
import me.neo.mtxlib.api.core.registering.MTXRegisters;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MTXBindCommands {
    private final ArrayList<Argument<?>> attachArgs = new ArrayList<>();

    protected MTXBindCommands() {
        attachArgs.add(new PlayerArgument("player")); // 0
        attachArgs.add(new GreedyStringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(MTXRegisters.getAttachables().getNames()))); // 1


    }

    protected CommandAPICommand attach() {
        return new CommandAPICommand("attach")
                .withArguments(attachArgs)
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    Player player = (Player) args.get(0);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: Player not found.");
                        return;
                    }
                    String name = (String) args.get(1);
                    IAttachable<?> attachable = (IAttachable<?>) MTXRegisters.getAttachables().get(name);
                    if (attachable == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + name + " is not attachable.");
                    } else {
                        boolean success = MTXBinder.tryBind(player, attachable, false);
                        if (!success) {
                            sender.sendMessage(ChatColor.DARK_RED + "Internal error occurred when trying to attach " + player.getName() + " to: " + name);
                            sender.sendMessage(ChatColor.DARK_RED + "Please contact man_in_matrix#4484 on discord with a picture or log of your server's console");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "Successfully attached: " + player.getName() + " to: " + name + ".");
                        }

                    }
                });
    }

    protected CommandAPICommand detach() {
        return new CommandAPICommand("detach")
                .withArguments(attachArgs)
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    Player player = (Player) args.get(0);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: Player not found.");
                        return;
                    }
                    String name = (String) args.get(1);
                    IAttachable<?> detachable = (IAttachable<?>) MTXRegisters.getAttachables().get(name);
                    if (detachable == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Error: " + name + " is not detachable.");
                    } else {
                        boolean success = MTXBinder.tryUnbind(player, detachable, false);
                        if (!success) {
                            sender.sendMessage(ChatColor.DARK_RED + "Internal error occurred when trying to detach " + player.getName() + " from: " + name);
                            sender.sendMessage(ChatColor.DARK_RED + "Please contact man_in_matrix#4484 on discord with a picture or log of your server's console");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "Successfully detached: " + player.getName() + " from: " + name + ".");
                        }
                    }
                });
    }

}
