package me.neo.mtxlib.api.twist;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

@SuppressWarnings("unused")
public abstract class ItemTwist<T> extends Twist<T> {
    private final boolean grantItemOnBind;

    private final boolean soulbound;

    private ItemStack customItem;

    private Recipe customRecipe;

    public ItemTwist(String name, String description, int id, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id);
        this.grantItemOnBind = grantItemOnBind;
        this.soulbound = soulbound;
    }

    public boolean isItemGrantedOnBind() {
        return grantItemOnBind;
    }

    public boolean isSoulbound() {
        return soulbound;
    }

    public ItemStack getCustomItem() {
        return customItem;
    }

    public Recipe getCustomRecipe() {
        return customRecipe;
    }

}
