package me.neo.mtxlib.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.neo.mtxlib.api.twist.ItemStash;
import me.neo.mtxlib.api.twist.ItemTwist;
import me.neo.mtxlib.api.twist.Twist;
import me.neo.mtxlib.api.twist.TwistManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
@SuppressWarnings("unused")
public class TwistCommands {
    private final JavaPlugin plugin;

    public TwistCommands(JavaPlugin plugin) {
        this.plugin = plugin;
        giveTwist();
        giveTwistSilent();
        takeTwist();
        claimStash();
    }


    public void giveTwist() {
        new CommandAPICommand("giveTwist")
                .withAliases("gt")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(TwistManager.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = TwistManager.get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = TwistManager.tryBind(sender, twist, false);
                        if (!success)
                            sender.sendMessage(ChatColor.RED + "Internal Occurred while trying to bind you to: " + twistName);
                    }
                }).register();
    }

    public void giveTwistSilent() {
        new CommandAPICommand("giveTwistSilent")
                .withAliases("gts")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(TwistManager.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = TwistManager.get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = TwistManager.tryBind(sender, twist, true);
                        if (!success)
                            sender.sendMessage(ChatColor.RED + "Internal Occurred while trying to bind you to: " + twistName);
                    }
                }).register();
    }

    public void takeTwist() {
        new CommandAPICommand("takeTwist")
                .withAliases("tt")
                .withArguments(new GreedyStringArgument("Twist Name").replaceSuggestions(ArgumentSuggestions.strings(TwistManager.twistNames)))
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    String twistName = (String) args.get(0);
                    Twist twist = TwistManager.get(twistName);
                    if (twist == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid twist inputted");
                    } else {
                        boolean success = TwistManager.tryUnbind(sender, twist, false);
                        if (!success)
                            sender.sendMessage(ChatColor.RED + "Internal Occurred while trying to bind you to: " + twistName);
                    }
                }).register();
    }

    public void claimStash() {
        new CommandAPICommand("claimStash")
                .withAliases("ct")
                .executesPlayer((sender, args) -> {
                    ItemStash stash = ItemStash.get(sender.getUniqueId());
                    if (stash == null) {
                        ItemStash.createStash(sender.getUniqueId());
                        return;
                    }
                    stash.tryClaim(sender);
                }).register();
    }
}
