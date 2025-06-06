package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomDataGenerator {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String getRandomEmail() {
        return "test-" + randomString(10) + "@yandex.ru";
    }

    public static String getRandomPassword() {
        return "Pass" + randomString(8) + "!";
    }

    public static String getRandomName() {
        return "User-" + randomString(6);
    }

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(ThreadLocalRandom.current().nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}



