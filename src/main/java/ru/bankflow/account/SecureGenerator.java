package ru.bankflow.account;

import java.security.SecureRandom;

public class SecureGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecure20DigitString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append(secureRandom.nextInt(9) + 1); // Первая цифра от 1 до 9

        for (int i = 0; i < 19; i++) {
            sb.append(secureRandom.nextInt(10)); // Остальные от 0 до 9
        }
        return sb.toString();
    }
}