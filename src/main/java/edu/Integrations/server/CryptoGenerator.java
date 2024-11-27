package edu.Integrations.server;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.logging.Logger;

@SuppressWarnings("HideUtilityClassConstructor")
public class CryptoGenerator {
    private static final String SALT = System.getenv("SALT");
    private static final String KEY_FOR_AES = System.getenv("KEY_FOR_AES");
    private static final Integer LENGTH_OF_RANDOM_BYTES = 16;
    private static final Integer LENGTH_OF_SALT = 8;
    private static final Integer LENGTH_OF_CHECK_SUM = 28;

    public static synchronized String generateCheckSum(Long userId) {

        // Получаем текущее время
        String requestTime = String.valueOf(Instant.now().toEpochMilli());

        // Генерируем криптографически стойкое случайное число
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[LENGTH_OF_RANDOM_BYTES]; // длина массива может быть изменена

        random.nextBytes(randomBytes);
        String randomValue = bytesToHex(randomBytes);

        // Объединяем время запроса, id пользователя, случайное значение и соль в одну строку
        String input = requestTime + userId + randomValue + SALT;
        String res;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            res = bytesToHex(hashInBytes);
        } catch (NoSuchAlgorithmException e) {
            res = randomValue;
            Logger.getAnonymousLogger().info("MD5 algorithm is not available: " + e.getMessage());
        }
        if (res.length() >= LENGTH_OF_CHECK_SUM) {
            //cc9ea69e10bbd2322e727eb11e359f8e
            res = res.substring(0, LENGTH_OF_CHECK_SUM - 1);
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

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[LENGTH_OF_SALT];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private static String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String encryptAES(String data, String key) {
        if (data.length() >= LENGTH_OF_CHECK_SUM) {
            //cc9ea69e10bbd2322e727eb11e359f8e
            return data.substring(0, LENGTH_OF_CHECK_SUM - 1);
        }
        return data;

    }


    public static String generateUsersHash(Update update) {
        Long tgUserId = update.getMessage().getFrom().getId();
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        String rawData = tgUserId + "_" + phoneNumber + "_" + name;
        String salt = generateSalt();
        long timestamp = System.currentTimeMillis();
        String saltedData = rawData + "_" + salt + "_" + timestamp;
        String hash = sha256(saltedData);
        String encryptedData = encryptAES(hash, KEY_FOR_AES);

        return encryptedData;
    }
}
