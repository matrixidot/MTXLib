package me.neo.mtxlib.api.customevents;

import me.neo.mtxlib.api.item.MTXItem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


/**
 * Fired immediately before a {@link MTXItem} is unregistered.
 * Event can be cancelled
 */
public class UnregisterItemEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;

    private final MTXItem item;

    public UnregisterItemEvent(MTXItem item) {
        cancelled = false;
        this.item = item;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns the {@link MTXItem} being unregistered.
     * @return The item being unregistered.
     */
    public MTXItem getItem() {
        return item;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you do not want the item to be unregistered.
     * @return If the event is cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be executed in the server, but will still pass to other plugins.
     * Cancelling this event will prevent the item from being unregistered.
     * This may cause unintended behaviour so be cautious when cancelling this event.
     * @param cancel True if you want to cancel this event.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
