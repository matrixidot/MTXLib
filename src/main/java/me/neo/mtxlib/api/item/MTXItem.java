package me.neo.mtxlib.api.item;

import me.neo.mtxlib.MTXLib;
import me.neo.mtxlib.api.core.IRegistrable;
import me.neo.mtxlib.util.Cooldown;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class MTXItem<T> implements IRegistrable<T> {
    private final String name;
    private final int id;
    private final int cooldownTime;

    private final ItemStack item;
    private final Recipe recipe;

    // Internal Fields ONLY
    private final String ITEM_ID;
    private final String COOLDOWN_ID;
    private final static NamespacedKey ITEM_KEY = new NamespacedKey(MTXLib.getPlugin(), "MTXLib.API.item.MTXItem.IDENTIFIER");

    public MTXItem(String name, int id, int cooldownTime) {
        this.name = name;
        this.id = id;
        this.cooldownTime = cooldownTime;
        ITEM_ID = "MTXLib.API.item.MTXItem.IDENTIFIER" + name + id;
        COOLDOWN_ID = "MTXLib.API.item.MTXItem.COOLDOWN" + name + id;
        item = configureItem();
        recipe = buildRecipe(item);
    }

    public String getName() {
        return name;
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

    public abstract ItemStack buildCustomItem();

    public abstract Recipe buildRecipe(ItemStack item);

    private ItemStack configureItem() {
        ItemStack item = buildCustomItem();
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.getPersistentDataContainer().set(MTXItem.ITEM_KEY, PersistentDataType.STRING, ITEM_ID);
        item.setItemMeta(meta);
        return item;
    }

    public boolean check(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return false;

        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(MTXItem.ITEM_KEY, PersistentDataType.STRING))
            return false;

        return Objects.equals(pdc.get(MTXItem.ITEM_KEY, PersistentDataType.STRING), ITEM_ID);
    }

    public boolean canUse(Player player) {
        if (player == null)
            return false;

        Cooldown cooldown = Cooldown.getCooldown(player, COOLDOWN_ID);
        if (cooldown == null) {
            Cooldown.newCooldown(player, COOLDOWN_ID, Duration.ofSeconds(cooldownTime));
            return true;
        }
        if (cooldown.isExpired()) {
            cooldown.updateCooldown(Duration.ofSeconds(cooldownTime));
            return true;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new ComponentBuilder("On Cooldown: " + cooldown.getRemainingCooldown() + " seconds.").color(ChatColor.RED).bold(true).create());
        return false;
    }

    public String abilityName(String name, String howToActivate) {
        return ChatColor.GOLD + name + ChatColor.GOLD + ChatColor.BOLD + howToActivate;
    }

    public String getCooldownText() {
        return ChatColor.GRAY + "Cooldown " + ChatColor.RED + cooldownTime + ChatColor.GRAY + " seconds.";
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || !check(event.getItem())) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR -> rightClick(event.getPlayer(), event.getItem(), event.getPlayer().isSneaking(), event);
            case LEFT_CLICK_AIR -> leftClick(event.getPlayer(), event.getItem(), event.getPlayer().isSneaking(), event);
            case RIGHT_CLICK_BLOCK -> rightClickBlock(event.getPlayer(), event.getItem(), event.getClickedBlock(), event.getPlayer().isSneaking(), event);
            case LEFT_CLICK_BLOCK -> leftClickBlock(event.getPlayer(), event.getItem(), event.getClickedBlock(), event.getPlayer().isSneaking(), event);
            default -> {}
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!check(event.getPlayer().getInventory().getItem(event.getHand())))
            return;
        rightClickEntity(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getHand()), event.getPlayer().isSneaking(), event);
    }

    @EventHandler
    public void onDealDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;
        if (check(player.getInventory().getItemInMainHand())) {
            dealtDamage(player, player.getInventory().getItemInMainHand(), event.getEntity(), event);
        }
        if (check(player.getInventory().getItemInOffHand())) {
            dealtDamage(player, player.getInventory().getItemInOffHand(), event.getEntity(), event);
        }
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (!check(event.getPlayer().getInventory().getItemInMainHand()))
            return;
        breakBlock(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand(), event.getBlock(), event.getPlayer().isSneaking(), event);
    }

    /**
     * Fired when the player right clicks with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.player.PlayerInteractEvent} being fired,
     */
    public abstract void rightClick(Player player, ItemStack item, boolean sneaking, PlayerInteractEvent event);

    /**
     * Fired when the player left clicks with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.player.PlayerInteractEvent} being fired,
     */
    public abstract void leftClick(Player player, ItemStack item, boolean sneaking, PlayerInteractEvent event);

    /**
     * Fired when the player right-clicks a block with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param clickedBlock the {@link org.bukkit.block.Block} that was clicked.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.player.PlayerInteractEvent} being fired,
     */
    public abstract void rightClickBlock(Player player, ItemStack item, Block clickedBlock, boolean sneaking, PlayerInteractEvent event);

    /**
     * Fired when the player left-clicks a block with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param clickedBlock the {@link org.bukkit.block.Block} that was clicked.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.player.PlayerInteractEvent} being fired,
     */
    public abstract void leftClickBlock(Player player, ItemStack item, Block clickedBlock, boolean sneaking, PlayerInteractEvent event);

    /**
     * Fired when the player hurts an entity with the item equipped in their main or offhand.
     * Note this method can be called twice if the player has the item in the main and offhand.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param entity the {@link org.bukkit.entity.Entity} that was hurt.
     * @param event the {@link org.bukkit.event.entity.EntityDamageByEntityEvent} being fired.
     */
    public abstract void dealtDamage(Player player, ItemStack item, Entity entity, EntityDamageByEntityEvent event);

    /**
     * Fired when the player right-clicks on an entity with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.player.PlayerInteractEntityEvent} being fired.
     */
    public abstract void rightClickEntity(Player player, ItemStack item, boolean sneaking, PlayerInteractEntityEvent event);

    /**
     * Fired when the player breaks a block with the item.
     * Note this method is fired even if canUse() is false. It is up to you to determine the effects.
     * @param player the {@link org.bukkit.entity.Player} using the item.
     * @param item the {@link org.bukkit.inventory.ItemStack} that is the custom item.
     * @param block the {@link org.bukkit.block.Block} being broken.
     * @param sneaking if the player is sneaking or not.
     * @param event the {@link org.bukkit.event.block.BlockBreakEvent} being fired.
     */
    public abstract void breakBlock(Player player, ItemStack item, Block block, boolean sneaking, BlockBreakEvent event);


    @Override
    public void onRegister() {
        MTXLib.log.info("Registered item: " + name + ".");
    }

    @Override
    public void onUnregister() {
        MTXLib.log.info("Unregistered item: " + name + ".");
    }
}
