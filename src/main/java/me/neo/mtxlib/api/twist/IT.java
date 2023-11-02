package me.neo.mtxlib.api.twist;

import me.neo.mtxlib.api.item.MTXItem;

public class IT extends ItemTwist<IT> {
    public IT(String name, String description, int id, MTXItem<?> item, boolean grantItemOnBind, boolean soulbound) {
        super(name, description, id, item, grantItemOnBind, soulbound);
    }

    @Override
    public String[] bindMessage() {
        return new String[0];
    }

    @Override
    public void onBind() {

    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onUnregister() {

    }
}
