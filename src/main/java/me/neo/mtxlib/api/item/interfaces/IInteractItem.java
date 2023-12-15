package me.neo.mtxlib.api.item.interfaces;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface IInteractItem extends Listener {

    void rightClick(PlayerInteractEvent event, ItemStack itemStack, boolean onCooldown);
    void leftClick(PlayerInteractEvent event, ItemStack itemStack, boolean onCooldown);
    void rightClickBlock(PlayerInteractEvent event, ItemStack itemStack, Block clicked, boolean onCooldown);
    void leftClickBlock(PlayerInteractEvent event, ItemStack itemStack, Block clicked, boolean onCooldown);
}
