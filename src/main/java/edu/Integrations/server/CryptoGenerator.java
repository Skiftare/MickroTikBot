package edu.Integrations.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

public class CryptoGenerator {
    private static final String salt = System.getenv("SALT");

    public static synchronized String generateCheckSum(Long userId) {

        // Получаем текущее время
        String requestTime = String.valueOf(Instant.now().toEpochMilli());

        // Генерируем криптографически стойкое случайное число
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16]; // длина массива может быть изменена
        random.nextBytes(randomBytes);
        String randomValue = bytesToHex(randomBytes);

        // Объединяем время запроса, id пользователя, случайное значение и соль в одну строку
        String input = requestTime + userId + randomValue + salt;
        String res;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            res = bytesToHex(hashInBytes);
        } catch (NoSuchAlgorithmException e) {
            res = randomValue;
            e.printStackTrace();
        }

        return res;


    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
