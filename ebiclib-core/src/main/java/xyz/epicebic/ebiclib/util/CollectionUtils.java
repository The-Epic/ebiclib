package xyz.epicebic.ebiclib.util;

import java.util.concurrent.ThreadLocalRandom;

public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> void shuffle(T[] array) {
        int random;
        for (int i = 1; i < array.length; i++) {
            random = (int) (ThreadLocalRandom.current().nextDouble(array.length) * i);
            T temp = array[i - 1];
            array[i - 1] = array[random];
            array[random] = temp;
        }
    }
}
