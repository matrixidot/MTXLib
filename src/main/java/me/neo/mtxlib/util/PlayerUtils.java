package me.neo.mtxlib.util;

import me.neo.mtxlib.api.twist.ItemStash;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {
    public static void addItem(ItemStack item, Player player) {
        if (player == null || item == null)
            return;

        if (player.getInventory().firstEmpty() == -1) {
            ItemStash.get(player.getUniqueId()).addItem(item);
        } else {
            player.getInventory().addItem(item);
        }
    }
}
