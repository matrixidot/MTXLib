package me.neo.mtxlib.api.item;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.util.Cooldown;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public abstract class MTXItem implements Listener {
    private final String name;
    private final String description;
    private final int id;
    private final int cooldownTime;

    private final ItemStack item;
    private final Recipe recipe;

    public MTXItem(String name, String description, int id, int cooldownTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.cooldownTime = cooldownTime;
        this.item = configureItem();
        this.recipe = buildRecipe(item);
        if (recipe != null)
            Bukkit.addRecipe(recipe);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public ItemStack getItem() {
        return item;
    }
    public Recipe getRecipe() {
        return recipe;
    }


    private ItemStack configureItem() {
        ItemStack customItem = buildCustomItem();
        ItemMeta meta = customItem.getItemMeta();
        meta.getPersistentDataContainer().set(MTXLib.getMtxItemInternalKey(), PersistentDataType.STRING,
                MTXLib.getItemInternalString() + getDescription() + getName() + getId());
        customItem.setItemMeta(meta);
        return customItem;
    }

    public boolean check(@NotNull ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (!stack.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        if (!PDC.has(MTXLib.getMtxItemInternalKey(), PersistentDataType.STRING))
            return false;
        return PDC.get(MTXLib.getMtxItemInternalKey(), PersistentDataType.STRING)
                .equals(MTXLib.getItemInternalString() + getDescription() + getName() + getId());
    }

    public boolean canUse(Player player) {
        Cooldown cooldown = Cooldown.getCooldown(player, name);
        if (cooldown == null) {
            Cooldown.newCooldown(player, getName(), Duration.ofSeconds(cooldownTime));
            return true;
        }
        if (cooldown.isExpired()) {
            cooldown.updateCooldown(Duration.ofSeconds(cooldownTime));
            return true;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.RED + "On cooldown: " + cooldown.getRemainingCooldown() + " seconds"));
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem())
            return;
        if (!check(event.getItem()))
            return;
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR -> rightClickAbility(event);
            case LEFT_CLICK_AIR -> leftClickAbility(event);
            case RIGHT_CLICK_BLOCK -> rightClickBlockAbility(event);
            case LEFT_CLICK_BLOCK -> leftClickBlockAbility(event);
            default -> {}
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!check(event.getPlayer().getInventory().getItem(event.getHand())))
            return;
        rightClickEntityAbility(event);
    }

    public abstract ItemStack buildCustomItem();
    public abstract Recipe buildRecipe(ItemStack stack);
    public abstract void rightClickAbility(PlayerInteractEvent event);
    public abstract void leftClickAbility(PlayerInteractEvent event);
    public abstract void rightClickBlockAbility(PlayerInteractEvent event);
    public abstract void leftClickBlockAbility(PlayerInteractEvent event);
    public abstract void rightClickEntityAbility(PlayerInteractEntityEvent event);


    protected void onRegister() {
        MTXLib.log.info("Registered item: " + name + ".");
    }

    protected void onUnregister() {
        MTXLib.log.info("Unregistered item: " + name + ".");
    }



}
