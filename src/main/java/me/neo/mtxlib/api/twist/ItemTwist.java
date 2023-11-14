package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.api.item.MTXItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@SuppressWarnings("unused")
public abstract class ItemTwist<T> extends Twist<T> {
    private final boolean grantItemOnBind;

    private final boolean soulbound;

    private final MTXItem<?> customItem;

    private final Recipe customRecipe;

    public ItemTwist(String name, String description, int id, MTXItem<?> item, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id);
        this.grantItemOnBind = grantItemOnBind;
        this.soulbound = soulbound;
        this.customItem = item;
        this.customRecipe = item.getRecipe() != null ? item.getRecipe() : buildCustomRecipe(item.getItem());
        if (item.getRecipe() == null && customRecipe != null)
            Bukkit.addRecipe(customRecipe);
    }

    public boolean isItemGrantedOnBind() {
        return grantItemOnBind;
    }

    public boolean isSoulbound() {
        return soulbound;
    }

    public ItemStack getCustomItem() {
        return customItem.getItem();
    }

    public Recipe getCustomRecipe() {
        return customRecipe;
    }

    public abstract Recipe buildCustomRecipe(ItemStack customItem);

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (customRecipe == null)
            return;
        if (!customItem.check(event.getRecipe().getResult()))
            return;
        Player player = (Player) event.getWhoClicked();
        if (!isBound(player)) {
            ItemStack no = new ItemStack(Material.BARRIER);
            ItemMeta meta = no.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Recipe Locked");
            meta.setLore(List.of(
                    ChatColor.DARK_RED + "You do not have the required twist to craft this item"
            ));

            no.setItemMeta(meta);
            event.getInventory().setResult(no);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            event.setCancelled(true);
        }
    }

    @Override
    public void onBind(Player player) {
        if (!grantItemOnBind)
            return;
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(customItem.getItem());
        } else {
            ItemStash.get(player.getUniqueId()).addItem(customItem.getItem());
        }
    }
}
