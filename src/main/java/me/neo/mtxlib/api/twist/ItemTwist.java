package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.api.item.MTXItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public abstract class ItemTwist extends Twist {
    private final boolean grantItemOnBind;
    private final boolean soulbound;
    private final MTXItem customItem;
    private final Recipe customRecipe;

    public ItemTwist(String name, String description, int id, MTXItem item, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id);
        customItem = item;
        if (item.getRecipe() == null) {
            customRecipe = buildRecipe(customItem.getItem());
            Bukkit.addRecipe(customRecipe);
        } else {
            customRecipe = item.getRecipe();
        }
        this.grantItemOnBind = grantItemOnBind;
        this.soulbound = soulbound;
    }
    public boolean isSoulbound() {
        return soulbound;
    }

    public boolean isItemGrantedOnBind() {
        return grantItemOnBind;
    }

    public MTXItem getCustomItem() {
        return customItem;
    }

    public Recipe getCustomRecipe() {
        return customRecipe;
    }

    /**
     * Only needed if MTXItem.getRecipe() is null
     * Even then it is optional.
     * @param customItem the item to add a recipe for.
     * @return the recipe.
     */
    public abstract Recipe buildRecipe(ItemStack customItem);

    @EventHandler
    public void onCraft(CraftItemEvent event) {
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


    /* Twist Overrides */
    public boolean bindPlayer(Player player, boolean silent) {
        if (customRecipe != null) {
            if (customRecipe instanceof ShapedRecipe sr) {
                player.discoverRecipe(sr.getKey());
            } else if (customRecipe instanceof ShapelessRecipe sr) {
                player.discoverRecipe(sr.getKey());
            }
            if (!silent)
                player.sendMessage(ChatColor.GREEN + "You have discovered a new recipe for: " + getName() + ".");
        }

        if (grantItemOnBind) {
            ItemStash.createStash(player.getUniqueId());
            if (player.getInventory().firstEmpty() == -1) {
                ItemStash.get(player.getUniqueId()).addItem(customItem.getItem());
                if (!silent)
                    doAddStashText(player);
            } else {
                player.getInventory().addItem(customItem.getItem());
            }
        }
        return super.bindPlayer(player, silent);
    }
    public boolean bindPlayer(UUID player, boolean silent) {
        return bindPlayer(Bukkit.getPlayer(player), silent);
    }

    public boolean unbindPlayer(Player player, boolean unregistered) {
        if (customRecipe != null) {
            if (customRecipe instanceof ShapedRecipe sr) {
                player.undiscoverRecipe(sr.getKey());
            } else if (customRecipe instanceof ShapelessRecipe sr) {
                player.undiscoverRecipe(sr.getKey());
            }
        }
        return super.unbindPlayer(player, unregistered);
    }
    public boolean unbindPlayer(UUID player, boolean unregistered) {
        return unbindPlayer(Bukkit.getPlayer(player), unregistered);
    }

    public String abilityText(String abilityName, String howToActivate) {
        return ChatColor.GOLD + "Item Ability: " + abilityName + ChatColor.YELLOW + ChatColor.BOLD + " " + howToActivate;
    }

    public String cooldownText(int cdTime) {
        return ChatColor.GRAY + "Cooldown: " + ChatColor.RED + cdTime + ChatColor.GRAY + " seconds";
    }

    public static void doAddStashText(Player player) {
        player.sendMessage(ChatColor.GREEN + "Your inventory is full so the item was added to your stash!");
        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + ItemStash.get(player.getUniqueId()).getItems().size() + " in your stash!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ct"));

        TextComponent filler1 = new TextComponent(" §eto claim the items, or run /ct.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }


    public static void doRemoveStashText(Player player, int itemsClaimed) {
        player.sendMessage(ChatColor.RED + "Your inventory was full, but you claimed " + ChatColor.RED + itemsClaimed + " items!");
        player.sendMessage(ChatColor.YELLOW + "You still have " + ChatColor.RED + ItemStash.get(player.getUniqueId()).getItems().size() + " items left!");

        TextComponent filler = new TextComponent("§eClick ");

        TextComponent clickable = new TextComponent("§6§lHERE");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ct"));

        TextComponent filler1 = new TextComponent(" §eto claim the items, or run /ct.");

        player.spigot().sendMessage(filler, clickable, filler1);
    }
}
