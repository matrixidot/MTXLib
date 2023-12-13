package me.neo.mtxlib.api.core;

import me.neo.mtxlib.api.item.MTXItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class test {
    private static final MRegistry<MTXItem<?>> ITEMS = MRegistry.create("items");
    public static final MTXItem<th> THITEM = (MTXItem<th>) ITEMS.registerItem("TH", new th("4", 2, 2));

}
class bla implements IRegistrable<bla> {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onUnregister() {

    }
}
class th extends MTXItem<th> {

    public th(String name, int id, int cooldownTime) {
        super(name, id, cooldownTime);
    }

    @Override
    public ItemStack buildCustomItem() {
        return null;
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
    public void dealtDamage(Player player, ItemStack item, Entity entity, EntityDamageByEntityEvent event) {

    }

    @Override
    public void rightClickEntity(Player player, ItemStack item, boolean sneaking, PlayerInteractEntityEvent event) {

    }

    @Override
    public void breakBlock(Player player, ItemStack item, Block block, boolean sneaking, BlockBreakEvent event) {

    }
}
