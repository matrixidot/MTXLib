package me.neo.mtxlib.api.customevents;

import me.neo.mtxlib.api.core.IBindable;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlayerUnbindEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;

    private final Player player;
    private final IBindable<?> bindable;

    public PlayerUnbindEvent(Player player, IBindable<?> bindable) {
        cancelled = false;
        this.player = player;
        this.bindable = bindable;
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
     * Returns the {@link Player} being unbound.
     * @return The Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the {@link IBindable} being registered.
     * @return The twist being registered.
     */
    public IBindable<?> getBindable() {
        return bindable;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you do not want the twist to be registered.
     * @return If the event is cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be executed in the server, but will still pass to other plugins.
     * Cancelling this event will prevent the twist from being registered and added to the command pools.
     * This can cause null exceptions if the twist is expected to be present so be cautious when cancelling this event.
     * @param cancel True if you want to cancel this event.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
