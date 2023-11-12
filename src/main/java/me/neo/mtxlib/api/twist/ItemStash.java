package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.MTXLib;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("unused")
public class ItemStash {
    private static final HashMap<UUID, ItemStash> stashes = new HashMap<>();
    private final UUID owner;
    private final ArrayList<ItemStack> items;

    @Nullable
    public static ItemStash get(UUID owner) {
        return stashes.get(owner);
    }

    public static void createStash(UUID owner) {
        if (ItemStash.get(owner) == null) {
            ItemStash stash = new ItemStash(owner);
            stashes.put(owner, stash);
        }
    }
    private ItemStash(UUID owner) {
        this.owner = owner;
        this.items = new ArrayList<>();
    }

    public void addItem(ItemStack toAdd) {
        if (!items.contains(toAdd))
            items.add(toAdd);
    }

    public void removeItem(int index) {
        items.remove(index);
    }

    public void removeItem(ItemStack toRemove) {
        items.remove(toRemove);
    }

    public void removeItems(ItemStack... toRemove) {
        for (ItemStack stack : toRemove)
            items.remove(stack);
    }

    public void tryClaim() {
        if (owner == null) {
            MTXLib.log.error("Owner is null while trying to claim stash.");
            return;
        }
        Player player = Bukkit.getPlayer(owner);
        if (player == null) {
            MTXLib.log.error("Player is null while trying to claim stash for: " + owner);
            return;
        }
        if (items.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "You have no items in your stash!");
            return;
        }
        int itemsClaimed = 0;
        // While the player has room and there are still items loop
        while (player.getInventory().firstEmpty() != -1 && !items.isEmpty()) {
            player.getInventory().addItem(getItem(0));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
            removeItem(0);
            itemsClaimed++;
        }
        if (items.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "All items claimed.");
            return;
        }
        doRemoveStashText(player, itemsClaimed);
    }

    public ItemStack getItem(int index) {
        return items.get(index);
    }

    public UUID getOwner() {
        return owner;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public static void doAddStashText(Player player) {
        player.sendMessage(ChatColor.GREEN + "Your inventory is full so the item was added to your stash!");
        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + ItemStash.get(player.getUniqueId()).getItems().size() + " item(s) in your stash!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mtx stash claim"));

        TextComponent filler1 = new TextComponent(" §eto try to claim the item(s), or run /mtx stash claim.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }


    public static void doRemoveStashText(Player player, int itemsClaimed) {
        player.sendMessage(ChatColor.RED + "Your inventory was full, but you claimed " + ChatColor.RED + itemsClaimed + " items!");
        player.sendMessage(ChatColor.YELLOW + "You still have " + ChatColor.RED + ItemStash.get(player.getUniqueId()).getItems().size() + " item(s) left!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mtx stash claim"));

        TextComponent filler1 = new TextComponent(" §eto try claim the item(s) again, or run /mtx stash claim.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }
}
