package me.neo.mtxlib.api.listeners;

import me.neo.mtxlib.api.twist.ItemStash;
import me.neo.mtxlib.api.twist.ItemTwist;
import me.neo.mtxlib.api.twist.TwistManager;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class TwistListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player player = ev.getPlayer();
        if (Boolean.TRUE.equals(ev.getPlayer().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)))
            return;
        List<ItemTwist> toReclaim = TwistManager.getSoulboundItemTwists(player);
        if (toReclaim.isEmpty())
            return;
        boolean doesStash = false;
        for (ItemTwist it : toReclaim) {
            if (player.getInventory().contains(it.getCustomItem().getItem()))
                continue;
            if (player.getInventory().firstEmpty() == -1) {
                ItemStash.get(player.getUniqueId()).addItem(it.getCustomItem().getItem());
                doesStash = true;
            } else {
                player.getInventory().addItem(it.getCustomItem().getItem());
            }
        }
        player.sendMessage(ChatColor.RED + "You seem to have lost some twist-specific items!");
        player.sendMessage(ChatColor.GREEN + "Not to worry as some of the items may have been " + ChatColor.GOLD + ChatColor.BOLD + "SOULBOUND" + ChatColor.GREEN + " and returned to your inventory!");
        if (doesStash)
            ItemTwist.doAddStashText(player);
    }
}
