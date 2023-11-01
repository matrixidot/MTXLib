package me.neo.mtxlib.api.core;

public class RegisterableTest implements MTXRegistrable<RegisterableTest> {
    @Override
    public String getName() {
        return "RT";
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
