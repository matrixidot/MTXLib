package me.neo.mtxlib.api.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

public class MIT extends MTXItem<MIT> {
    public MIT(String name, int id, int cooldownTime) {
        super("test", 1, 20);

    @Override
    public ItemStack buildCustomItem() {
        ItemStack stack = new ItemStack(Material.ECHO_SHARD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName();
    }

    @Override
    public Recipe buildRecipe(ItemStack item) {
        return null;
    }

    @Override
    public void rightClick(Player player, ItemStack item, boolean sneaking, PlayerInteractEvent event) {

    }

    @Override
    public void leftClick(Player player, ItemStack item, boolean sneaking, PlayerInteractEvent event) {

    }

    @Override
    public void rightClickBlock(Player player, ItemStack item, Block clickedBlock, boolean sneaking, PlayerInteractEvent event) {

    }

    @Override
    public void leftClickBlock(Player player, ItemStack item, Block clickedBlock, boolean sneaking, PlayerInteractEvent event) {

    }

    @Override
    public void hurtEntity(Player player, ItemStack item, Entity entity, EntityDamageByEntityEvent event) {

    }

    @Override
    public void rightClickEntity(Player player, ItemStack item, boolean sneaking, PlayerInteractEntityEvent event) {

    }

    @Override
    public void breakBlock(Player player, ItemStack item, Block block, boolean sneaking, BlockBreakEvent event) {

    }
}
