package me.neo.mtxlib.util;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class MTXRandom {
    private static final Random random = new Random();

    public static <T> T randomItem(List<T> list) {
        return list.get(random.nextInt(0, list.size()));
    }

    public static <T> T pullRandomItem(List<T> list)
    {
        int index = random.nextInt(0, list.size());
        T obj = list.get(index);
        list.remove(index);
        return obj;
    }
}
