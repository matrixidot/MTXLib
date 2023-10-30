package me.neo.mtxlib.api.registering;

public class RegisterableTest implements MTXRegisterable<RegisterableTest> {
    @Override
    public String getName() {
        return "RT";
    }

    @Override
    public int getId() {
        return 0;
    }
}
