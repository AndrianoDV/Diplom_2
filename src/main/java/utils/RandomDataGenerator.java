package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomDataGenerator {
    public static String getRandomEmail() {
        return "test" + System.currentTimeMillis() + "@yandex.ru";
    }

    public static String getRandomPassword() {
        return "password" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public static String getRandomName() {
        return "User" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}