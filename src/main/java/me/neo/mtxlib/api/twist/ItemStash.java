package me.neo.mtxlib.api.twist;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

    public void tryClaim(Player player) {
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
        ItemTwist.doRemoveStashText(player, itemsClaimed);
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
}
